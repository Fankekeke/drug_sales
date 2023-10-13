package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 药品采购
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PurchaseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 采购单号
     */
    private String code;

    /**
     * 供应商ID
     */
    private Integer supplierId;

    /**
     * 采购金额
     */
    private BigDecimal amount;

    /**
     * 采购人
     */
    private String purchaser;

    /**
     * 采购时间
     */
    private String createDate;

    /**
     * 采购状态（1.运输中 2.已验收）
     */
    private Integer status;

    /**
     * 采购物流
     */
    private String logistics;

    /**
     * 采购药品
     */
    private String purchaseDrug;

    /**
     * 总价格
     */
    private BigDecimal totalPrice;
}
