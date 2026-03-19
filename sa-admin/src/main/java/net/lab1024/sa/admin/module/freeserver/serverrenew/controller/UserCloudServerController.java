package net.lab1024.sa.admin.module.freeserver.serverrenew.controller;

import net.lab1024.sa.admin.module.freeserver.serverrenew.domain.form.UserCloudServerAddForm;
import net.lab1024.sa.admin.module.freeserver.serverrenew.domain.form.UserCloudServerQueryForm;
import net.lab1024.sa.admin.module.freeserver.serverrenew.domain.form.UserCloudServerUpdateForm;
import net.lab1024.sa.admin.module.freeserver.serverrenew.domain.vo.UserCloudServerVO;
import net.lab1024.sa.admin.module.freeserver.serverrenew.service.UserCloudServerService;
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
 * 用户云服务器配置 Controller
 *
 * @Author Mxl
 * @Date 2026-03-19 15:05:54
 * @Copyright 无
 */

@RestController
@Tag(name = "用户云服务器配置")
public class UserCloudServerController {

    @Resource
    private UserCloudServerService userCloudServerService;

    @Operation(summary = "分页查询 @author Mxl")
    @PostMapping("/userCloudServer/queryPage")
    @SaCheckPermission("userCloudServer:query")
    public ResponseDTO<PageResult<UserCloudServerVO>> queryPage(@RequestBody @Valid UserCloudServerQueryForm queryForm) {
        return ResponseDTO.ok(userCloudServerService.queryPage(queryForm));
    }

    @Operation(summary = "添加 @author Mxl")
    @PostMapping("/userCloudServer/add")
    @SaCheckPermission("userCloudServer:add")
    public ResponseDTO<String> add(@RequestBody @Valid UserCloudServerAddForm addForm) {
        return userCloudServerService.add(addForm);
    }

    @Operation(summary = "更新 @author Mxl")
    @PostMapping("/userCloudServer/update")
    @SaCheckPermission("userCloudServer:update")
    public ResponseDTO<String> update(@RequestBody @Valid UserCloudServerUpdateForm updateForm) {
        return userCloudServerService.update(updateForm);
    }

    @Operation(summary = "批量删除 @author Mxl")
    @PostMapping("/userCloudServer/batchDelete")
    @SaCheckPermission("userCloudServer:delete")
    public ResponseDTO<String> batchDelete(@RequestBody ValidateList<Long> idList) {
        return userCloudServerService.batchDelete(idList);
    }

    @Operation(summary = "单个删除 @author Mxl")
    @GetMapping("/userCloudServer/delete/{id}")
    @SaCheckPermission("userCloudServer:delete")
    public ResponseDTO<String> batchDelete(@PathVariable Long id) {
        return userCloudServerService.delete(id);
    }
}
