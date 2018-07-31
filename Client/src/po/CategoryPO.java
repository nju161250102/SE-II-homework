package po;

import java.io.Serializable;

/**
 * Create time: 2017/10/26<br>
 * Last update time: 2017/10/26<br>
 * <br>与商品分类相关的PO类
 * @author 恽叶霄
 */
public class CategoryPO implements Serializable {
    private static final long serialVersionUID = 802795139476003045L;
    private String id, name, fatherId;
	private boolean isExist;
    
    public CategoryPO(String id, String name, String fatherId, boolean isExist) {
        this.id = id;
        this.name = name;
        this.fatherId = fatherId;
        this.isExist = isExist;
    }

    public String getId() {return id;}

    public String getName() {return name;}

    public String getFatherId() {return fatherId;}
    
    public boolean getExistFlag() {return this.isExist;}
}