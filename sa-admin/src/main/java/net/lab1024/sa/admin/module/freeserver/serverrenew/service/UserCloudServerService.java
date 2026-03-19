package net.lab1024.sa.admin.module.freeserver.serverrenew.service;

import java.util.List;
import net.lab1024.sa.admin.module.freeserver.serverrenew.dao.UserCloudServerDao;
import net.lab1024.sa.admin.module.freeserver.serverrenew.domain.entity.UserCloudServerEntity;
import net.lab1024.sa.admin.module.freeserver.serverrenew.domain.form.UserCloudServerAddForm;
import net.lab1024.sa.admin.module.freeserver.serverrenew.domain.form.UserCloudServerQueryForm;
import net.lab1024.sa.admin.module.freeserver.serverrenew.domain.form.UserCloudServerUpdateForm;
import net.lab1024.sa.admin.module.freeserver.serverrenew.domain.vo.UserCloudServerVO;
import net.lab1024.sa.admin.util.AdminRequestUtil;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 用户云服务器配置 Service
 *
 * @Author Mxl
 * @Date 2026-03-19 15:05:54
 * @Copyright 无
 */

@Service
public class UserCloudServerService {

    @Resource
    private UserCloudServerDao userCloudServerDao;

    /**
     * 分页查询
     */
    public PageResult<UserCloudServerVO> queryPage(UserCloudServerQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<UserCloudServerVO> list = userCloudServerDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(UserCloudServerAddForm addForm) {
        UserCloudServerEntity userCloudServerEntity = SmartBeanUtil.copy(addForm, UserCloudServerEntity.class);
        Long userId = AdminRequestUtil.getRequestUserId();
        userCloudServerEntity.setCreateId(userId);
        userCloudServerEntity.setUpdateId(userId);
        userCloudServerDao.insert(userCloudServerEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(UserCloudServerUpdateForm updateForm) {
        UserCloudServerEntity userCloudServerEntity = SmartBeanUtil.copy(updateForm, UserCloudServerEntity.class);
        userCloudServerEntity.setUpdateId(AdminRequestUtil.getRequestUserId());
        userCloudServerDao.updateById(userCloudServerEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        userCloudServerDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        userCloudServerDao.deleteById(id);
        return ResponseDTO.ok();
    }
}
