package test;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import blservice.ViewBusinessSituationBLService;
import businesslogic.ViewBusinessSituationBL;
import rmi.Rmi;

public class ViewBusinessSituationBLTest {

	private ViewBusinessSituationBLService bl= new ViewBusinessSituationBL();
	
	@BeforeClass
    public static void runBeforeTestMethod() {
        Rmi.flag = false;
    }
	
	@Test
	public void testGetSalesIncome() {
		double result = bl.getSalesIncome("2017-12-03", "2017-12-31");
		Assert.assertEquals(58000, result, 0.0);
	}

	@Test
	public void testGetCommodityOverflowIncome() {
		double result = bl.getCommodityOverflowIncome("2017-12-03", "2017-12-31");
		Assert.assertEquals(320, result, 0.0);
	}

	@Test
	public void testGetPurchaseAndReturnIncome() {
		double result = bl.getPurchaseAndReturnIncome("2017-12-03", "2017-12-31");
		Assert.assertEquals(6500.0, result, 0.0);
	}

	@Test
	public void testGetCashCouPonIncome() {
		double result = bl.getCashCouPonIncome("2017-12-03", "2017-12-31");
		Assert.assertEquals(2000, result, 0.0);
	}

	@Test
	public void testGetSalesExpense() {
		double result = bl.getSalesExpense("2017-12-03", "2017-12-31");
		Assert.assertEquals(25000, result, 0.0);
	}

	@Test
	public void testGetCommodityBrokenExpense() {
		double result = bl.getCommodityBrokenExpense("2017-12-03", "2017-12-31");
		Assert.assertEquals(0, result, 0.0);
	}

	@Test
	public void testGetCommoditySentExpense() {
		double result = bl.getCommoditySentExpense("2017-12-03", "2017-12-31");
		Assert.assertEquals(0, result, 0.0);
	}

	@Test
	public void testGetProfit() {
		double result = bl.getProfit("2017-12-03", "2017-12-31");
		Assert.assertEquals(37820, result, 0.0);
	}

}
