package net.lab1024.sa.base.common.enumeration;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 通用状态枚举
 * 用于表示数据的“开启/关闭”或“启用/禁用”状态
 *
 * @author SmartAdmin AI
 */
@Getter
@AllArgsConstructor
public enum CommonStatusEnum implements IEnum<Integer> {

    /**
     * 开启/启用
     */
    ENABLE(0, "开启"),

    /**
     * 关闭/禁用
     */
    DISABLE(1, "关闭");

    /**
     * 数据库存储值 (0: 开启, 1: 关闭)
     */
    private final Integer value;

    /**
     * 描述/前端展示
     */
    private final String desc;

    /**
     * Jackson 序列化配置
     * 前端获取数据时，直接返回 desc (如 "开启")
     * 如果前端需要 value，可去掉此注解或调整序列化策略
     */
    @Override
    @JsonValue
    public String toString() {
        return this.desc;
    }

    /**
     * 根据 value 获取枚举
     * 用于业务逻辑判断，例如：CommonStatusEnum.valueOf(1) -> ENABLE
     */
    public static CommonStatusEnum valueOf(Integer value) {
        if (value == null) {
            return null;
        }
        return Arrays.stream(values())
                .filter(item -> item.value.equals(value))
                .findFirst()
                .orElse(null);
    }
}