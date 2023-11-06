package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.entity.ReportInfo;
import cc.mrbird.febs.cos.dao.ReportInfoMapper;
import cc.mrbird.febs.cos.service.IReportInfoService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

/**
 * @author FanK
 */
@Service
public class ReportInfoServiceImpl extends ServiceImpl<ReportInfoMapper, ReportInfo> implements IReportInfoService {

    /**
     * 分页获取员工信息
     *
     * @param page     分页对象
     * @param reportInfo 员工信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectReportPage(Page<ReportInfo> page, ReportInfo reportInfo) {
        return baseMapper.selectReportPage(page, reportInfo);
    }
}
