package net.lab1024.sa.admin.module.business.pay.controller;

import net.lab1024.sa.admin.module.business.pay.domain.form.PayRecordAddForm;
import net.lab1024.sa.admin.module.business.pay.domain.form.PayRecordQueryForm;
import net.lab1024.sa.admin.module.business.pay.domain.vo.PayRecordVO;
import net.lab1024.sa.admin.module.business.pay.service.PayRecordService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

/**
 * 支付流水表 Controller
 *
 * @Author Mxl
 * @Date 2026-03-20 14:59:51
 * @Copyright 1.0
 */

@RestController
@Tag(name = "支付流水表")
public class PayRecordController {

    @Resource
    private PayRecordService payRecordService;

    @Operation(summary = "分页查询 @author Mxl")
    @PostMapping("/payRecord/queryPage")
    @SaCheckPermission("payRecord:query")
    public ResponseDTO<PageResult<PayRecordVO>> queryPage(@RequestBody @Valid PayRecordQueryForm queryForm) {
        return ResponseDTO.ok(payRecordService.queryPage(queryForm));
    }


    @Operation(summary = "支付 @author Mxl")
    @PostMapping("/payRecord/pay")
    public ResponseDTO<String> pay(@RequestBody @Valid PayRecordAddForm addForm) {
        return ResponseDTO.ok(payRecordService.doPay(addForm));
    }


}
