package businesslogic;

import java.util.HashMap;
import java.util.Map;

import blservice.billblservice.BillOperationService;
import vo.billvo.BillVO;


public class BillOperationBL implements BillOperationService {
    
    private Map<String, BillOperationService> services;

    public BillOperationBL() {
        services = new HashMap<>();
        services.put("XJFYD", new CashCostBillBL());
        services.put("BYD", new ChangeBillBL(true));
        services.put("BSD", new ChangeBillBL(false));
        services.put("FKD", new PaymentBillBL());
        services.put("JHD", new PurchaseBillBL());
        services.put("JHTHD", new PurchaseReturnBillBL());
        services.put("XSD", new SalesBillBL());
        services.put("XSTHD", new SalesReturnBillBL());
        services.put("SKD", new ReceiptBillBL());
        services.put("SPZSD", new GiftBillBL());
    }

    @Override
    public boolean offsetBill(String billId) {
        String key = billId.split("-")[0];
        return services.get(key).offsetBill(billId);
    }

    @Override
    public boolean copyBill(BillVO bill) {
        String key = bill.getAllId().split("-")[0];
        return services.get(key).copyBill(bill);
    }

    @Override
    public boolean deleteBill(String billId) {
    	String key = billId.split("-")[0];
        return services.get(key).deleteBill(billId);
    }

	@Override
	public BillVO getBillById(String billId) {
		String key = billId.split("-")[0];
		return services.get(key).getBillById(billId);
	}
}
