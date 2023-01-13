package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.PaymentRecord;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.service.IOrderInfoService;
import cc.mrbird.febs.cos.service.IPaymentRecordService;
import cc.mrbird.febs.cos.service.IUserInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/payment-record")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PaymentRecordController {

    private final IPaymentRecordService paymentRecordService;

    private final IOrderInfoService orderInfoService;

    /**
     * 分页获取缴费记录信息
     *
     * @param page 分页对象
     * @param paymentRecord 缴费记录信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<PaymentRecord> page, PaymentRecord paymentRecord) {
        return R.ok(paymentRecordService.selectPaymentRecordPage(page, paymentRecord));
    }

    /**
     * 新增缴费记录信息
     *
     * @param paymentRecord 缴费记录信息
     * @return 结果
     */
    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public R save(PaymentRecord paymentRecord) {
        // 获取订单信息
        OrderInfo orderInfo = orderInfoService.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, paymentRecord.getOrderCode()));
        paymentRecord.setUserId(orderInfo.getUserId());
        paymentRecord.setMoney(orderInfo.getTotalCost());
        paymentRecord.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(paymentRecordService.save(paymentRecord));
    }

    /**
     * 修改缴费记录信息
     *
     * @param paymentRecord 缴费记录信息
     * @return 结果
     */
    @PutMapping
    public R edit(PaymentRecord paymentRecord) {
        return R.ok(paymentRecordService.updateById(paymentRecord));
    }

    /**
     * 删除缴费记录信息
     *
     * @param ids ids
     * @return 缴费记录信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(paymentRecordService.removeByIds(ids));
    }

}
