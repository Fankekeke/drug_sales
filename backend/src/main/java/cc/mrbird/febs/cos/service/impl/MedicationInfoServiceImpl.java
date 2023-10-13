package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.MedicationInfo;
import cc.mrbird.febs.cos.dao.MedicationInfoMapper;
import cc.mrbird.febs.cos.service.IMedicationInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class MedicationInfoServiceImpl extends ServiceImpl<MedicationInfoMapper, MedicationInfo> implements IMedicationInfoService {


    /**
     * 分页获取电子处方信息
     *
     * @param page 分页对象
     * @param medicationInfo 电子处方信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectMedication(Page<MedicationInfo> page, MedicationInfo medicationInfo) {
        return baseMapper.selectMedication(page, medicationInfo);
    }
}
