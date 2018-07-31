package ds_stub;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dataservice.ChangeBillDataService;
import po.billpo.BillPO;
import po.billpo.ChangeBillPO;
import po.billpo.ChangeItem;

public class ChangeBillDs_stub implements ChangeBillDataService {

	private static final ArrayList<ChangeBillPO> BILLS
    = new ArrayList<>();
	
	static{
        ArrayList<ChangeItem> items1 = new ArrayList<>();
        items1.add(new ChangeItem("000001", 20, 21));
        BILLS.add(new ChangeBillPO(
				"2017-12-08", "11:03:23", "00123", "0004", BillPO.COMMITED,
				true,  items1
				));
        
        ArrayList<ChangeItem> items2 = new ArrayList<>();
		items2.add(new ChangeItem("000002", 100, 90));
        BILLS.add(new ChangeBillPO(
				"2017-12-08", "20:06:37", "23333", "0006", BillPO.COMMITED,
				false,  items2
				));
    }
	
	private static int count = 0; 

	@Override
	public boolean saveBill(ChangeBillPO bill) {
		BILLS.add(bill);
		System.out.println("Change Bill added in database: " + bill.getId());
        return true;
	}

	@Override
	public String getNewId(boolean isOver) {
		count++;
		return String.format("%05d", count);
	}

	@Override
	public ChangeBillPO getBillById(String id, boolean isOver) throws RemoteException {
		System.out.println("Change Bill found in database: " + id);
        return BILLS.get(1);
	}

	@Override
	public boolean deleteBill(String id, boolean isOver) throws RemoteException {
		for(int i = 0; i < BILLS.size(); i++){
            if(BILLS.get(i).getId().equals(id)){
                BILLS.remove(i);
                break;
            }
        }
        System.out.println("Change Bill deleted in database: " + id);
        return true;
	}
}
