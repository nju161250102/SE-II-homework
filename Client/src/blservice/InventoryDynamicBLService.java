package blservice;

import vo.MyTableModel;

public interface InventoryDynamicBLService {
    
    MyTableModel getDynamic(String from, String to);
    
    MyTableModel getDefault();

}
