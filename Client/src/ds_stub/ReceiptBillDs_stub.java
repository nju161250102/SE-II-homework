package ds_stub;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dataservice.ReceiptBillDataService;
import po.billpo.BillPO;
import po.billpo.ReceiptBillPO;
import po.billpo.TransferItem;

public class ReceiptBillDs_stub implements ReceiptBillDataService {

	private static final ArrayList<ReceiptBillPO> BILLS
    = new ArrayList<>();
	
	static{
        ArrayList<TransferItem> items1 = new ArrayList<>();
        items1.add(new TransferItem("6209111100003333", 10000, "赎金"));
		items1.add(new TransferItem("6209111100003333", 12345, "大保健费用"));
        BILLS.add(new ReceiptBillPO(
				"2017-12-05", "07:10:11", "00123", "0002", BillPO.COMMITED,
				"000001",  items1, 22345
				));

        ArrayList<TransferItem> items2 = new ArrayList<>();
		items2.add(new TransferItem("6209111100003333", 100, "无"));
		items2.add(new TransferItem("6209111100003333", 127, "该内容涉嫌违规，已被删除"));
        BILLS.add(new ReceiptBillPO(
				"2017-12-05", "23:19:16", "23333", "0003", BillPO.COMMITED,
				"000001",  items2, 227
				));
    }
	
	@Override
	public boolean saveBill(ReceiptBillPO bill) {
        System.out.println("Receipt bill saved in database: " + bill.getId());
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
        System.out.println("Receipt Bill deleted in database: " + billid);
        return true;
	}

	@Override
	public String getNewId() {
		return "00001";
	}

	@Override
	public ReceiptBillPO getBillById(String id) {
        System.out.println("Receipt Bill found in database: " + id);
        return BILLS.get(1);
	}

	@Override
    public ArrayList<ReceiptBillPO> getBillsByDate(String from, String to) throws RemoteException {
        ArrayList<ReceiptBillPO> result = new ArrayList<>();
        result.add(BILLS.get(0));
        result.add(BILLS.get(1));
        System.out.println("Receipt Bill found by date in database");
        return result;
    }
}
