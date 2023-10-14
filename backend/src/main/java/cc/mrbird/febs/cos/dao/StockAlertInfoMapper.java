package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.StockAlertInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface StockAlertInfoMapper extends BaseMapper<StockAlertInfo> {

    /**
     * 分页获取库房预警信息
     *
     * @param page 分页对象
     * @param stockAlertInfo 库房预警信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectStockAlertPage(Page<StockAlertInfo> page, @Param("stockAlertInfo") StockAlertInfo stockAlertInfo);
}
