package ds_stub;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dataservice.PaymentBillDataService;
import po.billpo.BillPO;
import po.billpo.PaymentBillPO;
import po.billpo.TransferItem;

public class PaymentBillDs_stub implements PaymentBillDataService {
	private static final ArrayList<PaymentBillPO> BILLS
    = new ArrayList<>();
	
	static{
        ArrayList<TransferItem> items1 = new ArrayList<>();
        items1.add(new TransferItem("6209111100003333", 10000, "赎金"));
		items1.add(new TransferItem("6209111100003333", 12345, "大保健费用"));
        BILLS.add(new PaymentBillPO(
				"2017-12-06", "07:10:11", "00123", "0002", BillPO.COMMITED,
				"000001",  items1, 22345
				));

        ArrayList<TransferItem> items2 = new ArrayList<>();
		items2.add(new TransferItem("6209111100003333", 100, "无"));
		items2.add(new TransferItem("6209111100003333", 127, "该内容涉嫌违规，已被删除"));
        BILLS.add(new PaymentBillPO(
				"2017-12-06", "23:19:16", "23333", "0003", BillPO.COMMITED,
				"000001",  items2, 227
				));
    }
	
	
	@Override
	public boolean saveBill(PaymentBillPO bill) {
		BILLS.add(bill);
		System.out.println("Payment Bill added in database: " + bill.getId());
        return true;
	}

	@Override
	public boolean deleteBill(String billid) {
        for(int i = 0; i < BILLS.size(); i++){
            if(BILLS.get(i).getId().equals(billid)){
                BILLS.remove(i);
                break;
            }
        }
        System.out.println("Payment Bill deleted in database: " + billid);
        return true;
	}

	@Override
	public String getNewId() {
		return "00002";
	}

	@Override
	public PaymentBillPO getBillById(String id) {
        System.out.println("Payment Bill found in database: " + id);
        return BILLS.get(1);
	}

	@Override
    public ArrayList<PaymentBillPO> getBillsByDate(String from, String to) throws RemoteException {
        ArrayList<PaymentBillPO> result = new ArrayList<>();
        result.add(BILLS.get(0));
        result.add(BILLS.get(1));
        System.out.println("Payment Bill found by date in database");
        return result;
    }
}
