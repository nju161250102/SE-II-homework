package dataservice;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import po.billpo.SalesBillPO;

public interface SalesBillDataService extends Remote{

	/**
	 * 每一种单据都对应一个BillData
	 * 这里没有区分新增和更新！！！，或者区分一下？
	 * @param bill
	 * @return
	 */
	public boolean saveBill(SalesBillPO bill) throws RemoteException;
	/**
	 * 根据编号删除一张单据
	 * @param billid
	 * @return
	 */
	public boolean deleteBill(String billid) throws RemoteException;
	/**
	 * 获得销售单据的新编号
	 * @return
	 */
	public String getNewId() throws RemoteException;
	
	public SalesBillPO getBillById(String id) throws RemoteException;
	
	public ArrayList<SalesBillPO> getBillsBy(String field, String key, boolean isFuzzy) throws RemoteException;
	
	public ArrayList<SalesBillPO> getBillByDate(String from, String to) throws RemoteException;
	
}
