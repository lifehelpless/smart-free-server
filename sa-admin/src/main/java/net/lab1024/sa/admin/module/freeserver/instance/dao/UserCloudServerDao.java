package net.lab1024.sa.admin.module.freeserver.instance.dao;

import java.util.List;
import net.lab1024.sa.admin.module.freeserver.instance.domain.entity.UserCloudServerEntity;
import net.lab1024.sa.admin.module.freeserver.instance.domain.form.UserCloudServerQueryForm;
import net.lab1024.sa.admin.module.freeserver.instance.domain.vo.UserCloudServerVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户云服务器配置 Dao
 *
 * @Author Mxl
 * @Date 2026-03-19 15:05:54
 * @Copyright 无
 */

@Mapper
public interface UserCloudServerDao extends BaseMapper<UserCloudServerEntity> {

    /**
     * 分页 查询
     *
     * @param page
     * @param queryForm
     * @return
     */
    List<UserCloudServerVO> queryPage(Page page, @Param("queryForm") UserCloudServerQueryForm queryForm);

}
