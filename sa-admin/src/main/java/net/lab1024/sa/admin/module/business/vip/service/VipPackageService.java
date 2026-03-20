package net.lab1024.sa.admin.module.business.vip.service;

import java.util.List;
import net.lab1024.sa.admin.module.business.vip.dao.VipPackageDao;
import net.lab1024.sa.admin.module.business.vip.domain.entity.VipPackageEntity;
import net.lab1024.sa.admin.module.business.vip.domain.form.VipPackageAddForm;
import net.lab1024.sa.admin.module.business.vip.domain.form.VipPackageQueryForm;
import net.lab1024.sa.admin.module.business.vip.domain.form.VipPackageUpdateForm;
import net.lab1024.sa.admin.module.business.vip.domain.vo.VipPackageVO;
import net.lab1024.sa.base.common.util.SmartBeanUtil;
import net.lab1024.sa.base.common.util.SmartPageUtil;
import net.lab1024.sa.base.common.domain.ResponseDTO;
import net.lab1024.sa.base.common.domain.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * VIP会员套餐表 Service
 *
 * @Author Mxl
 * @Date 2026-03-20 16:52:08
 * @Copyright Mxl
 */

@Service
public class VipPackageService {

    @Resource
    private VipPackageDao vipPackageDao;

    /**
     * 分页查询
     */
    public PageResult<VipPackageVO> queryPage(VipPackageQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<VipPackageVO> list = vipPackageDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(VipPackageAddForm addForm) {
        VipPackageEntity vipPackageEntity = SmartBeanUtil.copy(addForm, VipPackageEntity.class);
        vipPackageDao.insert(vipPackageEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(VipPackageUpdateForm updateForm) {
        VipPackageEntity vipPackageEntity = SmartBeanUtil.copy(updateForm, VipPackageEntity.class);
        vipPackageDao.updateById(vipPackageEntity);
        return ResponseDTO.ok();
    }

    /**
     * 批量删除
     */
    public ResponseDTO<String> batchDelete(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)){
            return ResponseDTO.ok();
        }

        vipPackageDao.deleteBatchIds(idList);
        return ResponseDTO.ok();
    }

    /**
     * 单个删除
     */
    public ResponseDTO<String> delete(Long id) {
        if (null == id){
            return ResponseDTO.ok();
        }

        vipPackageDao.deleteById(id);
        return ResponseDTO.ok();
    }
}
