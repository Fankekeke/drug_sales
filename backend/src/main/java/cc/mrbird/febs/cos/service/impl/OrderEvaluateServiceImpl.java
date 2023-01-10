package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.OrderEvaluate;
import cc.mrbird.febs.cos.dao.OrderEvaluateMapper;
import cc.mrbird.febs.cos.service.IOrderEvaluateService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class OrderEvaluateServiceImpl extends ServiceImpl<OrderEvaluateMapper, OrderEvaluate> implements IOrderEvaluateService {

    /**
     * 分页获取订单评价信息
     *
     * @param page     分页对象
     * @param orderEvaluate 订单评价信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectEvaluatePage(Page<OrderEvaluate> page, OrderEvaluate orderEvaluate) {
        return baseMapper.selectEvaluatePage(page, orderEvaluate);
    }
}
