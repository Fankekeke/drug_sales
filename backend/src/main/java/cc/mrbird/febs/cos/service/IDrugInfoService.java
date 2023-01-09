package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.DrugInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface IDrugInfoService extends IService<DrugInfo> {

    /**
     * 分页获取药品信息
     *
     * @param page     分页对象
     * @param drugInfo 药品信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectDrugPage(Page<DrugInfo> page, DrugInfo drugInfo);
}
