package cc.mrbird.febs.cos.dao;

import cc.mrbird.febs.cos.entity.StockInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author FanK
 */
public interface StockInfoMapper extends BaseMapper<StockInfo> {

    // 分页获取库房信息
    IPage<LinkedHashMap<String, Object>> stockInfoByPage(Page page, @Param("stockInfo") StockInfo stockInfo);

    /**
     * 查询所有物品
     *
     * @return 结果
     */
    List<LinkedHashMap<String, Object>> queryStockGoods();

    // 分页获取物品出入库详情
    IPage<LinkedHashMap<String, Object>> stockInfoDetailPage(Page page, @Param("stockInfo") StockInfo stockInfo);

    // 根号用户ID获取领取物品
    IPage<LinkedHashMap<String, Object>> getGoodsPutByUserId(Page page, @Param("stockInfo") StockInfo stockInfo);

    // 入库统计
    List<LinkedHashMap<String, Object>> stockPutRate();

    // 入库类型统计
    List<LinkedHashMap<String, Object>> stockPutTypeRate();

    // 出库统计
    List<LinkedHashMap<String, Object>> stockOutRate();

    // 出库类型统计
    List<LinkedHashMap<String, Object>> stockOutTypeRate();

    // 本月数据统计
    LinkedHashMap<String, Object> stockInfoByMonth();
}
