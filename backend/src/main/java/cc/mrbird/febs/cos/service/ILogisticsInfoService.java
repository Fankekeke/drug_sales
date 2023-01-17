package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.LogisticsInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface ILogisticsInfoService extends IService<LogisticsInfo> {

    /**
     * 分页获取配送物流信息
     *
     * @param page     分页对象
     * @param logisticsInfo 配送物流信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectLogisticsPage(Page<LogisticsInfo> page, LogisticsInfo logisticsInfo);

    /**
     * 根据订单编号获取物流信息
     *
     * @param orderId 订单ID
     * @return 结果
     */
    List<LogisticsInfo> selectLogisticsByOrder(Integer orderId);

    /**
     * 新增配送物流信息
     *
     * @param logisticsInfo 配送物流信息
     * @return 结果
     */
    boolean saveLogistics(LogisticsInfo logisticsInfo);
}
