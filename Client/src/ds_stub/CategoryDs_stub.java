package ds_stub;

import java.rmi.RemoteException;
import java.util.ArrayList;

import dataservice.CategoryDataService;
import po.CategoryPO;


public class CategoryDs_stub implements CategoryDataService {

	private static ArrayList<CategoryPO> result = new ArrayList<>();
	
    public CategoryDs_stub() {
    	if (result.size() == 0) {
            result.add(new CategoryPO("000001", "some", "000000", true));
            result.add(new CategoryPO("000003", "same", "000000", true));
            result.add(new CategoryPO("000002", "come", "000001", true));
            result.add(new CategoryPO("000004", "sane", "000003", true));
    	}
    }

    @Override
    public String getNewId() throws RemoteException {
        return String.format("%06d", result.size()+1);
    }

    @Override
    public CategoryPO findById(String id) throws RemoteException {
        for (int i = 0; i < result.size(); i++) {
        	if (result.get(i).getId().equals(id)) return result.get(i);
        }
        return null;
    }

    @Override
    public boolean add(CategoryPO category) throws RemoteException {
        System.out.println("category added in database: " + category.getId());
        result.add(category);
        return true;
    }

    @Override
    public boolean delete(String id) throws RemoteException {
        System.out.println("category deleted in database: " + id);
        for (int i = 0; i < result.size(); i++) {
        	if (id.equals(result.get(i).getId())) {result.remove(i);break;}
        }
        return true;
    }

    @Override
    public boolean update(CategoryPO category) throws RemoteException {
        System.out.println("category updated in database: " + category.getId());
        return true;
    }

    @Override
    public ArrayList<CategoryPO> getAllCategory() throws RemoteException {
        System.out.println("all categories in database returned");
        return result;
    }

	@Override
	public ArrayList<CategoryPO> getCategorysBy(String field, String content, boolean isfuzzy) throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

}
