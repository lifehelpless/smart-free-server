package net.lab1024.sa.admin.module.business.vip.service;

import java.util.List;
import java.util.Objects;
import net.lab1024.sa.admin.enums.vip.VipLevelEnum;
import net.lab1024.sa.admin.module.business.vip.dao.VipPackageDao;
import net.lab1024.sa.admin.module.business.vip.domain.entity.VipPackageEntity;
import net.lab1024.sa.admin.module.business.vip.domain.form.VipPackageAddForm;
import net.lab1024.sa.admin.module.business.vip.domain.form.VipPackageQueryForm;
import net.lab1024.sa.admin.module.business.vip.domain.form.VipPackageUpdateForm;
import net.lab1024.sa.admin.module.business.vip.domain.vo.VipPackageVO;
import net.lab1024.sa.base.common.exception.BusinessException;
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
        validateVipLevelUnique(null, addForm.getVipLevel());
        VipPackageEntity vipPackageEntity = SmartBeanUtil.copy(addForm, VipPackageEntity.class);
        vipPackageDao.insert(vipPackageEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(VipPackageUpdateForm updateForm) {
        validateVipLevelUnique(updateForm.getId(), updateForm.getVipLevel());
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
        for (Long id : idList) {
            validateDeletePermission(id);
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

        validateDeletePermission(id);

        vipPackageDao.deleteById(id);
        return ResponseDTO.ok();
    }

    /**
     * 校验是否可变更
     */
    public void validateUpdatePermission() {

    }

    /**
     * 校验会员是否唯一
     */
    public void validateVipLevelUnique(Long id, Integer vipLevel) {
        // 1. 先判断等级是否合法
        if (VipLevelEnum.getByCode(vipLevel) == null) {
            throw new BusinessException("会员等级不合法！");
        }

        // 2. 查询数据库是否已存在该等级
        VipPackageEntity vipPackageEntity = vipPackageDao.selectPackageByVipLevel(vipLevel);
        if (vipPackageEntity != null) {
            if (id == null || !Objects.equals(vipPackageEntity.getId(), id)) {
                throw new BusinessException("该会员等级已配置套餐，不允许重复！");
            }
        }
    }


    /**
     * 校验是否可以删除
     */
    public void validateDeletePermission(Long id) {
        VipPackageEntity vipPackageEntity = vipPackageDao.selectById(id);
        if (vipPackageEntity == null) {
            throw new BusinessException("要删除的数据不存在");
        }
        VipLevelEnum vipLevelEnum = VipLevelEnum.getByCode(vipPackageEntity.getVipLevel());
        if (vipLevelEnum != null) {
            throw new BusinessException("系统内置数据，不可删除");
        }
    }
}
