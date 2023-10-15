package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.StockAlertInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IStockAlertInfoService extends IService<StockAlertInfo> {

    /**
     * 分页获取库房预警信息
     *
     * @param page           分页对象
     * @param stockAlertInfo 库房预警信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectStockAlertPage(Page<StockAlertInfo> page, StockAlertInfo stockAlertInfo);

    /**
     * 库存预警校验
     */
    void stockAlertCheck();
}
