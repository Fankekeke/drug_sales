package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.DrugInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface DrugInfoMapper extends BaseMapper<DrugInfo> {

    /**
     * 分页获取药品信息
     *
     * @param page     分页对象
     * @param drugInfo 药品信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDrugPage(Page<DrugInfo> page, @Param("drugInfo") DrugInfo drugInfo);
}
