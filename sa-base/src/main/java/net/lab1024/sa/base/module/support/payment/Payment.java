package net.lab1024.sa.base.module.support.payment;
import jakarta.servlet.http.HttpServletRequest;
import net.lab1024.sa.base.common.exception.BusinessException;
import net.lab1024.sa.base.module.support.payment.dto.PayParam;

/**
 * 支付接口
 *
 * @author Chopper
 * @since 2020-12-21 09:32
 */
public interface Payment {

    /**
     * 展示二维码扫描支付
     *
     * @param request  HttpServletRequest
     * @param payParam 支付参数
     * @return 二维码内容
     */
    default Object nativePay(HttpServletRequest request, PayParam payParam) {
        throw new BusinessException("支付异常");
    }

    /**
     * 回调
     *
     * @param request HttpServletRequest
     */
    default void callBack(HttpServletRequest request) {
        throw new BusinessException("支付业务异常，请稍后重试");
    }


}
