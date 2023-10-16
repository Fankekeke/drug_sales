package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.DrugInfo;
import cc.mrbird.febs.cos.entity.PharmacyInfo;
import cc.mrbird.febs.cos.entity.PharmacyInventory;
import cc.mrbird.febs.cos.entity.StockAlertInfo;
import cc.mrbird.febs.cos.dao.StockAlertInfoMapper;
import cc.mrbird.febs.cos.service.IDrugInfoService;
import cc.mrbird.febs.cos.service.IPharmacyInfoService;
import cc.mrbird.febs.cos.service.IPharmacyInventoryService;
import cc.mrbird.febs.cos.service.IStockAlertInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StockAlertInfoServiceImpl extends ServiceImpl<StockAlertInfoMapper, StockAlertInfo> implements IStockAlertInfoService {

    private final IPharmacyInfoService pharmacyInfoService;

    private final IPharmacyInventoryService pharmacyInventoryService;

    private final IDrugInfoService drugInfoService;

    /**
     * 分页获取库房预警信息
     *
     * @param page           分页对象
     * @param stockAlertInfo 库房预警信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectStockAlertPage(Page<StockAlertInfo> page, StockAlertInfo stockAlertInfo) {
        return baseMapper.selectStockAlertPage(page, stockAlertInfo);
    }

    /**
     * 库存预警校验
     */
    @Override
    public void stockAlertCheck() {
        List<PharmacyInfo> pharmacyInfoList = pharmacyInfoService.list(Wrappers.<PharmacyInfo>lambdaQuery().eq(PharmacyInfo::getBusinessStatus, 1));
        if (CollectionUtil.isEmpty(pharmacyInfoList)) {
            return;
        }
        List<Integer> pharmacyIds = pharmacyInfoList.stream().map(PharmacyInfo::getId).collect(Collectors.toList());

        // 获取药店库存
        List<PharmacyInventory> inventoryList = pharmacyInventoryService.list(Wrappers.<PharmacyInventory>lambdaQuery().in(PharmacyInventory::getPharmacyId, pharmacyIds));
        if (CollectionUtil.isEmpty(inventoryList)) {
            return;
        }
        List<Integer> drugIds = inventoryList.stream().map(PharmacyInventory::getDrugId).distinct().collect(Collectors.toList());
        // 药品信息
        List<DrugInfo> drugInfoList = (List<DrugInfo>) drugInfoService.listByIds(drugIds);
        Map<Integer, String> drugMap = drugInfoList.stream().collect(Collectors.toMap(DrugInfo::getId, DrugInfo::getName));

        // 待更新数据
        List<StockAlertInfo> stockAlertInfoList = new ArrayList<>();
        for (PharmacyInventory inventory : inventoryList) {
            if (inventory.getReserve() >= 15) {
                continue;
            }
            StockAlertInfo stockAlert = new StockAlertInfo();
            stockAlert.setDurgId(inventory.getDrugId());
            stockAlert.setShopId(inventory.getPharmacyId());
            stockAlert.setStatus(0);
            stockAlert.setCreateDate(DateUtil.formatDateTime(new Date()));
            stockAlert.setRemark("药品【" + drugMap.get(inventory.getDrugId()) + "】库存数量为 " + inventory.getReserve() + ", 请尽快补货");
            stockAlertInfoList.add(stockAlert);
        }
        if (CollectionUtil.isNotEmpty(stockAlertInfoList)) {
            this.saveBatch(stockAlertInfoList);
        }
    }
}
