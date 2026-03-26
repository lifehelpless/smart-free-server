package net.lab1024.sa.base.module.support.payment.alipay;

import cn.hutool.http.HtmlUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradePrecreateModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.context.ThreadContextHolder;
import net.lab1024.sa.base.common.exception.BusinessException;
import net.lab1024.sa.base.module.support.payment.Payment;
import net.lab1024.sa.base.module.support.payment.dto.PayParam;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝支付
 *
 * @author Chopper
 * @since 2020/12/17 09:55
 */
@Slf4j
@Component
public class AliPayPlugin implements Payment {

    // 临时硬编码配置 (正式环境请走配置中心)
    private static final String APP_ID = "你的AppID";
    private static final String MERCHANT_PRIVATE_KEY = "你的应用私钥";
    private static final String ALIPAY_PUBLIC_KEY = "支付宝公钥";
    private static final String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";

    @Override
    public Object nativePay(HttpServletRequest request, PayParam payParam) {
        try {
            // 2. 构造支付模型
            AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
            // 唯一流水号：建议 订单号 + 时间戳/雪花ID
            String outTradeNo = "PAY_" + payParam.getOrderNo() + "_" + System.currentTimeMillis();

            model.setOutTradeNo(outTradeNo);
            model.setTotalAmount("30.00"); // 示例金额
            model.setSubject("业务订单支付-" + payParam.getOrderNo());
            model.setTimeoutExpress("5m"); // 设置 5 分钟失效

            // 3. 执行请求 (此处假设你使用的是支付宝 SDK 原始调用方式)
            AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, APP_ID, MERCHANT_PRIVATE_KEY, "json", "UTF-8", ALIPAY_PUBLIC_KEY, "RSA2");
            AlipayTradePrecreateRequest alipayRequest = new AlipayTradePrecreateRequest();
            alipayRequest.setBizModel(model);
            // 设置回调地址 (需公网可访问)
            alipayRequest.setNotifyUrl("http://your-domain.com/api/order/pay/notify");

            AlipayTradePrecreateResponse response = alipayClient.execute(alipayRequest);

            if (response.isSuccess()) {
                log.info("支付宝预下单成功，流水号：{}", outTradeNo);
                // 4. 重要：此处应将 outTradeNo 存入数据库支付记录表，状态为“待支付”
                return response.getQrCode();
            } else {
                log.error("支付宝调用失败：{}", response.getMsg());
                throw new BusinessException(response.getMsg());
            }
        } catch (Exception e) {
            log.error("支付异常：", e);
            throw new BusinessException("发起支付异常");
        }
    }

    @Override
    public void callBack(HttpServletRequest request) {
        log.info("支付同步回调：");
        checkPaymentResult(request);

    }

    /**
     * 验证支付结果（同步回调：仅用于页面跳转）
     * 遵循 SmartAdmin 规范：逻辑尽量写在 Service/Manager，Controller 只负责跳转
     *
     * @param request 请求
     */
    private void checkPaymentResult(HttpServletRequest request) {
        try {
            //获取支付宝反馈信息
//            Map<String, String> map = AliPayApi.toMap(request);
            Map<String, String> map = toMap(request);
            log.info("同步回调：{}", JSONUtil.toJsonStr(map));
            boolean verifyResult = AlipaySignature.rsaCertCheckV1(map, ALIPAY_PUBLIC_KEY, "UTF-8",
                    "RSA2");
            if (verifyResult) {
                log.info("支付回调通知：支付成功-参数：{}", map);
            } else {
                log.info("支付回调通知：支付失败-参数：{}", map);
            }

            ThreadContextHolder.getHttpResponse().sendRedirect("域名" + "/pages/order/myOrder?status=0");
        } catch (Exception e) {
            log.error("支付回调同步通知异常", e);
        }

    }

    /**
     * 将异步通知的参数转化为Map
     *
     * @param request {HttpServletRequest}
     * @return 转化后的Map
     */
    public static Map<String, String> toMap(HttpServletRequest request) {
        Map<String, String> params = new HashMap<String, String>(16);
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, HtmlUtil.unescape(valueStr));
        }
        return params;
    }

}