package cc.mrbird.febs.cos.entity.vo;

import cc.mrbird.febs.cos.entity.OrderEvaluate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 评价排名
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EvaluateRankVo {

    private String pharmacyName;

    private BigDecimal score;

    private List<OrderEvaluate> evaluateList;
}
