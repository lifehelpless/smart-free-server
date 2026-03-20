package net.lab1024.sa.admin.module.freeserver.renew.manager;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.freeserver.renew.conmmon.BlogGit;
import net.lab1024.sa.admin.module.freeserver.renew.conmmon.CommonCode;
import net.lab1024.sa.admin.module.freeserver.renew.constant.*;
import net.lab1024.sa.admin.util.*;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.cookie.BasicCookieStore;
import org.apache.hc.core5.http.HttpEntity;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class PostponeManager {

    private static final Logger log = LoggerFactory.getLogger(PostponeManager.class);
    private static final Integer WAIT_TIME = 1000 * 60 * Profile.BLOG_INIT_WAIT_TIME;
    private static final int MAX_WAIT_COUNT = Profile.BLOG_INIT_WAIT_COUNT;

    public void postpone() {
        List<Map<String, String>> cloudServers = Profile.cloudServers;
        MailUtil mailUtil = initializeMailUtil();

        for (Map<String, String> serverInfo : cloudServers) {
            try {
                processCloudServer(serverInfo, mailUtil);
            } catch (Exception e) {
                log.error("服务器延期过程出错", e);
                handleException(serverInfo, e, mailUtil);
            }
        }
    }

    private MailUtil initializeMailUtil() {
        return MailUtil.MailUtilBuilder.getBuilder()
                .setHost(Profile.MAIL_SERVER_HOST)
                .setPort(Profile.MAIL_SERVER_PORT)
                .setPassword(Profile.MAIL_PASSWORD)
                .setUsername(Profile.MAIL_USERNAME)
                .setReceiveUser(Profile.MAIL_RECEIVE_USER)
                .build();
    }

    private void processCloudServer(Map<String, String> serverInfo, MailUtil mailUtil) throws Exception {
        BasicCookieStore cookieStore = new BasicCookieStore();
        HttpClient httpClient = HttpUtil.getHttpClient(cookieStore);
        Map<String, String> cookieMap = new LinkedHashMap<>();

        String username = serverInfo.get(Constans.CLOUD_USERNAME);
        String type = serverInfo.get(Constans.CLOUD_TYPE);
        boolean enable = Boolean.parseBoolean(serverInfo.get(Constans.ENABLE));

        CloudInfo cloudInfo = CloudInfo.getCloudInfo(type);
        String cloudName = Objects.requireNonNull(cloudInfo).getCloudName();
        String ukLog = CommonCode.getUKLog(username, cloudName);
        String uKey = CommonCode.getUserKey(username, type);

        if (!enable) {
            log.warn("{}未启用，已跳过", cloudName);
            return;
        }

        String status = loginAndCheck(httpClient, cookieStore, cookieMap, mailUtil, serverInfo, cloudInfo, ukLog, uKey);
        // TODO 先不执行延期任务，后续创建多个延期任务提供客户选择
//        if ("1".equals(status)) {
//            executeDelayProcess(httpClient, cookieStore, cookieMap, mailUtil, serverInfo, cloudInfo, ukLog, uKey);
//        }
    }

    public String loginAndCheck(HttpClient httpClient,
                                BasicCookieStore cookieStore,
                                Map<String, String> cookieMap,
                                MailUtil mailUtil,
                                Map<String, String> serverInfo,
                                CloudInfo cloudInfo,
                                String ukLog,
                                String uKey) throws Exception {

        String username = serverInfo.get(Constans.CLOUD_USERNAME);
        String password = serverInfo.get(Constans.CLOUD_PASSWORD);
        String cloudName = cloudInfo.getCloudName();
        String type = cloudInfo.getType();

        Map<String, String> userInfo = loadUserInfo(uKey);
        String nextTime = userInfo.get(CloudDataKey.NEXT_TIME);

        if (StringUtil.isEmpty(nextTime) || CommonCode.isExpire(nextTime)) {
            log.info("{}开始登录...", ukLog);
            return performLoginAndCheck(httpClient, cookieStore, cookieMap, mailUtil, username, password,
                    cloudInfo, cloudName, type, ukLog, uKey, userInfo);
        } else {
            log.info("{}未到期", ukLog);
            return null;
        }
    }

    private Map<String, String> loadUserInfo(String uKey) {
        Map<String, String> userInfo = Profile.userInfos.get(uKey);
        if (userInfo == null) {
            userInfo = new HashMap<>();
        }
        return userInfo;
    }

    private String performLoginAndCheck(HttpClient httpClient, BasicCookieStore cookieStore,
                                        Map<String, String> cookieMap,
                                        MailUtil mailUtil, String username, String password,
                                        CloudInfo cloudInfo, String cloudName, String type,
                                        String ukLog, String uKey, Map<String, String> userInfo) throws Exception {

        JSONObject loginJson = executeLoginRequest(httpClient, cookieStore, cookieMap, cloudInfo.getLoginUri(),
                username, password, ukLog);

        if (!CloudDataKey.LOGIN_SUCCESS.equals(loginJson.getString(CloudDataKey.LOGIN_STATUS))) {
            log.warn("{}登录失败", ukLog);
            mailUtil.sendMail(cloudName + "账号：" + username + ", 登录失败", loginJson.toString());
            return null;
        }

        if (!HttpUtil.hasEssentialCookies(cookieMap)) {
            log.warn("{}登录成功，但关键 Cookie 不完整: {}", ukLog, HttpUtil.buildCookieHeader(cookieMap));
        } else {
            log.info("{}登录成功，关键 Cookie 已就绪", ukLog);
        }

        log.info("{}登录成功，开始检查免费服务器状态", ukLog);

        // 登录成功，调用状态接口
        JSONObject statusJson = checkServerStatus(httpClient, cookieStore, cookieMap, cloudInfo.getBusUri(), ukLog);
        String status = extractServerStatus(statusJson);

        // 审核状态接口
        JSONObject checkJson = queryCheckRecords(httpClient, cookieStore, cookieMap, cloudInfo.getBusUri(), ukLog);

        return processServerStatus(status, statusJson, checkJson, ukLog, uKey, type,
                userInfo.get("blogUrl"), mailUtil);
    }

    private JSONObject executeLoginRequest(HttpClient httpClient, BasicCookieStore cookieStore,
                                           Map<String, String> cookieMap,
                                           String loginUri, String username, String password, String ukLog)
            throws IOException, URISyntaxException {

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

    private JSONObject checkServerStatus(HttpClient httpClient, BasicCookieStore cookieStore,
                                         Map<String, String> cookieMap,
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

    private JSONObject queryCheckRecords(HttpClient httpClient, BasicCookieStore cookieStore,
                                         Map<String, String> cookieMap,
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
                                       String ukLog, String uKey, String type,
                                       String blogUrl, MailUtil mailUtil) throws Exception {

        switch (status) {
            case "1":
                CommonCode.checkCheckStatus(checkJson, ukLog, blogUrl, mailUtil);
                log.info("{}已到审核期", ukLog);
                break;

            case "0":
                // 未到审核期
                CommonCode.checkCheckStatus(checkJson, ukLog, blogUrl, mailUtil);
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

    private void executeDelayProcess(HttpClient httpClient, BasicCookieStore cookieStore,
                                     Map<String, String> cookieMap,
                                     MailUtil mailUtil, Map<String, String> serverInfo,
                                     CloudInfo cloudInfo, String ukLog, String uKey) throws Exception {

        String username = serverInfo.get(Constans.CLOUD_USERNAME);
        String cloudName = cloudInfo.getCloudName();
        String type = cloudInfo.getType();

        mailUtil.sendMail(ukLog + "已到延期时间", ukLog + "开始执行延期程序...");

        log.info("{}开始发送延期博客", ukLog);
        String blogUrl = BlogGit.sendCustomBlogByType(type);
        log.info("{}发送延期博客 url:{}", ukLog, blogUrl);

        if (!waitForBlogInitialization(blogUrl, ukLog, mailUtil, cloudName, username)) {
            return;
        }

        persistBlogUrl(uKey, blogUrl);

        log.info("{}开始生成截图", ukLog);
        boolean picCreated = createScreenshot(blogUrl, ukLog);

        if (picCreated) {
            log.info("{}截图生成成功，开始提交", ukLog);
            submitDelayInfo(httpClient, cookieStore, cookieMap, mailUtil, blogUrl, serverInfo, cloudInfo, ukLog, username, cloudName);
        } else {
            log.warn("{}网页截图生成失败:{}", ukLog, blogUrl);
            cleanupFailedBlog(blogUrl, cloudName, username, mailUtil);
        }
    }

    private boolean waitForBlogInitialization(String blogUrl, String ukLog, MailUtil mailUtil,
                                              String cloudName, String username) {
        try {
            Thread.sleep(WAIT_TIME);

            int waitCount = 0;
            while (!CommonCode.isInitBlog(blogUrl)) {
                log.info("{}延期博客未初始化，等待{}分钟", ukLog, Profile.BLOG_INIT_WAIT_TIME / 60000);
                waitCount++;

                if (waitCount > MAX_WAIT_COUNT) {
                    BlogGit.deleteBlog(blogUrl);
                    log.error("{}博客初始化失败:{}", ukLog, blogUrl);
                    mailUtil.sendMail(ukLog + "博客初始化失败", blogUrl);
                    return false;
                }

                Thread.sleep(WAIT_TIME);
            }

            return true;
        } catch (Exception e) {
            log.error("{}等待博客初始化时出错", ukLog, e);
            return false;
        }
    }

    private void persistBlogUrl(String uKey, String blogUrl) throws IOException {
        Map<String, String> userInfo = loadUserInfo(uKey);
        userInfo.put("blogUrl", blogUrl);
        CommonCode.userInfosPermanent(uKey, userInfo);
        log.debug("已持久化博客 URL: {}", blogUrl);
    }

    private boolean createScreenshot(String blogUrl, String ukLog) throws Exception {
        log.info("{}开始创建截图文件", ukLog);
        String command = buildScreenshotCommand(blogUrl);
        File file = FileUtil.deleteFile(Profile.PJ_PIC_PATH);

        log.info("{}执行截图命令:{}", ukLog, command);

        Process process = null;
        try {
            process = CmdUtil.execCmdGetP(command);
            Thread.sleep(20000);

            int waitSeconds = 0;
            while (!file.exists()) {
                log.info("{}文件未创建成功，等待 10 秒...", ukLog);
                waitSeconds += 10;

                if (waitSeconds >= 200) {
                    log.error("{}文件创建超时", ukLog);
                    CmdUtil.destroy(process);
                    return false;
                }

                Thread.sleep(10000);
            }

            log.info("{}截图文件创建成功", ukLog);
            return true;

        } catch (Exception e) {
            log.error("{}截图文件创建失败", ukLog, e);
            CmdUtil.destroy(process);
            return false;
        }
    }

    private String buildScreenshotCommand(String blogUrl) {
        return new StringBuilder()
                .append(Profile.PJ_EXEC).append("  ")
                .append(ResourceAbPath.PIC_JS_ABPATH).append("  ")
                .append(blogUrl).append("  ")
                .append(Profile.PJ_PIC_PATH)
                .toString();
    }

    private void submitDelayInfo(HttpClient httpClient, BasicCookieStore cookieStore,
                                 Map<String, String> cookieMap,
                                 MailUtil mailUtil, String blogUrl, Map<String, String> serverInfo,
                                 CloudInfo cloudInfo, String ukLog, String username, String cloudName)
            throws IOException, GitAPIException {

        File screenshotFile = new File(Profile.PJ_PIC_PATH);
        String response = submitBlogInfo(httpClient, cookieStore, cookieMap, cloudInfo, screenshotFile, blogUrl, ukLog);
        handleSubmissionResult(response, blogUrl, cloudName, username, mailUtil, ukLog, screenshotFile);
    }

    private String submitBlogInfo(HttpClient httpClient, BasicCookieStore cookieStore,
                                  Map<String, String> cookieMap,
                                  CloudInfo cloudInfo, File screenshotFile, String blogUrl, String ukLog)
            throws IOException {

        log.info("{}提交延期博客信息", ukLog);
        HttpEntity entity = CloudPostParams.getBlogInfo(cloudInfo, screenshotFile, blogUrl).build();

        return HttpUtil.getPostRes(
                httpClient,
                cloudInfo.getBusUri(),
                entity,
                HttpUtil.buildHeadersWithCookies(null, cookieMap)
        );
    }

    private void handleSubmissionResult(String response, String blogUrl, String cloudName,
                                        String username, MailUtil mailUtil, String ukLog, File screenshotFile) {

        JSONObject json = JSONUtils.parseObject(response);
        log.info("{}提交延期记录返回结果：{}", ukLog, json);

        if (CloudDataKey.BLOG_SUCCESS.equals(json.getString(CloudDataKey.BLOG_STATUS))) {
            log.info("{}提交延期记录成功", ukLog);
        } else {
            log.error("{}提交延期记录失败，删除发布博客", ukLog);
            mailUtil.sendMail(cloudName + "账号:" + username + "发送延期博客失败", json.toString());
            try {
                BlogGit.deleteBlog(blogUrl);
            } catch (Exception e) {
                log.error("{}删除博客失败", ukLog, e);
            }
        }

        if (screenshotFile.exists()) {
            screenshotFile.delete();
            log.debug("已删除临时截图文件");
        }
    }

    private void cleanupFailedBlog(String blogUrl, String cloudName, String username, MailUtil mailUtil) {
        try {
            BlogGit.deleteBlog(blogUrl);
        } catch (Exception e) {
            log.error("删除失败博客时出错：{}", blogUrl, e);
        }
        mailUtil.sendMail(cloudName + "账号:" + username + ",网页截图生成失败", "blog Url: " + blogUrl);
    }

    private void handleException(Map<String, String> serverInfo, Exception e, MailUtil mailUtil) {
        String username = serverInfo.get(Constans.CLOUD_USERNAME);
        String type = serverInfo.get(Constans.CLOUD_TYPE);
        CloudInfo cloudInfo = CloudInfo.getCloudInfo(type);

        if (cloudInfo != null) {
            String cloudName = cloudInfo.getCloudName();
            String ukLog = CommonCode.getUKLog(username, cloudName);
            log.error("{}延期过程出错", ukLog, e);
            mailUtil.sendMail(cloudName + "账号：" + username + ",延期过程出错", e.getMessage());
        } else {
            log.error("处理服务器配置时出错：{}", serverInfo, e);
            mailUtil.sendMail("服务器配置错误", "无法获取云服务信息：" + serverInfo);
        }
    }
}
