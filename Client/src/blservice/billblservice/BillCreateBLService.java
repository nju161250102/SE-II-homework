package blservice.billblservice;

public interface BillCreateBLService {

	/**
	 * 返回完整的Id[XXX-yyyyMMdd-00000]
	 * @return 下一条单据的Id
	 */
	public String getNewId();
}
