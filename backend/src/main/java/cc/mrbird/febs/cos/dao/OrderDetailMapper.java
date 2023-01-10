package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {

    /**
     * 分页获取订单详情信息
     *
     * @param page     分页对象
     * @param orderDetail 订单详情信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderDeatilPage(Page<OrderDetail> page, @Param("orderDetail") OrderDetail orderDetail);

    /**
     * 根据订单ID获取购买详细物品
     *
     * @param orderId 订单ID
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectDetailByOrder(@Param("orderId") Integer orderId);
}
