package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.SalaryRecords;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface ISalaryRecordsService extends IService<SalaryRecords> {

    /**
     * 分页查询员工薪资发放记录
     *
     * @param page          分页读写
     * @param salaryRecords 员工薪资发放记录
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectSalaryRecordsPage(Page<SalaryRecords> page, SalaryRecords salaryRecords);

    /**
     * 查询导出员工薪资发放记录
     *
     * @param salaryRecords 员工薪资发放记录
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> export(SalaryRecords salaryRecords);

    /**
     * 导出员工薪资发放记录
     *
     * @param year  年度
     * @param month 月度
     * @return 结果
     * @throws Exception 异常
     */
    LinkedHashMap<String, Object> export(String year, String month) throws Exception;

    /**
     * 导出员工薪资发放
     *
     * @param id  xx
     * @return 结果
     * @throws Exception 异常
     */
    LinkedHashMap<String, Object> exportByStaff(Integer id) throws Exception;

    /**
     * 查询薪资发放详情
     *
     * @param recordId 薪资发放ID
     * @return 结果
     */
    SalaryRecords selectSalaryDetail(Integer recordId);

    /**
     * 根据员工编号获取薪资发放记录
     *
     * @param staffCode 员工编号
     * @param year      所属年份
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectSalaryRecordsByStaff(String staffCode, String year);

    /**
     * 添加员工薪资发放记录
     *
     * @param salaryRecords 员工薪资发放记录
     * @return 结果
     * @throws Exception 异常
     */
    boolean saveSalaryRecords(SalaryRecords salaryRecords) throws Exception;

}
