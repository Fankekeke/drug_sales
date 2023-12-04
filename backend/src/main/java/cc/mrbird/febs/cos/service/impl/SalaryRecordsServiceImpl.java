package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.dao.SalaryRecordsMapper;
import cc.mrbird.febs.cos.entity.SalaryRecords;
import cc.mrbird.febs.cos.entity.StaffInfo;
import cc.mrbird.febs.cos.service.ISalaryRecordsService;
import cc.mrbird.febs.cos.service.IStaffInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SalaryRecordsServiceImpl extends ServiceImpl<SalaryRecordsMapper, SalaryRecords> implements ISalaryRecordsService {

    private final TemplateEngine templateEngine;

    private final IStaffInfoService staffInfoService;

    /**
     * 分页查询员工薪资发放记录
     *
     * @param page          分页读写
     * @param salaryRecords 员工薪资发放记录
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectSalaryRecordsPage(Page<SalaryRecords> page, SalaryRecords salaryRecords) {
        return baseMapper.selectSalaryRecordsPage(page, salaryRecords);
    }

    /**
     * 查询导出员工薪资发放记录
     *
     * @param salaryRecords 员工薪资发放记录
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> export(SalaryRecords salaryRecords) {
        return baseMapper.export(salaryRecords);
    }

    /**
     * 导出员工薪资发放记录
     *
     * @param year  年度
     * @param month 月度
     * @return 结果
     * @throws Exception 异常
     */
    @Override
    public LinkedHashMap<String, Object> export(String year, String month) throws Exception {
        if (StrUtil.isEmpty(year) || StrUtil.isEmpty(month)) {
            throw new FebsException("参数不得为空！");
        }
        List<SalaryRecords> salaryRecordsList = this.list(Wrappers.<SalaryRecords>lambdaQuery().eq(SalaryRecords::getYear, year).eq(SalaryRecords::getMonth, month));
        if (CollectionUtil.isEmpty(salaryRecordsList)) {
            throw new FebsException("无员工薪资发放记录！");
        }
        List<String> staffCodeList = salaryRecordsList.stream().map(SalaryRecords::getStaffCode).collect(Collectors.toList());
        List<StaffInfo> staffInfoList = staffInfoService.list(Wrappers.<StaffInfo>lambdaQuery().in(StaffInfo::getCode, staffCodeList));
        Map<String, String> staffInfoMap = staffInfoList.stream().collect(Collectors.toMap(StaffInfo::getCode, StaffInfo::getName));
        if (CollectionUtil.isNotEmpty(staffInfoMap)) {
            salaryRecordsList.forEach(item -> item.setStaffName(staffInfoMap.get(item.getStaffCode())));
        }
        BigDecimal totalPrice = salaryRecordsList.stream().map(SalaryRecords::getPayroll).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new LinkedHashMap<String, Object>() {
            {
                put("totalPrice", totalPrice);
                put("salaryRecordsList", salaryRecordsList);
                put("peopleNum", salaryRecordsList.size());
            }
        };
    }

    /**
     * 导出员工薪资发放
     *
     * @param id xx
     * @return 结果
     * @throws Exception 异常
     */
    @Override
    public LinkedHashMap<String, Object> exportByStaff(Integer id) throws Exception {
        SalaryRecords salaryRecords = this.getById(id);
        if (salaryRecords == null) {
            throw new FebsException("信息不存在！");
        }
        // 获取员工信息
        StaffInfo staff = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getCode, salaryRecords.getStaffCode()));
        return new LinkedHashMap<String, Object>() {
            {
                put("staff", staff);
                put("salary", salaryRecords);
            }
        };
    }

    /**
     * 查询薪资发放详情
     *
     * @param recordId 薪资发放ID
     * @return 结果
     */
    @Override
    public SalaryRecords selectSalaryDetail(Integer recordId) {
        return this.getById(recordId);
    }

    /**
     * 根据员工编号获取薪资发放记录
     *
     * @param staffCode 员工编号
     * @param year      所属年份
     * @return 结果
     */
    @Override
    public List<LinkedHashMap<String, Object>> selectSalaryRecordsByStaff(String staffCode, String year) {
        return baseMapper.selectSalaryRecordsByStaff(staffCode, year);
    }

    /**
     * 添加员工薪资发放记录
     *
     * @param salaryRecords 员工薪资发放记录
     * @return 结果
     * @throws Exception 异常
     */
    @Override
    public boolean saveSalaryRecords(SalaryRecords salaryRecords) throws Exception {
        salaryRecords.setYear(StrUtil.toString(DateUtil.year(new Date())));
        salaryRecords.setMonth(StrUtil.toString(DateUtil.month(new Date()) + 1));
        // 校验是否本月度是否发放
        int count = this.count(Wrappers.<SalaryRecords>lambdaQuery().eq(SalaryRecords::getStaffCode, salaryRecords.getStaffCode()).eq(SalaryRecords::getYear, salaryRecords.getYear()).eq(SalaryRecords::getMonth, salaryRecords.getMonth()));
        if (count > 0) {
            throw new FebsException("本月度对该员工已发放工资！");
        }
        // 员工信息
        StaffInfo staffInfo = staffInfoService.getOne(Wrappers.<StaffInfo>lambdaQuery().eq(StaffInfo::getCode, salaryRecords.getStaffCode()));
        // 实发工资
        BigDecimal sum = salaryRecords.getBasicWage().add(salaryRecords.getPostAllowance()).add(salaryRecords.getPerformanceBonus()).add(salaryRecords.getOvertimePay()).add(salaryRecords.getHolidayCosts()).add(salaryRecords.getPension()).add(salaryRecords.getUnemployment()).add(salaryRecords.getMedicalInsurance()).add(salaryRecords.getTax()).add(salaryRecords.getHousingFund());
        salaryRecords.setPayroll(sum);
        salaryRecords.setCreateDate(DateUtil.formatDateTime(new Date()));
        return this.save(salaryRecords);
    }


}
