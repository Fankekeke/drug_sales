package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.InventoryStatistics;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IInventoryStatisticsService extends IService<InventoryStatistics> {

    /**
     * 分页获取库存统计信息
     *
     * @param page     分页对象
     * @param inventoryStatistics 库存统计信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectInventoryPage(Page<InventoryStatistics> page, InventoryStatistics inventoryStatistics);

    /**
     * 获取药品出入库详情
     *
     * @param pharmacyId 药房ID
     * @param drugId     药品ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectInventoryStatistics(Integer pharmacyId, Integer drugId);
}
