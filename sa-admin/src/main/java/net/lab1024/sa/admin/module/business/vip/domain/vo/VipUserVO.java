package net.lab1024.sa.admin.module.business.vip.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 用户VIP权益表 列表VO
 *
 * @Author Mxl
 * @Date 2026-03-20 15:07:19
 * @Copyright 1.0
 */

@Data
public class VipUserVO {


    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "VIP等级")
    private Integer vipLevel;

    @Schema(description = "VIP开始时间")
    private LocalDateTime startTime;

    @Schema(description = "VIP过期时间")
    private LocalDateTime endTime;

    @Schema(description = "状态: 0-已过期, 1-生效中")
    private Integer status;

    @Schema(description = "创建日期")
    private LocalDateTime createTime;

    @Schema(description = "修改日期")
    private LocalDateTime updateTime;

}
