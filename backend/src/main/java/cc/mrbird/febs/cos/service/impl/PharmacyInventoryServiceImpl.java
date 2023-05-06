package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.entity.InventoryStatistics;
import cc.mrbird.febs.cos.entity.PharmacyInventory;
import cc.mrbird.febs.cos.dao.PharmacyInventoryMapper;
import cc.mrbird.febs.cos.entity.vo.InventoryVo;
import cc.mrbird.febs.cos.service.IInventoryStatisticsService;
import cc.mrbird.febs.cos.service.IPharmacyInventoryService;
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

import java.util.*;
import java.util.stream.Collectors;

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
     * @param page              分页对象
     * @param pharmacyInventory 药店库存信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectPharmacyInventoryPage(Page<PharmacyInventory> page, PharmacyInventory pharmacyInventory) {
        return baseMapper.selectPharmacyInventoryPage(page, pharmacyInventory);
    }

    /**
     * 获取药品信息
     *
     * @param key key
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectPharmacyDrugList(String key) {
        return baseMapper.selectPharmacyDrugList(key);
    }

    /**
     * 批量设置库房库存
     * @param pharmacyId 参数
     * @param pharmacyInventorys 参数
     * @return 结果
     * @throws Exception 异常
     */
    @Override
    public boolean batchPutInventory(Integer pharmacyId, String pharmacyInventorys) throws Exception {
        List<PharmacyInventory> inventoryList = JSONUtil.toList(pharmacyInventorys, PharmacyInventory.class);
        if (pharmacyId == null || CollectionUtil.isEmpty(inventoryList)) {
            throw new FebsException("所属药店和药品信息不能为空！");
        }
        List<Integer> drugIds = inventoryList.stream().map(PharmacyInventory::getDrugId).filter(Objects::nonNull).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(drugIds)) {
            return false;
        }
        // 根据药品编号查询库存
        List<PharmacyInventory> pharmacyInventoryList = this.list(Wrappers.<PharmacyInventory>lambdaQuery().eq(PharmacyInventory::getPharmacyId, pharmacyId).in(PharmacyInventory::getDrugId, drugIds));
        // 转MAP
        Map<Integer, PharmacyInventory> inventoryMap = pharmacyInventoryList.stream().collect(Collectors.toMap(PharmacyInventory::getDrugId, e -> e));
        List<PharmacyInventory> batchData = new ArrayList<>();
        List<InventoryStatistics> statisticsList = new ArrayList<>();
        for (PharmacyInventory pharmacyInventoryVo : inventoryList) {
            InventoryStatistics inventoryStatistics = new InventoryStatistics();
            inventoryStatistics.setDrugId(pharmacyInventoryVo.getDrugId());
            inventoryStatistics.setPharmacyId(pharmacyId);
            inventoryStatistics.setQuantity(pharmacyInventoryVo.getReserve());
            inventoryStatistics.setCreateDate(DateUtil.formatDateTime(new Date()));
            inventoryStatistics.setStorageType(2);
            statisticsList.add(inventoryStatistics);

            PharmacyInventory item = inventoryMap.get(pharmacyInventoryVo.getDrugId());
            if (item == null || item.getDrugId() == null) {
                item = new PharmacyInventory();
                item.setPharmacyId(pharmacyId);
                item.setShelfStatus(1);
                item.setDrugId(pharmacyInventoryVo.getDrugId());
                item.setReserve(pharmacyInventoryVo.getReserve());
            } else {
                item.setReserve(item.getReserve() + pharmacyInventoryVo.getReserve());
            }
            batchData.add(item);
        }
        inventoryStatisticsService.saveBatch(statisticsList);
        return this.saveOrUpdateBatch(batchData);
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
