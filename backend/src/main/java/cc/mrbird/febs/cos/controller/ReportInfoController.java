package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.ReportInfo;
import cc.mrbird.febs.cos.service.IReportInfoService;
import cn.hutool.core.date.DateUtil;
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
@RequestMapping("/cos/report-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReportInfoController {

    private final IReportInfoService reportInfoService;

    /**
     * 分页获取员工信息
     *
     * @param page     分页对象
     * @param reportInfo 员工信息
     * @return 结果
     */
    @GetMapping("/page")
    public R page(Page<ReportInfo> page, ReportInfo reportInfo) {
        return R.ok(reportInfoService.selectReportPage(page, reportInfo));
    }

    /**
     * 获取详情信息
     *
     * @param id id
     * @return 结果
     */
    @GetMapping("/{id}")
    public R detail(@PathVariable("id") Integer id) {
        return R.ok(reportInfoService.getById(id));
    }

    /**
     * 获取信息列表
     *
     * @return 结果
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(reportInfoService.list());
    }

    /**
     * 新增员工信息
     *
     * @param reportInfo 员工信息
     * @return 结果
     */
    @PostMapping
    public R save(ReportInfo reportInfo) {
        reportInfo.setCode("STF-" + System.currentTimeMillis());
        reportInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(reportInfoService.save(reportInfo));
    }

    /**
     * 修改员工信息
     *
     * @param reportInfo 员工信息
     * @return 结果
     */
    @PutMapping
    public R edit(ReportInfo reportInfo) {
        return R.ok(reportInfoService.updateById(reportInfo));
    }

    /**
     * 删除员工信息
     *
     * @param ids ids
     * @return 员工信息
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(reportInfoService.removeByIds(ids));
    }
}
