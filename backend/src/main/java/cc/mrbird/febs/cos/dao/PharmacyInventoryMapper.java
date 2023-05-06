package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.PharmacyInventory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface PharmacyInventoryMapper extends BaseMapper<PharmacyInventory> {

    /**
     * 分页获取药店库存信息
     *
     * @param page     分页对象
     * @param pharmacyInventory 药店库存信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectPharmacyInventoryPage(Page<PharmacyInventory> page, @Param("pharmacyInventory") PharmacyInventory pharmacyInventory);

    /**
     * 根据药房ID获取库存信息
     *
     * @param pharmacyId 药房ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectInventoryByPharmacy(@Param("pharmacyId") Integer pharmacyId);

    /**
     * 获取药品信息
     *
     * @param key key
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectPharmacyDrugList(@Param("key") String key);
}
