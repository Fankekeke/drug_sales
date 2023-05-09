package cc.mrbird.febs.cos.service;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.cos.entity.OrderInfo;
import cc.mrbird.febs.cos.entity.vo.OrderDetailVo;
import cc.mrbird.febs.cos.entity.vo.OrderInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface IOrderInfoService extends IService<OrderInfo> {

    /**
     * 分页获取订单信息
     *
     * @param page      分页对象
     * @param orderInfo 订单信息
     * @return 结果
     */
    IPage<LinkedHashMap<String, Object>> selectOrderPage(Page<OrderInfo> page, OrderInfo orderInfo);

    /**
     * 添加订单信息
     *
     * @param orderInfoVo 订单信息
     * @return 结果
     */
    boolean orderAdd(OrderInfoVo orderInfoVo, boolean flag);

    /**
     * 订单付款
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    boolean orderPayment(String orderCode);

    /**
     * 订单打印小票
     *
     * @param orderId 订单ID
     * @return 结果
     */
    LinkedHashMap<String, Object> receipt(Integer orderId);

    /**
     * 订单发货
     *
     * @param orderId 订单ID
     * @param remark  物流信息
     * @return 结果
     */
    boolean orderShip(Integer orderId, String remark);

    /**
     * 订单付款
     *
     * @param orderCode 订单编号
     * @param staffCode 员工编号
     */
    void orderPaymentPlatform(String orderCode, String staffCode);

    /**
     * 获取用户订单统计
     *
     * @param userId 用户ID
     * @return 结果
     */
    LinkedHashMap<String, Object> selectOrderRateByUser(Integer userId);

    /**
     * 用户提交订单
     *
     * @param orderDetailVo 订单信息
     * @return 结果
     */
    boolean orderSubmit(OrderDetailVo orderDetailVo) throws FebsException;
}
