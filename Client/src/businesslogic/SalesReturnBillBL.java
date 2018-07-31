package businesslogic;

import java.rmi.RemoteException;
import java.util.ArrayList;

import blservice.billblservice.BillExamineService;
import blservice.billblservice.BillOperationService;
import blservice.billblservice.SalesReturnBillBLService;
import blservice.infoservice.GetCustomerInterface;
import businesslogic.inter.AddLogInterface;
import dataservice.CommodityDataService;
import dataservice.CustomerDataService;
import dataservice.SalesReturnBillDataService;
import ds_stub.CommodityDs_stub;
import ds_stub.CustomerDs_stub;
import ds_stub.SalesReturnBillDs_stub;
import po.CommodityPO;
import po.CustomerPO;
import po.billpo.BillPO;
import po.billpo.SalesItemsPO;
import po.billpo.SalesReturnBillPO;
import presentation.tools.Timetools;
import rmi.Rmi;
import vo.MyTableModel;
import vo.billvo.BillVO;
import vo.billvo.SalesReturnBillVO;


public class SalesReturnBillBL implements SalesReturnBillBLService, BillOperationService, BillExamineService{
    
    private SalesReturnBillDataService salesReturnBillDs = Rmi.flag ? Rmi.getRemote(SalesReturnBillDataService.class) : new SalesReturnBillDs_stub();
    private CustomerDataService customerDs = Rmi.flag ? Rmi.getRemote(CustomerDataService.class) : new CustomerDs_stub();
    private CommodityDataService commodityDs = Rmi.flag ? Rmi.getRemote(CommodityDataService.class) : new CommodityDs_stub();
    private GetCustomerInterface customerInfo = new CustomerBL();
    private AddLogInterface addLog = new LogBL();

    @Override
    public String getNewId() {
        try{
            return "XSTHD-" + Timetools.getDate() + "-" + salesReturnBillDs.getNewId();
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteBill(String id) {
        try{
            SalesReturnBillPO bill = salesReturnBillDs.getBillById(id);
            if(bill.getState() == BillPO.PASS) return false;
            if (salesReturnBillDs.deleteBill(id)) {
            	addLog.add("删除销售退货单", "删除的销售退货单单据编号为"+id);
            	return true;
            } else return false;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
	public boolean saveBill(SalesReturnBillVO bill) {
		return saveBill(bill, "保存销售退货单", "保存的销售退货单单据编号为"+bill.getAllId());
	}
	
	private boolean saveBill(SalesReturnBillVO bill, String operation, String detail) {
		try{
            if (salesReturnBillDs.saveBill(toPO(bill))) {
            	addLog.add(operation, detail);
            	return true;
            } else return false;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public ArrayList<SalesReturnBillPO> getBillPOsByDate(String from, String to){
        try{
            return salesReturnBillDs.getBillsByDate(from, to);
        }catch(RemoteException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean offsetBill(String id){
        try{
            SalesReturnBillPO bill = salesReturnBillDs.getBillById(id);
            ArrayList<SalesItemsPO> items = new ArrayList<>();
            bill.getSalesReturnBillItems().forEach(i -> items.add(new SalesItemsPO(
                i.getComId(), i.getComRemark(), -i.getComQuantity(), i.getComPrice(), -i.getComSum()
            )));
            SalesReturnBillPO offset = new SalesReturnBillPO(
                Timetools.getDate(), Timetools.getTime(), salesReturnBillDs.getNewId(), bill.getOperator(), BillPO.PASS,
                bill.getCustomerId(), bill.getSalesManName(), bill.getRemark(), bill.getOriginalSBId(), 
                -bill.getOriginalSum(), -bill.getReturnSum(), items);
            if (salesReturnBillDs.saveBill(offset)) {
            	addLog.add("红冲销售退货单", "被红冲的销售退货单单据编号为"+bill.getAllId());
            	return true;
            } else return false;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean copyBill(BillVO bill){
        if(bill instanceof SalesReturnBillVO){
            SalesReturnBillVO old = (SalesReturnBillVO) bill;
            SalesReturnBillVO copy = new SalesReturnBillVO(
                Timetools.getDate(), Timetools.getTime(), this.getNewId().split("-")[2], old.getOperator(),
                BillVO.PASS, old.getCustomerId(), old.getModel(), old.getRemark(), 
                old.getOriginalSBId(), old.getDiscountRate(), old.getOriginalSum(), old.getSum()
            );
            return saveBill(copy, "红冲并复制销售退货单", "红冲并复制后新的销售退货单单据编号为"+copy.getAllId());
        }
        return false;
    }

    private SalesReturnBillPO toPO(SalesReturnBillVO bill){
        String date = bill.getDate(),
               time = bill.getTime(),
               id = bill.getId(),
               operatorId = bill.getOperator(),
               customerId = bill.getCustomerId(),
               salesManName = customerInfo.getCustomer(bill.getCustomerId()).getSalesman(),
               remark = bill.getRemark(),
               originalSBId = bill.getOriginalSBId();
        int state = bill.getState();
        double originalSum = bill.getOriginalSum(),
               returnSum = bill.getSum();
        ArrayList<SalesItemsPO> items = new ArrayList<>();
        MyTableModel model = bill.getModel();
        for(int i = 0; i < model.getRowCount(); i++){
            items.add(getItem(model.getValueAtRow(i)));
        }
        return new SalesReturnBillPO(date, time, id, operatorId
            , state, customerId, salesManName, remark, originalSBId
            , originalSum, returnSum, items);
    }
    
    

    private SalesItemsPO getItem(String[] data){
        String id = data[0], remark = data[7];
        int num = Integer.parseInt(data[5]);
        double price = Double.parseDouble(data[4]),
               sum = Double.parseDouble(data[6]);
        return new SalesItemsPO(
            id, remark, num, price, sum);
    }

	@Override
	public boolean examineBill(String billId) {
        try{
        	SalesReturnBillPO billPO = salesReturnBillDs.getBillById(billId);
            SalesReturnBillVO billVO = BillTools.toSalesReturnBillVO(salesReturnBillDs.getBillById(billId));
            ArrayList<SalesItemsPO> list = billPO.getSalesReturnBillItems();
            ArrayList<CommodityPO> commodityList = new ArrayList<CommodityPO>();
            CustomerPO customerPO = customerDs.findById(billPO.getCustomerId());
            boolean flag = customerPO.setPayment(customerPO.getPayment() - billPO.getReturnSum());
            for (SalesItemsPO item : list) {
            	CommodityPO commodityPO = commodityDs.findById(item.getComId());
            	if (!commodityPO.setAmount(commodityPO.getAmount()+item.getComQuantity())) flag = false;
            	commodityList.add(commodityPO);
            }
            if (flag) {
            	customerDs.update(customerPO);
            	for (CommodityPO c : commodityList) commodityDs.update(c);
            	billVO.setState(3);
                return saveBill(billVO, "审核销售退货单", "通过审核的销售退货单单据编号为"+billId);
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
        	SalesReturnBillVO billVO = BillTools.toSalesReturnBillVO(salesReturnBillDs.getBillById(billId));
            billVO.setState(4);
            return saveBill(billVO, "审核销售退货单", "单据编号为"+billId+"的销售退货单审核未通过");
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
	}

	@Override
	public BillVO getBillById(String billId) {
		try {
			return BillTools.toSalesReturnBillVO(salesReturnBillDs.getBillById(billId));
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
}
