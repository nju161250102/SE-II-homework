package businesslogic;

import java.rmi.RemoteException;
import java.util.ArrayList;

import blservice.MailBLService;
import blservice.PromotionBLService;
import blservice.billblservice.BillExamineService;
import blservice.billblservice.BillOperationService;
import blservice.billblservice.SalesBillBLService;
import blservice.infoservice.GetCustomerInterface;
import businesslogic.best_promotion.BestGroupDiscount;
import businesslogic.best_promotion.BestRankPromotion;
import businesslogic.best_promotion.BestSumPromotion;
import businesslogic.inter.AddLogInterface;
import businesslogic.inter.GiftBillCreation;
import businesslogic.inter.IBestPromotion;
import dataservice.CommodityDataService;
import dataservice.CustomerDataService;
import dataservice.SalesBillDataService;
import ds_stub.CommodityDs_stub;
import ds_stub.CustomerDs_stub;
import ds_stub.SalesBillDs_stub;
import po.CommodityPO;
import po.CustomerPO;
import po.UserPO;
import po.billpo.BillPO;
import po.billpo.SalesBillPO;
import po.billpo.SalesItemsPO;
import presentation.tools.Timetools;
import rmi.Rmi;
import vo.MyTableModel;
import vo.PromotionVO;
import vo.billvo.BillVO;
import vo.billvo.SalesBillVO;

/**
 * @author 恽叶霄
 */
public class SalesBillBL implements SalesBillBLService, BillOperationService, BillExamineService {
    
    private SalesBillDataService salesBillDs = Rmi.flag ? Rmi.getRemote(SalesBillDataService.class) : new SalesBillDs_stub();
    private CustomerDataService customerDs = Rmi.flag ? Rmi.getRemote(CustomerDataService.class) : new CustomerDs_stub();
    private CommodityDataService commodityDs = Rmi.flag ? Rmi.getRemote(CommodityDataService.class) : new CommodityDs_stub();
    private GetCustomerInterface customerInfo = new CustomerBL();
    private AddLogInterface addLog = new LogBL();
	private MailBLService mailBL = new MailBL();
   
    @Override
    public String getNewId() {
        try{
            return "XSD-" + Timetools.getDate() + "-" + salesBillDs.getNewId();
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean deleteBill(String id) {
        try{
            SalesBillPO bill = salesBillDs.getBillById(id);
            if(bill.getState() == BillPO.PASS) return false;
            if (salesBillDs.deleteBill(id)) {
            	addLog.add("删除销售单", "删除的销售单单据编号为"+id);
            	return true;
            } else return false;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveBill(SalesBillVO bill) {
		return saveBill(bill, "保存销售单", "保存的销售单单据编号为"+bill.getAllId());
	}
	
	private boolean saveBill(SalesBillVO bill, String operation, String detail) {
		try{
            if (salesBillDs.saveBill(toPO(bill))) {
            	addLog.add(operation, detail);
            	createGiftBill(bill);
            	return true;
            } else return false;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public MyTableModel getFinishedBills(){
        return search("按状态搜索", BillVO.PASS + "");
    }
    
    public MyTableModel getFinishedBills(String customerId){
        try{
            String field = "CONCAT(SBCondition,',',SBCustomerID)";
            String key = BillPO.PASS + "," + customerId;
            ArrayList<SalesBillPO> bills = salesBillDs.getBillsBy(field, key, true);
            return toModel(bills);
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel search(String type, String key) {
        try{
            String field = null;
            if("按编号搜索".equals(type)){
                field = "SBID";
            } // other searching methods not yet considered
            ArrayList<SalesBillPO> bills = salesBillDs.getBillsBy(field, key, true);
            return toModel(bills);
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public MyTableModel getBillsByDate(String from, String to){
        try{
            ArrayList<SalesBillPO> bills = salesBillDs.getBillByDate(from, to);
            return toModel(bills);
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }
    
    public ArrayList<SalesBillPO> getBillPOsByDate(String from, String to){
        try{
            return salesBillDs.getBillByDate(from, to);
        }catch(RemoteException e){
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean offsetBill(String id){
        try{
            SalesBillPO bill = salesBillDs.getBillById(id);
            ArrayList<SalesItemsPO> items = new ArrayList<>();
            bill.getSalesBillItems().forEach(i -> items.add(new SalesItemsPO(
                i.getComId(), i.getComRemark(), -i.getComQuantity(), i.getComPrice(), -i.getComSum()
            )));
            SalesBillPO offset = new SalesBillPO(
                Timetools.getDate(), Timetools.getTime(), salesBillDs.getNewId(), bill.getOperator(), BillPO.PASS,
                bill.getCustomerId(), bill.getSalesManName(), bill.getRemark(), bill.getPromotionId(),
                -bill.getBeforeDiscount(), -bill.getDiscount(), -bill.getCoupon(), -bill.getAfterDiscount(), items);
            if (salesBillDs.saveBill(offset)) {
            	addLog.add("红冲销售单", "被红冲的销售单单据编号为"+bill.getAllId());
            	return true;
            } else return false;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean copyBill(BillVO bill){
        if(bill instanceof SalesBillVO){
            SalesBillVO old = (SalesBillVO) bill;
            SalesBillVO copy = new SalesBillVO(
                Timetools.getDate(), Timetools.getTime(), this.getNewId().split("-")[2], old.getOperator(),
                BillVO.PASS, old.getCustomerId(), old.getModel(),
                old.getRemark(), old.getBeforeDiscount(), old.getDiscount(), old.getCoupon(), old.getSum(), old.getPromotionId()
            );
            return saveBill(copy, "红冲并复制销售单", "红冲并复制后新的销售单单据编号为"+copy.getAllId());
        }
        return false;
    }

    private MyTableModel toModel(ArrayList<SalesBillPO> bills){
        String[] columnNames = {"制定时间", "单据编号"};
        String[][] data = new String[bills.size()][columnNames.length];
        for(int i = 0; i < bills.size(); i++){
            SalesBillPO salesBill = bills.get(i);
            data[i][0] = salesBill.getDate() + " " + salesBill.getTime();
            data[i][1] = "XSD-" + salesBill.getDate() + "-" + salesBill.getId();
        }
        return new MyTableModel(data, columnNames);
    }

    private SalesBillPO toPO(SalesBillVO bill){
        MyTableModel model = bill.getModel();
        ArrayList<SalesItemsPO> items = new ArrayList<>();
        for(int i = 0; i < model.getRowCount(); i++){
            String[] row = model.getValueAtRow(i);
            int num = Integer.parseInt(row[5]);
            double price = Double.parseDouble(row[4]),
                   sum = Double.parseDouble(row[6]);
            items.add(new SalesItemsPO(row[0], row[7], num, price, sum));
        }
        return new SalesBillPO(bill.getDate(), bill.getTime()
            , bill.getId(), bill.getOperator(), bill.getState()
            , bill.getCustomerId(), customerInfo.getCustomer(bill.getCustomerId()).getSalesman()
            , bill.getRemark(), bill.getPromotionId(), bill.getBeforeDiscount()
            , bill.getDiscount(), bill.getCoupon(), bill.getSum()
            , items);
    }
    
	@Override
	public boolean examineBill(String billId) {
        try{
        	SalesBillPO billPO = salesBillDs.getBillById(billId);
            SalesBillVO billVO = BillTools.toSalesBillVO(billPO);
            ArrayList<SalesItemsPO> list = billPO.getSalesBillItems();
            ArrayList<CommodityPO> commodityList = new ArrayList<CommodityPO>();
            boolean flag = true;
            CustomerPO customerPO = customerDs.findById(billPO.getCustomerId());
            customerPO.setPayment(customerPO.getPayment()+billPO.getAfterDiscount());
            
            for (int i = 0; i < list.size(); i++) {
            	SalesItemsPO item = list.get(i);
            	CommodityPO commodityPO = commodityDs.findById(item.getComId());
            	commodityPO.setRecentSalePrice(item.getComPrice());
            	if (!commodityPO.setAmount(commodityPO.getAmount()-item.getComQuantity())) flag = false;
            	commodityList.add(commodityPO);
            }

            if (flag) {
            	for (CommodityPO c : commodityList) {
            		if(c.getAmount() < c.getAlarmNum()) mailBL.saveMail("0001", UserPO.UserType.STORE_KEEPER, "编号为"+c.getId()+"的商品"+c.getName()+"库存数量不足");
            		commodityDs.update(c);
            	}
            	customerDs.update(customerPO);
            	billVO.setState(3);
            	mailBL.saveMail("0001", billPO.getOperator(), "单据编号为"+billId+"的销售单通过审核，请尽快完成商品出库操作");
                return saveBill(billVO, "审核销售单", "通过审核的销售单单据编号为"+billId);
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
        	SalesBillVO billVO = BillTools.toSalesBillVO(salesBillDs.getBillById(billId));
            billVO.setState(4);
            return saveBill(billVO, "审核销售单", "单据编号为"+billId+"的销售单审核未通过");
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
	}

	@Override
	public BillVO getBillById(String billId) {
		try {
			return BillTools.toSalesBillVO(salesBillDs.getBillById(billId));
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

    @Override
    public PromotionVO getBestPromotion(int rank, MyTableModel goods, double sum) {
        double reductionMax = 0;
        PromotionVO best = null;
        IBestPromotion groupBest = new BestGroupDiscount(goods);
        double tempReduction = groupBest.getBenefit();
        if(reductionMax < tempReduction){
            reductionMax = tempReduction;
            best = groupBest.getBest();
        }
        IBestPromotion rankBest = new BestRankPromotion(rank);
        tempReduction = rankBest.getBenefit();
        if(reductionMax < tempReduction){
            reductionMax = tempReduction;
            best = rankBest.getBest();
        }
        IBestPromotion sumBest = new BestSumPromotion(sum);
        tempReduction = sumBest.getBenefit();
        if(reductionMax < tempReduction){
            reductionMax = tempReduction;
            best = sumBest.getBest();
        }
        return best;
    }

    private void createGiftBill(SalesBillVO bill){
        if(bill.getState() != BillVO.COMMITED || bill.getPromotionId() == null) return;
        PromotionBLService promotionBl = new PromotionBL();
        PromotionVO promotion = promotionBl.findById(bill.getPromotionId());
        MyTableModel gifts = promotion.getGifts();
        if(gifts != null){
            GiftBillCreation giftCreator = new GiftBillBL();
            giftCreator.createAndCommit(gifts, bill.getAllId(),bill.getOperator(), bill.getCustomerId());
        }
    }

}
