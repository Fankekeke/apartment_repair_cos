package cc.mrbird.febs.cos.service.impl;

import cc.mrbird.febs.common.utils.R;
import cc.mrbird.febs.cos.entity.HousesInfo;
import cc.mrbird.febs.cos.dao.HousesInfoMapper;
import cc.mrbird.febs.cos.entity.OwnerInfo;
import cc.mrbird.febs.cos.service.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HousesInfoServiceImpl extends ServiceImpl<HousesInfoMapper, HousesInfo> implements IHousesInfoService {

    private final IOwnerInfoService ownerInfoService;

    private final HousesInfoMapper housesInfoMapper;

    private final IBulletinInfoService bulletinInfoService;

    @Override
    public IPage<LinkedHashMap<String, Object>> housesInfoByPage(Page page, HousesInfo housesInfo) {
        return baseMapper.housesInfoByPage(page, housesInfo);
    }

    @Override
    public List<LinkedHashMap<String, Object>> housesInfoByOwnerId(Integer ownerId) {
        return baseMapper.housesInfoByOwnerId(ownerId);
    }

    @Override
    public List<LinkedHashMap<String, Object>> housesConsumption(Integer housesId) {
        return baseMapper.housesConsumption(housesId);
    }

    @Override
    public List<LinkedHashMap<String, Object>> housesConsumptionMonth(Integer housesId) {
        return baseMapper.housesConsumptionMonth(housesId);
    }

    @Override
    public List<LinkedHashMap<String, Object>> housesConsumptionCheck(Integer housesId, String date) {
        return baseMapper.housesConsumptionCheck(housesId, date);
    }

    @Override
    public LinkedHashMap<String, Object> home(Integer type) {
        return null;
    }

    @Override
    public List<LinkedHashMap<String, Object>> housesListByOwnerId(Integer userId) {
        return baseMapper.housesListByOwnerId(userId);
    }
}
