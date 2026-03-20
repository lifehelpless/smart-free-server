package net.lab1024.sa.admin.module.business.vip.controller;

import net.lab1024.sa.admin.module.business.vip.domain.form.VipUserQueryForm;
import net.lab1024.sa.admin.module.business.vip.domain.vo.VipUserVO;
import net.lab1024.sa.admin.module.business.vip.service.VipUserService;
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
 * 用户VIP权益表 Controller
 *
 * @Author Mxl
 * @Date 2026-03-20 15:07:19
 * @Copyright 1.0
 */

@RestController
@Tag(name = "用户VIP权益表")
public class VipUserController {

    @Resource
    private VipUserService vipUserService;

    @Operation(summary = "分页查询 @author Mxl")
    @PostMapping("/userVip/queryPage")
    @SaCheckPermission("userVip:query")
    public ResponseDTO<PageResult<VipUserVO>> queryPage(@RequestBody @Valid VipUserQueryForm queryForm) {
        return ResponseDTO.ok(vipUserService.queryPage(queryForm));
    }


}
