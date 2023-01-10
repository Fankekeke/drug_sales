package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.InventoryStatistics;
import cc.mrbird.febs.cos.entity.PharmacyInventory;
import cc.mrbird.febs.cos.dao.PharmacyInventoryMapper;
import cc.mrbird.febs.cos.service.IInventoryStatisticsService;
import cc.mrbird.febs.cos.service.IPharmacyInventoryService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PharmacyInventoryServiceImpl extends ServiceImpl<PharmacyInventoryMapper, PharmacyInventory> implements IPharmacyInventoryService {

    private final IInventoryStatisticsService inventoryStatisticsService;

    /**
     * 分页获取药店库存信息
     *
     * @param page     分页对象
     * @param pharmacyInventory 药店库存信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectPharmacyInventoryPage(Page<PharmacyInventory> page, PharmacyInventory pharmacyInventory) {
        return baseMapper.selectPharmacyInventoryPage(page, pharmacyInventory);
    }

    /**
     * 根据药房ID获取库存信息
     *
     * @param pharmacyId 药房ID
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectInventoryByPharmacy(Integer pharmacyId) {
        return baseMapper.selectInventoryByPharmacy(pharmacyId);
    }

    /**
     * 设置库存
     *
     * @param pharmacyInventory 药店库存信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean putInventory(PharmacyInventory pharmacyInventory) {
        // 判断库存中是否存在
        PharmacyInventory inventory = this.getOne(Wrappers.<PharmacyInventory>lambdaQuery().eq(PharmacyInventory::getPharmacyId, pharmacyInventory.getPharmacyId()).eq(PharmacyInventory::getDrugId, pharmacyInventory.getDrugId()));
        // 添加入库记录
        InventoryStatistics inventoryStatistics = new InventoryStatistics();
        inventoryStatistics.setPharmacyId(pharmacyInventory.getPharmacyId());
        inventoryStatistics.setDrugId(pharmacyInventory.getDrugId());
        inventoryStatistics.setQuantity(pharmacyInventory.getReserve());
        inventoryStatistics.setStorageType(1);
        inventoryStatistics.setCreateDate(DateUtil.formatDateTime(new Date()));
        inventoryStatisticsService.save(inventoryStatistics);
        if (inventory == null) {
            pharmacyInventory.setShelfStatus(1);
            return this.save(pharmacyInventory);
        }
        inventory.setReserve(inventory.getReserve() + pharmacyInventory.getReserve());
        return this.save(inventory);
    }

    /**
     * 设置库存
     *
     * @param pharmacyInventory 药店库存信息
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean outInventory(PharmacyInventory pharmacyInventory) {
        PharmacyInventory inventory = this.getOne(Wrappers.<PharmacyInventory>lambdaQuery().eq(PharmacyInventory::getPharmacyId, pharmacyInventory.getPharmacyId()).eq(PharmacyInventory::getDrugId, pharmacyInventory.getDrugId()));
        // 添加入库记录
        InventoryStatistics inventoryStatistics = new InventoryStatistics();
        inventoryStatistics.setPharmacyId(pharmacyInventory.getPharmacyId());
        inventoryStatistics.setDrugId(pharmacyInventory.getDrugId());
        inventoryStatistics.setQuantity(pharmacyInventory.getReserve());
        inventoryStatistics.setStorageType(2);
        inventoryStatistics.setCreateDate(DateUtil.formatDateTime(new Date()));
        inventoryStatisticsService.save(inventoryStatistics);
        inventory.setReserve(inventory.getReserve() - pharmacyInventory.getReserve());
        return this.updateById(inventory);
    }


}
