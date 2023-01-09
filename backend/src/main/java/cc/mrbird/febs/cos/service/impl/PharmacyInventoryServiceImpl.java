package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.PharmacyInventory;
import cc.mrbird.febs.cos.mapper.PharmacyInventoryMapper;
import cc.mrbird.febs.cos.service.IPharmacyInventoryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class PharmacyInventoryServiceImpl extends ServiceImpl<PharmacyInventoryMapper, PharmacyInventory> implements IPharmacyInventoryService {

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
}
