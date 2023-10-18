package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.PurchaseInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IPurchaseInfoService extends IService<PurchaseInfo> {

    /**
     * 分页获取药品采购信息
     *
     * @param page 分页对象
     * @param purchaseInfo 药品采购信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectPurchasePage(Page<PurchaseInfo> page, PurchaseInfo purchaseInfo);

    /**
     * 收货
     *
     * @param id 采购ID
     * @return 结果
     */
    boolean receipt(Integer id) throws Exception;

    /**
     * 新增药品采购信息
     *
     * @param purchaseInfo 药品采购信息
     * @return 结果
     */
    boolean purchaseAdd(PurchaseInfo purchaseInfo);

    /**
     * 采购单详情-药品物流
     *
     * @param id 采购ID
     * @return 结果
     */
    LinkedHashMap<String, Object> detailPurchase(String id);
}
