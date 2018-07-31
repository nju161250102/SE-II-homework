package data;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import dataservice.InitDataService;
import po.AccountPO;
import po.CategoryPO;
import po.CommodityPO;
import po.CustomerPO;

public class InitData extends UnicastRemoteObject implements InitDataService{

	public InitData() throws RemoteException {
		super();
	}

	@Override
	public String[] getInitInfo() throws RemoteException {
		ArrayList<String> list = new ArrayList<String>();
		try{		
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery("SELECT DISTINCT InitTime FROM InitCommodity;");
			while(r.next()) list.add(r.getString("InitTime"));
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return (String[]) list.toArray(new String[list.size()]);
	}

	@Override
	public String getYear() throws RemoteException {
	
		String date = null;
		
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery("SELECT MAX(InitTime) AS time FROM InitCommodity;");
			while(r.next())
			{
				date=r.getString("time");
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return date;
	}

	@Override
	public ArrayList<CommodityPO> getCommodityInfo(String year) throws RemoteException {

		ArrayList<CommodityPO> coms=new ArrayList<CommodityPO>();
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery("SELECT * FROM InitCommodity WHERE InitTime='"+year+"';");
			while(r.next()){
				CommodityPO com=new CommodityPO(
						r.getString("ComID"),
						r.getString("ComName"),
						r.getString("ComType"),
						r.getString("ComStore"),
						r.getString("ComCateID"),
						r.getInt("ComQuantity"),
						r.getInt("ComAlarmQuantity"),
						r.getDouble("ComInPrice"),
						r.getDouble("ComSalePrice"),
						r.getDouble("ComRecInPrice"),
						r.getDouble("ComRecSalePrice"),
						r.getBoolean("ComIsExist")
						);
				coms.add(com);
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return coms;
	}

	@Override
	public ArrayList<CustomerPO> getCustomerInfo(String year) throws RemoteException {
		ArrayList<CustomerPO> cuss=new ArrayList<CustomerPO>();
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery("SELECT * FROM InitCustomer WHERE InitTime='"+year+"';");
			while(r.next()){
				CustomerPO cus=new CustomerPO(
						r.getString("CusID"),
						r.getString("CusName"),
						r.getString("CusTel"),
						r.getString("CusAddress"),
						r.getString("CusMail"),
						r.getString("CusCode"),
						r.getString("CusSalesman"),
						r.getInt("CusRank"),
						r.getInt("CusType"),
						r.getDouble("CusReceiRange"),
						r.getDouble("CusReceivable"),
						r.getDouble("CusPayment"),
						r.getBoolean("CusIsExist")
						);
				cuss.add(cus);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return cuss;
	}

	@Override
	public ArrayList<AccountPO> getAccountInfo(String year) throws RemoteException {
	
		ArrayList<AccountPO> accs=new ArrayList<AccountPO>();
		try{
			Statement s=DataHelper.getInstance().createStatement();
			ResultSet r=s.executeQuery("SELECT * FROM InitAccount WHERE InitTime='"+year+"';");
			while(r.next()){
				//String id, String name, double money, boolean isExist
				AccountPO acc=new AccountPO(
						r.getString("AccountID"),
						r.getString("AccountName"),
						r.getDouble("AccountMoney"),
						r.getBoolean("AccountIsExist")
						);
				accs.add(acc);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return accs;
	}

	@Override
	public boolean initNewOne() throws RemoteException {
		Calendar now = Calendar.getInstance(); 
		String today=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH);
		boolean res=true;
		
		try{
		CategoryData cate=new CategoryData();
		ArrayList<CategoryPO> cpos=cate.getAllCategory();
		for(int i=0;i<cpos.size();i++){			
			boolean flag=SQLQueryHelper.add("InitCategory",today, cpos.get(i).getId(), cpos.get(i).getName(),
					cpos.get(i).getFatherId(),cpos.get(i).getExistFlag());
			if(!flag)res=false;
		}
		
		CommodityData com=new CommodityData();
		ArrayList<CommodityPO> compos=com.getAllCommodity();
		for(int i=0;i<compos.size();i++)
		{
			boolean flag=SQLQueryHelper.add("InitCommodity",today, compos.get(i).getId(), compos.get(i).getName(),
					compos.get(i).getCategoryId(), compos.get(i).getType(), compos.get(i).getStore(),compos.get(i).getAmount(),
					compos.get(i).getInPrice(), compos.get(i).getSalePrice(),compos.get(i).getRecentInPrice(),
					compos.get(i).getRecentSalePrice(), compos.get(i).getAlarmNum(), compos.get(i).getExistFlag());
			if(!flag)res=false;
		}
		
		CustomerData cus=new CustomerData();
		ArrayList<CustomerPO> cuspos=cus.getAllCustomer();
		for(int i=0;i<cuspos.size();i++){
			boolean flag=SQLQueryHelper.add("InitCustomer",today,cuspos.get(i).getId(),cuspos.get(i).getName(),
					cuspos.get(i).getRank(),cuspos.get(i).getTelNumber(),cuspos.get(i).getAddress(),cuspos.get(i).getCode(),
					cuspos.get(i).getMail(),cuspos.get(i).getRecRange(),cuspos.get(i).getReceivable(),cuspos.get(i).getPayment(),
					cuspos.get(i).getSalesman(),cuspos.get(i).getType(),cuspos.get(i).getExistFlag());
			if(!flag)res=false;
		}
		
		AccountData acc=new AccountData();
		ArrayList<AccountPO> apos=acc.getAllAccount();
		for(int i=0;i<apos.size();i++){
			boolean flag=SQLQueryHelper.add("InitAccount", today, apos.get(i).getId(), apos.get(i).getName(),
					apos.get(i).getMoney(), apos.get(i).getExistFlag());
			if(!flag)res=false;
		}
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}

		return res;
	}

}
