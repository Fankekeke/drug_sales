package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.LogisticsInfo;
import cc.mrbird.febs.cos.service.ILogisticsInfoService;
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
@RequestMapping("/cos/logistics-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LogisticsInfoController {

    private final ILogisticsInfoService logisticsInfoService;

    /**
     * 分页获取配送物流信息
     *
     * @param page          分页对象
     * @param logisticsInfo 配送物流信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<LogisticsInfo> page, LogisticsInfo logisticsInfo) {
        return R.ok(logisticsInfoService.selectLogisticsPage(page, logisticsInfo));
    }

    /**
     * 根据订单编号获取物流信息
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @GetMapping("/order/{orderId}")
    public R selectLogisticsByOrder(@PathVariable("orderId") Integer orderId) {
        return R.ok(logisticsInfoService.selectLogisticsByOrder(orderId));
    }


    /**
     * 获取详情信息
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(logisticsInfoService.getById(id));
    }

    /**
     * 获取信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(logisticsInfoService.list());
    }

    /**
     * 新增配送物流信息
     *
     * @param logisticsInfo 配送物流信息
     * @return 结果
     */
    @PostMapping
    public R save(LogisticsInfo logisticsInfo) {
        return R.ok(logisticsInfoService.saveLogistics(logisticsInfo));
    }

    /**
     * 修改配送物流信息
     *
     * @param logisticsInfo 配送物流信息
     * @return 结果
     */
    @PutMapping
    public R edit(LogisticsInfo logisticsInfo) {
        return R.ok(logisticsInfoService.updateById(logisticsInfo));
    }

    /**
     * 删除配送物流信息
     *
     * @param ids ids
     * @return 配送物流信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(logisticsInfoService.removeByIds(ids));
    }

}
