package net.lab1024.sa.admin.module.business.vip.controller;

import net.lab1024.sa.admin.module.business.vip.domain.form.VipPackageAddForm;
import net.lab1024.sa.admin.module.business.vip.domain.form.VipPackageQueryForm;
import net.lab1024.sa.admin.module.business.vip.domain.form.VipPackageUpdateForm;
import net.lab1024.sa.admin.module.business.vip.domain.vo.VipPackageVO;
import net.lab1024.sa.admin.module.business.vip.service.VipPackageService;
import net.lab1024.sa.base.common.domain.ValidateList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
 * VIP会员套餐表 Controller
 *
 * @Author Mxl
 * @Date 2026-03-20 16:52:08
 * @Copyright Mxl
 */

@RestController
@Tag(name = "VIP会员套餐表")
public class VipPackageController {

    @Resource
    private VipPackageService vipPackageService;

    @Operation(summary = "分页查询 @author Mxl")
    @PostMapping("/vipPackage/queryPage")
    @SaCheckPermission("vipPackage:query")
    public ResponseDTO<PageResult<VipPackageVO>> queryPage(@RequestBody @Valid VipPackageQueryForm queryForm) {
        return ResponseDTO.ok(vipPackageService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author Mxl")
    @PostMapping("/vipPackage/add")
    @SaCheckPermission("vipPackage:add")
    public ResponseDTO<String> add(@RequestBody @Valid VipPackageAddForm addForm) {
        return vipPackageService.add(addForm);
    }

    @Operation(summary = "更新 @author Mxl")
    @PostMapping("/vipPackage/update")
    @SaCheckPermission("vipPackage:update")
    public ResponseDTO<String> update(@RequestBody @Valid VipPackageUpdateForm updateForm) {
        return vipPackageService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author Mxl")
    @PostMapping("/vipPackage/batchDelete")
    @SaCheckPermission("vipPackage:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return vipPackageService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author Mxl")
    @GetMapping("/vipPackage/delete/{id}")
    @SaCheckPermission("vipPackage:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long id) {
        return vipPackageService.delete(id);
    }
}
