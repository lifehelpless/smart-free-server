package net.lab1024.sa.admin.module.business.membership.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.membership.domain.form.order.MemberOrderAddForm;
import net.lab1024.sa.admin.module.business.membership.domain.form.order.MemberOrderQueryForm;
import net.lab1024.sa.admin.module.business.membership.domain.vo.MemberOrderVO;
import net.lab1024.sa.admin.module.business.membership.service.MemberOrderService;
import net.lab1024.sa.base.common.domain.PageResult;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 业务订单表 Controller
 *
 * @Author Mxl
 * @Date 2026-03-20 14:55:12
 * @Copyright 1.0
 */

@RestController
@Tag(name = "业务订单表")
public class MemberOrderController {

    @Resource
    private MemberOrderService memberOrderService;

    @Operation(summary = "分页查询 @author Mxl")
    @PostMapping("/order/queryPage")
    @SaCheckPermission("order:query")
    public ResponseDTO<PageResult<MemberOrderVO>> queryPage(@RequestBody @Valid MemberOrderQueryForm queryForm) {
        return ResponseDTO.ok(memberOrderService.queryPage(queryForm));
    }


    @PostMapping("/create")
    @Operation(summary = "创建待支付订单")
    public ResponseDTO<String> createOrder(@RequestBody @Valid MemberOrderAddForm addForm) {
        String orderNo = memberOrderService.createBizOrder(addForm);
        return ResponseDTO.ok(orderNo);
    }

}
