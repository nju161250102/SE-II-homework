package businesslogic;

import java.rmi.RemoteException;
import java.util.ArrayList;

import blservice.MailBLService;
import blservice.billblservice.BillExamineService;
import blservice.billblservice.BillOperationService;
import businesslogic.inter.AddLogInterface;
import businesslogic.inter.GiftBillCreation;
import dataservice.CommodityDataService;
import dataservice.GiftBillDataService;
import ds_stub.CommodityDs_stub;
import ds_stub.GiftBillDs_stub;
import po.CommodityPO;
import po.UserPO;
import po.billpo.BillPO;
import po.billpo.GiftBillPO;
import po.billpo.GiftItem;
import presentation.tools.Timetools;
import rmi.Rmi;
import vo.MyTableModel;
import vo.billvo.BillVO;


public class GiftBillBL implements GiftBillCreation, BillOperationService, BillExamineService{
    
    private GiftBillDataService giftBillDs = Rmi.flag ? Rmi.getRemote(GiftBillDataService.class) : new GiftBillDs_stub();;
    private CommodityDataService commodityDs = Rmi.flag ? Rmi.getRemote(CommodityDataService.class) : new CommodityDs_stub();
    private AddLogInterface logger = new LogBL();
	private MailBLService mailBL = new MailBL();

    @Override
    public boolean examineBill(String billId) {
        try{
            GiftBillPO bill = giftBillDs.getById(billId);
            if(bill == null) return false;
            ArrayList<CommodityPO> commodityList = new ArrayList<CommodityPO>();
            boolean flag = true;
            for (GiftItem item :bill.getGifts()) {
            	CommodityPO commodityPO = commodityDs.findById(item.getComId());
            	if (!commodityPO.setAmount(commodityPO.getAmount()-item.getNum())) flag = false;
            	commodityList.add(commodityPO);
            }
            if (flag) {
            	for (CommodityPO c : commodityList) {
            		if(c.getAmount() < c.getAlarmNum()) mailBL.saveMail("0001", UserPO.UserType.STORE_KEEPER, "编号为"+c.getId()+"的商品"+c.getName()+"库存数量不足");
            		commodityDs.update(c);
            	}
            	bill.setState(BillPO.PASS);
            	boolean success = giftBillDs.add(bill);
                if(success) logger.add("审核商品赠送单", "通过审核的商品赠送单编号：" + billId);
                return success;
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
            GiftBillPO bill = giftBillDs.getById(billId);
            if(bill == null){
                return false;
            }
            bill.setState(BillPO.NOTPASS);
            boolean success = giftBillDs.add(bill);
            if(success){
                logger.add("审核商品赠送单", "编号：" + billId + " 未通过审核。");
            }
            return success;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean createAndCommit(MyTableModel gifts, String salesBillId, String operatorId, String customerId) {
        try{
            String date = Timetools.getDate();
            String time = Timetools.getTime();
            String id = giftBillDs.getNewId();
            int state = BillPO.COMMITED;
            ArrayList<GiftItem> items = GiftItemTools.toArrayList(gifts);
            GiftBillPO bill = new GiftBillPO(date, time, id, operatorId, state, items, salesBillId, customerId);
            boolean success = giftBillDs.add(bill);
            if(success){
                logger.add("新建一张商品赠送单", "单据编号：" + bill.getAllId());
            }
            return success;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }

	@Override
	public boolean offsetBill(String billId) {
		return false;
	}

	@Override
	public boolean copyBill(BillVO bill) {
		return false;
	}

	@Override
	public boolean deleteBill(String billId) {
		return false;
	}

	@Override
	public BillVO getBillById(String billId) {
		try {
			return BillTools.toGiftBillVO(giftBillDs.getById(billId));
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}
}
