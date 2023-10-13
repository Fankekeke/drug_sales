package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.StockAlertInfo;
import cc.mrbird.febs.cos.dao.StockAlertInfoMapper;
import cc.mrbird.febs.cos.service.IStockAlertInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class StockAlertInfoServiceImpl extends ServiceImpl<StockAlertInfoMapper, StockAlertInfo> implements IStockAlertInfoService {

    /**
     * 分页获取库房预警信息
     *
     * @param page 分页对象
     * @param stockAlertInfo 库房预警信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectStockAlertPage(Page<StockAlertInfo> page, StockAlertInfo stockAlertInfo) {
        return baseMapper.selectStockAlertPage(page, stockAlertInfo);
    }
}
