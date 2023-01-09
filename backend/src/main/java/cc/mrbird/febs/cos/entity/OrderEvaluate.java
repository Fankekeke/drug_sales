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
 * 订单评价
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class OrderEvaluate implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 订单ID
     */
    private Integer orderId;

    /**
     * 评价用户
     */
    private Integer userId;

    /**
     * 所属药店
     */
    private Integer pharmacyId;

    /**
     * 评价分数
     */
    private BigDecimal score;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价图片
     */
    private String images;

    /**
     * 创建时间
     */
    private String createDate;

    @TableField(exist = false)
    private String orderCode;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String pharmacyName;
}
