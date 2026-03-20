package net.lab1024.sa.admin.module.business.order.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "创建订单请求参数")
public class OrderAddForm {
    @NotNull(message = "用户ID不能为空")
    @Schema(description = "用户ID")
    private Long userId;

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品(VIP套餐)ID")
    private Long goodsId;

    @Schema(description = "商品名称")
    private String goodsName;

    @NotNull(message = "订单金额不能为空")
    @DecimalMin(value = "0.01", message = "订单金额必须大于0")
    @Schema(description = "实际支付金额")
    private BigDecimal orderAmount;
}