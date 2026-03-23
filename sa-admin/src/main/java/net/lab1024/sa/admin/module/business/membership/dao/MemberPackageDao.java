package net.lab1024.sa.admin.module.business.membership.dao;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.lab1024.sa.admin.module.business.membership.domain.entity.MemberPackageEntity;
import net.lab1024.sa.admin.module.business.membership.domain.form.vpackage.MemberPackageQueryForm;
import net.lab1024.sa.admin.module.business.membership.domain.vo.MemberPackageVO;
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
public interface MemberPackageDao extends BaseMapper<MemberPackageEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<MemberPackageVO> queryPage(Page page, @Param("queryForm") MemberPackageQueryForm queryForm);


    default MemberPackageEntity selectPackageByVipLevel(Integer vipLevel) {
        LambdaQueryWrapper<MemberPackageEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MemberPackageEntity::getVipLevel, vipLevel);

        return this.selectOne(wrapper);
    }

}
