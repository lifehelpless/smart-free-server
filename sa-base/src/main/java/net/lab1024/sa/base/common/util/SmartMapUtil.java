package net.lab1024.sa.base.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import net.lab1024.sa.base.common.core.KeyValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Map 工具类
 *
 * @author Mxl
 */
public class SmartMapUtil {

    /**
     * 从哈希表表中，获得 keys 对应的所有 value 数组
     *
     * @param multimap 哈希表
     * @param keys keys
     * @return value 数组
     */
    public static <K, V> List<V> getList(Multimap<K, V> multimap, Collection<K> keys) {
        List<V> result = new ArrayList<>();
        keys.forEach(k -> {
            Collection<V> values = multimap.get(k);
            if (CollectionUtil.isEmpty(values)) {
                return;
            }
            result.addAll(values);
        });
        return result;
    }

    /**
     * 从哈希表查找到 key 对应的 value，然后进一步处理
     * key 为 null 时, 不处理
     * 注意，如果查找到的 value 为 null 时，不进行处理
     *
     * @param map 哈希表
     * @param key key
     * @param consumer 进一步处理的逻辑
     */
    public static <K, V> void findAndThen(Map<K, V> map, K key, Consumer<V> consumer) {
        if (ObjUtil.isNull(key) || CollUtil.isEmpty(map)) {
            return;
        }
        V value = map.get(key);
        if (value == null) {
            return;
        }
        consumer.accept(value);
    }

    public static <K, V> Map<K, V> convertMap(List<KeyValue<K, V>> keyValues) {
        Map<K, V> map = Maps.newLinkedHashMapWithExpectedSize(keyValues.size());
        keyValues.forEach(keyValue -> map.put(keyValue.getKey(), keyValue.getValue()));
        return map;
    }


    public static <K, V> Map<K, V> convertMap(
            List<V> dataList,
            Function<V, K> keyMapper,
            Function<V, V> valueMapper
    ) {
        // 自动把Lambda生成的Key/Value封装为KeyValue列表，再调用原有方法
        List<KeyValue<K, V>> keyValueList = dataList.stream()
                .map(data -> new KeyValue<>(
                        keyMapper.apply(data),   // 多字段组合生成Key（Lambda）
                        valueMapper.apply(data) // 指定Value（Lambda）
                ))
                .collect(Collectors.toList());
        return convertMap(keyValueList); // 复用原有方法
    }

    public static <K, V, T> Map<K, T> convertFieldMap(
            List<V> dataList,
            Function<V, K> keyMapper,
            Function<V, T> valueMapper
    ) {
        if (dataList == null || dataList.isEmpty()) {
            return Maps.newLinkedHashMap();
        }
        List<KeyValue<K, T>> keyValueList = dataList.stream()
                .map(data -> new KeyValue<>(
                        keyMapper.apply(data),
                        valueMapper.apply(data)
                ))
                .collect(Collectors.toList());
        return convertMap(keyValueList);
    }


}
