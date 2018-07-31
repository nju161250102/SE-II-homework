package ds_stub;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dataservice.CommodityDataService;
import po.CommodityPO;


public class CommodityDs_stub implements CommodityDataService {

    private static ArrayList<CommodityPO> result = new ArrayList<>();
    
    public CommodityDs_stub() {
    	if (result.size() == 0) {
        	result.add(new CommodityPO("000001", "·ê¿¼±Ø¹ýµÆ", "TBD", "A", "000002"
                    , 2000L, 200L, 120.0, 340.0, 120.0, 320.0, true));
            result.add(new CommodityPO("000002", "µç´ÅÂö³åµÆ", "TDD", "A", "000002"
                , 1000L, 100L, 200.0, 350.0, 200.0, 380.0, true));
            result.add(new CommodityPO("000003", "ºË¾ÛÄÜÉñµÆ", "TDD", "A", "000004"
                , 500L, 70L, 100.0, 350.0, 150.0, 160.0, true));
            result.add(new CommodityPO("000004", "³¬¿Õ¼äÕÕÃ÷µÆ", "TBD", "A", "000004"
                    , 500L, 70L, 100.0, 350.0, 150.0, 160.0, true));
    	}
    }

    @Override
    public String getNewId() throws RemoteException {
        return String.format("%06d", result.size()+1);
    }

    @Override
    public CommodityPO findById(String id) throws RemoteException {
        System.out.println("commodity found in database: " + id);
        for (int i = 0; i < result.size(); i++) {
        	if (id.equals(result.get(i).getId())) return result.get(i);
        }
		return null;
    }

    @Override
    public boolean add(CommodityPO commodity) throws RemoteException {
        System.out.println("commodity added in database: " + commodity.getId());
        result.add(commodity);
        return true;
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        System.out.println("commodity deleted in database: " + id);
        for (int i = 0; i < result.size(); i++) {
        	if (id.equals(result.get(i).getId())) {result.remove(i);break;}
        }
        return true;
    }

    @Override
    public boolean update(CommodityPO commodity) throws RemoteException {
        System.out.println("commodity updated in database: " + commodity.getId());
        return true;
    }

    @Override
    public ArrayList<CommodityPO> getAllCommodity() throws RemoteException {
        System.out.println("all commodities in database returned");
        return result;
    }

	@Override
	public ArrayList<CommodityPO> getCommoditysBy(String field, String content, boolean isfuzzy) throws RemoteException {
	    ArrayList<CommodityPO> commodities = new ArrayList<>();
	    commodities.add(result.get(0));
	    commodities.add(result.get(3));
		return commodities;
	}

}
