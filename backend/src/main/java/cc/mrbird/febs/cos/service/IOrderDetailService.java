package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.OrderDetail;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IOrderDetailService extends IService<OrderDetail> {

    /**
     * 分页获取订单详情信息
     *
     * @param page     分页对象
     * @param orderDetail 订单详情信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderDeatilPage(Page<OrderDetail> page, OrderDetail orderDetail);
}
