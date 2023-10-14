package cc.mrbird.febs.cos.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 药店库存
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PharmacyInventory implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 药店ID
     */
    private Integer pharmacyId;

    /**
     * 药品ID
     */
    private Integer drugId;

    /**
     * 库存数量
     */
    private Integer reserve;

    /**
     * 上架状态（1.上架中 2.下架）
     */
    private Integer shelfStatus;

    @TableField(exist = false)
    private String pharmacyName;

    @TableField(exist = false)
    private String drugName;

    @TableField(exist = false)
    private String brand;

    @TableField(exist = false)
    private BigDecimal unitPrice;

    @TableField(exist = false)
    private String images;

}
