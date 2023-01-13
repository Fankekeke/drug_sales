package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.OrderInfoMapper;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.PharmacyInfoMapper;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PharmacyInfoServiceImpl extends ServiceImpl<PharmacyInfoMapper, PharmacyInfo> implements IPharmacyInfoService {

    private final IStaffInfoService staffInfoService;

    private final IOrderInfoService orderInfoService;

    private final IInventoryStatisticsService inventoryStatisticsService;

    private final IPharmacyInventoryService pharmacyInventoryService;

    private final OrderInfoMapper orderInfoMapper;

    /**
     * 分页获取药店信息
     *
     * @param page     分页对象
     * @param pharmacyInfo 药店信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectPharmacyPage(Page<PharmacyInfo> page, PharmacyInfo pharmacyInfo) {
        return baseMapper.selectPharmacyPage(page, pharmacyInfo);
    }

    /**
     * 获取药店订单销售情况
     *
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectOrderNumByPharmacy() {
        // 所有员工信息
        List<StaffInfo> staffInfoList = staffInfoService.list(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getStatus,1 ));
        // 所有药店信息
        List<PharmacyInfo> pharmacyInfoList = this.list(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1));
        // 所有订单信息
        List<OrderInfo> orderInfoList = orderInfoService.list();
        // 所有入库信息
        List<InventoryStatistics> inventoryPut = inventoryStatisticsService.list(Wrappers.<InventoryStatistics>lambdaQuery().eq(InventoryStatistics::getStorageType, 2));

        if (CollectionUtil.isEmpty(pharmacyInfoList)) {
            return Collections.emptyList();
        }
        Map<Integer, List<StaffInfo>> staffMap = staffInfoList.stream().collect(Collectors.groupingBy(StaffInfo::getPharmacyId));
        Map<Integer, List<OrderInfo>> orderMap = orderInfoList.stream().collect(Collectors.groupingBy(OrderInfo::getPharmacyId));
        Map<Integer, List<InventoryStatistics>> inventoryPutMap = inventoryPut.stream().collect(Collectors.groupingBy(InventoryStatistics::getPharmacyId));

        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        pharmacyInfoList.forEach(e -> {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>() {
                {
                    put("pharmacy", e);
                }
            };
            List<OrderInfo> currentOrderList = orderMap.get(e.getId());
            // 订单数量
            item.put("orderNum", CollectionUtil.isNotEmpty(currentOrderList) ? currentOrderList.size() : 0);
            // 员工信息
            item.put("staff", staffMap.get(e.getId()));
            // 收益
            BigDecimal total = BigDecimal.ZERO;
            if (CollectionUtil.isNotEmpty(currentOrderList)) {
                total = currentOrderList.stream().map(OrderInfo::getTotalCost).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            item.put("total", total);
            // 入库统计
            item.put("put", CollectionUtil.isNotEmpty(inventoryPutMap.get(e.getId())) ? inventoryPutMap.get(e.getId()).size() : 0);
            result.add(item);
        });
        return result;
    }

    /**
     * 查询本月订单数量排行
     *
     * @param type 1.按订单数 2.按交易金额
     * @return 结果
     */
    @Override
    public List<PharmacyOrderRank> selectOrderRank(Integer type) {
        // 所有药店信息
        List<PharmacyInfo> pharmacyInfoList = this.list(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1));
        // 本月订单数据
        List<OrderInfo> orderInfoList = orderInfoMapper.selectOrderByMonth();
        if (CollectionUtil.isEmpty(orderInfoList) || CollectionUtil.isEmpty(pharmacyInfoList)) {
            return Collections.emptyList();
        }
        Map<Integer, List<OrderInfo>> orderMap = orderInfoList.stream().collect(Collectors.groupingBy(OrderInfo::getPharmacyId));
        List<PharmacyOrderRank> result = new ArrayList<>();
        pharmacyInfoList.forEach(e -> {
            PharmacyOrderRank pharmacyOrderRank = new PharmacyOrderRank(e.getId(), e.getName(), 0, BigDecimal.ZERO);
            List<OrderInfo> orderInfoItemList = orderMap.get(e.getId());
            if (CollectionUtil.isNotEmpty(orderInfoItemList)) {
                pharmacyOrderRank.setOrderNum(orderInfoItemList.size());
                BigDecimal totalPrice = orderInfoItemList.stream().map(OrderInfo::getTotalCost).reduce(BigDecimal.ZERO, BigDecimal::add);
                pharmacyOrderRank.setTotalPrice(totalPrice);
            }
            result.add(pharmacyOrderRank);
        });
        // 排序
        if (type == 0) {
            return result.stream().sorted(Comparator.comparing(PharmacyOrderRank::getOrderNum)).collect(Collectors.toList());
        } else {
            return result.stream().sorted(Comparator.comparing(PharmacyOrderRank::getTotalPrice)).collect(Collectors.toList());
        }
    }

    /**
     * 查询药店库存信息
     *
     * @param pharmacyId 药店ID
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectStockByPharmacy(Integer pharmacyId) {
        // 药房库存信息
        List<PharmacyInventory> pharmacyInventoryList = pharmacyInventoryService.list(Wrappers.<PharmacyInventory>lambdaQuery().eq(PharmacyInventory::getPharmacyId, pharmacyId));

        return null;
    }

    /**
     * 主页数据
     *
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> homeData() {
        // 总订单数量

        // 总收益

        // 店铺数量

        // 员工数量
        return null;
    }
}
