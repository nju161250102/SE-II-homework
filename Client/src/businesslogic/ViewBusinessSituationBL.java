package businesslogic;

import java.rmi.RemoteException;
import java.util.ArrayList;

import blservice.ViewBusinessSituationBLService;
import blservice.infoservice.GetCommodityInterface;
import dataservice.BillSearchDataService;
import ds_stub.BillSearchDs_stub;
import po.billpo.ChangeBillPO;
import po.billpo.ChangeItem;
import po.billpo.PurchaseBillPO;
import po.billpo.PurchaseReturnBillPO;
import po.billpo.SalesBillPO;
import po.billpo.SalesItemsPO;
import rmi.Rmi;
import vo.CommodityVO;
import vo.MyTableModel;

public class ViewBusinessSituationBL implements ViewBusinessSituationBLService{

    private BillSearchDataService billSearchDs;
    private GetCommodityInterface commodityBl;

    public ViewBusinessSituationBL() {
        billSearchDs = Rmi.flag ? Rmi.getRemote(BillSearchDataService.class) : new BillSearchDs_stub();
        commodityBl = new CommodityBL();
    }
	
	@Override
	public double getSalesIncome(String from, String to) {
		double sum = 0;
		try{
            ArrayList<SalesBillPO> bills = billSearchDs.searchSalesBills(from, to, null, null, 3);
            for (int i = 0; i < bills.size(); i++) {
            	sum += (bills.get(i).getBeforeDiscount() - bills.get(i).getDiscount());
            }
            return sum;
        }catch(RemoteException e){
            e.printStackTrace();
            return sum;
        }
	}

	@Override
	public double getCommodityOverflowIncome(String from, String to) {
		double sum = 0;
		try{
            ArrayList<ChangeBillPO> bills = billSearchDs.searchChangeBills(from, to, null, null, true, 3);
            for (int i = 0; i < bills.size(); i++) {
            	ChangeBillPO changeBill = bills.get(i);
        		ArrayList<ChangeItem> list = changeBill.getCommodityList();
        		for (int j = 0; j < list.size(); j++) {
        			ChangeItem item = list.get(j);
            		CommodityVO commodity = commodityBl.getCommodity(item.getCommodityId());
            		sum += commodity.getRecentSalePrice() * (item.getChangedValue() - item.getOriginalValue());
        		}
            }
            return sum;
        }catch(RemoteException e){
            e.printStackTrace();
            return sum;
        }
	}

	@Override
	public double getPurchaseAndReturnIncome(String from, String to) {
		double sum = 0;
		try{
            ArrayList<PurchaseReturnBillPO> bills = billSearchDs.searchPurchaseReturnBills(from, to, null, null, 3);
            for (int i = 0; i < bills.size(); i++) {
            	PurchaseReturnBillPO purchaseReturnBill = bills.get(i);
        		ArrayList<SalesItemsPO> list = purchaseReturnBill.getPurchaseReturnBillItems();
        		for (int j = 0; j < list.size(); j++) {
        			SalesItemsPO item = list.get(j);
            		CommodityVO commodity = commodityBl.getCommodity(item.getComId());
            		sum += (commodity.getRecentInPrice() - commodity.getInPrice()) * item.getComQuantity();
        		}	
            }
            return sum;
        }catch(RemoteException e){
            e.printStackTrace();
            return sum;
        }
	}

//	1. 收入类：销售收入、商品类收入（商品报溢收入 成本调价收入 进货退货差价 代金券与实际收款差额收入）。收入类显示折让后总收入，并显示折让了多少。
//	2. 支出类：销售成本、商品类支出（商品报损 商品赠出）。支出类显示总支出。3. 利润：折让后总收入-总支出。）
	@Override
	public double getCashCouPonIncome(String from, String to) {
		double sum = 0;
		try{
            ArrayList<SalesBillPO> bills = billSearchDs.searchSalesBills(from, to, null, null, 3);
            for (int i = 0; i < bills.size(); i++) {
            	sum += bills.get(i).getCoupon();
            }
            return sum;
        }catch(RemoteException e){
            e.printStackTrace();
            return sum;
        }
	}

	@Override
	public double getSalesExpense(String from, String to) {
		double sum = 0;
		try{
            ArrayList<PurchaseBillPO> bills = billSearchDs.searchPurchaseBills(from, to, null, null, 3);
            for (int i = 0; i < bills.size(); i++) {
            	sum += bills.get(i).getSum();
            }
            return sum;
        }catch(RemoteException e){
            e.printStackTrace();
            return sum;
        }
	}

	@Override
	public double getCommodityBrokenExpense(String from, String to) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getCommoditySentExpense(String from, String to) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public MyTableModel fillIncomeTable(String from, String to) {
        String[] columnNames = {"总收入", "销售收入", "商品报溢收入", "成本调价收入", "进货退货差价收入", "代金券"};
        String[][] data = new String[1][6];
        data[0][0] = Double.toString(getSalesIncome(from, to) + getCommodityOverflowIncome(from, to) 
           + getPurchaseAndReturnIncome(from, to) - getCashCouPonIncome(from, to));
        data[0][1] = Double.toString(getSalesIncome(from, to));
        data[0][2] = Double.toString(getCommodityOverflowIncome(from, to));
        data[0][3] = Double.toString(0);
        data[0][4] = Double.toString(getPurchaseAndReturnIncome(from, to));
        data[0][5] = Double.toString(getCashCouPonIncome(from, to));
        return new MyTableModel(data, columnNames);
	}

	@Override
	public MyTableModel fillExpenseTable(String from, String to) {
        String[] columnNames = {"总支出", "销售成本", "商品报损支出", "商品赠出支出"};
        String[][] data = new String[1][4];
        data[0][0] = Double.toString(getSalesExpense(from, to));
        data[0][1] = Double.toString(getSalesExpense(from, to));
        data[0][2] = Double.toString(getCommodityBrokenExpense(from, to));
        data[0][3] = Double.toString(getCommoditySentExpense(from, to));
        return new MyTableModel(data, columnNames);
	}

	@Override
	public double getProfit(String from, String to) {
		double profit = getSalesIncome(from, to) + getCommodityOverflowIncome(from, to) 
        + getPurchaseAndReturnIncome(from, to) - getCashCouPonIncome(from, to) - getSalesExpense(from, to);
		return profit;
	}
}
