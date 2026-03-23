package net.lab1024.sa.admin.module.business.membership.constant;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员权益状态枚举
 */
@Getter
@AllArgsConstructor
public enum MemberStatusEnum {

    EXPIRED(0, "已过期"),
    VALID(1, "生效中");

    /**
     * @EnumValue 标记数据库存的值是这个 code
     */
    @EnumValue
    private final Integer code;

    private final String desc;

    /**
     * 根据业务码获取描述信息
     *
     * @param code 状态码
     * @return 状态描述
     */
    public static String getDescByCode(Integer code) {
        if (code == null) {
            return "未知状态";
        }
        for (MemberStatusEnum statusEnum : MemberStatusEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum.getDesc();
            }
        }
        return "未知状态";
    }
}