package net.lab1024.sa.admin.module.freeserver.renew.manager;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import jakarta.mail.MessagingException;
import net.lab1024.sa.admin.module.freeserver.instance.dao.UserCloudServerDao;
import net.lab1024.sa.admin.module.freeserver.instance.domain.entity.UserCloudServerEntity;
import net.lab1024.sa.admin.module.freeserver.renew.conmmon.CommonCode;
import net.lab1024.sa.admin.module.freeserver.renew.constant.*;
import net.lab1024.sa.admin.module.freeserver.renew.factory.RenewalStrategyFactory;
import net.lab1024.sa.admin.module.freeserver.renew.strategy.AbstractRenewalStrategy;
import net.lab1024.sa.admin.util.*;
import net.lab1024.sa.base.common.enumeration.CommonStatusEnum;
import net.lab1024.sa.base.common.exception.BusinessException;
import net.lab1024.sa.base.module.support.apiencrypt.service.ApiEncryptService;
import net.lab1024.sa.base.module.support.mail.MailService;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.core5.http.HttpEntity;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * 延期服务
 */
@Service
public class PostponeManager {

    @Resource
    private UserCloudServerDao userCloudServerDao;

    @Resource
    private ApiEncryptService apiEncryptService;

    @Resource
    private RenewalStrategyFactory renewalStrategyFactory;

    @Resource
    private MailService mailService;

    private static final Logger log = LoggerFactory.getLogger(PostponeManager.class);

    public void postpone() {
        // 查询所有已启用的服务
        QueryWrapper<UserCloudServerEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("enable", CommonStatusEnum.ENABLE.getValue());
        List<UserCloudServerEntity> userCloudServerEntities = userCloudServerDao.selectList(queryWrapper);

        // 循环处理服务
        for (UserCloudServerEntity userCloudServerEntity : userCloudServerEntities) {
            try {
                processCloudServer(userCloudServerEntity);
            } catch (Exception e) {
                log.error("服务器延期过程出错", e);
                // 发送邮件
                if (userCloudServerEntity.getEnableEmail().equals(CommonStatusEnum.ENABLE.getValue()))
                    handleException(userCloudServerEntity, e);
            }
        }
    }

    /**
     * 延期服务
     * @param userCloudServer 服务配置
     * @throws Exception 异常
     */
    private void processCloudServer(UserCloudServerEntity userCloudServer) throws Exception {
        HttpClient httpClient = HttpUtil.getHttpClient(new BasicCookieStore());
        Map<String, String> cookieMap = new LinkedHashMap<>();

        String username = userCloudServer.getUsername();
        String type = String.valueOf(userCloudServer.getServerType());
        boolean enable = userCloudServer.getEnable().equals(CommonStatusEnum.ENABLE.getValue());

        CloudInfo cloudInfo = CloudInfo.getCloudInfo(type);
        String cloudName = Objects.requireNonNull(cloudInfo).getCloudName();
        String ukLog = CommonCode.getUKLog(username, cloudName);
        String uKey = CommonCode.getUserKey(username, type);

        if (!enable) {
            log.warn("{}未启用，已跳过", cloudName);
            return;
        }

        String status = loginAndCheck(httpClient, cookieMap, userCloudServer, cloudInfo, ukLog, uKey);
        // TODO 先不执行延期任务，后续创建多个延期任务提供客户选择
        if ("1".equals(status)) {
            AbstractRenewalStrategy renewalStrategy = renewalStrategyFactory.getStrategy(MethodRenewalTypeEnum.IMAGE.getType());
            String imageUrl = renewalStrategy.getImageUrl();
            log.info("附件地址：{}", imageUrl);
            // 执行延期
            String blogUrl = "https://blog.csdn.net/" + imageUrl;
            this.submitDelayInfo(httpClient, cookieMap, blogUrl, imageUrl, cloudInfo, ukLog, username, cloudName);
        }
    }

    /**
     * 登录并查验
     * @param httpClient
     * @param cookieMap
     * @param userCloudServer
     * @param cloudInfo
     * @param ukLog
     * @param uKey
     * @return
     * @throws Exception
     */
    public String loginAndCheck(HttpClient httpClient,
                                Map<String, String> cookieMap,
                                UserCloudServerEntity userCloudServer,
                                CloudInfo cloudInfo,
                                String ukLog,
                                String uKey) throws Exception {

        String username = userCloudServer.getUsername();
        // 密码解密
        String password = apiEncryptService.decrypt(userCloudServer.getPassword());
        String cloudName = cloudInfo.getCloudName();
        String type = cloudInfo.getType();

        Map<String, String> userInfo = loadUserInfo(uKey);
        String nextTime = userInfo.get(CloudDataKey.NEXT_TIME);

        if (StringUtil.isEmpty(nextTime) || CommonCode.isExpire(nextTime)) {
            log.info("{}开始登录...", ukLog);
            return performLoginAndCheck(httpClient, cookieMap, username, password, cloudInfo, cloudName, type, ukLog, uKey, userInfo);
        } else {
            log.info("{}未到期", ukLog);
            return null;
        }
    }

    /**
     * 用户信息
     * @param uKey
     * @return
     */
    private Map<String, String> loadUserInfo(String uKey) {
        Map<String, String> userInfo = Profile.userInfos.get(uKey);
        if (userInfo == null) {
            userInfo = new HashMap<>();
        }
        return userInfo;
    }

    private String performLoginAndCheck(HttpClient httpClient,
                                        Map<String, String> cookieMap,
                                        String username, String password,
                                        CloudInfo cloudInfo, String cloudName, String type,
                                        String ukLog, String uKey, Map<String, String> userInfo) throws Exception {

        JSONObject loginJson = executeLoginRequest(httpClient, cookieMap, cloudInfo.getLoginUri(),
                username, password, ukLog);

        if (!CloudDataKey.LOGIN_SUCCESS.equals(loginJson.getString(CloudDataKey.LOGIN_STATUS))) {
            log.warn("{}登录失败", ukLog);
            this.sendMail(cloudName + "账号：" + username + ", 登录失败", loginJson.toString());
            return null;
        }

        if (!HttpUtil.hasEssentialCookies(cookieMap)) {
            log.warn("{}登录成功，但关键 Cookie 不完整: {}", ukLog, HttpUtil.buildCookieHeader(cookieMap));
        } else {
            log.info("{}登录成功，关键 Cookie 已就绪", ukLog);
        }

        log.info("{}登录成功，开始检查免费服务器状态", ukLog);

        // 登录成功，调用状态接口
        JSONObject statusJson = checkServerStatus(httpClient, cookieMap, cloudInfo.getBusUri(), ukLog);
        String status = extractServerStatus(statusJson);

        // 审核状态接口
        JSONObject checkJson = queryCheckRecords(httpClient, cookieMap, cloudInfo.getBusUri(), ukLog);

        return processServerStatus(status, statusJson, checkJson, ukLog, uKey, userInfo.get("blogUrl"));
    }

    private JSONObject executeLoginRequest(HttpClient httpClient, Map<String, String> cookieMap,
                                           String loginUri, String username, String password, String ukLog) throws IOException {

        log.info("{}执行登录请求", ukLog);

        String response = HttpUtil.getPostResAndCaptureCookies(
                httpClient,
                loginUri,
                CloudPostParams.getYunLogin(username, password),
                null,
                cookieMap
        );

        JSONObject loginJson = JSONUtils.parseObject(response);
        log.info("{}登录接口返回:{}", ukLog, loginJson);
        log.info("{}登录后 Cookie:{}", ukLog, HttpUtil.buildCookieHeader(cookieMap));

        return loginJson;
    }

    private JSONObject checkServerStatus(HttpClient httpClient, Map<String, String> cookieMap,
                                         String busUri, String ukLog) throws IOException, URISyntaxException {

        String response = HttpUtil.getPostRes(
                httpClient,
                busUri,
                CloudPostParams.getFreeStatus(),
                HttpUtil.buildHeadersWithCookies(null, cookieMap)
        );

        JSONObject statusJson = JSONUtils.parseObject(response);
        log.info("{}检查服务器状态返回:{}", ukLog, statusJson);

        return statusJson;
    }

    private JSONObject queryCheckRecords(HttpClient httpClient, Map<String, String> cookieMap,
                                         String busUri, String ukLog) throws IOException, URISyntaxException {

        String response = HttpUtil.getPostRes(
                httpClient,
                busUri,
                CloudPostParams.getCheckStatus(),
                HttpUtil.buildHeadersWithCookies(null, cookieMap)
        );

        JSONObject checkJson = JSONUtils.parseObject(response);
        log.info("{}延期记录接口返回:{}", ukLog, checkJson);

        return checkJson;
    }

    private String extractServerStatus(JSONObject statusJson) {
        JSONObject msgJson = JSONUtils.getObject(statusJson, CloudDataKey.STATUS_DATA);

        if (JSONUtils.containsKey(msgJson, CloudDataKey.DELAY_STATUS1)) {
            return JSONUtils.getString(msgJson, CloudDataKey.DELAY_STATUS1);
        } else if (JSONUtils.containsKey(msgJson, CloudDataKey.DELAY_STATUS2)) {
            return JSONUtils.getString(msgJson, CloudDataKey.DELAY_STATUS2);
        }

        return null;
    }

    private String processServerStatus(String status, JSONObject statusJson, JSONObject checkJson,
                                       String ukLog, String uKey,
                                       String blogUrl) throws Exception {

        switch (status) {
            case "1":
                CommonCode.checkCheckStatus(checkJson, ukLog, blogUrl);
                log.info("{}已到审核期", ukLog);
                break;

            case "0":
                // 未到审核期
                CommonCode.checkCheckStatus(checkJson, ukLog, blogUrl);
                String nextTime = JSONUtils.getString(JSONUtils.getObject(statusJson, CloudDataKey.STATUS_DATA), CloudDataKey.NEXT_TIME);

                Map<String, String> userInfo = loadUserInfo(uKey);
                userInfo.put(CloudDataKey.NEXT_TIME, nextTime);
                CommonCode.userInfosPermanent(uKey, userInfo);
                log.info("{}未到审核期，下次审核开始时间:{}", ukLog, nextTime);
                break;

            default:
                log.info("{}正在审核", ukLog);
                break;
        }

        return status;
    }

    /**
     * 执行延期程序
     * @param httpClient
     * @param cookieMap
     * @param blogUrl
     * @param cloudInfo
     * @param ukLog
     * @param username
     * @param cloudName
     * @throws IOException
     * @throws GitAPIException
     */
    private void submitDelayInfo(HttpClient httpClient, Map<String, String> cookieMap, String blogUrl, String filePath,
                                 CloudInfo cloudInfo, String ukLog, String username, String cloudName) throws IOException {

        File screenshotFile = new File(filePath);
        // 提交延期
        String response = submitBlogInfo(httpClient, cookieMap, cloudInfo, screenshotFile, blogUrl, ukLog);
        // 验证延期结果
        handleSubmissionResult(response, cloudName, username, ukLog, screenshotFile);
    }

    /**
     * 提交延期
     * @param httpClient
     * @param cookieMap
     * @param cloudInfo
     * @param screenshotFile
     * @param blogUrl
     * @param ukLog
     * @return
     * @throws IOException
     */
    private String submitBlogInfo(HttpClient httpClient,
                                  Map<String, String> cookieMap,
                                  CloudInfo cloudInfo, File screenshotFile, String blogUrl, String ukLog)
            throws IOException {

        log.info("{}提交延期网址及截图信息", ukLog);
        HttpEntity entity = CloudPostParams.getBlogInfo(cloudInfo, screenshotFile, blogUrl).build();

        return HttpUtil.getPostRes(
                httpClient,
                cloudInfo.getBusUri(),
                entity,
                HttpUtil.buildHeadersWithCookies(null, cookieMap)
        );
    }


    private void handleSubmissionResult(String response, String cloudName,
                                        String username, String ukLog, File screenshotFile) {

        JSONObject json = JSONUtils.parseObject(response);
        log.info("{}提交延期记录返回结果：{}", ukLog, json);

        if (CloudDataKey.BLOG_SUCCESS.equals(json.getString(CloudDataKey.BLOG_STATUS))) {
            log.info("{}提交延期记录成功", ukLog);
        } else {
            log.error("{}提交延期记录失败，删除发布博客", ukLog);
            this.sendMail(cloudName + "账号:" + username + "发送延期博客失败", json.toString());
        }

        if (screenshotFile.exists()) {
            boolean delete = screenshotFile.delete();
            log.debug("已删除临时截图文件");
        }
    }

    private void handleException(UserCloudServerEntity userCloudServerEntity, Exception e) {
        String username = userCloudServerEntity.getUsername();
        String type = String.valueOf(userCloudServerEntity.getServerType());
        CloudInfo cloudInfo = CloudInfo.getCloudInfo(type);

        if (cloudInfo != null) {
            String cloudName = cloudInfo.getCloudName();
            String ukLog = CommonCode.getUKLog(username, cloudName);
            log.error("{}延期过程出错", ukLog, e);
            this.sendMail(cloudName + "账号：" + username + ",延期过程出错", e.getMessage());
        } else {
            log.error("处理服务器配置时出错：{}", userCloudServerEntity.getUsername(), e);
            this.sendMail("服务器配置错误", "无法获取云服务信息：" + userCloudServerEntity.getUsername());
        }
    }

    /**
     * 发送邮件
     * @param title
     * @param body
     */
    private void sendMail(String title, String body) {
        try {
            List<String> userIds = new ArrayList<>();
            mailService.sendMail(title, body, null, userIds, false);
        } catch (MessagingException e) {
            log.error("邮件发送失败");
        }

    }
}
