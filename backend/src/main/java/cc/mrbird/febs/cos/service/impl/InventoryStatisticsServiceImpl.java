package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.InventoryStatistics;
import cc.mrbird.febs.cos.dao.InventoryStatisticsMapper;
import cc.mrbird.febs.cos.service.IInventoryStatisticsService;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class InventoryStatisticsServiceImpl extends ServiceImpl<InventoryStatisticsMapper, InventoryStatistics> implements IInventoryStatisticsService {

    /**
     * 分页获取库存统计信息
     *
     * @param page     分页对象
     * @param inventoryStatistics 库存统计信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectInventoryPage(Page<InventoryStatistics> page, InventoryStatistics inventoryStatistics) {
        return baseMapper.selectInventoryPage(page, inventoryStatistics);
    }

    /**
     * 获取药品出入库详情
     *
     * @param pharmacyId 药房ID
     * @param drugId     药品ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectInventoryStatistics(Integer pharmacyId, Integer drugId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("put", null);
                put("out", null);
            }
        };
        List<InventoryStatistics> inventoryList = this.list(Wrappers.<InventoryStatistics>lambdaQuery().eq(InventoryStatistics::getDrugId, drugId).eq(InventoryStatistics::getPharmacyId, pharmacyId));
        if (CollectionUtil.isEmpty(inventoryList)) {
            return result;
        }
        result.put("put", inventoryList.stream().filter(e -> e.getStorageType() == 2));
        result.put("out", inventoryList.stream().filter(e -> e.getStorageType() == 1));
        return result;
    }
}
