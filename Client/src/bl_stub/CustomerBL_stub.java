package bl_stub;

import blservice.CustomerBLService;
import blservice.infoservice.GetCustomerInterface;
import vo.CustomerVO;
import vo.MyTableModel;

public class CustomerBL_stub implements CustomerBLService, GetCustomerInterface {

	@Override
	public boolean delete(String id) {
		System.out.println("客户信息已成功删除");
		return true;
	}

	@Override
	public MyTableModel search(String type, String key) {
		String[] attributes={"客户编号","姓名","分类","级别","电话","地址","邮编","电子邮箱","应收额度","应收","应付","默认业务员"};
		String[][] info={{"JHS-001","清流","进货商","LV5","","deep dark ♂ fantasy","","","4000","0","0","Van"},
				{"XSS-001","浊流","销售商","LV1","","幻想♂乡","","","2000","100","400","Van"}};
		System.out.println("显示搜索的客户信息");
		return new MyTableModel(info, attributes);
	}

	@Override
	public MyTableModel update() {
		String[] attributes={"客户编号","姓名","分类","级别","电话","地址","邮编","电子邮箱","应收额度","应收","应付","默认业务员"};
		String[][] info={{"JHS-001","清流","进货商","LV5","","deep dark ♂ fantasy","","","4000","0","0","Van"},
				{"XSS-001","浊流","销售商","LV1","","幻想♂乡","","","2000","100","400","Van"}};
		System.out.println("客户信息已成功更新");
		return new MyTableModel(info, attributes);
	}

	@Override
	public boolean add(CustomerVO customer) {
		System.out.println("客户信息已成功添加");
		return true;
	}

	@Override
	public boolean change(CustomerVO customer) {
		System.out.println("客户信息已更改");
		return true;
	}

	@Override
	public String getNewId() {
		return "0003";
	}

    @Override
    public CustomerVO getCustomer(String id) {
        return new CustomerVO("JHS-001","清流", 1, 5,""
            ,"deep dark ♂ fantasy","","",4000,0,0,"Van");
    }
	
}
