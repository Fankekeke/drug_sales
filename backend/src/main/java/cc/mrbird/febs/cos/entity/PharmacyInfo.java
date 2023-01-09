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
 * 药店管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PharmacyInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 店铺编号
     */
    private String code;

    /**
     * 药店名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 营业状态（1.营业中 2.歇业）
     */
    private Integer businessStatus;

    /**
     * 资质照片
     */
    private String qualification;

    /**
     * 经度
     */
    private BigDecimal longitude;

    /**
     * 纬度
     */
    private BigDecimal latitude;

    /**
     * 店铺照片
     */
    private String images;

    /**
     * 营业时间
     */
    private String businessHours;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 法人姓名
     */
    private String legalPerson;

    /**
     * 联系电话
     */
    private String phone;


}
