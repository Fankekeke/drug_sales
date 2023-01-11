package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.InventoryStatistics;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.PharmacyInfo;
import cc.mrbird.febs.cos.dao.PharmacyInfoMapper;
import cc.mrbird.febs.cos.entity.StaffInfo;
import cc.mrbird.febs.cos.service.IInventoryStatisticsService;
import cc.mrbird.febs.cos.service.IOrderInfoService;
import cc.mrbird.febs.cos.service.IPharmacyInfoService;
import cc.mrbird.febs.cos.service.IStaffInfoService;
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

    private final IPharmacyInfoService pharmacyInfoService;

    private final IOrderInfoService orderInfoService;

    private final IInventoryStatisticsService inventoryStatisticsService;

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
        List<PharmacyInfo> pharmacyInfoList = pharmacyInfoService.list(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1));
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
}
