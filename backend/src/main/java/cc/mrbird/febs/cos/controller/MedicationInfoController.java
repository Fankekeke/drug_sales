package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.MedicationInfo;
import cc.mrbird.febs.cos.service.IMedicationInfoService;
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
@RequestMapping("/cos/medication-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicationInfoController {

    private final IMedicationInfoService medicationInfoService;

    /**
     * 分页获取电子处方信息
     *
     * @param page 分页对象
     * @param medicationInfo 电子处方信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<MedicationInfo> page, MedicationInfo medicationInfo) {
        return R.ok(medicationInfoService.selectMedication(page, medicationInfo));
    }

    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(medicationInfoService.getById(id));
    }

    @GetMapping("/list")
    public R list() {
        return R.ok(medicationInfoService.list());
    }

    /**
     * 新增电子处方信息
     *
     * @param medicationInfo 电子处方信息
     * @return 结果
     */
    @PostMapping
    public R save(MedicationInfo medicationInfo) {
        medicationInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(medicationInfoService.save(medicationInfo));
    }

    /**
     * 修改电子处方信息
     *
     * @param medicationInfo 电子处方信息
     * @return 结果
     */
    @PutMapping
    public R edit(MedicationInfo medicationInfo) {
        return R.ok(medicationInfoService.updateById(medicationInfo));
    }

    /**
     * 删除电子处方信息
     *
     * @param ids ids
     * @return 电子处方信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(medicationInfoService.removeByIds(ids));
    }
}
