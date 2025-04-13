package cc.mrbird.febs.cos.controller;


import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.RepairInfo;
import cc.mrbird.febs.cos.entity.WorkerInfo;
import cc.mrbird.febs.cos.service.IRepairInfoService;
import cc.mrbird.febs.cos.service.IWorkerInfoService;
import cc.mrbird.febs.system.service.UserService;
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
@RequestMapping("/cos/worker-info")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkerInfoController {

    private final IWorkerInfoService workerInfoService;

    private final IRepairInfoService repairInfoService;

    private final UserService userService;

    /**
     * 根据类型获取工作人员
     *
     * @param type
     * @return
     */
    @GetMapping("/list")
    public R listByType(@RequestParam Integer type) {
        return R.ok(workerInfoService.list(Wrappers.<WorkerInfo>lambdaQuery().eq(WorkerInfo::getType, type)));
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
                put("staff", null);
                put("order", Collections.emptyList());
            }
        };
        // 获取员工信息
        WorkerInfo workerInfo = workerInfoService.getOne(Wrappers.<WorkerInfo>lambdaQuery().eq(WorkerInfo::getUserId, id));
        if (workerInfo == null) {
            return R.ok(result);
        }

        result.put("staff", workerInfo);
        // 获取员工维修任务
        List<RepairInfo> repairInfoList = repairInfoService.list(Wrappers.<RepairInfo>lambdaQuery().eq(RepairInfo::getWorker, workerInfo.getId()));
        result.put("order", repairInfoList);
        return R.ok(result);
    }

    /**
     * 获取工作人员
     */
    @GetMapping("/list/all")
    public R listByType() {
        return R.ok(workerInfoService.list());
    }

    /**
     * 分页查询工作人员信息
     *
     * @param page
     * @param workerInfo
     * @return
     */
    @GetMapping("/page")
    public R page(Page page, WorkerInfo workerInfo) {
        return R.ok(workerInfoService.workerInfoByPage(page, workerInfo));
    }

    /**
     * 添加工作人员信息
     *
     * @param workerInfo
     * @return
     */
    @PostMapping
    public R save(WorkerInfo workerInfo) throws Exception {
        workerInfo.setCreateDate(DateUtil.formatDateTime(new Date()));
        userService.registStaff(workerInfo.getName(), "1234qwer", workerInfo);
        return R.ok(true);
    }

    /**
     * 修改工作人员信息
     *
     * @param workerInfo
     * @return
     */
    @PutMapping
    public R edit(WorkerInfo workerInfo) {
        return R.ok(workerInfoService.updateById(workerInfo));
    }

    /**
     * 删除工作人员信息
     *
     * @param ids
     * @return
     */
    @DeleteMapping("/{ids}")
    public R deleteByIds(@PathVariable("ids") List<Integer> ids) {
        return R.ok(workerInfoService.removeByIds(ids));
    }

}
