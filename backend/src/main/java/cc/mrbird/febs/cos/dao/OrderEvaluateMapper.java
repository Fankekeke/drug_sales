package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.OrderEvaluate;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface OrderEvaluateMapper extends BaseMapper<OrderEvaluate> {

    /**
     * 分页获取订单评价信息
     *
     * @param page     分页对象
     * @param orderEvaluate 订单评价信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectEvaluatePage(Page<OrderEvaluate> page, @Param("orderEvaluate") OrderEvaluate orderEvaluate);
}
