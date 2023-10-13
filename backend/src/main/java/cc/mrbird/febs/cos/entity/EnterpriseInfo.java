package cc.mrbird.febs.cos.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 企业信息管理
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EnterpriseInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 功能供应商名称
     */
    private String name;

    /**
     * 单位简称或代号
     */
    private String abbreviation;

    /**
     * 统一社会信用代码
     */
    private String creditCode;

    /**
     * 编码
     */
    private String code;

    /**
     * 单位性质(1:企业单位;2:事业单位;3:社会团体;4:军队单位;5:机关单位;6:其它单位;7:个体经营)
     */
    private String nature;

    /**
     * 二级企业单位性质
     */
    private String natureTwo;

    /**
     * 经营状态(1:存续2:在业3:吊销4:注销5:迁入6:迁出7:停业8:清算)
     */
    private String status;

    /**
     * 法定代表人
     */
    private String corporateRepresentative;

    /**
     * 法定代表人身份证号
     */
    private String corporateRepresentativeId;

    /**
     * 法定代表人电话
     */
    private String corporateRepresentativePhone;

    /**
     * 注册资本（万元）
     */
    private String registeredCapital;

    /**
     * 注册资金币种
     */
    private String registeredCapitalCurrency;

    /**
     * 成立日期
     */
    private String establishmentDate;

    /**
     * 营业期限始期
     */
    private String businessBeginDate;

    /**
     * 营业期限止期
     */
    private String businessEndDate;

    /**
     * 注册地址
     */
    private String registeredAddress;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 来源(1:自主注册、2:邀请注册、3:企业维护)
     */
    private String source;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;

    /**
     * 英文企业名称
     */
    private String enName;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 单位简介
     */
    private String unitDescription;

    /**
     * 创建时间
     */
    private String createDate;

    /**
     * 企业照片
     */
    private String images;
}
