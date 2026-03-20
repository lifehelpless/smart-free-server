package net.lab1024.sa.admin.module.freeserver.renew.factory;

import jakarta.annotation.PostConstruct;
import net.lab1024.sa.admin.module.freeserver.renew.strategy.AbstractRenewalStrategy;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 延期策略自动路由工厂
 */
@Component
public class RenewalStrategyFactory {

    // Spring 自动注入所有继承了 AbstractRenewalStrategy 的 Bean
    private final List<AbstractRenewalStrategy> strategyList;
    
    private final Map<Integer, AbstractRenewalStrategy> strategyMap = new ConcurrentHashMap<>();

    public RenewalStrategyFactory(List<AbstractRenewalStrategy> strategyList) {
        this.strategyList = strategyList;
    }

    @PostConstruct
    public void init() {
        // 启动时将所有策略放入 Map 缓存，达到 O(1) 路由
        for (AbstractRenewalStrategy strategy : strategyList) {
            strategyMap.put(strategy.getMethodType(), strategy);
        }
    }

    /**
     * 根据数据库里用户配置的 type，获取对应的处理策略
     */
    public AbstractRenewalStrategy getStrategy(Integer methodType) {
        AbstractRenewalStrategy strategy = strategyMap.get(methodType);
        if (strategy == null) {
            throw new IllegalArgumentException("未知的延期方式: " + methodType);
        }
        return strategy;
    }
}