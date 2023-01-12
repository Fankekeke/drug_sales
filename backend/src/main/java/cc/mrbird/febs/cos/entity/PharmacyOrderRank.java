package cc.mrbird.febs.cos.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 订单统计排名
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PharmacyOrderRank {

    private Integer pharmacyId;

    private String pharmacyName;

    private Integer orderNum;

    private BigDecimal totalPrice;

    public PharmacyOrderRank(Integer pharmacyId, String pharmacyName, Integer orderNum, BigDecimal totalPrice) {
        this.pharmacyId = pharmacyId;
        this.pharmacyName = pharmacyName;
        this.orderNum = orderNum;
        this.totalPrice = totalPrice;
    }

    public PharmacyOrderRank() {}
}
