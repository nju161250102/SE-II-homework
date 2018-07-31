package businesslogic;

import java.rmi.RemoteException;
import java.util.ArrayList;

import blservice.MailBLService;
import blservice.billblservice.BillExamineService;
import blservice.billblservice.BillOperationService;
import blservice.billblservice.PurchaseReturnBillBLService;
import businesslogic.inter.AddLogInterface;
import dataservice.CommodityDataService;
import dataservice.CustomerDataService;
import dataservice.PurchaseReturnBillDataService;
import ds_stub.CommodityDs_stub;
import ds_stub.CustomerDs_stub;
import ds_stub.PurchaseReturnBillDs_stub;
import po.CommodityPO;
import po.CustomerPO;
import po.UserPO;
import po.billpo.BillPO;
import po.billpo.PurchaseReturnBillPO;
import po.billpo.SalesItemsPO;
import presentation.tools.Timetools;
import rmi.Rmi;
import vo.billvo.BillVO;
import vo.billvo.PurchaseReturnBillVO;


public class PurchaseReturnBillBL implements PurchaseReturnBillBLService, BillOperationService, BillExamineService {
    
    private PurchaseReturnBillDataService purchaseReturnBillDs = Rmi.flag ? Rmi.getRemote(PurchaseReturnBillDataService.class) : new PurchaseReturnBillDs_stub();
    private AddLogInterface addLog = new LogBL();
	private MailBLService mailBL = new MailBL();
    private CustomerDataService customerDs = Rmi.flag ? Rmi.getRemote(CustomerDataService.class) : new CustomerDs_stub();
    private CommodityDataService commodityDs = Rmi.flag ? Rmi.getRemote(CommodityDataService.class) : new CommodityDs_stub();

    @Override
    public String getNewId() {
        try{
            return "JHTHD-" + Timetools.getDate() + "-" + purchaseReturnBillDs.getNewId();
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteBill(String id) {
        try{
            PurchaseReturnBillPO bill = purchaseReturnBillDs.getBillById(id);
            if(bill.getState() == BillPO.PASS) return false;
            if (purchaseReturnBillDs.deleteBill(id)) {
            	addLog.add("删除进货退货单", "删除的进货退货单单据编号为"+id);
            	return true;
            } else return false;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }
    @Override
	public boolean saveBill(PurchaseReturnBillVO bill) {
		return saveBill(bill, "保存进货退货单", "保存的进货退货单单据编号为"+bill.getAllId());
	}
    private boolean saveBill(PurchaseReturnBillVO bill, String operation, String detail) {
    	try{
            if (purchaseReturnBillDs.saveBill(toPO(bill))) {
            	addLog.add(operation, detail);
            	return true;
            } else return false;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public ArrayList<PurchaseReturnBillPO> getPurchaseReturnBillPOsByDate(String from, String to){
        try{
            return purchaseReturnBillDs.getBillsByDate(from, to);
        }catch(RemoteException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean offsetBill(String id){
        try{
            PurchaseReturnBillPO bill = purchaseReturnBillDs.getBillById(id);
            ArrayList<SalesItemsPO> items = new ArrayList<>();
            bill.getPurchaseReturnBillItems().forEach(i -> items.add(new SalesItemsPO(
                i.getComId(), i.getComRemark(), -i.getComQuantity(), i.getComPrice(), -i.getComSum()
            )));
            PurchaseReturnBillPO offset = new PurchaseReturnBillPO(
                Timetools.getDate(), Timetools.getTime(), purchaseReturnBillDs.getNewId(), bill.getOperator(), BillPO.PASS, 
                bill.getSupplierId(), bill.getRemark(), -bill.getSum(), items);
            if (purchaseReturnBillDs.saveBill(offset)) {
            	addLog.add("红冲进货退货单", "被红冲的进货退货单单据编号为"+bill.getAllId());
            	return true;
            } else return false;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean copyBill(BillVO bill){
        if(bill instanceof PurchaseReturnBillVO){
            PurchaseReturnBillVO old = (PurchaseReturnBillVO) bill;
			try {
				PurchaseReturnBillVO copy = new PurchaseReturnBillVO(
				    Timetools.getDate(), Timetools.getTime(), purchaseReturnBillDs.getNewId(), old.getOperator(),
				    BillVO.PASS, old.getCustomerId(), old.getModel(), old.getRemark(), old.getSum()
				);
				return saveBill(copy, "红冲并复制进货退货单", "红冲并复制后新的进货退货单编号为"+copy.getAllId());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
        }
        return false;
    }

    private PurchaseReturnBillPO toPO(PurchaseReturnBillVO bill){
        ArrayList<SalesItemsPO> items = new ArrayList<>();
        for(int i = 0; i < bill.getModel().getRowCount(); i++){
            String[] row = bill.getModel().getValueAtRow(i);
            int num = Integer.parseInt(row[5]);
            double price = Double.parseDouble(row[4]),
                   sum = Double.parseDouble(row[6]);
            items.add(new SalesItemsPO(
                row[0], row[7], num, price, sum));
        }
        return new PurchaseReturnBillPO(bill.getDate(), bill.getTime()
            , bill.getId(), bill.getOperator(), bill.getState()
            , bill.getCustomerId(), bill.getRemark(), bill.getSum(), items);
    }

	@Override
	public boolean examineBill(String billId) {
        try{
        	PurchaseReturnBillPO billPO = purchaseReturnBillDs.getBillById(billId);
            PurchaseReturnBillVO billVO = BillTools.toPurchaseReturnBillVO(purchaseReturnBillDs.getBillById(billId));
            ArrayList<SalesItemsPO> list = billPO.getPurchaseReturnBillItems();
            ArrayList<CommodityPO> commodityList = new ArrayList<CommodityPO>();
            CustomerPO customerPO = customerDs.findById(billPO.getSupplierId());
            boolean flag = customerPO.setReceivable(customerPO.getReceivable() - billPO.getSum());
            for (SalesItemsPO item : list) {
            	CommodityPO commodityPO = commodityDs.findById(item.getComId());
        		if (!commodityPO.setAmount(commodityPO.getAmount() - item.getComQuantity())) flag = false;
        		commodityList.add(commodityPO);
        	}
            if (flag) {
            	customerDs.update(customerPO);
            	billVO.setState(3);
            	for (CommodityPO c : commodityList) {
            		if(c.getAmount() < c.getAlarmNum()) mailBL.saveMail("0001", UserPO.UserType.STORE_KEEPER, "编号为"+c.getId()+"的商品"+c.getName()+"库存数量不足");
            		commodityDs.update(c);
            	}
                return saveBill(billVO, "审核进货退货单", "通过审核的进货退货单单据编号为"+billId);
            } else {
            	notPassBill(billId);
            	return false;
            }
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
	}

	@Override
	public boolean notPassBill(String billId) {
        try{
        	PurchaseReturnBillVO billVO = BillTools.toPurchaseReturnBillVO(purchaseReturnBillDs.getBillById(billId));
            billVO.setState(4);
            return saveBill(billVO, "审核进货退货单", "单据编号为"+billId+"的进货退货单审核未通过");
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
	}

	@Override
	public BillVO getBillById(String billId) {
		try {
			return BillTools.toPurchaseReturnBillVO(purchaseReturnBillDs.getBillById(billId));
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
}
