package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.PurchaseInfo;
import cc.mrbird.febs.cos.service.IPurchaseInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/purchase-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PurchaseInfoController {

    private final IPurchaseInfoService purchaseInfoService;

    /**
     * 分页获取药品采购信息
     *
     * @param page         分页对象
     * @param purchaseInfo 药品采购信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<PurchaseInfo> page, PurchaseInfo purchaseInfo) {
        return R.ok(purchaseInfoService.selectPurchasePage(page, purchaseInfo));
    }

    /**
     * 收货
     *
     * @param id 采购ID
     * @return 结果
     */
    @GetMapping("/receipt/{id}")
    public R receipt(@PathVariable("id") Integer id) throws Exception {
        return R.ok(purchaseInfoService.receipt(id));
    }

    /**
     * 采购单详情-药品物流
     *
     * @param purchaseId 采购ID
     * @return 结果
     */
    @GetMapping("/detail/{purchaseId}")
    public R detailPurchase(@PathVariable("purchaseId") String purchaseId) {
        return R.ok(purchaseInfoService.detailPurchase(purchaseId));
    }

    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(purchaseInfoService.getById(id));
    }

    @GetMapping("/list")
    public R list() {
        return R.ok(purchaseInfoService.list());
    }

    /**
     * 新增药品采购信息
     *
     * @param purchaseInfo 药品采购信息
     * @return 结果
     */
    @PostMapping
    public R save(PurchaseInfo purchaseInfo) {
        purchaseInfo.setCode("PUR-" + System.currentTimeMillis());
        purchaseInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(purchaseInfoService.purchaseAdd(purchaseInfo));
    }

    /**
     * 修改药品采购信息
     *
     * @param purchaseInfo 药品采购信息
     * @return 结果
     */
    @PutMapping
    public R edit(PurchaseInfo purchaseInfo) {
        return R.ok(purchaseInfoService.updateById(purchaseInfo));
    }

    /**
     * 删除药品采购信息
     *
     * @param ids ids
     * @return 药品采购信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(purchaseInfoService.removeByIds(ids));
    }
}
