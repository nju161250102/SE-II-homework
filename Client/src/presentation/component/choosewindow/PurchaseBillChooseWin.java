package presentation.component.choosewindow;

import blservice.billblservice.BillOperationService;
import blservice.billblservice.PurchaseBillBLService;
import businesslogic.BillOperationBL;
import businesslogic.PurchaseBillBL;
import vo.MyTableModel;
import vo.billvo.PurchaseBillVO;


public class PurchaseBillChooseWin extends BillChooseWin {
    
    private PurchaseBillBLService purchaseBillBl;
    private BillOperationService billOperationBL;
    private PurchaseBillVO bill;

    public PurchaseBillChooseWin(String customerId) {
        super(customerId);
        frame.setVisible(true);
    }

    @Override
    protected void initBLService() {
        purchaseBillBl = new PurchaseBillBL();
        billOperationBL = new BillOperationBL();
    }

    @Override
    protected MyTableModel getInitModel() {
        return purchaseBillBl.getFinishedBills(customerId);
    }

    @Override
    protected MyTableModel search(String type, String key) {
        return purchaseBillBl.search(type, key);
    }

    @Override
    protected MyTableModel getBillByDate(String from, String to) {
        return purchaseBillBl.getBillByDate(from, to);
    }

    @Override
    protected void setBill(String id) {
        bill = (PurchaseBillVO) billOperationBL.getBillById(id);
    }
    
    public PurchaseBillVO getPurchaseBill(){
        return this.bill;
    }

}
