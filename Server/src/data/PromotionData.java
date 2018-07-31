package data;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import dataservice.PromotionDataService;
import po.GroupDiscountPO;
import po.PromotionPO;
import po.RankPromotionPO;
import po.SumPromotionPO;
import po.billpo.GiftItem;
public class PromotionData extends UnicastRemoteObject implements PromotionDataService {
	private String billName="PromotionInfo", groupProName="PromotionGoodsGroup",
			rankProName="PromotionCustomerRank", sumProName="PromotionSum";
	private String[] billAttributes={"PIID","PIStart","PIEnd","PIType","PIIsExist"},
			groupAttributes={"PGGID","PGGDiscount","PGGComID1","PGGComID2","PGGComID3","PGGComID4","PGGComID5"},
			rankAttributes={"PCRID","PCRCusRank","PCRComID1","PCRComQuantity1","PCRComID2",
					"PCRComQuantity2","PCRComID3","PCRComQuantity3","PCRDiscount","PCRCoupon"},
			sumAttributes={"PSID","PSStartMoney","PSEndMoney","PSComID1","PSComQuantity1",
					"PSComID2","PSComQuantity2","PSComID3","PSComQuantity3","PSCoupon"};
	
	public PromotionData() throws RemoteException {
		super();
	}

	@Override
	public boolean add(GroupDiscountPO promotion) throws RemoteException {
		boolean isExist=SQLQueryHelper.isPromotionExist(billName, billAttributes[0], promotion);
		try{
			if(!isExist)return addGroup(promotion);
			else if(delete(promotion.getId()))return addGroup(promotion);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public boolean add(RankPromotionPO promotion) throws RemoteException {
		boolean isExist=SQLQueryHelper.isPromotionExist(billName, billAttributes[0], promotion);
		try{
			if(!isExist)return addRank(promotion);
			else if(delete(promotion.getId()))return addRank(promotion);	
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public boolean add(SumPromotionPO promotion) throws RemoteException {
		boolean isExist=SQLQueryHelper.isPromotionExist(billName, billAttributes[0], promotion);
		try{
			if(!isExist)return addSum(promotion);
		    else if(delete(promotion.getId()))return addSum(promotion);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return false;
	}

	@Override
	public boolean delete(String id) throws RemoteException {
		return SQLQueryHelper.getFalseDeleteResult(billName, billAttributes[4], billAttributes[0], id);
	}

	@Override
	public ArrayList<RankPromotionPO> searchRankPromotion(String from, String to, int rank) throws RemoteException {
		String sql="";
		if(rank==-1){
	        sql="SELECT * FROM "+billName+","+rankProName+" WHERE "
	        		+billAttributes[0]+"="+rankAttributes[0]+" AND "
	        		+billAttributes[1]+">='"+from
	        		+"' AND "+billAttributes[2]+"<DATEADD(DAY,1,"+"'"+to+"') AND "
	        		+billAttributes[3]+"="+1;
		}
		else{
	        sql="SELECT * FROM "+billName+","+rankProName+" WHERE "
	        		+billAttributes[0]+"="+rankAttributes[0]+" AND "
	        		+billAttributes[1]+">='"+from
	        		+"' AND "+billAttributes[2]+"<DATEADD(DAY,1,"+"'"+to+"') AND "
	        		+billAttributes[3]+"="+1+" AND "
	        		+rankAttributes[1]+"="+rank;
		}		
		return getRankPros(sql);	
	}

	@Override
	public ArrayList<RankPromotionPO> searchRankPromotion(String date, int rank) throws RemoteException {
		String sql="";
		if(rank==-1){
	        sql="SELECT * FROM "+billName+","+rankProName+" WHERE "
	        		+billAttributes[0]+"="+rankAttributes[0]+" AND "
	        		+billAttributes[1]+"<='"+date
	        		+"' AND "+billAttributes[2]+">DATEADD(DAY,1,"+"'"+date+"') AND "
	        		+billAttributes[3]+"="+1;
		}
		else{
	        sql="SELECT * FROM "+billName+","+rankProName+" WHERE "
	        		+billAttributes[0]+"="+rankAttributes[0]+" AND "
	        		+billAttributes[1]+"<='"+date
	        		+"' AND "+billAttributes[2]+">DATEADD(DAY,1,"+"'"+date+"') AND "
	        		+billAttributes[3]+"="+1+" AND "
	        		+rankAttributes[1]+"="+rank;
		}		
		return getRankPros(sql);	
	}

	@Override
	public ArrayList<GroupDiscountPO> searchGroupDiscount(String from, String to) throws RemoteException {
		String sql="";
        sql="SELECT * FROM "+billName+","+groupProName+" WHERE "
        		+billAttributes[0]+"="+groupAttributes[0]+" AND "
        		+billAttributes[1]+">='"+from
        		+"' AND "+billAttributes[2]+"<DATEADD(DAY,1,"+"'"+to+"') AND "
        		+billAttributes[3]+"="+2;
        return getGroupPros(sql);
	}

	@Override
	public ArrayList<GroupDiscountPO> searchGroupDiscount(String date) throws RemoteException {
		String sql="";
	        sql="SELECT * FROM "+billName+","+groupProName+" WHERE "
	        		+billAttributes[0]+"="+groupAttributes[0]+" AND "
	        		+billAttributes[1]+"<='"+date
	        		+"' AND "+billAttributes[2]+">DATEADD(DAY,1,"+"'"+date+"') AND "
	        		+billAttributes[3]+"="+2;
	        return getGroupPros(sql);
	}

	@Override
	public ArrayList<SumPromotionPO> searchSumPromotion(String from, String to) throws RemoteException {
		String sql="";
        sql="SELECT * FROM "+billName+","+sumProName+" WHERE "
        		+billAttributes[0]+"="+sumAttributes[0]+" AND "
        		+billAttributes[1]+">='"+from
        		+"' AND "+billAttributes[2]+"<DATEADD(DAY,1,"+"'"+to+"') AND "
        		+billAttributes[3]+"="+3;
		return getSumPros(sql);
	}

	@Override
	public ArrayList<SumPromotionPO> searchSumPromotion(String date) throws RemoteException {
		String sql="";
        sql="SELECT * FROM "+billName+","+sumProName+" WHERE "
        		+billAttributes[0]+"="+sumAttributes[0]+" AND "
        		+billAttributes[1]+"<='"+date
        		+"' AND "+billAttributes[2]+">DATEADD(DAY,1,"+"'"+date+"') AND "
        		+billAttributes[3]+"="+3;
		return getSumPros(sql);
	}

	@Override
	public PromotionPO findById(String id) throws RemoteException {
		PromotionPO pro=null;
		String sql="SELECT * FROM "+billName+" WHERE "+billAttributes[0]+"='"+id+"';";
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery(sql);
			if (!r.next()) return null;
			if(r.getInt(billAttributes[3])==1){
				String sql2="SELECT * FROM "+rankProName+" WHERE "+rankAttributes[0]+"='"+id+"';";
				Statement s2=DataHelper.getInstance().createStatement();
				ResultSet r2=s2.executeQuery(sql2);
				while(r2.next()){
					pro=new RankPromotionPO(
							r.getString(billAttributes[0]),
							r.getString(billAttributes[1]),
							r.getString(billAttributes[2]),
							r2.getInt(rankAttributes[1]),
							getRankItems(r.getString(billAttributes[0])),
							r2.getDouble(rankAttributes[8]),
							r2.getDouble(rankAttributes[9])
							);		
				}
			}
			else if(r.getInt(billAttributes[3])==2){
				String sql2="SELECT * FROM "+groupProName+" WHERE "+groupAttributes[0]+"='"+id+"';";
				Statement s2=DataHelper.getInstance().createStatement();
				ResultSet r2=s2.executeQuery(sql2);
				while(r2.next()){
					pro=new GroupDiscountPO(
							r.getString(billAttributes[0]),
							r.getString(billAttributes[1]),
							r.getString(billAttributes[2]),
							getGroupCom(r.getString(billAttributes[0])),
							r2.getDouble(groupAttributes[1])
							);		
				}
	
			}
			else{
				String sql2="SELECT * FROM "+sumProName+" WHERE "+sumAttributes[0]+"='"+id+"';";
				Statement s2=DataHelper.getInstance().createStatement();
				ResultSet r2=s2.executeQuery(sql2);
				while(r2.next()){
					pro=new SumPromotionPO(
							r.getString(billAttributes[0]),
							r.getString(billAttributes[1]),
							r.getString(billAttributes[2]),
							r2.getDouble(sumAttributes[1]),
							r2.getDouble(sumAttributes[2]),
							r2.getDouble(sumAttributes[9]),
							getSumItems(r.getString(billAttributes[0]))
	     					);					
				}
				
			}
			return pro;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getNewId() throws RemoteException {
		return SQLQueryHelper.getNewId(billName, billAttributes[0], "%06d");
	}
	
	private boolean addGroup(GroupDiscountPO promotion){
		ArrayList<String> coms=promotion.getGroup();
		String str="INSERT INTO "+groupProName+"("+groupAttributes[0]+","+groupAttributes[1]+",";
		for(int i=0;i<coms.size();i++){
			str=str+groupAttributes[2+i];
			if(i==coms.size()-1)str+=")";
			else str+=",";		
		}
		str+=" VALUES('"+promotion.getId()+"',"+promotion.getReduction()+",";
		for(int i=0;i<coms.size();i++){
			str=str+"'"+coms.get(i)+"'";
			if(i!=coms.size()-1)str+=",";
			else str+=");";
		}
		
		Object[] billValues={promotion.getId(),promotion.getFromDate(),promotion.getToDate(),2,promotion.isExist()};
		boolean b1 = SQLQueryHelper.add(billName, billValues);
		boolean b2=false;
		try{
			Statement s=DataHelper.getInstance().createStatement();
			int r=s.executeUpdate(str);
			if(r>0) b2=true;
			return b1&b2;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}	
	}
	
	private boolean addRank(RankPromotionPO promotion){
		ArrayList<GiftItem> items=promotion.getGifts();
		String str="INSERT INTO "+rankProName+"("+rankAttributes[0]+","+rankAttributes[1]+",";
		for(int i=0;i<items.size();i++){
			str=str+rankAttributes[2+2*i]+","+rankAttributes[3+2*i]+",";
		}
		str=str+rankAttributes[8]+","+rankAttributes[9]+") VALUES('"+promotion.getId()
		+"',"+promotion.getRank()+",";
		for(int i=0;i<items.size();i++){
			str=str+"'"+items.get(i).getComId()+"',"+items.get(i).getNum()+",";
		}
		str=str+promotion.getReduction()+","+promotion.getCoupon()+");";
		
		Object[] billValues={promotion.getId(),promotion.getFromDate(),promotion.getToDate(),1,promotion.isExist()};
		boolean b1 = SQLQueryHelper.add(billName, billValues);
		boolean b2=false;

		try{
			Statement s=DataHelper.getInstance().createStatement();
			int r=s.executeUpdate(str);
			if(r>0)b2=true;
			return b1&b2;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean addSum(SumPromotionPO promotion){
		ArrayList<GiftItem> items=promotion.getGifts();
		String str="INSERT INTO "+sumProName+"("+sumAttributes[0]+","
		+sumAttributes[1]+","+sumAttributes[2]+",";
		for(int i=0;i<items.size();i++){
			str=str+sumAttributes[3+2*i]+","+sumAttributes[4+2*i]+",";
		}
		str=str+sumAttributes[9]+") VALUES('"+promotion.getId()
		+"',"+promotion.getStartPrice()+","+promotion.getEndPrice()+",";
		for(int i=0;i<items.size();i++){
			str=str+"'"+items.get(i).getComId()+"',"+items.get(i).getNum()+",";
		}	
		str=str+promotion.getCoupon()+");";
		
		Object[] billValues={promotion.getId(),promotion.getFromDate(),promotion.getToDate(),3,promotion.isExist()};
		boolean b1 = SQLQueryHelper.add(billName, billValues);
		boolean b2=false;
		try{
			Statement s=DataHelper.getInstance().createStatement();
			System.out.println(str);
			int r=s.executeUpdate(str);
			if(r>0)b2=true;
			return b1&b2;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean updateGroup(GroupDiscountPO promotion){
		ArrayList<String> coms=promotion.getGroup();
		String str="UPDATE "+groupProName+" SET "+groupAttributes[1]+"="+promotion.getReduction()+",";
		for(int i=0;i<coms.size();i++){
			str=str+groupAttributes[2+i]+"='"+coms.get(i)+"'";
			if(i!=coms.size()-1)str+=",";
		}
		str=str+" WHERE "+groupAttributes[0]+"='"+promotion.getId()+"';";
		
		try{
			Statement s=DataHelper.getInstance().createStatement();
			int r=s.executeUpdate(str);
			if(r>0)return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return false;	
	}
	
	private boolean updateRank(RankPromotionPO promotion){
		ArrayList<GiftItem> items=promotion.getGifts();
		String str="UPDATE "+rankProName+" SET "+rankAttributes[1]+"="+promotion.getRank()+",";
		for(int i=0;i<items.size();i++){
			str=str+rankAttributes[2+2*i]+"='"+items.get(i).getComId()+"',"
					+rankAttributes[3+2*i]+"="+items.get(i).getNum()+",";
		}
		str=str+rankAttributes[8]+"="+promotion.getReduction()+","+rankAttributes[9]+"="+promotion.getCoupon()
		+" WHERE "+rankAttributes[0]+"='"+promotion.getId()+"';";
		
		try{
			Statement s=DataHelper.getInstance().createStatement();
			int r=s.executeUpdate(str);
			if(r>0)return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	private boolean updateSum(SumPromotionPO promotion){
		ArrayList<GiftItem> items=promotion.getGifts();
		String str="UPDATE "+sumProName+" SET "+sumAttributes[1]+"="+promotion.getStartPrice()+","
				+sumAttributes[2]+"="+promotion.getEndPrice()+",";
		for(int i=0;i<items.size();i++){
			str=str+sumAttributes[3+2*i]+"='"+items.get(i).getComId()+"',"
					+sumAttributes[4+2*i]+"="+items.get(i).getNum()+",";
		}
		str=str+rankAttributes[9]+"="+promotion.getCoupon()+" WHERE "+sumAttributes[0]
				+"='"+promotion.getId()+"';";
		try{
			Statement s=DataHelper.getInstance().createStatement();
			int r=s.executeUpdate(str);
			if(r>0)return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	private double  getComPrice(String id){
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery("SELECT ComInPrice FROM CommodityInfo WHERE ComID='"+id+"';");
			r.next();
			return r.getDouble("ComInPrice");
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;	
	}
	
	private ArrayList<GiftItem> getRankItems(String id){
		ArrayList<GiftItem> items=new ArrayList<GiftItem>();
		String str="SELECT * FROM "+rankProName+" WHERE "+rankAttributes[0]+"='"+id+"';";
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery(str);
			while(r.next()){
				for(int i=0;i<3;i++){
					if(r.getString(rankAttributes[2+2*i])!=null){
						GiftItem item=new GiftItem(
								r.getString(rankAttributes[2+2*i]),
								r.getInt(rankAttributes[3+2*i]),
								getComPrice(r.getString(rankAttributes[2+2*i]))
								);
						items.add(item);
					}
				}
			}
		}catch(Exception e){
		return null;
		}
		return items;
	}
	
	private ArrayList<GiftItem> getSumItems(String id){
		ArrayList<GiftItem> items=new ArrayList<GiftItem>();
		String str="SELECT * FROM "+sumProName+" WHERE "+sumAttributes[0]+"='"+id+"';";
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery(str);
			while(r.next()){
				for(int i=0;i<3;i++){
					if(r.getString(sumAttributes[3+2*i])!=null){
						GiftItem item=new GiftItem(
								r.getString(sumAttributes[3+2*i]),
								r.getInt(sumAttributes[4+2*i]),
								getComPrice(r.getString(sumAttributes[3+2*i]))
								);
						items.add(item);
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return items;
	}
	
	private ArrayList<String> getGroupCom(String id){
		ArrayList<String> coms=new ArrayList<String>();
		String str="SELECT * FROM "+groupProName+" WHERE "+groupAttributes[0]+"='"+id+"';";
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery(str);
			while(r.next()){
				for(int i=0;i<5;i++){
					if(r.getString(groupAttributes[2+i])!=null)
						coms.add(r.getString(groupAttributes[2+i]));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return coms;
	}
	
	private ArrayList<RankPromotionPO> getRankPros(String sql){
		ArrayList<RankPromotionPO> pros=new ArrayList<RankPromotionPO>();
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery(sql);
			while(r.next()){
				if(r.getBoolean(billAttributes[4])){
					RankPromotionPO pro=new RankPromotionPO(
							r.getString(billAttributes[0]),
							r.getString(billAttributes[1]),
							r.getString(billAttributes[2]),
							r.getInt(rankAttributes[1]),
							getRankItems(r.getString(billAttributes[0])),
							r.getDouble(rankAttributes[8]),
							r.getDouble(rankAttributes[9])
							);
					pros.add(pro);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}		
		return pros;
	}
	
	private ArrayList<GroupDiscountPO> getGroupPros(String sql){
		ArrayList<GroupDiscountPO> pros=new ArrayList<GroupDiscountPO>();
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery(sql);
			while(r.next()){
				if(r.getBoolean(billAttributes[4])){
					GroupDiscountPO pro=new GroupDiscountPO(
							r.getString(billAttributes[0]),
							r.getString(billAttributes[1]),
							r.getString(billAttributes[2]),
							getGroupCom(r.getString(billAttributes[0])),
							r.getDouble(groupAttributes[1])
							);
					pros.add(pro);
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return pros;
	}

	private ArrayList<SumPromotionPO> getSumPros(String sql){
		ArrayList<SumPromotionPO> pros=new ArrayList<SumPromotionPO>();
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery(sql);
			//String id, String fromDate, String toDate, double startPrice, double endPrice, 
	        //double coupon, ArrayList<GiftItem> gifts
			while(r.next()){
				if(r.getBoolean(billAttributes[4])){
					SumPromotionPO pro=new SumPromotionPO(
							r.getString(billAttributes[0]),
							r.getString(billAttributes[1]),
							r.getString(billAttributes[2]),
							r.getDouble(sumAttributes[1]),
							r.getDouble(sumAttributes[2]),
							r.getDouble(sumAttributes[9]),
							getSumItems(r.getString(billAttributes[0]))
	     					);
					pros.add(pro);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return pros;
	}
}
