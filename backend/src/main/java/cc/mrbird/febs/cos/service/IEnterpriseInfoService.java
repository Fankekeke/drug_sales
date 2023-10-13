package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.cos.entity.EnterpriseInfo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IEnterpriseInfoService extends IService<EnterpriseInfo> {

    /**
     * 分页获取企业信息
     *
     * @param page 分页对象
     * @param enterpriseInfo 企业信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectEnterprisePage(Page<EnterpriseInfo> page, EnterpriseInfo enterpriseInfo);


    /**
     * 导入企业信息列表
     *
     * @param file 文件
     * @return 结果
     */
    String importExcel(MultipartFile file) throws Exception;

    /**
     * 企业注册
     *
     * @param enterpriseCode 企业编号
     * @return 结果
     */
    String enterpriseRegister(String enterpriseCode);
}
