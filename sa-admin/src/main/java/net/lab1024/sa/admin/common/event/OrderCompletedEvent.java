package net.lab1024.sa.admin.common.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 业务订单完成事件 (已支付)
 */
@Getter
public class OrderCompletedEvent extends ApplicationEvent {

    private final Long userId;
    private final Long goodsId; // 这里代表购买的 VIP 套餐 ID
    private final String orderNo;

    public OrderCompletedEvent(Object source, Long userId, Long goodsId, String orderNo) {
        super(source);
        this.userId = userId;
        this.goodsId = goodsId;
        this.orderNo = orderNo;
    }
}