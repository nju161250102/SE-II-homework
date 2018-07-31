package businesslogic;

import java.rmi.RemoteException;
import java.util.ArrayList;

import blservice.PromotionBLService;
import blservice.infoservice.GetCommodityInterface;
import blservice.infoservice.GetPromotionInterface;
import businesslogic.inter.AddLogInterface;
import businesslogic.inter.IPromotionSearch;
import dataservice.PromotionDataService;
import ds_stub.PromotionDs_stub;
import po.GroupDiscountPO;
import po.PromotionPO;
import po.RankPromotionPO;
import po.SumPromotionPO;
import rmi.Rmi;
import vo.CommodityVO;
import vo.GroupDiscountVO;
import vo.MyTableModel;
import vo.PromotionVO;
import vo.RankPromotionVO;
import vo.SumPromotionVO;


public class PromotionBL implements PromotionBLService, IPromotionSearch, GetPromotionInterface{
    
    private PromotionDataService promotionDs;
    private AddLogInterface logger;

    public PromotionBL() {
        promotionDs = Rmi.flag ? Rmi.getRemote(PromotionDataService.class) : new PromotionDs_stub();
        logger = new LogBL();
    }
    
    @Override
    public String getNewId(){
        try{
            return promotionDs.getNewId();
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean add(GroupDiscountVO promotion) {
        try{
            boolean success = promotionDs.add(promotion.toPO());
            if(success){
                logger.add("添加一个组合促销策略", "编号：" + promotion.getId());
            }
            return success;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean add(RankPromotionVO promotion) {
        try{
            boolean success = promotionDs.add(promotion.toPO());
            if(success){
                logger.add("添加一个等级促销策略", "编号：" + promotion.getId());
            }
            return success;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean add(SumPromotionVO promotion) {
        try{
            boolean success = promotionDs.add(promotion.toPO());
            if(success){
                logger.add("添加一个总额促销策略", "编号：" + promotion.getId());
            }
            return success;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try{
            boolean success = promotionDs.delete(id);
            if(success){
                logger.add("删除一个促销策略", "编号：" + id);
            }
            return success;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PromotionVO findById(String id) {
        try{
            PromotionPO po = promotionDs.findById(id);
            return toVO(po);
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel searchRankPromotion(String from, String to, int rank) {
        try{
            ArrayList<RankPromotionPO> promotions = promotionDs.searchRankPromotion(from, to, rank);
            return toModel(promotions);
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel searchRankPromotion(String date, int rank) {
        try{
            return toModel(promotionDs.searchRankPromotion(date, rank));
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel searchGroupDiscount(String from, String to) {
        try{
            return toModel(promotionDs.searchGroupDiscount(from, to));
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel searchGroupDiscount(String date) {
        try{
            return toModel(promotionDs.searchGroupDiscount(date));
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel searchSumPromotion(String from, String to) {
        try{
            return toModel(promotionDs.searchSumPromotion(from, to));
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel searchSumPromotion(String date) {
        try{
            return toModel(promotionDs.searchSumPromotion(date));
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ArrayList<GroupDiscountPO> searchGroupDiscountPO(String date) {
        try{
            return promotionDs.searchGroupDiscount(date);
        }catch(RemoteException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public ArrayList<RankPromotionPO> searchRankPromotionPO(String date, int rank) {
        try{
            return promotionDs.searchRankPromotion(date, rank);
        }catch(RemoteException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public ArrayList<SumPromotionPO> searchSumPromotionPO(String date) {
        try{
            return promotionDs.searchSumPromotion(date);
        }catch(RemoteException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public PromotionVO toVO(PromotionPO po){
    	if (po == null) return null;
        if(po instanceof GroupDiscountPO){
            GroupDiscountPO gpo = (GroupDiscountPO)po;
            return new GroupDiscountVO(gpo.getId(), gpo.getFromDate(), 
                gpo.getToDate(), gpo.getReduction(), toGroupModel(gpo.getGroup()));
        }else if(po instanceof RankPromotionPO){
            RankPromotionPO rpo = (RankPromotionPO)po;
            return new RankPromotionVO(rpo.getId(), rpo.getFromDate(), rpo.getToDate(), 
                rpo.getReduction(), rpo.getCoupon(), rpo.getRank(), GiftItemTools.toModel(rpo.getGifts()));
        }else{
            SumPromotionPO spo = (SumPromotionPO)po;
            return new SumPromotionVO(spo.getId(), spo.getFromDate(), spo.getToDate(), 
                spo.getStartPrice(), spo.getEndPrice(), spo.getCoupon(), GiftItemTools.toModel(spo.getGifts()));
        }
    }
    
    private MyTableModel toGroupModel(ArrayList<String> comIds){
        GetCommodityInterface comInfo = new CommodityBL();
        String[] columnNames = {"商品编号", "名称", "型号"};
        int size = comIds.size();
        String[][] data = new String[size][];
        for(int i = 0; i < size; i++){
            String id = comIds.get(i);
            CommodityVO com = comInfo.getCommodity(id);
            String name = com.getName();
            String type = com.getType();
            data[i] = new String[]{id, name, type};
        }
        return new MyTableModel(data, columnNames);
    }
   
    private MyTableModel toModel(ArrayList<? extends PromotionPO> promotions){
        String[] columnNames = {"编号", "生效日期", "失效日期"};
        int size = promotions.size();
        String[][] data = new String[size][];
        for(int i = 0; i < size; i++){
            PromotionPO po = promotions.get(i);
            data[i] = new String[]{po.getId(), po.getFromDate(), po.getToDate()};
        }
        return new MyTableModel(data, columnNames);
    }

}
