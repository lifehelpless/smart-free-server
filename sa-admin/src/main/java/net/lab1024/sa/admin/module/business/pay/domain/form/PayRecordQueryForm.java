package net.lab1024.sa.admin.module.business.pay.domain.form;

import net.lab1024.sa.base.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付流水表 分页查询表单
 *
 * @Author Mxl
 * @Date 2026-03-20 14:59:51
 * @Copyright 1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class PayRecordQueryForm extends PageParam {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "流水号")
    private String payNo;

}
