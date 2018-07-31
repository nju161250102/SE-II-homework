package businesslogic;

import java.util.HashMap;
import java.util.Map;

import blservice.billblservice.BillExamineService;

public class BillExamineBL implements BillExamineService{

	private Map<String, BillExamineService> services;

    public BillExamineBL() {
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
	public boolean examineBill(String billId) {
        String key = billId.split("-")[0];
		return services.get(key).examineBill(billId);
	}

	@Override
	public boolean notPassBill(String billId) {
        String key = billId.split("-")[0];
		return services.get(key).notPassBill(billId);
	}

}
