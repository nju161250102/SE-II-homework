package businesslogic;

import java.rmi.RemoteException;
import java.util.ArrayList;

import blservice.InitBLService;
import blservice.infoservice.GetCategoryInterface;
import dataservice.InitDataService;
import ds_stub.InitDs_stub;
import po.AccountPO;
import po.CommodityPO;
import po.CustomerPO;
import rmi.Rmi;
import vo.MyTableModel;

public class InitBL implements InitBLService {
	
	private InitDataService initDs = Rmi.flag ? Rmi.getRemote(InitDataService.class) : new InitDs_stub();
	private GetCategoryInterface categoryInfo = new CategoryBL();
	
	@Override
	public String getYear() {
		try {
			return initDs.getYear();
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String[] getInitInfo() {
		try {
			return initDs.getInitInfo();
		} catch (RemoteException e) {
			e.printStackTrace();
			return new String[]{};
		}
	}

	@Override
	public MyTableModel getCommodityInfo(String year) {
		String[] header = new String[]{"商品分类", "商品名称", "型号", "进价", "售价"};
		if (year == null) return new CommodityBL().update();
		try {
			ArrayList<CommodityPO> commodityList = initDs.getCommodityInfo(year);
			String[][] data = new String[commodityList.size()][header.length];
			for (int i = 0; i < commodityList.size(); i++) {
				CommodityPO c = commodityList.get(i);
				data[i][0] = categoryInfo.getCategory(c.getCategoryId()).getName();
				data[i][1] = c.getName();
				data[i][2] = c.getType();
				data[i][3] = ""+c.getInPrice();
				data[i][4] = ""+c.getSalePrice();
			}
			return new MyTableModel(data, header);
		} catch (RemoteException e) {
			e.printStackTrace();
			return new MyTableModel(null, header);
		}
	}

	@Override
	public MyTableModel getCustomerInfo(String year) {
		String[] header = new String[]{"客户分类", "姓名", "联系方式", "应收", "应付"};
		if (year == null) return new CustomerBL().update();
		try {
			ArrayList<CustomerPO> customerList = initDs.getCustomerInfo(year);
			String[][] data = new String[customerList.size()][header.length];
			for (int i = 0; i < customerList.size(); i++) {
				CustomerPO c = customerList.get(i);
				data[i][0] = c.getType() == CustomerPO.CustomerType.SUPPLIER ? "供应商" : "销售商";
				data[i][1] = c.getName();
				data[i][2] = c.getTelNumber();
				data[i][3] = ""+c.getReceivable();
				data[i][4] = ""+c.getPayment();
			}
			return new MyTableModel(data, header);
		} catch (RemoteException e) {
			e.printStackTrace();
			return new MyTableModel(null, header);
		}
	}

	@Override
	public MyTableModel getAccountInfo(String year) {
		String[] header = new String[]{"账户名称", "余额"};
		if (year == null) return new AccountBL().update();
		try {
			ArrayList<AccountPO> accountList = initDs.getAccountInfo(year);
			String[][] data = new String[accountList.size()][header.length];
			for (int i = 0; i < accountList.size(); i++) {
				AccountPO a = accountList.get(i);
				data[i][0] = a.getName();
				data[i][1] = a.getMoney()+"";
			}
			return new MyTableModel(data, header);
		} catch (RemoteException e) {
			e.printStackTrace();
			return new MyTableModel(null, header);
		}
	}

	@Override
	public boolean initNewOne() {
		try {
			return initDs.initNewOne();
		} catch (RemoteException e) {
			e.printStackTrace();
			return false;
		}
	}

}
