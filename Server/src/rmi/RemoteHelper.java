package rmi;

import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import data.AccountData;
import data.BillSearchData;
import data.CashCostBillData;
import data.CategoryData;
import data.ChangeBillData;
import data.CommodityData;
import data.CustomerData;
import data.GiftBillData;
import data.InitData;
import data.LogData;
import data.MailData;
import data.PaymentBillData;
import data.PromotionData;
import data.PurchaseBillData;
import data.PurchaseReturnBillData;
import data.ReceiptBillData;
import data.SaleReturnBillData;
import data.SalesBillData;
import data.UserData;

public class RemoteHelper {

	public RemoteHelper() {
		try {
			LocateRegistry.createRegistry(6667);
			bind(new UserData());
			bind(new LogData());
			bind(new CategoryData());
			bind(new CommodityData());
			bind(new CustomerData());
			bind(new AccountData());
			bind(new BillSearchData());
			bind(new CashCostBillData());
			bind(new ChangeBillData());
			bind(new PaymentBillData());
			bind(new PurchaseBillData());
			bind(new PurchaseReturnBillData());
			bind(new ReceiptBillData());
			bind(new SaleReturnBillData());
			bind(new SalesBillData());
			bind(new MailData());
			bind(new PromotionData());
			bind(new InitData());
			bind(new GiftBillData());
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 注册一个远程对象
	 * @param remote 需要绑定的接口
	 */
	private void bind(Remote remote) {
		try {
			System.out.println(remote.getClass().getInterfaces()[0].getName());
			Naming.bind("rmi://localhost:6667/" + remote.getClass().getInterfaces()[0].getName(), remote);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("绑定" + remote.getClass().getName() + "出现问题");
		}
	}
}
