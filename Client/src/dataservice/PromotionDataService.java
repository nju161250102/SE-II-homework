package dataservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import po.GroupDiscountPO;
import po.PromotionPO;
import po.RankPromotionPO;
import po.SumPromotionPO;

/**
 * 处理有关促销策略的持久化任务
 * 
 * @author 恽叶霄
 */
public interface PromotionDataService extends Remote{
    
    String getNewId() throws RemoteException;
    
    boolean add(GroupDiscountPO promotion) throws RemoteException;
    
    boolean add(RankPromotionPO promotion) throws RemoteException;
    
    boolean add(SumPromotionPO promotion) throws RemoteException;
    
    boolean delete(String id) throws RemoteException;
    
    /**
     * 搜索某个日期段内所有的该等级的促销策略<p>
     * 即是指促销策略的开始日期晚于或等于from，结束日期早于或等于to
     * 下同
     * <p>rank若为 -1 则是指所有等级的策略
     */
    ArrayList<RankPromotionPO> searchRankPromotion(String from, String to, int rank) throws RemoteException;
    
    /**
     * 搜索在某天有效的所有该等级的促销策略<p>
     * 即是指促销策略的开始日期早于或等于date，结束日期晚于或等于date
     * 下同
     * <p>rank若为 -1 则是指所有等级的策略
     */
    ArrayList<RankPromotionPO> searchRankPromotion(String date, int rank) throws RemoteException;
    
    ArrayList<GroupDiscountPO> searchGroupDiscount(String from, String to) throws RemoteException;
    
    ArrayList<GroupDiscountPO> searchGroupDiscount(String date) throws RemoteException;
    
    ArrayList<SumPromotionPO> searchSumPromotion(String from, String to) throws RemoteException;
    
    ArrayList<SumPromotionPO> searchSumPromotion(String date) throws RemoteException;
    
    PromotionPO findById(String id) throws RemoteException;

}
