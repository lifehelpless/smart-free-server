package net.lab1024.sa.admin.enums.pay;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支付流水状态枚举
 */
@Getter
@AllArgsConstructor
public enum PayStatusEnum {

    PAYING(0, "支付中"),
    SUCCESS(1, "支付成功"),
    FAIL(2, "支付失败"),
    CLOSED(3, "支付关闭");

    private final Integer code;
    private final String desc;

    /**
     * 根据业务码获取描述信息
     *
     * @param code 支付状态码
     * @return 状态描述
     */
    public static String getDescByCode(Integer code) {
        if (code == null) {
            return "未知状态";
        }
        for (PayStatusEnum statusEnum : PayStatusEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getDesc();
            }
        }
        return "未知状态";
    }
}