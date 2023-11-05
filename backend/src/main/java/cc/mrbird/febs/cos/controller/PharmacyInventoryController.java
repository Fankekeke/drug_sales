package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.PharmacyInventory;
import cc.mrbird.febs.cos.entity.vo.InventoryVo;
import cc.mrbird.febs.cos.service.IPharmacyInventoryService;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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
@RequestMapping("/cos/pharmacy-inventory")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PharmacyInventoryController {

    private final IPharmacyInventoryService pharmacyInventoryService;

    /**
     * 获取药品信息
     *
     * @param key key
     * @return 结果
     */
    @GetMapping("/drug/list")
    public R selectPharmacyDrugList(@RequestParam(value = "key", required = false) String key) {
        return R.ok(pharmacyInventoryService.selectPharmacyDrugList(key));
    }

    /**
     * 药品上下架
     *
     * @param id     库存ID
     * @param status 状态
     * @return 结果
     */
    @GetMapping("/audit")
    public R audit(Integer id, Integer status, @RequestParam(value = "remark", required = false) String remark) {
        return R.ok(pharmacyInventoryService.update(Wrappers.<PharmacyInventory>lambdaUpdate().set(PharmacyInventory::getShelfStatus, status)
                .set(StrUtil.isNotEmpty(remark), PharmacyInventory::getRemark, remark).eq(PharmacyInventory::getId, id)));
    }

    /**
     * 分页获取药店库存信息
     *
     * @param page              分页对象
     * @param pharmacyInventory 药店库存信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<PharmacyInventory> page, PharmacyInventory pharmacyInventory) {
        return R.ok(pharmacyInventoryService.selectPharmacyInventoryPage(page, pharmacyInventory));
    }

    /**
     * 批量设置库房库存
     *
     * @param pharmacyId            参数
     * @param pharmacyInventoryList 参数
     * @return 结果
     * @throws Exception 异常
     */
    @PostMapping("/batch/put")
    public R batchPutInventory(@RequestParam("pharmacyId") Integer pharmacyId, @RequestParam("pharmacyInventoryList") String pharmacyInventoryList) throws Exception {
        return R.ok(pharmacyInventoryService.batchPutInventory(pharmacyId, pharmacyInventoryList));
    }

    /**
     * 根据药房ID获取库存信息
     *
     * @param pharmacyId 药房ID
     * @return 结果
     */
    @GetMapping("/detail/pharmacy/{pharmacyId}")
    public R selectInventoryByPharmacy(@PathVariable("pharmacyId") Integer pharmacyId) {
        return R.ok(pharmacyInventoryService.selectInventoryByPharmacy(pharmacyId));
    }

    /**
     * 设置库存
     *
     * @param pharmacyInventory 药店库存信息
     * @return 结果
     */
    @PostMapping("/put/reserve")
    public R putInventory(PharmacyInventory pharmacyInventory) {
        return R.ok(pharmacyInventoryService.putInventory(pharmacyInventory));
    }

    /**
     * 设置库存
     *
     * @param pharmacyInventory 药店库存信息
     * @return 结果
     */
    @PostMapping("/out/reserve")
    public R outInventory(PharmacyInventory pharmacyInventory) {
        return R.ok(pharmacyInventoryService.outInventory(pharmacyInventory));
    }

    /**
     * 获取详情信息
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(pharmacyInventoryService.getById(id));
    }

    /**
     * 获取信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(pharmacyInventoryService.list());
    }

    /**
     * 新增药店库存信息
     *
     * @param pharmacyInventory 药店库存信息
     * @return 结果
     */
    @PostMapping
    public R save(PharmacyInventory pharmacyInventory) {
        return R.ok(pharmacyInventoryService.save(pharmacyInventory));
    }

    /**
     * 修改保质期时间
     *
     * @param pharmacyInventory xx
     * @return 结果
     */
    @PutMapping("/date/put")
    public R updateDate(PharmacyInventory pharmacyInventory) {
        // 判断结束日期是否小于今天
        pharmacyInventoryService.updateById(pharmacyInventory);
        return R.ok(DateUtil.between(new Date(), DateUtil.parseDate(pharmacyInventory.getEndDate()), DateUnit.DAY, false));
    }

    /**
     * 修改药店库存信息
     *
     * @param pharmacyInventory 药店库存信息
     * @return 结果
     */
    @PutMapping
    public R edit(PharmacyInventory pharmacyInventory) {
        return R.ok(pharmacyInventoryService.updateById(pharmacyInventory));
    }

    /**
     * 删除药店库存信息
     *
     * @param ids ids
     * @return 药店库存信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(pharmacyInventoryService.removeByIds(ids));
    }

}
