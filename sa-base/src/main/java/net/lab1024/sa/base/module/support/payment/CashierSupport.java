package net.lab1024.sa.base.module.support.payment;


import com.google.protobuf.ServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.base.common.exception.BusinessException;
import net.lab1024.sa.base.common.util.SpringContextUtil;
import net.lab1024.sa.base.module.support.payment.dto.PayParam;
import net.lab1024.sa.base.module.support.payment.enums.PaymentClientEnum;
import net.lab1024.sa.base.module.support.payment.enums.PaymentMethodEnum;
import org.springframework.stereotype.Component;

/**
 * 收银台工具
 *
 * @author Chopper
 * @since 2020-12-19 09:25
 */
@Component
@Slf4j
public class CashierSupport {


    /**
     * 支付
     *
     * @param paymentMethodEnum 支付渠道枚举
     * @param paymentClientEnum 支付方式枚举
     * @return 支付消息
     */
    public Object payment(PaymentMethodEnum paymentMethodEnum, PaymentClientEnum paymentClientEnum,
                          HttpServletRequest request, HttpServletResponse response,
                          PayParam payParam) {
        if (paymentClientEnum == null || paymentMethodEnum == null) {
            throw new BusinessException("支付暂不支持");
        }
        //获取支付插件
        Payment payment = (Payment) SpringContextUtil.getBean(paymentMethodEnum.getPlugin());
        log.info("支付请求：客户端：{},支付类型：{},请求：{}", paymentClientEnum.name(), paymentMethodEnum.name(), payParam.toString());

        //支付方式调用
        switch (paymentClientEnum) {
//            case H5:
//                return payment.h5pay(request, response, payParam);
//            case APP:
//                return payment.appPay(request, payParam);
//            case JSAPI:
//                return payment.jsApiPay(request, payParam);
            case NATIVE:
                return payment.nativePay(request, payParam);
//            case MP:
//                return payment.mpPay(request, payParam);
            default:
                return null;
        }
    }


    /**
     * 支付回调
     *
     * @param paymentMethodEnum 支付渠道枚举
     * @return 回调消息
     */
    public void callback(PaymentMethodEnum paymentMethodEnum,
                         HttpServletRequest request) {

        log.info("支付回调：支付类型：{}", paymentMethodEnum.name());

        //获取支付插件
        Payment payment = (Payment) SpringContextUtil.getBean(paymentMethodEnum.getPlugin());
        payment.callBack(request);
    }
}
