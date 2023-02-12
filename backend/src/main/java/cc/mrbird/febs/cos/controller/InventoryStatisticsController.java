package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.InventoryStatistics;
import cc.mrbird.febs.cos.service.IInventoryStatisticsService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/inventory-statistics")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InventoryStatisticsController {

    private final IInventoryStatisticsService inventoryStatisticsService;

    /**
     * 分页获取库存统计信息
     *
     * @param page                分页对象
     * @param inventoryStatistics 库存统计信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<InventoryStatistics> page, InventoryStatistics inventoryStatistics) {
        return R.ok(inventoryStatisticsService.selectInventoryPage(page, inventoryStatistics));
    }

    /**
     * 获取药品出入库详情
     *
     * @param pharmacyId 药房ID
     * @param drugId     药品ID
     * @return 结果
     */
    @GetMapping("/statistics")
    public R selectInventoryStatistics(@RequestParam("pharmacyId") Integer pharmacyId, @RequestParam("drugId") Integer drugId) {
        return R.ok(inventoryStatisticsService.selectInventoryStatistics(pharmacyId, drugId));
    }

    /**
     * 获取详情信息
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(inventoryStatisticsService.getById(id));
    }

    /**
     * 获取信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(inventoryStatisticsService.list());
    }

    /**
     * 新增库存统计信息
     *
     * @param inventoryStatistics 库存统计信息
     * @return 结果
     */
    @PostMapping
    public R save(InventoryStatistics inventoryStatistics) {
        inventoryStatistics.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(inventoryStatisticsService.save(inventoryStatistics));
    }

    /**
     * 修改库存统计信息
     *
     * @param inventoryStatistics 库存统计信息
     * @return 结果
     */
    @PutMapping
    public R edit(InventoryStatistics inventoryStatistics) {
        return R.ok(inventoryStatisticsService.updateById(inventoryStatistics));
    }

    /**
     * 删除库存统计信息
     *
     * @param ids ids
     * @return 库存统计信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(inventoryStatisticsService.removeByIds(ids));
    }

}
