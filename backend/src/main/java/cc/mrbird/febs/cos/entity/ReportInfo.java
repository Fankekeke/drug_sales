package cc.mrbird.febs.cos.entity;

import java.time.LocalDate;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 报表生成管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ReportInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 编号
     */
    private String code;

    /**
     * 标题
     */
    private String title;

    /**
     * 药店ID
     */
    private Integer pharmacyId;

    /**
     * 药品ID
     */
    private Integer drugId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 开始时间
     */
    private String startDate;

    /**
     * 结束时间
     */
    private String endDate;

    /**
     * 报表生成时间
     */
    private String createDate;

    /**
     * 备注信息
     */
    private String remark;


}
