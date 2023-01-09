package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.DrugInfo;
import cc.mrbird.febs.cos.service.IDrugInfoService;
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
@RequestMapping("/salm/drug-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DrugInfoController {

    private final IDrugInfoService drugInfoService;

    /**
     * 分页获取药品信息
     *
     * @param page     分页对象
     * @param drugInfo 药品信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<DrugInfo> page, DrugInfo drugInfo) {
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
        return R.ok(drugInfoService.getById(id));
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
