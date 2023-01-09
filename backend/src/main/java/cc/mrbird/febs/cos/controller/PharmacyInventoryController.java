package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.PharmacyInventory;
import cc.mrbird.febs.cos.service.IPharmacyInventoryService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/salm/pharmacy-inventory")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PharmacyInventoryController {

    private final IPharmacyInventoryService pharmacyInventoryService;

    /**
     * 分页获取药店库存信息
     *
     * @param page     分页对象
     * @param pharmacyInventory 药店库存信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<PharmacyInventory> page, PharmacyInventory pharmacyInventory) {
        return R.ok();
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
