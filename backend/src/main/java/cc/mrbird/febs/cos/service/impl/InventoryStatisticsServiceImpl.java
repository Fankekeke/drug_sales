package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.InventoryStatistics;
import cc.mrbird.febs.cos.mapper.InventoryStatisticsMapper;
import cc.mrbird.febs.cos.service.IInventoryStatisticsService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

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
}
