package net.lab1024.sa.admin.module.business.membership.service;

import java.util.List;
import java.util.Objects;
import net.lab1024.sa.admin.module.business.membership.constant.MemberLevelEnum;
import net.lab1024.sa.admin.module.business.membership.dao.MemberPackageDao;
import net.lab1024.sa.admin.module.business.membership.domain.entity.MemberPackageEntity;
import net.lab1024.sa.admin.module.business.membership.domain.vo.MemberPackageVO;
import net.lab1024.sa.admin.module.business.membership.domain.form.vpackage.MemberPackageAddForm;
import net.lab1024.sa.admin.module.business.membership.domain.form.vpackage.MemberPackageQueryForm;
import net.lab1024.sa.admin.module.business.membership.domain.form.vpackage.MemberPackageUpdateForm;
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
public class MemberPackageService {

    @Resource
    private MemberPackageDao memberPackageDao;

    /**
     * 分页查询
     */
    public PageResult<MemberPackageVO> queryPage(MemberPackageQueryForm queryForm) {
        Page<?> page = SmartPageUtil.convert2PageQuery(queryForm);
        List<MemberPackageVO> list = memberPackageDao.queryPage(page, queryForm);
        return SmartPageUtil.convert2PageResult(page, list);
    }

    /**
     * 添加
     */
    public ResponseDTO<String> add(MemberPackageAddForm addForm) {
        validateVipLevelUnique(null, addForm.getVipLevel());
        MemberPackageEntity vipPackageEntity = SmartBeanUtil.copy(addForm, MemberPackageEntity.class);
        memberPackageDao.insert(vipPackageEntity);
        return ResponseDTO.ok();
    }

    /**
     * 更新
     *
     */
    public ResponseDTO<String> update(MemberPackageUpdateForm updateForm) {
        validateVipLevelUnique(updateForm.getId(), updateForm.getVipLevel());
        MemberPackageEntity vipPackageEntity = SmartBeanUtil.copy(updateForm, MemberPackageEntity.class);
        memberPackageDao.updateById(vipPackageEntity);
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

        memberPackageDao.deleteBatchIds(idList);
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

        memberPackageDao.deleteById(id);
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
        if (MemberLevelEnum.getByCode(vipLevel) == null) {
            throw new BusinessException("会员等级不合法！");
        }

        // 2. 查询数据库是否已存在该等级
        MemberPackageEntity vipPackageEntity = memberPackageDao.selectPackageByVipLevel(vipLevel);
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
        MemberPackageEntity memberPackageEntity = memberPackageDao.selectById(id);
        if (memberPackageEntity == null) {
            throw new BusinessException("要删除的数据不存在");
        }
        MemberLevelEnum vipLevelEnum = MemberLevelEnum.getByCode(memberPackageEntity.getVipLevel());
        if (vipLevelEnum != null) {
            throw new BusinessException("系统内置数据，不可删除");
        }
    }
}
