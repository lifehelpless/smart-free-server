package net.lab1024.sa.admin.module.freeserver.renew.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MethodRenewalTypeEnum {
    /**
     * 图片
     */
    IMAGE(1),
    /**
     * 主机网站
     */
    ZJ_PC(2);

    private final Integer type;


    private static boolean exists(Integer type) {
        for (MethodRenewalTypeEnum value : MethodRenewalTypeEnum.values()) {
            if (value.type.equals(type)) {
                return true;
            }
        }
        return false;
    }
}
