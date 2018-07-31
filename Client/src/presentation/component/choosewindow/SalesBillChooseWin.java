package presentation.component.choosewindow;

import blservice.billblservice.BillOperationService;
import blservice.billblservice.SalesBillBLService;
import businesslogic.BillOperationBL;
import businesslogic.SalesBillBL;
import vo.MyTableModel;
import vo.billvo.SalesBillVO;


public class SalesBillChooseWin extends BillChooseWin {
    
    private SalesBillBLService salesBillBl;
    private BillOperationService billOperationBl;
    private SalesBillVO bill;

    public SalesBillChooseWin(String customerId) {
        super(customerId);
        frame.setVisible(true);
    }

    @Override
    protected void initBLService() {
        salesBillBl = new SalesBillBL();
        billOperationBl = new BillOperationBL();
    }

    @Override
    protected MyTableModel getInitModel() {
        return salesBillBl.getFinishedBills(customerId);
    }

    @Override
    protected MyTableModel search(String type, String key) {
        return salesBillBl.search(type, key);
    }

    @Override
    protected MyTableModel getBillByDate(String from, String to) {
        return salesBillBl.getBillsByDate(from, to);
    }

    @Override
    protected void setBill(String id) {
        bill = (SalesBillVO) billOperationBl.getBillById(id);
    }
    
    public SalesBillVO getSalesBill(){
        return bill;
    }

}
