package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.StockAlertInfo;
import cc.mrbird.febs.cos.service.IStockAlertInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/stock-alert-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockAlertInfoController {

    private final IStockAlertInfoService stockAlertInfoService;

    /**
     * 分页获取库房预警信息
     *
     * @param page           分页对象
     * @param stockAlertInfo 库房预警信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<StockAlertInfo> page, StockAlertInfo stockAlertInfo) {
        return R.ok(stockAlertInfoService.selectStockAlertPage(page, stockAlertInfo));
    }

    /**
     * 库存预警校验
     *
     * @return 结果
     */
    @GetMapping("/stockAlertCheck")
    public R stockAlertCheck() {
        stockAlertInfoService.stockAlertCheck();
        return R.ok();
    }

    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(stockAlertInfoService.getById(id));
    }

    @GetMapping("/list")
    public R list() {
        return R.ok(stockAlertInfoService.list());
    }

    /**
     * 新增库房预警信息
     *
     * @param stockAlertInfo 库房预警信息
     * @return 结果
     */
    @PostMapping
    public R save(StockAlertInfo stockAlertInfo) {
        stockAlertInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(stockAlertInfoService.save(stockAlertInfo));
    }

    /**
     * 修改库房预警信息
     *
     * @param stockAlertInfo 库房预警信息
     * @return 结果
     */
    @PutMapping
    public R edit(StockAlertInfo stockAlertInfo) {
        return R.ok(stockAlertInfoService.updateById(stockAlertInfo));
    }

    /**
     * 删除库房预警信息
     *
     * @param ids ids
     * @return 库房预警信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(stockAlertInfoService.removeByIds(ids));
    }

}
