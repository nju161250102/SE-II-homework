package dataservice;

import java.rmi.Remote;
import java.rmi.RemoteException;

import po.billpo.ChangeBillPO;

public interface ChangeBillDataService extends Remote{
	public ChangeBillPO getBillById(String id, boolean isOver) throws RemoteException;

	public boolean saveBill(ChangeBillPO bill) throws RemoteException;

	public boolean deleteBill(String id, boolean isOver) throws RemoteException;
	
	public String getNewId(boolean isOver) throws RemoteException;
}
