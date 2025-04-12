package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.*;
import cc.mrbird.febs.cos.service.IGoodsBelongService;
import cc.mrbird.febs.cos.service.IGoodsRequestService;
import cc.mrbird.febs.cos.service.IOwnerInfoService;
import cc.mrbird.febs.cos.service.IRepairInfoService;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/repair-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RepairInfoController {

    private final IRepairInfoService repairInfoService;

    private final IOwnerInfoService ownerInfoService;

    private final IGoodsRequestService goodsRequestService;

    private final IGoodsBelongService goodsBelongService;

    /**
     * 查看维修详情
     *
     * @param repairId
     * @return
     */
    @GetMapping("/repairInfoById")
    public R repairInfoById(Integer repairId) {
        return R.ok(repairInfoService.repairInfoById(repairId));
    }

    /**
     * 添加维修信息
     *
     * @param repairInfo 维修信息
     * @return 结果
     */
    @PostMapping("/repairRequest")
    @Transactional(rollbackFor = Exception.class)
    public R repairRequest(RepairInfo repairInfo) {

        GoodsRequest goodsRequest = new GoodsRequest();
        goodsRequest.setNum("REQ-" + System.currentTimeMillis());
        goodsRequest.setStep(0);
        goodsRequest.setCreateDate(DateUtil.formatDateTime(new Date()));
        goodsRequest.setUserId(repairInfo.getUserId());
        goodsRequest.setContent(repairInfo.getContent());

        JSONArray array = JSONUtil.parseArray(repairInfo.getGoodsList());
        List<GoodsBelong> goodsBelongList = JSONUtil.toList(array, GoodsBelong.class);
        // 计算总价格
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (GoodsBelong goodsBelong : goodsBelongList) {
            totalAmount = NumberUtil.add(totalAmount, NumberUtil.mul(goodsBelong.getPrice(), goodsBelong.getAmount()));
        }
        goodsBelongList.forEach(item -> {
            // 添加所属信息
            GoodsBelong goodsBelong = new GoodsBelong();
            goodsBelong.setNum(goodsRequest.getNum());
            goodsBelong.setCreateDate(DateUtil.formatDateTime(new Date()));
            goodsBelong.setAmount(item.getAmount());
            goodsBelong.setName(item.getName());
            goodsBelong.setPrice(item.getPrice());
            goodsBelong.setType(item.getType());
            goodsBelong.setTypeId(item.getTypeId());
            goodsBelong.setUnit(item.getUnit());
            goodsBelongService.save(goodsBelong);
        });

        // 更新维修订单申请单号
        repairInfoService.update(Wrappers.<RepairInfo>lambdaUpdate().set(RepairInfo::getRequestNo, goodsRequest.getNum())
                .set(RepairInfo::getTotalPrice, totalAmount)
                .set(RepairInfo::getDeviceId, repairInfo.getDeviceId())
                .eq(RepairInfo::getId, repairInfo.getId()));

        return R.ok(goodsRequestService.save(goodsRequest));
    }

    /**
     * 分页查询房屋维修统计
     *
     * @param page       分页对象
     * @param repairInfo 房屋
     * @return 结果
     */
    @GetMapping("/repair/page")
    public R selectHouseRepairPage(Page<HousesInfo> page, RepairInfo repairInfo) {
        return R.ok(repairInfoService.selectHouseRepairPage(page, repairInfo));
    }

    /**
     * 查询主页信息
     *
     * @return 结果
     */
    @GetMapping("/home/data")
    public R homeData() {
        return R.ok(repairInfoService.homeData());
    }

    /**
     * 数据统计
     *
     * @param checkDate 选择日期
     * @return 结果
     */
    @GetMapping("/statistics")
    public R selectRoomStatistics(@RequestParam(value = "checkDate", required = false) String checkDate) {
        return R.ok(repairInfoService.selectStatistics(checkDate));
    }

    /**
     * 修改维修状态
     *
     * @param repairId
     * @return
     */
    @GetMapping("/down")
    public R down(Integer repairId) {
        return R.ok(repairInfoService.update(Wrappers.<RepairInfo>lambdaUpdate().set(RepairInfo::getRepairStatus, 2).set(RepairInfo::getRepairDate, DateUtil.formatDateTime(new Date())).eq(RepairInfo::getId, repairId)));
    }

    /**
     * 支付回调
     *
     * @param orderCode 订单编号
     * @return 结果
     */
    @GetMapping("/payment")
    public R payment(String orderCode) {
        return R.ok(repairInfoService.update(Wrappers.<RepairInfo>lambdaUpdate().set(RepairInfo::getPayDate, DateUtil.formatDateTime(new Date())).eq(RepairInfo::getCode, orderCode)));
    }

    /**
     * 分页查询维修信息
     *
     * @param page
     * @param repairInfo
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, RepairInfo repairInfo) {
        return R.ok(repairInfoService.repairInfoByPage(page, repairInfo));
    }

    /**
     * 添加维修信息
     *
     * @param repairInfo
     * @return
     */
    @PostMapping
    public R save(RepairInfo repairInfo) {
        repairInfo.setCode("ORD-" + System.currentTimeMillis());
        repairInfo.setRepairStatus(0);
        repairInfo.setCreateDate(DateUtil.formatDateTime(new Date()));

        OwnerInfo ownerInfo = ownerInfoService.getOne(Wrappers.<OwnerInfo>lambdaQuery().eq(OwnerInfo::getUserId, repairInfo.getUserId()));
        repairInfo.setUserId(ownerInfo.getId());
        return R.ok(repairInfoService.save(repairInfo));
    }

    /**
     * 修改维修信息
     *
     * @param repairInfo
     * @return
     */
    @PutMapping
    public R edit(RepairInfo repairInfo) {
        repairInfo.setRepairStatus(1);
        // 分析此房屋维修情况
        RepairInfo repair = repairInfoService.getById(repairInfo.getId());
        repairInfoService.repairAnalyze(repair.getHousesId());
        return R.ok(repairInfoService.updateById(repairInfo));
    }

    /**
     * 删除维修信息
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(repairInfoService.removeByIds(ids));
    }

}
