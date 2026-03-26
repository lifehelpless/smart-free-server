package net.lab1024.sa.base.module.support.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class PayParam {
    @NotNull
    @Schema(description = "交易类型", allowableValues = "TRADE,ORDER,RECHARGE")
    private String orderType;

    @NotNull
    @Schema(description = "订单号")
    private String orderNo;
//
//    @NotNull
//    @Schema(description = "客户端类型")
//    private String clientType;

}