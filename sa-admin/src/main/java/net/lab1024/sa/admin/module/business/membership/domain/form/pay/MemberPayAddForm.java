package net.lab1024.sa.admin.module.business.membership.domain.form.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "发起支付请求参数")
public class MemberPayAddForm {
    @NotBlank(message = "业务订单号不能为空")
    @Schema(description = "关联的业务订单号")
    private String orderNo;

    @NotBlank(message = "支付渠道不能为空")
    @Schema(description = "支付渠道: WECHAT, ALIPAY")
    private String payChannel;

//    @NotNull(message = "支付金额不能为空")
//    @Schema(description = "实际需支付金额")
//    private BigDecimal payAmount;
}