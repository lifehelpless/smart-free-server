package net.lab1024.sa.admin.module.business.order.domain.form;

import net.lab1024.sa.base.common.domain.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务订单表 分页查询表单
 *
 * @Author Mxl
 * @Date 2026-03-20 14:55:12
 * @Copyright 1.0
 */

@Data
@EqualsAndHashCode(callSuper = false)
public class OrderQueryForm extends PageParam {

    @Schema(description = "订单号")
    private String orderNo;

}
