package cc.mrbird.febs.cos.mapper;

import cc.mrbird.febs.cos.entity.OrderDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

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
}
