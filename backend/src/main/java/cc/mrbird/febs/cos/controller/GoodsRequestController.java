package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.GoodsBelong;
import cc.mrbird.febs.cos.entity.GoodsRequest;
import cc.mrbird.febs.cos.entity.OwnerInfo;
import cc.mrbird.febs.cos.entity.RepairInfo;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/goods-request")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GoodsRequestController {

    private final IGoodsRequestService goodsRequestService;

    private final IGoodsBelongService goodsBelongService;

    private final IOwnerInfoService ownerInfoService;

    private final IRepairInfoService repairInfoService;

    /**
     * 分页获取物品申请
     *
     * @param page
     * @param goodsRequest
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, GoodsRequest goodsRequest) {
        return R.ok(goodsRequestService.goodsRequestByPage(page, goodsRequest));
    }

    /**
     * 新增物品申请
     *
     * @param goodsRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping
    public R save(GoodsRequest goodsRequest) {
        goodsRequest.setNum("REQ-" + System.currentTimeMillis());
        goodsRequest.setStep(0);
        goodsRequest.setCreateDate(DateUtil.formatDateTime(new Date()));
        OwnerInfo studentInfo = ownerInfoService.getOne(Wrappers.<OwnerInfo>lambdaQuery().eq(OwnerInfo::getUserId, goodsRequest.getUserId()));
        goodsRequest.setUserId(studentInfo.getId());

        JSONArray array = JSONUtil.parseArray(goodsRequest.getGoods());
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

        if (StrUtil.isNotEmpty(goodsRequest.getRepairOrderCode())) {
            // 更新维修订单申请单号
            repairInfoService.update(Wrappers.<RepairInfo>lambdaUpdate().set(RepairInfo::getRequestNo, goodsRequest.getNum()).set(RepairInfo::getTotalPrice, totalAmount).eq(RepairInfo::getCode, goodsRequest.getRepairOrderCode()));
        }
        return R.ok(goodsRequestService.save(goodsRequest));
    }

    /**
     * 修改物品申请
     *
     * @param goodsRequest
     * @return
     */
    @PutMapping
    public R edit(GoodsRequest goodsRequest) {
        return R.ok(goodsRequestService.updateById(goodsRequest));
    }

    /**
     * 删除物品申请
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(goodsRequestService.removeByIds(ids));
    }
}
