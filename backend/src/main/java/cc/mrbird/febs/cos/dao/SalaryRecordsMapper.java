package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.SalaryRecords;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface SalaryRecordsMapper extends BaseMapper<SalaryRecords> {

    /**
     * 分页查询员工薪资发放记录
     *
     * @param page          分页对象
     * @param salaryRecords 员工薪资发放记录
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectSalaryRecordsPage(Page<SalaryRecords> page, @Param("salaryRecords") SalaryRecords salaryRecords);

    /**
     * 根据员工编号获取薪资发放记录
     *
     * @param staffCode 员工编号
     * @param year      所属年份
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectSalaryRecordsByStaff(@Param("staffCode") String staffCode, @Param("year") String year);
}
