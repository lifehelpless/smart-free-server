package net.lab1024.sa.admin.enums.vip;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会员等级枚举
 */
@Getter
@AllArgsConstructor
public enum VipLevelEnum {

    NONE(0, "基础会员"),
    NORMAL_VIP(1, "普通会员"),
    SUPER_VIP(2, "超级会员"),
    DIAMOND_VIP(3, "钻石会员");

    private final Integer code;
    private final String desc;

    /**
     * 根据业务码获取描述信息
     *
     * @param code 会员等级码
     * @return 等级描述
     */
    public static String getDescByCode(Integer code) {
        if (code == null) {
            return "未知等级";
        }
        VipLevelEnum vipLevelEnum = getByCode(code);
        if (vipLevelEnum != null) {
            return vipLevelEnum.getDesc();
        }
        return "未知等级";
    }

    public static VipLevelEnum getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        for (VipLevelEnum levelEnum : VipLevelEnum.values()) {
            if (levelEnum.getCode().equals(code)) {
                return levelEnum;
            }
        }
        return null;
    }
}