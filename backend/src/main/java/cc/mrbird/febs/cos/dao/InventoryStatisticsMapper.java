package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.InventoryStatistics;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface InventoryStatisticsMapper extends BaseMapper<InventoryStatistics> {

    /**
     * 分页获取库存统计信息
     *
     * @param page     分页对象
     * @param inventoryStatistics 库存统计信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectInventoryPage(Page<InventoryStatistics> page, @Param("inventoryStatistics") InventoryStatistics inventoryStatistics);
}
