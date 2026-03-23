package net.lab1024.sa.admin.module.business.membership.controller;

import net.lab1024.sa.admin.module.business.membership.domain.form.pay.MemberPayAddForm;
import net.lab1024.sa.admin.module.business.membership.domain.form.pay.MemberPayQueryForm;
import net.lab1024.sa.admin.module.business.membership.domain.vo.MemberPayVO;
import net.lab1024.sa.admin.module.business.membership.service.MemberPayService;
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
public class MemberPayController {

    @Resource
    private MemberPayService memberPayService;

    @Operation(summary = "分页查询 @author Mxl")
    @PostMapping("/payRecord/queryPage")
    @SaCheckPermission("payRecord:query")
    public ResponseDTO<PageResult<MemberPayVO>> queryPage(@RequestBody @Valid MemberPayQueryForm queryForm) {
        return ResponseDTO.ok(memberPayService.queryPage(queryForm));
    }


    @Operation(summary = "支付 @author Mxl")
    @PostMapping("/payRecord/pay")
    public ResponseDTO<String> pay(@RequestBody @Valid MemberPayAddForm addForm) {
        return ResponseDTO.ok(memberPayService.doPay(addForm));
    }


}
