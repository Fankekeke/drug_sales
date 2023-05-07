package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DrugInfo;
import cc.mrbird.febs.cos.service.IDrugInfoService;
import cc.mrbird.febs.cos.service.IPharmacyInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/drug-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DrugInfoController {

    private final IDrugInfoService drugInfoService;

    private final IPharmacyInfoService pharmacyInfoService;

    /**
     * 分页获取药品信息
     *
     * @param page     分页对象
     * @param drugInfo 药品信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DrugInfo> page, DrugInfo drugInfo) {
        return R.ok(drugInfoService.selectDrugPage(page, drugInfo));
    }

    /**
     * 获取详情信息
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(drugInfoService.getById(id));
    }

    @GetMapping("/selectDrugDetail")
    public R selectDrugDetail(@RequestParam("drugId") Integer drugId, @RequestParam("pharmacyId") Integer pharmacyId) {
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("drug", drugInfoService.getById(drugId));
        result.put("pharmacy", pharmacyInfoService.getById(pharmacyId));
        return R.ok(result);
    }

    /**
     * 获取信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(drugInfoService.list());
    }

    /**
     * 新增药品信息
     *
     * @param drugInfo 药品信息
     * @return 结果
     */
    @PostMapping
    public R save(DrugInfo drugInfo) {
        drugInfo.setCode("DG-" + System.currentTimeMillis());
        drugInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(drugInfoService.save(drugInfo));
    }

    /**
     * 修改药品信息
     *
     * @param drugInfo 药品信息
     * @return 结果
     */
    @PutMapping
    public R edit(DrugInfo drugInfo) {
        return R.ok(drugInfoService.updateById(drugInfo));
    }

    /**
     * 删除药品信息
     *
     * @param ids ids
     * @return 药品信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(drugInfoService.removeByIds(ids));
    }
}
