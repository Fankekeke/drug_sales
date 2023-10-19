package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.dao.UserInfoMapper;
import cc.mrbird.febs.cos.entity.MedicationInfo;
import cc.mrbird.febs.cos.dao.MedicationInfoMapper;
import cc.mrbird.febs.cos.entity.OrderDetail;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.UserInfo;
import cc.mrbird.febs.cos.entity.vo.OrderDetailVo;
import cc.mrbird.febs.cos.entity.vo.OrderSubVo;
import cc.mrbird.febs.cos.service.IMedicationInfoService;
import cc.mrbird.febs.cos.service.IOrderDetailService;
import cc.mrbird.febs.cos.service.IOrderInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicationInfoServiceImpl extends ServiceImpl<MedicationInfoMapper, MedicationInfo> implements IMedicationInfoService {

    private final IOrderInfoService orderInfoService;

    private final IOrderDetailService orderDetailService;

    private final UserInfoMapper userInfoMapper;

    /**
     * 分页获取电子处方信息
     *
     * @param page 分页对象
     * @param medicationInfo 电子处方信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectMedication(Page<MedicationInfo> page, MedicationInfo medicationInfo) {
        return baseMapper.selectMedication(page, medicationInfo);
    }

    /**
     * 新增电子处方信息
     *
     * @param medicationInfo 电子处方信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean checkMedication(MedicationInfo medicationInfo) throws FebsException {
        // 获取订单信息
        List<OrderSubVo> orderSubVos = JSONUtil.toList(medicationInfo.getDrugString(), OrderSubVo.class);
        // 根据药店分组
        Map<Integer, List<OrderSubVo>> orderSubMap = orderSubVos.stream().collect(Collectors.groupingBy(OrderSubVo::getPharmacyId));
        // 添加的订单详情
        List<OrderDetail> orderDetailList = new ArrayList<>();
        // 用户信息
        UserInfo userInfo = userInfoMapper.selectById(medicationInfo.getUserId());

        orderSubMap.forEach((key, value) -> {
            OrderInfo orderItem = new OrderInfo();
            orderItem.setCode(StrUtil.toString(System.currentTimeMillis()) + key);
            orderItem.setPharmacyId(key);
            orderItem.setCreateDate(DateUtil.formatDateTime(new Date()));
            orderItem.setOrderStatus(0);
            orderItem.setUserId(medicationInfo.getUserId());
            orderInfoService.save(orderItem);
            // 总价格
            BigDecimal totalCost = BigDecimal.ZERO;
            for (OrderSubVo orderSubItem: value) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setDrugId(orderSubItem.getDrugId());
                orderDetail.setQuantity(orderSubItem.getTotal().intValue());
                orderDetail.setUnitPrice(orderSubItem.getUnitPrice());
                orderDetail.setOrderId(orderItem.getId());
                orderDetail.setAllPrice(orderDetail.getUnitPrice().multiply(orderSubItem.getTotal()));
                totalCost = totalCost.add(orderDetail.getAllPrice());
                orderDetailList.add(orderDetail);
            }
            // 如果用户为会员 打折
            if (userInfo != null && userInfo.getIsMember() != null && userInfo.getIsMember() == 1) {
                orderItem.setTotalCost(NumberUtil.mul(totalCost, 0.8));
            } else {
                orderItem.setTotalCost(totalCost);
            }
            orderInfoService.updateById(orderItem);
            medicationInfo.setOrderCode(orderItem.getCode());
            medicationInfo.setStatus(1);
        });
        this.updateById(medicationInfo);
        return orderDetailService.saveBatch(orderDetailList);
    }
}
