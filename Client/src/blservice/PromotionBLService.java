package blservice;

import vo.GroupDiscountVO;
import vo.MyTableModel;
import vo.PromotionVO;
import vo.RankPromotionVO;
import vo.SumPromotionVO;

/**
 * 与促销策略相关的业务逻辑接口定义
 * 
 * @author 恽叶霄
 */
public interface PromotionBLService {
    /**
     * 获得新一条促销策略的编号
     * @return 一个长度为6的字符串
     */
    String getNewId();
    /** 增加一条组合策略*/
    boolean add(GroupDiscountVO promotion);
    /** 增加一条等级策略*/
    boolean add(RankPromotionVO promotion);
    /** 增加一条总额促销策略*/
    boolean add(SumPromotionVO promotion);
    /** 删除一条促销策略*/
    boolean delete(String id);
    /** 根据日期范围和等价搜索等级策略*/
    MyTableModel searchRankPromotion(String from, String to, int rank);
    /** 根据某一天和等级搜索等级策略*/
    MyTableModel searchRankPromotion(String date, int rank);
    /** 根据日期范围搜索商品组合策略*/
    MyTableModel searchGroupDiscount(String from, String to);
    /** 根据某一天搜索商品组合策略*/
    MyTableModel searchGroupDiscount(String date);
    /** 根据日期范围搜索总额策略*/
    MyTableModel searchSumPromotion(String from, String to);
    /** 根据某一天搜索总额策略*/
    MyTableModel searchSumPromotion(String date);
    /** 删除一条组合策略*/
    PromotionVO findById(String id);

}
