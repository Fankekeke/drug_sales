package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.dao.OrderDetailMapper;
import cc.mrbird.febs.cos.dao.OrderEvaluateMapper;
import cc.mrbird.febs.cos.dao.OrderInfoMapper;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.dao.PharmacyInfoMapper;
import cc.mrbird.febs.cos.entity.vo.EvaluateRankVo;
import cc.mrbird.febs.cos.service.*;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
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

    private final IDrugInfoService drugInfoService;

    private final OrderInfoMapper orderInfoMapper;

    private final OrderDetailMapper orderDetailMapper;

    private final PharmacyInfoMapper pharmacyInfoMapper;

    private final OrderEvaluateMapper orderEvaluateMapper;

    private final IBulletinInfoService bulletinInfoService;

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
     * 查询近十天内各家订单收益统计
     *
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectOrderPriceDays() {
        // 正在营业供应商
        List<PharmacyInfo> pharmacyInfoList = this.list(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1));
        if (CollectionUtil.isEmpty(pharmacyInfoList)) {
            return Collections.emptyList();
        }
        // 返回数据
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        pharmacyInfoList.forEach(e -> {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>() {
                {
                    put("name", e.getName());
                    put("value", orderInfoMapper.selectOrderPriceWithinDays(e.getId()));
                }
            };
            result.add(item);
        });
        return result;
    }

    /**
     * 查询近十天内各家订单数量统计
     *
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectOrderNumDays() {
        // 正在营业供应商
        List<PharmacyInfo> pharmacyInfoList = this.list(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1));
        if (CollectionUtil.isEmpty(pharmacyInfoList)) {
            return Collections.emptyList();
        }
        // 返回数据
        List<LinkedHashMap<String, Object>> result = new ArrayList<>();
        pharmacyInfoList.forEach(e -> {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>() {
                {
                    put("name", e.getName());
                    put("value", orderInfoMapper.selectOrderNumWithinDays(e.getId()));
                }
            };
            result.add(item);
        });
        return result;
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
        // 药店信息
        List<PharmacyInfo> pharmacyInfoList = this.list(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1));
        // 药品信息
        List<DrugInfo> drugInfoList = drugInfoService.list();
        if (CollectionUtil.isEmpty(pharmacyInventoryList) || CollectionUtil.isEmpty(pharmacyInfoList) || CollectionUtil.isEmpty(drugInfoList)) {
            return Collections.emptyList();
        }

        List<LinkedHashMap<String, Object>> result = new ArrayList<>();

        // 库房信息根据药店ID转MAP
        Map<Integer, List<PharmacyInventory>> pharmacyInventoryMap = pharmacyInventoryList.stream().collect(Collectors.groupingBy(PharmacyInventory::getPharmacyId));
        // 药店信息转MAP
        Map<Integer, String> pharmacyMap = pharmacyInfoList.stream().collect(Collectors.toMap(PharmacyInfo::getId, PharmacyInfo::getName));
        // 药品信息转MAP
        Map<Integer, String> drugMap = drugInfoList.stream().collect(Collectors.toMap(DrugInfo::getId, DrugInfo::getName));

        pharmacyInfoList.forEach(e -> {
            LinkedHashMap<String, Object> item = new LinkedHashMap<String, Object>() {
                {
                    put("pharmacyName", pharmacyMap.get(e.getId()));
                }
            };
            List<PharmacyInventory> inventoryList = pharmacyInventoryMap.get(e.getId());
            if (CollectionUtil.isEmpty(inventoryList)) {
                item.put("inventory", Collections.emptyList());
            }
            List<Map<String, Object>> drugList = new ArrayList<>();
            inventoryList.forEach(inventory -> {
                Map<String, Object> inventoryItem = new HashMap<String, Object>(16) {
                    {
                        put("name", drugMap.get(inventory.getDrugId()));
                        put("count", inventory.getReserve());
                        put("status", inventory.getShelfStatus());
                    }
                };
                drugList.add(inventoryItem);
            });
            item.put("inventory", drugList);
            result.add(item);
        });
        return result;
    }

    /**
     * 查询药店评价排名
     *
     * @return 结果
     */
    @Override
    public List<EvaluateRankVo> selectPharmacyEvaluateRank() {
        List<EvaluateRankVo> result = new ArrayList<>();
        // 所有营业药店信息
        List<PharmacyInfo> pharmacyList = this.list(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1));
        // 所有评价信息
        List<OrderEvaluate> orderEvaluateList = orderEvaluateMapper.selectList(Wrappers.<OrderEvaluate>lambdaQuery());
        if (CollectionUtil.isEmpty(pharmacyList)) {
            return Collections.emptyList();
        }
        // 评价信息转MAP
        Map<Integer, List<OrderEvaluate>> evaluateMap = orderEvaluateList.stream().collect(Collectors.groupingBy(OrderEvaluate::getPharmacyId));
        for (PharmacyInfo pharmacyInfo : pharmacyList) {
            EvaluateRankVo item = new EvaluateRankVo();
            item.setPharmacyName(pharmacyInfo.getName());
            List<OrderEvaluate> evaluateList = evaluateMap.get(pharmacyInfo.getId());
            if (CollectionUtil.isEmpty(evaluateList)) {
                item.setScore(BigDecimal.ZERO);
                result.add(item);
                continue;
            }
            BigDecimal score = orderEvaluateList.stream().map(OrderEvaluate::getScore).reduce(BigDecimal.ZERO, BigDecimal::add);
            item.setScore(score.divide(BigDecimal.valueOf(orderEvaluateList.size()), 2));
            result.add(item);
        }
        // 排名
        return result.stream().sorted(Comparator.comparing(EvaluateRankVo::getScore)).collect(Collectors.toList());
    }

    /**
     * 查询药店评价信息
     *
     * @param pharmacyId 药店ID
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectPharmacyEvaluateByCode(Integer pharmacyId) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        List<OrderEvaluate> orderEvaluateList = orderEvaluateMapper.selectList(Wrappers.<OrderEvaluate>lambdaQuery().eq(OrderEvaluate::getPharmacyId, pharmacyId));
        if (CollectionUtil.isEmpty(orderEvaluateList)) {
            return null;
        }
        BigDecimal score = orderEvaluateList.stream().map(OrderEvaluate::getScore).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("score", score.divide(BigDecimal.valueOf(orderEvaluateList.size()), 2));
        result.put("evaluate", orderEvaluateList);
        return result;
    }

    /**
     * 主页数据
     *
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> homeData() {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        // 总订单数量
        result.put("orderCode", orderInfoService.count());
        // 总收益
        result.put("orderPrice", orderInfoMapper.selectOrderPrice());
        // 店铺数量
        result.put("pharmacyNum", pharmacyInfoMapper.selectCount(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1)));
        // 员工数量
        result.put("staffNum", staffInfoService.count(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getStatus, 1)));

        // 本月订单数量
        List<OrderInfo> orderList = orderInfoMapper.selectOrderByMonth();
        result.put("monthOrderNum", CollectionUtil.isEmpty(orderList) ? 0 : orderList.size());
        BigDecimal orderPrice = orderList.stream().map(OrderInfo::getTotalCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        // 获取本月收益
        result.put("monthOrderPrice", orderPrice);

        // 本年订单数量
        List<OrderInfo> orderYearList = orderInfoMapper.selectOrderByYear();
        result.put("yearOrderNum", CollectionUtil.isEmpty(orderYearList) ? 0 : orderYearList.size());
        // 本年总收益
        BigDecimal orderYearPrice = orderYearList.stream().map(OrderInfo::getTotalCost).reduce(BigDecimal.ZERO, BigDecimal::add);
        result.put("yearOrderPrice", orderYearPrice);

        // 公告信息
        result.put("bulletin", bulletinInfoService.list(Wrappers.<BulletinInfo>lambdaQuery().eq(BulletinInfo::getRackUp, 1)));

        // 近十天内订单统计
        result.put("orderNumWithinDays", orderInfoMapper.selectOrderNumWithinDays(null));
        // 近十天内收益统计
        result.put("orderPriceWithinDays", orderInfoMapper.selectOrderPriceWithinDays(null));
        // 订单销售药品类别统计
        result.put("orderDrugType", orderInfoMapper.selectOrderDrugType());
        return result;
    }

    /**
     * 根据月份获取药品统计情况
     *
     * @param date 日期
     * @return 结果
     */
    @Override
    public LinkedHashMap<String, Object> selectStatisticsByMonth(String date) throws FebsException {
        if (StrUtil.isEmpty(date)) {
            throw new FebsException("参数不能为空");
        }

        int year = DateUtil.year(DateUtil.parseDate(date));
        int month = DateUtil.month(DateUtil.parseDate(date)) + 1;

        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("num", Collections.emptyList());
                put("price", Collections.emptyList());
            }
        };

        // 获取订单详情
        List<OrderInfo> orderList = orderInfoMapper.selectOrderByCheckMonth(year, month);
        if (CollectionUtil.isEmpty(orderList)) {
            return result;
        }

        List<Map<String, Object>> numMap = new ArrayList<>();
        List<Map<String, Object>> priceMap = new ArrayList<>();

        List<Integer> orderIds = orderList.stream().map(OrderInfo::getId).collect(Collectors.toList());
        List<OrderDetail> detailList = orderDetailMapper.selectList(Wrappers.<OrderDetail>lambdaQuery().in(OrderDetail::getOrderId, orderIds));
        // 按药品ID分组
        Map<Integer, List<OrderDetail>> drugDetailMap = detailList.stream().collect(Collectors.groupingBy(OrderDetail::getDrugId));

        // 药品信息
        List<DrugInfo> drugInfoList = (List<DrugInfo>) drugInfoService.listByIds(drugDetailMap.keySet());
        Map<Integer, String> drugMap = drugInfoList.stream().collect(Collectors.toMap(DrugInfo::getId, DrugInfo::getName));

        drugDetailMap.forEach((key, value) -> {
            String drugName = drugMap.get(key);
            Map<String, Object> numItem = new HashMap<String, Object>() {
                {
                    put("name", drugName);
                }
            };
            Map<String, Object> priceItem = new HashMap<String, Object>() {
                {
                    put("name", drugName);
                }
            };
            // 本月药品销售数量统计
            int num = value.stream().map(OrderDetail::getQuantity).reduce(0, Integer::sum);
            numItem.put("value", num);

            // 本月药品销售金额统计
            BigDecimal price = value.stream().map(OrderDetail::getAllPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
            priceItem.put("value", price);
            numMap.add(numItem);
            priceMap.add(priceItem);
        });

        result.put("num", numMap);
        result.put("price", priceMap);
        return result;
    }
}
