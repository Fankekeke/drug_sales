package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.PharmacyInfo;
import cc.mrbird.febs.cos.mapper.PharmacyInfoMapper;
import cc.mrbird.febs.cos.service.IPharmacyInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class PharmacyInfoServiceImpl extends ServiceImpl<PharmacyInfoMapper, PharmacyInfo> implements IPharmacyInfoService {

    /**
     * 分页获取药店信息
     *
     * @param page     分页对象
     * @param pharmacyInfo 药店信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectPharmacyPage(Page<PharmacyInfo> page, PharmacyInfo pharmacyInfo) {
        return baseMapper.selectPharmacyPage(page, pharmacyInfo);
    }
}
