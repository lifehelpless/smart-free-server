package net.lab1024.sa.admin.module.business.vip.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import net.lab1024.sa.admin.module.business.vip.domain.entity.VipPackageEntity;
import net.lab1024.sa.admin.module.business.vip.domain.form.VipPackageQueryForm;
import net.lab1024.sa.admin.module.business.vip.domain.vo.VipPackageVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * VIP会员套餐表 Dao
 *
 * @Author Mxl
 * @Date 2026-03-20 16:52:08
 * @Copyright Mxl
 */

@Mapper
public interface VipPackageDao extends BaseMapper<VipPackageEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<VipPackageVO> queryPage(Page page, @Param("queryForm") VipPackageQueryForm queryForm);


    default VipPackageEntity selectPackageByVipLevel(Integer vipLevel) {
        LambdaQueryWrapper<VipPackageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VipPackageEntity::getVipLevel, vipLevel);

        return this.selectOne(wrapper);
    }

}
