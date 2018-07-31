package vo;

import po.CategoryPO;

/**
 * 商品分类的VO对象,由父分类编号、编号、名称组成
 * @author 钱美缘
 */
public class CategoryVO {

	private String fatherId;
	private String id;
	private String name;
	/**
	 * 构造函数
	 * @param fatherId 父分类编号
	 * @param id 编号
	 * @param name 名称
	 */
	public CategoryVO(String fatherId, String id, String name) {
		this.fatherId = fatherId;
		this.id = id;
		this.name = name;
	}
	/**
	 * @return 返回父分类编号
	 */
	public String getFatherId() {
		return fatherId;
	}
	/**
	 * @return 返回编号
	 */
	public String getId() {
		return id;
	}
	/**
	 * @return 返回名称
	 */
	public String getName() {
		return name;
	}
	
	@Override
	public String toString(){
	    return name;
	}

	public CategoryPO toPO(){
	    return new CategoryPO(id, name, fatherId, true);
	}
}
