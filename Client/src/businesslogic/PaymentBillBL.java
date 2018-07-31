package businesslogic;

import java.rmi.RemoteException;
import java.util.ArrayList;

import blservice.MailBLService;
import blservice.billblservice.BillExamineService;
import blservice.billblservice.BillOperationService;
import blservice.billblservice.PaymentBillBLService;
import businesslogic.inter.AddLogInterface;
import dataservice.AccountDataService;
import dataservice.CustomerDataService;
import dataservice.PaymentBillDataService;
import ds_stub.AccountDs_stub;
import ds_stub.CustomerDs_stub;
import ds_stub.PaymentBillDs_stub;
import po.AccountPO;
import po.CustomerPO;
import po.billpo.BillPO;
import po.billpo.PaymentBillPO;
import po.billpo.TransferItem;
import presentation.tools.Timetools;
import rmi.Rmi;
import vo.billvo.BillVO;
import vo.billvo.PaymentBillVO;

public class PaymentBillBL implements PaymentBillBLService, BillOperationService, BillExamineService{

	private PaymentBillDataService paymentBillDataService = Rmi.flag ? Rmi.getRemote(PaymentBillDataService.class) : new PaymentBillDs_stub();
	private AddLogInterface addLog = new LogBL();
	private MailBLService mailBL = new MailBL();
	private AccountDataService accountDataService = Rmi.flag ? Rmi.getRemote(AccountDataService.class) : new AccountDs_stub();
    private CustomerDataService customerDataService = Rmi.flag ? Rmi.getRemote(CustomerDataService.class) : new CustomerDs_stub();

    @Override
	public String getNewId() {
        try {
            return "FKD-" + Timetools.getDate() + "-" + paymentBillDataService.getNewId();
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
	}

	@Override
	public boolean deleteBill(String id) {
		try{
            PaymentBillPO bill = paymentBillDataService.getBillById(id);
            if(bill.getState() == BillPO.PASS) return false;
            if (paymentBillDataService.deleteBill(id)) {
            	addLog.add("删除付款单", "删除的付款单单据编号为"+id);
            	return true;
            } else return false;
		}catch(RemoteException e){
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public boolean saveBill(PaymentBillVO bill) {
		return saveBill(bill, "保存付款单", "保存的付款单单据编号为"+bill.getAllId());
	}
	private boolean saveBill(PaymentBillVO bill, String operation, String detail) {
		try{
            if (paymentBillDataService.saveBill(toPO(bill))) {
            	addLog.add(operation, detail);
            	return true;
            } else return false;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
	}
	
	@Override
	public boolean offsetBill(String id){
	    try{
	        PaymentBillPO bill = paymentBillDataService.getBillById(id);
	        ArrayList<TransferItem> items = new ArrayList<>();
	        bill.getTransferList().forEach(i -> items.add(
	            new TransferItem(i.getAccountId(), -i.getMoney(), i.getRemark())));
	        PaymentBillPO offset = new PaymentBillPO(
	            Timetools.getDate(), Timetools.getTime(), paymentBillDataService.getNewId(), bill.getOperator()
	            , BillPO.PASS, bill.getCustomerId(), items, -bill.getSum());
	        if (paymentBillDataService.saveBill(offset)) {
            	addLog.add("红冲付款单", "被红冲的付款单单据编号为"+bill.getAllId());
            	return true;
            } else return false;
	    }catch(RemoteException e){
	        e.printStackTrace();
	        return false;
	    }
	}
	
	@Override
	public boolean copyBill(BillVO bill){
	    if(bill instanceof PaymentBillVO){
	        PaymentBillVO old = (PaymentBillVO) bill;
	        PaymentBillVO copy = new PaymentBillVO(
	            Timetools.getDate(), Timetools.getTime(), this.getNewId().split("-")[2], 
	            old.getOperator(), BillVO.PASS, old.getCustomerId(),old.getTableModel());
	        return saveBill(copy, "红冲并复制付款单", "红冲并复制后新的付款单单据编号为"+copy.getAllId());
	    }
	    return false;
	}

	private PaymentBillPO toPO(PaymentBillVO bill){
        ArrayList<TransferItem> items = new ArrayList<>();
        double sum = 0;
        for(int i = 0; i < bill.getTableModel().getRowCount(); i++){
            String[] row = bill.getTableModel().getValueAtRow(i);
            double price = Double.parseDouble(row[1]);
            items.add(new TransferItem(row[0], price, row[2]));
            sum += price;
        }

        return new PaymentBillPO(bill.getDate(), bill.getTime()
            , bill.getId(), bill.getOperator(), bill.getState()
            , bill.getCustomerId(), items, sum);
    }

	@Override
	public boolean examineBill(String billId) {
        try{
            PaymentBillPO billPO = paymentBillDataService.getBillById(billId);
            PaymentBillVO billVO = BillTools.toPaymentBillVO(billPO);
            ArrayList<TransferItem> list = billPO.getTransferList();
            ArrayList<AccountPO> accountList = new ArrayList<AccountPO>();
            CustomerPO customerPO = customerDataService.findById(billPO.getCustomerId());
            boolean flag = true;
            for (int i = 0; i < list.size(); i++) {
            	TransferItem item = list.get(i);
            	AccountPO accountPO = accountDataService.findById(item.getAccountId());
            	if (!accountPO.subMoney(item.getMoney())) flag = false;
            	accountList.add(accountPO);
            }
            if (!customerPO.setReceivable(customerPO.getReceivable() - billPO.getSum())) flag = false;
            if (flag) {
            	for (AccountPO a : accountList) accountDataService.update(a);
            	customerDataService.update(customerPO);
            	billVO.setState(3);
            	mailBL.saveMail("0001", billPO.getOperator(), "单据编号为"+billId+"的付款单通过审核，请尽快完成银行操作");
                return saveBill(billVO, "审核付款单", "通过审核的付款单单据编号为"+billId);
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
            PaymentBillPO billPO = paymentBillDataService.getBillById(billId);
            PaymentBillVO billVO = BillTools.toPaymentBillVO(billPO);
            billVO.setState(4);
            return saveBill(billVO, "审核付款单", "单据编号为"+billId+"的付款单审核未通过");
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
	}

	@Override
	public BillVO getBillById(String id) {
		try {
			return BillTools.toPaymentBillVO(paymentBillDataService.getBillById(id));
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
}
