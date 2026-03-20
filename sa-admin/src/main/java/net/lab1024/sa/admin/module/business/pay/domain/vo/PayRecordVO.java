package net.lab1024.sa.admin.module.business.pay.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 支付流水表 列表VO
 *
 * @Author Mxl
 * @Date 2026-03-20 14:59:51
 * @Copyright 1.0
 */

@Data
public class PayRecordVO {


    @Schema(description = "主键")
    private Long id;

    @Schema(description = "支付流水号(发给第三方的单号)")
    private String payNo;

    @Schema(description = "关联业务订单号")
    private String orderNo;

    @Schema(description = "支付渠道: WECHAT, ALIPAY")
    private String payChannel;

    @Schema(description = "实际支付金额")
    private BigDecimal payAmount;

    @Schema(description = "支付状态: 0-支付中, 1-支付成功, 2-支付失败")
    private Integer status;

    @Schema(description = "第三方支付交易号")
    private String thirdPartyNo;

    @Schema(description = "支付成功时间")
    private LocalDateTime payTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
