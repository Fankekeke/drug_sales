package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.OrderEvaluate;
import cc.mrbird.febs.cos.service.IOrderEvaluateService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
@RequestMapping("/cos/order-evaluate")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderEvaluateController {

    private final IOrderEvaluateService orderEvaluateService;

    /**
     * 分页获取订单评价信息
     *
     * @param page          分页对象
     * @param orderEvaluate 订单评价信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<OrderEvaluate> page, OrderEvaluate orderEvaluate) {
        return R.ok(orderEvaluateService.selectEvaluatePage(page, orderEvaluate));
    }

    /**
     * 根据订单ID获取评价信息
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @GetMapping("/order/{orderId}")
    public R selectEvaluateByOrder(@PathVariable("orderId") Integer orderId) {
        return R.ok(orderEvaluateService.getOne(Wrappers.<OrderEvaluate>lambdaQuery().eq(OrderEvaluate::getOrderId, orderId)));
    }

    /**
     * 获取详情信息
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(orderEvaluateService.getById(id));
    }

    /**
     * 获取信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(orderEvaluateService.list());
    }

    /**
     * 新增订单评价信息
     *
     * @param orderEvaluate 订单评价信息
     * @return 结果
     */
    @PostMapping
    public R save(OrderEvaluate orderEvaluate) {
        orderEvaluate.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(orderEvaluateService.save(orderEvaluate));
    }

    /**
     * 修改订单评价信息
     *
     * @param orderEvaluate 订单评价信息
     * @return 结果
     */
    @PutMapping
    public R edit(OrderEvaluate orderEvaluate) {
        return R.ok(orderEvaluateService.updateById(orderEvaluate));
    }

    /**
     * 删除订单评价信息
     *
     * @param ids ids
     * @return 订单评价信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(orderEvaluateService.removeByIds(ids));
    }

}
