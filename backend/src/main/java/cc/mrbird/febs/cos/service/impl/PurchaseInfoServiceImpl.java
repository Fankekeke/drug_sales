package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.PurchaseInfo;
import cc.mrbird.febs.cos.dao.PurchaseInfoMapper;
import cc.mrbird.febs.cos.service.IPurchaseInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class PurchaseInfoServiceImpl extends ServiceImpl<PurchaseInfoMapper, PurchaseInfo> implements IPurchaseInfoService {

    /**
     * 分页获取药品采购信息
     *
     * @param page 分页对象
     * @param purchaseInfo 药品采购信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectPurchasePage(Page<PurchaseInfo> page, PurchaseInfo purchaseInfo) {
        return baseMapper.selectPurchasePage(page, purchaseInfo);
    }
}
