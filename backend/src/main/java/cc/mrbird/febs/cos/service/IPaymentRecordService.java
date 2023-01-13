package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.PaymentRecord;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IPaymentRecordService extends IService<PaymentRecord> {

    /**
     * 分页获取缴费记录信息
     *
     * @param page 分页对象
     * @param paymentRecord 缴费记录信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectPaymentRecordPage(Page<PaymentRecord> page, PaymentRecord paymentRecord);
}
