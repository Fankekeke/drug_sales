package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.ReportInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface ReportInfoMapper extends BaseMapper<ReportInfo> {

    /**
     * 分页获取员工信息
     *
     * @param page     分页对象
     * @param reportInfo 员工信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectReportPage(Page<ReportInfo> page, @Param("reportInfo") ReportInfo reportInfo);

    /**
     * 获取订单信息
     *
     * @param orderInfo 订单信息
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> selectOrderList(@Param("orderInfo") ReportInfo orderInfo);
}
