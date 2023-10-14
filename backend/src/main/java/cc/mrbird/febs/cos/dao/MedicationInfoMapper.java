package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.MedicationInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
public interface MedicationInfoMapper extends BaseMapper<MedicationInfo> {

    /**
     * 分页获取电子处方信息
     *
     * @param page 分页对象
     * @param medicationInfo 电子处方信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectMedication(Page<MedicationInfo> page, @Param("medicationInfo") MedicationInfo medicationInfo);
}
