package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.PaymentRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface PaymentRecordMapper extends BaseMapper<PaymentRecord> {

    /**
     * 分页获取缴费记录信息
     *
     * @param page          分页对象
     * @param paymentRecord 缴费记录信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectPaymentRecordPage(Page<PaymentRecord> page, @Param("paymentRecord") PaymentRecord paymentRecord);

    /**
     * 获取缴费总收益
     *
     * @return 结果
     */
    BigDecimal selectAmountPrice();

    /**
     * 查询十天内缴费统计
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectPaymentRecord();
}
