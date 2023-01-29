package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 药品管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class DrugInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 药品编号
     */
    private String code;

    /**
     * 药品名称
     */
    private String name;

    /**
     * 所属品牌
     */
    private String brand;

    /**
     * 所属分类
     */
    private Integer category;

    /**
     * 药品类别
     */
    private Integer classification;

    /**
     * 通用名
     */
    private String commonName;

    /**
     * 剂型
     */
    private String dosageForm;

    /**
     * 用法
     */
    private String usages;

    /**
     * 适用症状
     */
    private String applicableSymptoms;

    /**
     * 适用疾病
     */
    private String applicableDisease;

    /**
     * 包装清单
     */
    private String packingList;

    /**
     * 使用剂量
     */
    private String dosageUse;

    /**
     * 有效期
     */
    private Integer validityPeriod;

    /**
     * 批准文号
     */
    private String approvalNumber;

    /**
     * 生产企业
     */
    private String manufacturer;

    /**
     * 单价
     */
    private BigDecimal unitPrice;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 药品图片
     */
    private String images;


}
