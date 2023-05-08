package cc.mrbird.febs.cos.entity.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderSubVo {

    /**
     * 所属药店
     */
    private Integer pharmacyId;

    /**
     * 药品ID
     */
    private Integer drugId;

    /**
     * 购买数量
     */
    private BigDecimal total;

    /**
     * 单价
     */
    private BigDecimal unitPrice;
}
