package net.lab1024.sa.admin.module.business.membership.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.math.BigDecimal;

/**
 * 支付成功事件
 */
@Getter
public class PaySuccessEvent extends ApplicationEvent {

    private final String orderNo;
    private final String payNo;
    private final String thirdPartyNo;
    private final BigDecimal payAmount;

    public PaySuccessEvent(Object source, String orderNo, String payNo, String thirdPartyNo, BigDecimal payAmount) {
        super(source);
        this.orderNo = orderNo;
        this.payNo = payNo;
        this.thirdPartyNo = thirdPartyNo;
        this.payAmount = payAmount;
    }
}