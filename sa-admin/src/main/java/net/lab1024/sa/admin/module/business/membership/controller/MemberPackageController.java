package net.lab1024.sa.admin.module.business.membership.controller;

import net.lab1024.sa.admin.module.business.membership.controller.base.MemberBaseController;
import net.lab1024.sa.admin.module.business.membership.domain.form.vpackage.MemberPackageAddForm;
import net.lab1024.sa.admin.module.business.membership.domain.form.vpackage.MemberPackageQueryForm;
import net.lab1024.sa.admin.module.business.membership.domain.form.vpackage.MemberPackageUpdateForm;
import net.lab1024.sa.admin.module.business.membership.domain.vo.MemberPackageVO;
import net.lab1024.sa.admin.module.business.membership.service.MemberPackageService;
import net.lab1024.sa.base.common.domain.ValidateList;
import org.springframework.web.bind.annotation.*;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import cn.dev33.satoken.annotation.SaCheckPermission;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;

import java.util.List;

/**
 * VIP会员套餐表 Controller
 *
 * @Author Mxl
 * @Date 2026-03-20 16:52:08
 * @Copyright Mxl
 */

@RestController
@Tag(name = "VIP会员套餐表")
public class MemberPackageController extends MemberBaseController {

    @Resource
    private MemberPackageService memberPackageService;

    @Operation(summary = "分页查询 @author Mxl")
    @PostMapping("/package/queryPage")
    @SaCheckPermission("memberPackage:query")
    public ResponseDTO<PageResult<MemberPackageVO>> queryPage(@RequestBody @Valid MemberPackageQueryForm queryForm) {
        return ResponseDTO.ok(memberPackageService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author Mxl")
    @PostMapping("/package/add")
    @SaCheckPermission("memberPackage:add")
    public ResponseDTO<String> add(@RequestBody @Valid MemberPackageAddForm addForm) {
        return memberPackageService.add(addForm);
    }

    @Operation(summary = "更新 @author Mxl")
    @PostMapping("/package/update")
    @SaCheckPermission("memberPackage:update")
    public ResponseDTO<String> update(@RequestBody @Valid MemberPackageUpdateForm updateForm) {
        return memberPackageService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author Mxl")
    @PostMapping("/package/batchDelete")
    @SaCheckPermission("memberPackage:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return memberPackageService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author Mxl")
    @GetMapping("/package/delete/{id}")
    @SaCheckPermission("memberPackage:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long id) {
        return memberPackageService.delete(id);
    }


    @Operation(summary = "用户查询可用会员 @author Mxl")
    @GetMapping("/package/user/simp-list")
    public ResponseDTO<List<MemberPackageVO>> simpList() {
        return ResponseDTO.ok(memberPackageService.queryList(0));
    }
}
