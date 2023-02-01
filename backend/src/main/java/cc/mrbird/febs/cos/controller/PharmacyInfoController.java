package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.PharmacyInfo;
import cc.mrbird.febs.cos.service.IPharmacyInfoService;
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
@RequestMapping("/cos/pharmacy-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PharmacyInfoController {

    private final IPharmacyInfoService pharmacyInfoService;

    /**
     * 分页获取药店信息
     *
     * @param page         分页对象
     * @param pharmacyInfo 药店信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<PharmacyInfo> page, PharmacyInfo pharmacyInfo) {
        return R.ok(pharmacyInfoService.selectPharmacyPage(page, pharmacyInfo));
    }

    /**
     * 查询本月订单数量排行
     *
     * @return 结果
     */
    @GetMapping("/order/rank/{type}")
    public R selectOrderRank(@PathVariable("type") Integer type) {
        return R.ok(pharmacyInfoService.selectOrderRank(type));
    }

    /**
     * 查询药店评价排名
     *
     * @return 结果
     */
    @GetMapping("/evaluate/rank")
    public R selectPharmacyEvaluateRank() {
        return R.ok(pharmacyInfoService.selectPharmacyEvaluateRank());
    }

    /**
     * 查询药店评价信息
     *
     * @param pharmacyId 药店ID
     * @return 结果
     */
    @GetMapping("/evaluate/code/{pharmacyId}")
    public R selectPharmacyEvaluateByCode(@PathVariable("pharmacyId") Integer pharmacyId) {
        return R.ok(pharmacyInfoService.selectPharmacyEvaluateByCode(pharmacyId));
    }

    /**
     * 获取药店情况
     *
     * @return 结果
     */
    @GetMapping("/statistics")
    public R selectOrderNumByPharmacy() {
        return R.ok(pharmacyInfoService.selectOrderNumByPharmacy());
    }

    /**
     * 获取详情信息
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(pharmacyInfoService.getById(id));
    }

    /**
     * 获取信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(pharmacyInfoService.list());
    }

    /**
     * 新增药店信息
     *
     * @param pharmacyInfo 药店信息
     * @return 结果
     */
    @PostMapping
    public R save(PharmacyInfo pharmacyInfo) {
        pharmacyInfo.setCode("PM-" + System.currentTimeMillis());
        pharmacyInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(pharmacyInfoService.save(pharmacyInfo));
    }

    /**
     * 修改药店信息
     *
     * @param pharmacyInfo 药店信息
     * @return 结果
     */
    @PutMapping
    public R edit(PharmacyInfo pharmacyInfo) {
        return R.ok(pharmacyInfoService.updateById(pharmacyInfo));
    }

    /**
     * 删除药店信息
     *
     * @param ids ids
     * @return 药店信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(pharmacyInfoService.removeByIds(ids));
    }

}
