package net.lab1024.sa.admin.module.job.sample;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import net.lab1024.sa.admin.module.freeserver.renew.manager.PostponeManager;
import net.lab1024.sa.base.module.support.job.core.SmartJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 定时任务  免费服务器延期任务
 *
 * @author huke
 * @date 2024/6/17 21:30
 */
@Slf4j
@Service
public class FreeServerRenewJobSample implements SmartJob {

    @Resource
    private PostponeManager postponeManager;

    /**
     * 定时任务示例
     *
     * @param param 可选参数 任务不需要时不用管
     * @return
     */
    @Override
    public String run(String param) {
        postponeManager.postpone();
        // 写点什么业务逻辑
        return "执行完毕,随便说点什么吧";
    }

}
