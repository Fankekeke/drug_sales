package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.cos.dao.EnterpriseInfoMapper;
import cc.mrbird.febs.cos.entity.EnterpriseInfo;
import cc.mrbird.febs.cos.service.IEnterpriseInfoService;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnterpriseInfoServiceImpl extends ServiceImpl<EnterpriseInfoMapper, EnterpriseInfo> implements IEnterpriseInfoService {

    /**
     * 分页获取企业信息
     *
     * @param page 分页对象
     * @param enterpriseInfo 企业信息
     * @return 结果
     */
    @Override
    public IPage<LinkedHashMap<String, Object>> selectEnterprisePage(Page<EnterpriseInfo> page, EnterpriseInfo enterpriseInfo) {
        return baseMapper.selectEnterprisePage(page, enterpriseInfo);
    }

    /**
     * 导入企业信息列表
     *
     * @param file 文件
     * @return 结果
     */
    @Override
    public String importExcel(MultipartFile file) throws Exception {
        ExcelReader excelReader = ExcelUtil.getReader(file.getInputStream(), 0);
        setExcelHeaderAlias(excelReader);
        List<EnterpriseInfo> reports = excelReader.read(1, 2, Integer.MAX_VALUE, EnterpriseInfo.class);
        StringBuilder error = new StringBuilder("");
        if (CollectionUtil.isEmpty(reports)) {
            error.append("导入数据不得为空。");
            return error.toString();
        }
        for (EnterpriseInfo expert : reports) {
            if (StrUtil.isEmpty(expert.getName())) {
                error.append("\n名称不能为空");
                return error.toString();
            }
            if (StrUtil.isEmpty(expert.getCreditCode())) {
                error.append("\n统一社会信用代码不能为空");
                return error.toString();
            }
            expert.setCode("EP-" + System.currentTimeMillis());
            expert.setCreateDate(DateUtil.formatDateTime(new Date()));
        }
        if (StrUtil.isEmpty(error.toString())) {
            this.saveBatch(reports);
            return null;
        }
        return error.toString();
    }

    /**
     * 企业注册
     *
     * @param enterpriseCode 企业编号
     * @return 结果
     */
    @Override
    public String enterpriseRegister(String enterpriseCode) {
        if (StrUtil.isEmpty(enterpriseCode)) {
            EnterpriseInfo enterpriseInfo = new EnterpriseInfo();
            enterpriseInfo.setCode("EP-" + System.currentTimeMillis());
            enterpriseInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
            this.save(enterpriseInfo);
            return enterpriseInfo.getCode();
        }
        return enterpriseCode;
    }

    /**
     * 设置HeaderAlias
     *
     * @param excelReader HeaderAlias
     */
    public void setExcelHeaderAlias(ExcelReader excelReader) {
        excelReader.addHeaderAlias("功能供应商名称", "name");
        excelReader.addHeaderAlias("单位简称或代号", "abbreviation");
        excelReader.addHeaderAlias("统一社会信用代码", "creditCode");
        excelReader.addHeaderAlias("单位性质", "nature");
        excelReader.addHeaderAlias("二级企业单位性质", "natureTwo");
        excelReader.addHeaderAlias("经营状态", "status");
        excelReader.addHeaderAlias("法定代表人", "corporateRepresentative");
        excelReader.addHeaderAlias("法定代表人身份证号", "corporateRepresentativeId");
        excelReader.addHeaderAlias("法定代表人电话", "corporateRepresentativePhone");
        excelReader.addHeaderAlias("注册资本", "registeredCapital");
        excelReader.addHeaderAlias("注册资金币种", "registeredCapitalCurrency");
        excelReader.addHeaderAlias("成立日期", "establishmentDate");
        excelReader.addHeaderAlias("营业期限始期", "businessBeginDate");
        excelReader.addHeaderAlias("营业期限止期", "businessEndDate");
        excelReader.addHeaderAlias("注册地址", "registeredAddress");
        excelReader.addHeaderAlias("经营范围", "businessScope");
        excelReader.addHeaderAlias("省", "province");
        excelReader.addHeaderAlias("市", "city");
        excelReader.addHeaderAlias("区", "district");
        excelReader.addHeaderAlias("英文企业名称", "enName");
        excelReader.addHeaderAlias("所属行业", "industry");
        excelReader.addHeaderAlias("单位简介", "unitDescription");
    }
}
