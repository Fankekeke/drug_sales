package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.PharmacyInventory;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IPharmacyInventoryService extends IService<PharmacyInventory> {

    /**
     * 分页获取药店库存信息
     *
     * @param page     分页对象
     * @param pharmacyInventory 药店库存信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectPharmacyInventoryPage(Page<PharmacyInventory> page, PharmacyInventory pharmacyInventory);

    /**
     * 根据药房ID获取库存信息
     *
     * @param pharmacyId 药房ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectInventoryByPharmacy(Integer pharmacyId);

    /**
     * 设置库存
     *
     * @param pharmacyInventory 药店库存信息
     * @return 结果
     */
    boolean putInventory(PharmacyInventory pharmacyInventory);

    /**
     * 设置库存
     *
     * @param pharmacyInventory 药店库存信息
     * @return 结果
     */
    boolean outInventory(PharmacyInventory pharmacyInventory);
}
