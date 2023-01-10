package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.LogisticsInfo;
import cc.mrbird.febs.cos.dao.LogisticsInfoMapper;
import cc.mrbird.febs.cos.service.ILogisticsInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
public class LogisticsInfoServiceImpl extends ServiceImpl<LogisticsInfoMapper, LogisticsInfo> implements ILogisticsInfoService {

    /**
     * 分页获取配送物流信息
     *
     * @param page     分页对象
     * @param logisticsInfo 配送物流信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectLogisticsPage(Page<LogisticsInfo> page, LogisticsInfo logisticsInfo) {
        return baseMapper.selectLogisticsPage(page, logisticsInfo);
    }

    /**
     * 根据订单编号获取物流信息
     *
     * @param orderId 订单ID
     * @return 结果
     */
    @Override
    public List<LogisticsInfo> selectLogisticsByOrder(Integer orderId) {
        return this.list(Wrappers.<LogisticsInfo>lambdaQuery().eq(LogisticsInfo::getOrderId, orderId));
    }
}
