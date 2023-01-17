package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 订单编号
     */
    private String code;

    /**
     * 所属药店
     */
    private Integer pharmacyId;

    /**
     * 总价格
     */
    private BigDecimal totalCost;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 购买时间
     */
    private String createDate;

    /**
     * 物流信息
     */
    private Integer logisticsId;

    /**
     * 订单状态（0.待付款 1.已下单 2.配送中 3.已收货）
     */
    private Integer orderStatus;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String pharmacyName;

}
