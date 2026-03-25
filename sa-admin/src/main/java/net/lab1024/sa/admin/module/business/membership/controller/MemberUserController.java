package net.lab1024.sa.admin.module.business.membership.controller;

import net.lab1024.sa.admin.module.business.membership.controller.base.MemberBaseController;
import net.lab1024.sa.admin.module.business.membership.domain.form.user.MemberUserQueryForm;
import net.lab1024.sa.admin.module.business.membership.domain.vo.MemberUserVO;
import net.lab1024.sa.admin.module.business.membership.service.MemberUserService;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class MemberUserController extends MemberBaseController {

    @Resource
    private MemberUserService memberUserService;

    @Operation(summary = "分页查询 @author Mxl")
    @PostMapping("/user/queryPage")
    @SaCheckPermission("user:query")
    public ResponseDTO<PageResult<MemberUserVO>> queryPage(@RequestBody @Valid MemberUserQueryForm queryForm) {
        return ResponseDTO.ok(memberUserService.queryPage(queryForm));
    }


}
