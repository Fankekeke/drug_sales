package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.OrderEvaluate;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IOrderEvaluateService extends IService<OrderEvaluate> {

    /**
     * 分页获取订单评价信息
     *
     * @param page     分页对象
     * @param orderEvaluate 订单评价信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectEvaluatePage(Page<OrderEvaluate> page, OrderEvaluate orderEvaluate);
}
