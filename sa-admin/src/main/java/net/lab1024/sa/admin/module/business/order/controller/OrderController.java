package net.lab1024.sa.admin.module.business.order.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import net.lab1024.sa.admin.module.business.order.domain.form.OrderAddForm;
import net.lab1024.sa.admin.module.business.order.domain.form.OrderQueryForm;
import net.lab1024.sa.admin.module.business.order.domain.vo.OrderVO;
import net.lab1024.sa.admin.module.business.order.service.OrderService;
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
public class OrderController {

    @Resource
    private OrderService orderService;

    @Operation(summary = "分页查询 @author Mxl")
    @PostMapping("/order/queryPage")
    @SaCheckPermission("order:query")
    public ResponseDTO<PageResult<OrderVO>> queryPage(@RequestBody @Valid OrderQueryForm queryForm) {
        return ResponseDTO.ok(orderService.queryPage(queryForm));
    }


    @PostMapping("/create")
    @Operation(summary = "创建待支付订单")
    public ResponseDTO<String> createOrder(@RequestBody @Valid OrderAddForm addForm) {
        String orderNo = orderService.createBizOrder(addForm);
        return ResponseDTO.ok(orderNo);
    }

}
