package cc.mrbird.febs.cos.entity;

import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 电子处方
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class MedicationInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private String code;

    /**
     * 病因
     */
    private String cause;

    /**
     * 所属用户
     */
    private Integer userId;

    /**
     * 出具人
     */
    private String checkIssuer;

    /**
     * 出具机构
     */
    private String checkAgency;

    /**
     * 状态（0.未处理 1.已处理）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 所属订单
     */
    private String orderCode;


}
