package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.PurchaseInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface PurchaseInfoMapper extends BaseMapper<PurchaseInfo> {

    /**
     * 分页获取药品采购信息
     *
     * @param page 分页对象
     * @param purchaseInfo 药品采购信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectPurchasePage(Page<PurchaseInfo> page, @Param("purchaseInfo") PurchaseInfo purchaseInfo);
}
