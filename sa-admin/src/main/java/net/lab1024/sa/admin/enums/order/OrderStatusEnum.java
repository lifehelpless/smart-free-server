package net.lab1024.sa.admin.enums.order;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态枚举
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    WAIT_PAY(0, "待支付"),
    PAID(1, "已支付"),
    CANCELED(2, "已取消"),
    REFUNDED(3, "已退款");

    private final Integer code;
    private final String desc;

    /**
     * 根据业务码获取描述信息
     *
     * @param code 业务状态码
     * @return 状态描述
     */
    public static String getDescByCode(Integer code) {
        if (code == null) {
            return "未知状态";
        }
        for (OrderStatusEnum statusEnum : OrderStatusEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getDesc();
            }
        }
        return "未知状态";
    }
}