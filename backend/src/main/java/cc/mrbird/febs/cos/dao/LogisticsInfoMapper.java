package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.LogisticsInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface LogisticsInfoMapper extends BaseMapper<LogisticsInfo> {

    /**
     * 分页获取配送物流信息
     *
     * @param page     分页对象
     * @param logisticsInfo 配送物流信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectLogisticsPage(Page<LogisticsInfo> page, @Param("logisticsInfo") LogisticsInfo logisticsInfo);
}
