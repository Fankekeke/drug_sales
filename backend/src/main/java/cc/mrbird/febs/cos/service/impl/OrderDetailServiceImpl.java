package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.OrderDetail;
import cc.mrbird.febs.cos.mapper.OrderDetailMapper;
import cc.mrbird.febs.cos.service.IOrderDetailService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements IOrderDetailService {

    /**
     * 分页获取订单详情信息
     *
     * @param page     分页对象
     * @param orderDetail 订单详情信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectOrderDeatilPage(Page<OrderDetail> page, OrderDetail orderDetail) {
        return baseMapper.selectOrderDeatilPage(page, orderDetail);
    }
}
