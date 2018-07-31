package businesslogic;

import java.rmi.RemoteException;
import java.util.ArrayList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import blservice.CategoryBLService;
import blservice.infoservice.GetCategoryInterface;
import businesslogic.inter.AddLogInterface;
import dataservice.CategoryDataService;
import ds_stub.CategoryDs_stub;
import po.CategoryPO;
import rmi.Rmi;
import vo.CategoryVO;

/**
 * 商品分类的BL<br>
 * 与CommodityBL有直接依赖关系
 * @author 恽叶霄*/
public class CategoryBL implements CategoryBLService, GetCategoryInterface {
    
    private CategoryDataService categoryDs = Rmi.flag ? Rmi.getRemote(CategoryDataService.class) : new CategoryDs_stub();
    private AddLogInterface addLog = new LogBL();

    @Override
    public String getNewId() {
        try{
            return categoryDs.getNewId();
        }catch(RemoteException e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public DefaultTreeModel getModel() {
        DefaultTreeModel model = new DefaultTreeModel(null);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(new CategoryVO("","000000","所有分类"));
        model.setRoot(root);
        try{
            ArrayList<CategoryPO> list = categoryDs.getAllCategory();
            ArrayList<CategoryVO> volist = new ArrayList<CategoryVO>();
            for(CategoryPO category: list) {
            	volist.add(new CategoryVO(category.getFatherId(), category.getId(), category.getName()));
            }
            ArrayList<DefaultMutableTreeNode> nodeList = new ArrayList<DefaultMutableTreeNode>();
            nodeList.add(root);
            for(CategoryVO category: volist) {
            	for(DefaultMutableTreeNode node : nodeList) {
            		CategoryVO c = (CategoryVO)node.getUserObject();
            		if(category.getFatherId().equals(c.getId())){
            			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(category);
            			node.add(newNode);
            			nodeList.add(newNode);
            			break;
            		}
            	}
            }
            return model;
        }catch(RemoteException e){
            e.printStackTrace();
            return model;
        }
    }

    @Override
    public boolean add(CategoryVO category) {
        try{
            if (categoryDs.add(category.toPO())) {
            	addLog.add("增加商品分类", "新增商品分类编号："+ category.getId() + "  名称：" + category.getName());
            	return true;
            }
            return false;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(String id) {
        try{
            if (categoryDs.delete(id)) {
            	addLog.add("删除商品分类", "删除的分类Id:"+id);
            	return true;
            }
            return false;
        }catch(RemoteException e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean change(CategoryVO category) {
        try{
            if (categoryDs.update(category.toPO())) {
            	addLog.add("修改商品分类", "被修改的商品分类："+category.getId());
            	return true;
            }
            return false;
        }catch(RemoteException e){
            return false;
        }
    }
    
    public ArrayList<CategoryPO> searchByName(String name){
        try {
            return categoryDs.getCategorysBy("CateName", name, true);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public boolean hasContent(String id){
        try {
            ArrayList<CategoryPO> categories = categoryDs.getAllCategory();
            for(CategoryPO category: categories){
                if(category.getFatherId().equals(id))
                    return true;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return false;
    }
	@Override
	public CategoryVO findById(String id) {
		try{
			CategoryPO category = categoryDs.findById(id);
			if (category == null) return null;
            return new CategoryVO(category.getFatherId(),category.getId(),category.getName());
        }catch(RemoteException e){
            return null;
        }
	}

	@Override
	public CategoryVO getCategory(String id) {
		try {
			CategoryPO category = categoryDs.findById(id);
			if (category == null) return null;
            return new CategoryVO(category.getFatherId(),category.getId(),category.getName());
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

}