package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.dao.PharmacyInfoMapper;
import cc.mrbird.febs.cos.dao.StaffInfoMapper;
import cc.mrbird.febs.cos.dao.UserInfoMapper;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.OrderInfoMapper;
import cc.mrbird.febs.cos.entity.vo.OrderDetailVo;
import cc.mrbird.febs.cos.entity.vo.OrderInfoVo;
import cc.mrbird.febs.cos.entity.vo.OrderSubVo;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements IOrderInfoService {

    private final UserInfoMapper userInfoMapper;

    private final IOrderDetailService orderDetailService;

    private final IPharmacyInventoryService pharmacyInventoryService;

    private final IPaymentRecordService paymentRecordService;

    private final IInventoryStatisticsService inventoryStatisticsService;

    private final ILogisticsInfoService logisticsInfoService;

    private final PharmacyInfoMapper pharmacyInfoMapper;

    private final StaffInfoMapper staffInfoMapper;

    /**
     * 分页获取订单信息
     *
     * @param page     分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<OrderInfo> page, OrderInfo orderInfo) {
        return baseMapper.selectOrderPage(page, orderInfo);
    }

    /**
     * 添加订单信息
     *
     * @param orderInfoVo 订单信息
     * @param flag 是否付款完成（0.否 1.是）
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean orderAdd(OrderInfoVo orderInfoVo, boolean flag) {
        // 添加订单信息
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderStatus(flag ? 3 : 0);
        orderInfo.setCode("OR-" + System.currentTimeMillis());
        orderInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        orderInfo.setPharmacyId(orderInfoVo.getPharmacyId());
        // 所属用户
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderInfoVo.getUserId()));
        if (userInfo != null) {
            orderInfo.setUserId(userInfo.getId());
        }
        this.save(orderInfo);
        // 添加订单详情信息
        List<OrderDetail> detailList = JSONUtil.toList(orderInfoVo.getOrderDetailList(), OrderDetail.class);
        if (CollectionUtil.isNotEmpty(detailList)) {
            BigDecimal totalCost = BigDecimal.ONE;
            // 计算药品总价，绑定订单
            for (OrderDetail e : detailList) {
                e.setOrderId(orderInfo.getId());
                e.setAllPrice(e.getUnitPrice().multiply(BigDecimal.valueOf(e.getQuantity())));
                totalCost = totalCost.add(e.getAllPrice());
            }
            orderInfo.setTotalCost(totalCost);
            orderDetailService.saveBatch(detailList);
        }
        boolean result = this.updateById(orderInfo);
        if (flag) {
            this.orderPaymentPlatform(orderInfo.getCode(), orderInfoVo.getStaffCode());
        }
        // 重新更新订单信息

        return result;
    }

    /**
     * 平台内订单付款
     *
     * @param orderCode 订单编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void orderPaymentPlatform(String orderCode, String staffCode) {
        if (StrUtil.isEmpty(orderCode)) {
            return;
        }
        // 获取订单信息
        OrderInfo orderInfo = this.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, orderCode));
        if (StrUtil.isEmpty(staffCode)) {
            List<StaffInfo> staffInfoList = staffInfoMapper.selectList(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getPharmacyId, orderInfo.getPharmacyId()));
            staffCode = staffInfoList.get(0).getCode();
        }
        // 订单详情
        List<OrderDetail> detailList = orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, orderInfo.getId()));
        Map<Integer, Integer> detailMap = detailList.stream().collect(Collectors.toMap(OrderDetail::getDrugId, OrderDetail::getQuantity));
        // 根据药品ID获取库存信息
        List<PharmacyInventory> inventoryList = pharmacyInventoryService.list(Wrappers.<PharmacyInventory>lambdaQuery().in(PharmacyInventory::getDrugId, detailMap.keySet()).eq(PharmacyInventory::getPharmacyId, orderInfo.getPharmacyId()));
        List<InventoryStatistics> statisticsList = new ArrayList<>();

        String finalStaffCode = staffCode;
        inventoryList.forEach(e -> {
            InventoryStatistics inventoryStatistics = new InventoryStatistics();
            inventoryStatistics.setDrugId(e.getDrugId());
            inventoryStatistics.setPharmacyId(e.getPharmacyId());
            inventoryStatistics.setQuantity(detailMap.get(e.getDrugId()));
            inventoryStatistics.setStorageType(1);
            inventoryStatistics.setCustodian(finalStaffCode);
            inventoryStatistics.setCreateDate(DateUtil.formatDateTime(new Date()));
            statisticsList.add(inventoryStatistics);
            e.setReserve(e.getReserve() - detailMap.get(e.getDrugId()));
        });
        // 修改库存信息
        pharmacyInventoryService.updateBatchById(inventoryList);
        // 添加库房统计
        inventoryStatisticsService.saveBatch(statisticsList);
        // 添加付款记录
        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setCreateDate(DateUtil.formatDateTime(new Date()));
        paymentRecord.setMoney(orderInfo.getTotalCost());
        paymentRecord.setOrderCode(orderCode);
        paymentRecord.setUserId(orderInfo.getUserId());
        paymentRecordService.save(paymentRecord);
        orderInfo.setOrderStatus(1);
        this.updateById(orderInfo);
    }

    /**
     * 获取用户订单统计
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectOrderRateByUser(Integer userId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        if (userId == null) {
            return result;
        }
        // 获取用户信息
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, userId));
        if (userInfo == null) {
            return result;
        }
        result.put("user", userInfo);
        // 获取用户订单
        List<OrderInfo> orderList = this.list(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getUserId, userInfo.getUserId()));
        if (CollectionUtil.isEmpty(orderList)) {
            return result;
        }
        LinkedHashMap<String, Object> rate = new LinkedHashMap<>();
        return null;
    }

    /**
     * 用户提交订单
     *
     * @param orderDetailVo 订单信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean orderSubmit(OrderDetailVo orderDetailVo) throws FebsException {
        // 获取用户信息
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getUserId, orderDetailVo.getUserId()));
        if (null == userInfo) {
            throw new FebsException("未获取到用户信息");
        }
        // 获取订单信息
        List<OrderSubVo> orderSubVos = JSONUtil.toList(orderDetailVo.getDrugString(), OrderSubVo.class);
        // 根据药店分组
        Map<Integer, List<OrderSubVo>> orderSubMap = orderSubVos.stream().collect(Collectors.groupingBy(OrderSubVo::getPharmacyId));
        // 设置要添加订单
        List<OrderInfo> orderList = new ArrayList<>();
        // 添加的订单详情
        List<OrderDetail> orderDetailList = new ArrayList<>();

        orderSubMap.forEach((key, value) -> {
            OrderInfo orderItem = new OrderInfo();
            orderItem.setCode(StrUtil.toString(System.currentTimeMillis()) + key);
            orderItem.setPharmacyId(key);
            orderItem.setCreateDate(DateUtil.formatDateTime(new Date()));
            orderItem.setOrderStatus(0);
            orderItem.setUserId(userInfo.getId());
            this.save(orderItem);
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
            orderItem.setTotalCost(totalCost);
            this.updateById(orderItem);
        });
        return orderDetailService.saveBatch(orderDetailList);
    }

    /**
     * 订单付款
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean orderPayment(String orderCode) {
        if (StrUtil.isEmpty(orderCode)) {
            return false;
        }
        // 获取订单信息
        OrderInfo orderInfo = this.getOne(Wrappers.<OrderInfo>lambdaQuery().eq(OrderInfo::getCode, orderCode));
        // 修改订单状态
        orderInfo.setOrderStatus(1);
        // 订单详情
        List<OrderDetail> detailList = orderDetailService.list(Wrappers.<OrderDetail>lambdaQuery().eq(OrderDetail::getOrderId, orderInfo.getId()));
        Map<Integer, Integer> detailMap = detailList.stream().collect(Collectors.toMap(OrderDetail::getDrugId, OrderDetail::getQuantity));
        // 根据药品ID获取库存信息
        List<PharmacyInventory> inventoryList = pharmacyInventoryService.list(Wrappers.<PharmacyInventory>lambdaQuery().in(PharmacyInventory::getDrugId, detailMap.keySet()).eq(PharmacyInventory::getPharmacyId, orderInfo.getPharmacyId()));
        List<InventoryStatistics> statisticsList = new ArrayList<>();

        inventoryList.forEach(e -> {
            e.setReserve(e.getReserve() - detailMap.get(e.getDrugId()));
            InventoryStatistics inventoryStatistics = new InventoryStatistics();
            inventoryStatistics.setDrugId(e.getDrugId());
            inventoryStatistics.setPharmacyId(e.getPharmacyId());
            inventoryStatistics.setQuantity(e.getReserve());
            inventoryStatistics.setStorageType(1);
            inventoryStatistics.setCreateDate(DateUtil.formatDateTime(new Date()));
            statisticsList.add(inventoryStatistics);
        });
        // 订单物流信息初始化
        LogisticsInfo logisticsInfo = new LogisticsInfo();
        logisticsInfo.setOrderId(orderInfo.getId());
        logisticsInfo.setRemark("订单正在揽收");
        logisticsInfo.setCurrentLogistics(1);
        logisticsInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        logisticsInfoService.save(logisticsInfo);
        // 修改库存信息
        pharmacyInventoryService.updateBatchById(inventoryList);
        // 添加库房统计
        inventoryStatisticsService.saveBatch(statisticsList);
        // 添加付款记录
        PaymentRecord paymentRecord = new PaymentRecord();
        paymentRecord.setCreateDate(DateUtil.formatDateTime(new Date()));
        paymentRecord.setMoney(orderInfo.getTotalCost());
        paymentRecord.setOrderCode(orderCode);
        paymentRecord.setUserId(orderInfo.getUserId());
        paymentRecordService.save(paymentRecord);
        return this.updateById(orderInfo);
    }

    /**
     * 订单打印小票
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> receipt(Integer orderId) {
        if (orderId == null) {
            return null;
        }
        // 获取订单信息
        OrderInfo orderInfo = this.getById(orderId);
        // 药店信息
        PharmacyInfo pharmacyInfo = pharmacyInfoMapper.selectOne(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getId, orderInfo.getPharmacyId()));
        orderInfo.setPharmacyName(pharmacyInfo.getName());
        // 客户信息
        UserInfo userInfo = userInfoMapper.selectOne(Wrappers.<UserInfo>lambdaQuery().eq(UserInfo::getId, orderInfo.getUserId()));
        orderInfo.setUserName(userInfo.getName());
        // 订单详情
        List<LinkedHashMap<String, Object>> detailList = orderDetailService.selectDetailByOrder(orderId);
        // 返回数据
        return new LinkedHashMap<String, Object>() {
            {
                put("order", orderInfo);
                put("detail", detailList);
            }
        };
    }

    /**
     * 订单发货
     *
     * @param orderId 订单ID
     * @param remark  物流信息
     * @return 结果
     */
    @Override
    public boolean orderShip(Integer orderId, String remark) {
        logisticsInfoService.update(Wrappers.<LogisticsInfo>lambdaUpdate().set(LogisticsInfo::getCurrentLogistics, 0).eq(LogisticsInfo::getOrderId, orderId));
        LogisticsInfo logisticsInfo = new LogisticsInfo();
        logisticsInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        logisticsInfo.setOrderId(orderId);
        logisticsInfo.setRemark(remark);
        logisticsInfo.setCurrentLogistics(1);
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(orderId);
        orderInfo.setOrderStatus(2);
        this.updateById(orderInfo);
        return logisticsInfoService.save(logisticsInfo);
    }
}
