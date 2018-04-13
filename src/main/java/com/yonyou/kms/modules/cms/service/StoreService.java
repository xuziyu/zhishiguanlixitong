package com.yonyou.kms.modules.cms.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.modules.cms.dao.CategoryDao;
import com.yonyou.kms.modules.cms.dao.StoreDao;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.Store;


/**
 * 收藏Service
 * @author zy
 * 
 */
@Service
public class StoreService extends CrudService<StoreDao, Store>{
	
	@Autowired 
	StoreDao storedao;
	@Autowired
	private CategoryDao categoryDao;

	public Page<Store> findPage(Page<Store> page, Store store) {//分页显示
		store.getSqlMap().put("dsf", dataScopeFilter(store.getCurrentUser(), "o", "u"));//获取当前user
		if (store.getCategory()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(store.getCategory().getId()) && !Category.isRoot(store.getCategory().getId())){
			Category category = categoryDao.get(store.getCategory().getId());
			if (category==null){
				category = new Category();
			}
			category.setParentIds(category.getId());
			category.setSite(category.getSite());
			store.setCategory(category);
		}
		else{
			store.setCategory(new Category());
		}
		store.setPage(page);
		page.setList(dao.findList(store));
		return page;
	}
	
	@Override
	public String save(Store entity) {
		Store store= storedao.get(entity);
		if(store==null){
			entity.setIsNewRecord(false);
			entity.preInsert();
			dao.insert(entity);
		}else{
			entity.preUpdate();
			dao.update(entity);
		}
		return "";
	}

	public List<Store> getstore( String tid ){
		return storedao.getStore(tid);
	}
	public Store get(Store store){
		return storedao.get(store);
		
	}
	public void delete(Store entity, Boolean isRe) {
		super.delete(entity);
	}
	
	public void deleteUserStore(Store entity) {
		storedao.deleteUserStore(entity);
	}
	public void MergeArticle(String originalcategoryId,String categoryId,String articleId){
		dao.MergeArticle(originalcategoryId, categoryId, articleId);
	}

	public void MergeCategory(String originalcategoryId, String categoryId) {
		dao.MergeCategory(originalcategoryId, categoryId);
	}
}
