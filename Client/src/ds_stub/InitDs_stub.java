package ds_stub;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dataservice.InitDataService;
import po.AccountPO;
import po.CommodityPO;
import po.CustomerPO;

public class InitDs_stub implements InitDataService {

	@Override
	public String[] getInitInfo() throws RemoteException{
		return new String[]{"20160901", "20170901"};
	}

	@Override
	public String getYear() throws RemoteException{
		return null;
	}

	@Override
	public ArrayList<CommodityPO> getCommodityInfo(String year) throws RemoteException {
		return new CommodityDs_stub().getAllCommodity();
	}

	@Override
	public ArrayList<CustomerPO> getCustomerInfo(String year) throws RemoteException{
		return new CustomerDs_stub().getAllCustomer();
	}

	@Override
	public ArrayList<AccountPO> getAccountInfo(String year) throws RemoteException{
		return new AccountDs_stub().getAllAccount();
	}

	@Override
	public boolean initNewOne() throws RemoteException{
		return true;
	}

}
