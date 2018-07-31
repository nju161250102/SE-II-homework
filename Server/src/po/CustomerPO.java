package po;

import java.io.Serializable;

/**
 * Create time: 2017/10/26<br>
 * Last update time: 2017/10/26<br>
 * <br>顾客的PO类
 * @author 恽叶霄
 */
public class CustomerPO implements Serializable {

    private static final long serialVersionUID = -6850102650969420098L;
    private String id, name, telNumber, address, mail, code;
    private String salesman;// 默认业务员
    private int rank;//客户的级别，共5级
    private int type;//客户的类别
    private double recRange;//应收额度
    private double receivable;//应收
    private double payment;//应付
	private boolean isExist;
    
    public CustomerPO(String id, String name, String telNumber, String address
            , String mail, String code,String salesman, int rank, int type, double recRange
            , double receivable, double payment, boolean isExist) {
		super();
		this.id = id;
		this.name = name;
		this.telNumber = telNumber;
		this.address = address;
		this.mail = mail;
		this.code = code;
		this.salesman = salesman;
		this.rank = rank;
		this.type = type;
		this.recRange = recRange;
		this.receivable = receivable;
		this.payment = payment;
		this.isExist = isExist;
	}

    public String getId() {return id;}

    public String getName() {return name;}

    public String getTelNumber() {return telNumber;}

    public String getAddress() {return address;}

    public String getMail() {return mail;}
    
    public String getCode() {return code;}

    public String getSalesman() {return salesman;}

    public int getRank() {return rank;}

    public int getType() {return type;}

    public double getRecRange() {return recRange;}

    public double getReceivable() {return receivable;}

    public double getPayment() {return payment;}
    
    public boolean getExistFlag() {return this.isExist;}

    public boolean setPayment(double n) {
    	if (n < 0) return false;
    	payment = n;
    	return true;
    }
    
    public boolean setReceivable(double n) {
    	if (n < 0 || n > recRange) return false;
    	receivable = n;
    	return true;
    }
    
    public class CustomerType{
        public static final int SUPPLIER = 1;
        public static final int VENDER = 2;
    }
}