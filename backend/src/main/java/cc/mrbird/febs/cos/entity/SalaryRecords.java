package cc.mrbird.febs.cos.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 员工薪资记录
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SalaryRecords implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 员工编号
     */
    private String staffCode;

    /**
     * 所属年份
     */
    private String year;

    /**
     * 所属月份
     */
    private String month;

    /**
     * 本月收入
     */
    private BigDecimal income;

    /**
     * 基本工资
     */
    private BigDecimal basicWage;

    /**
     * 岗位津贴
     */
    private BigDecimal postAllowance;

    /**
     * 绩效奖金
     */
    private BigDecimal performanceBonus;

    /**
     * 加班费
     */
    private BigDecimal overtimePay;

    /**
     * 过节费
     */
    private BigDecimal holidayCosts;

    /**
     * 养老保险
     */
    private BigDecimal pension;

    /**
     * 失业保险
     */
    private BigDecimal unemployment;

    /**
     * 医疗保险
     */
    private BigDecimal medicalInsurance;

    /**
     * 税金
     */
    private BigDecimal tax;

    /**
     * 住房公积金
     */
    private BigDecimal housingFund;

    /**
     * 实发工资
     */
    private BigDecimal payroll;

    /**
     * 发放时间
     */
    private String createDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 员工姓名
     */
    @TableField(exist = false)
    private String staffName;

}
