package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.OwnerInfo;
import cc.mrbird.febs.cos.entity.RepairInfo;
import cc.mrbird.febs.cos.entity.WorkerInfo;
import cc.mrbird.febs.cos.service.IOwnerInfoService;
import cc.mrbird.febs.cos.service.IRepairInfoService;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@RestController
@RequestMapping("/cos/owner-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OwnerInfoController {

    private final IOwnerInfoService ownerInfoService;

    private final IRepairInfoService repairInfoService;

    /**
     * 根据系统账户ID获取业主信息
     * @param userId
     * @return
     */
    @GetMapping("/OwnerInfoByUserId")
    public R OwnerInfoByUserId(Integer userId) {
        return R.ok(ownerInfoService.getOne(Wrappers.<OwnerInfo>lambdaQuery().eq(OwnerInfo::getUserId, userId)));
    }

    /**
     * 根据作业人员获取个人信息和维修任务
     *
     * @param id 作业人员ID
     * @return 结果
     */
    @GetMapping("/detail/{id}")
    public R detail(@PathVariable("id") Integer id) {
        // 返回数据
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>() {
            {
                put("owner", null);
                put("order", Collections.emptyList());
            }
        };
        // 获取员工信息
        OwnerInfo ownerInfo = ownerInfoService.getOne(Wrappers.<OwnerInfo>lambdaQuery().eq(OwnerInfo::getUserId, id));
        if (ownerInfo == null) {
            return R.ok(result);
        }

        result.put("owner", ownerInfo);
        // 获取员工维修任务
        List<RepairInfo> repairInfoList = repairInfoService.list(Wrappers.<RepairInfo>lambdaQuery().eq(RepairInfo::getUserId, ownerInfo.getId()));
        result.put("order", repairInfoList);
        return R.ok(result);
    }

    /**
     *
     * @return
     */
    @GetMapping("/list")
    public R list() {
        return R.ok(ownerInfoService.list());
    }

    /**
     * 分页获取业主信息
     * @param page
     * @param ownerInfo
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, OwnerInfo ownerInfo) {
        return R.ok(ownerInfoService.ownerInfoByPage(page, ownerInfo));
    }

    /**
     * 添加业主信息
     * @param ownerInfo
     * @return
     */
    @PostMapping
    public R save(OwnerInfo ownerInfo) {
        ownerInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        return R.ok(ownerInfoService.save(ownerInfo));
    }

    /**
     * 修改业主信息
     * @param ownerInfo
     * @return
     */
    @PutMapping
    public R edit(OwnerInfo ownerInfo) {
        return R.ok(ownerInfoService.updateById(ownerInfo));
    }

    /**
     * 删除业主信息
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(ownerInfoService.removeByIds(ids));
    }

}
