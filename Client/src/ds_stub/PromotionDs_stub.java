package ds_stub;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dataservice.PromotionDataService;
import po.GroupDiscountPO;
import po.PromotionPO;
import po.RankPromotionPO;
import po.SumPromotionPO;
import po.billpo.GiftItem;


public class PromotionDs_stub implements PromotionDataService {
    
    public static final ArrayList<PromotionPO> PROMOTIONS = new ArrayList<>();
    
    static{
        ArrayList<String> group0 = new ArrayList<>();
        group0.add("000001");
        group0.add("000002");
        PROMOTIONS.add(new GroupDiscountPO("0001", "2018-01-11", "2018-02-21", group0, 100.0));

        ArrayList<String> group1 = new ArrayList<>();
        group1.add("000002");
        group1.add("000003");
        PROMOTIONS.add(new GroupDiscountPO("0002", "2017-11-20", "2017-12-20", group1, 50.0));

        ArrayList<GiftItem> gifts0 = new ArrayList<>();
        gifts0.add(new GiftItem("000001", 10, 100.0));
        PROMOTIONS.add(new RankPromotionPO("0003", "2018-02-04", "2018-04-04", 1, gifts0, 0, 0));

        ArrayList<GiftItem> gifts1 = new ArrayList<>();
        gifts1.addAll(gifts0);
        gifts1.add(new GiftItem("000002", 10, 120.0));
        PROMOTIONS.add(new RankPromotionPO("0004", "2017-12-20", "2018-02-20", 2, gifts1, 0, 100));
        
        ArrayList<GiftItem> gifts2 = new ArrayList<>();
        gifts2.add(gifts1.get(1));
        gifts2.add(new GiftItem("000003", 15, 80.0));
        PROMOTIONS.add(new SumPromotionPO("0005", "2018-01-01", "2018-03-01", 5000.0, 7000.0, 200.0, gifts2));
        
        ArrayList<GiftItem> gifts3 = new ArrayList<>();
        gifts3.add(gifts2.get(1));
        PROMOTIONS.add(new SumPromotionPO("0006", "2018-01-01", "2018-03-01", 3000.0, 5000.0, 0.0, gifts3));
    }

    public PromotionDs_stub() {}
    
    @Override
    public String getNewId(){
        return "01234";
    }

    @Override
    public boolean add(GroupDiscountPO promotion) throws RemoteException {
        System.out.println("Group Discount added in database: " + promotion.getId());
        return PROMOTIONS.add(promotion);
    }

    @Override
    public boolean add(RankPromotionPO promotion) throws RemoteException {
        System.out.println("Rank Promotion added in database: " + promotion.getId());
        return PROMOTIONS.add(promotion);
    }

    @Override
    public boolean add(SumPromotionPO promotion) throws RemoteException {
        System.out.println("Sum Promotion added in database: " + promotion.getId());
        return PROMOTIONS.add(promotion);
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        int index = -1;
        for(int i = 0; i < PROMOTIONS.size(); i++)if(PROMOTIONS.get(i).getId().equals(id)) index = i;
        if(index >= 0) PROMOTIONS.remove(index);
        System.out.println("Promotion deleted in database: " + id);
        return true;
    }

    @Override
    public PromotionPO findById(String id) throws RemoteException {
        return PROMOTIONS.get(0);
    }

    @Override
    public ArrayList<RankPromotionPO> searchRankPromotion(String from, String to, int rank) throws RemoteException {
        ArrayList<RankPromotionPO> result = new ArrayList<>();
        result.add((RankPromotionPO)PROMOTIONS.get(2));
        result.add((RankPromotionPO)PROMOTIONS.get(3));
        return result;
    }

    @Override
    public ArrayList<RankPromotionPO> searchRankPromotion(String date, int rank) throws RemoteException {
        ArrayList<RankPromotionPO> result = new ArrayList<>();
        result.add((RankPromotionPO)PROMOTIONS.get(2));
        return result;
    }

    @Override
    public ArrayList<GroupDiscountPO> searchGroupDiscount(String from, String to) throws RemoteException {
        ArrayList<GroupDiscountPO> result = new ArrayList<>();
        result.add((GroupDiscountPO)PROMOTIONS.get(0));
        result.add((GroupDiscountPO)PROMOTIONS.get(1));
        return result;
    }

    @Override
    public ArrayList<GroupDiscountPO> searchGroupDiscount(String date) throws RemoteException {
        ArrayList<GroupDiscountPO> result = new ArrayList<>();
        result.add((GroupDiscountPO)PROMOTIONS.get(0));
        return result;
    }

    @Override
    public ArrayList<SumPromotionPO> searchSumPromotion(String from, String to) throws RemoteException {
        ArrayList<SumPromotionPO> result = new ArrayList<>();
        result.add((SumPromotionPO)PROMOTIONS.get(4));
        result.add((SumPromotionPO)PROMOTIONS.get(5));
        return result;
    }

    @Override
    public ArrayList<SumPromotionPO> searchSumPromotion(String date) throws RemoteException {
        ArrayList<SumPromotionPO> result = new ArrayList<>();
        result.add((SumPromotionPO)PROMOTIONS.get(4));
        return result;
    }

}
