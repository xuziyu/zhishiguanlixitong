/**
 * 
 */
package com.yonyou.kms.modules.cms.dao;

import java.util.List;
import java.util.Map;

import com.yonyou.kms.common.persistence.TreeDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.RoleCategory;
import com.yonyou.kms.modules.cms.entity.UserCategory;
import com.yonyou.kms.modules.sys.entity.Office;
import com.yonyou.kms.modules.sys.entity.User;

/**
 * 栏目DAO接口
 * @author hotsum
 * @version 2013-8-23
 */
@MyBatisDao
public interface CategoryDao extends TreeDao<Category> {
	
	public List<Category> findModule(Category category);
	
//	public List<Category> findByParentIdsLike(Category category);
//	{
//		return find("from Category where parentIds like :p1", new Parameter(parentIds));
//	}

	public List<Category> findByModule(String module);
//	{
//		return find("from Category where delFlag=:p1 and (module='' or module=:p2) order by site.id, sort", 
//				new Parameter(Category.DEL_FLAG_NORMAL, module));
//	}
	
	public List<Category> findByParentId(String parentId, String isMenu);
//	{
//		return find("from Category where delFlag=:p1 and parent.id=:p2 and inMenu=:p3 order by site.id, sort", 
//				new Parameter(Category.DEL_FLAG_NORMAL, parentId, isMenu));
//	}

	public List<Category> findByParentIdAndSiteId(Category entity);
	
	public List<Map<String, Object>> findStats(String sql);
//	{
//		return find("from Category where delFlag=:p1 and parent.id=:p2 and site.id=:p3 order by site.id, sort", 
//				new Parameter(Category.DEL_FLAG_NORMAL, parentId, siteId));
//	}
	
	//public List<Category> findByIdIn(String[] ids);
//	{
//		return find("from Category where id in (:p1)", new Parameter(new Object[]{ids}));
//	}
	//public List<Category> find(Category category);

//	@Query("select distinct c from Category c, Role r, User u where c in elements (r.categoryList) and r in elements (u.roleList)" +
//			" and c.delFlag='" + Category.DEL_FLAG_NORMAL + "' and r.delFlag='" + Role.DEL_FLAG_NORMAL + 
//			"' and u.delFlag='" + User.DEL_FLAG_NORMAL + "' and u.id=?1 or (c.user.id=?1 and c.delFlag='" + Category.DEL_FLAG_NORMAL +
//			"') order by c.site.id, c.sort")
//	public List<Category> findByUserId(Long userId);
//add by luqibao
	public List<String> findByOffice(Office office);
	
	public List<String> batchFindOffice(String param);
	//传入知识库或者知识分类的id，获取下级的category实体
	public List<Category> getChildsByCategoryId(String id);
	//传入知识库或者知识分类的id，获取下级的id
	public List<String> getChildIdByCategoryId(String id);
	//批量查询
	public List<Category> findAllByIds(List<String> list);
	
	//获取用户具有权限的知识分类的id
	public List<String> findCategoryIds(User user);
	
	//查询管理用户具有权限的知识分类 2015-12-28
	public List<Category> findCategorysByUser(User user);
	
	// 2015-12-28 修改sys_user_category表
	public void updateUserCategory(UserCategory uc);
	
	//插入   sys_user_category表
	public void insertUserCategory(UserCategory uc);
	
	//批量插入   sys_user_category表
	public void batchinsertUserCategory(List<UserCategory> uclist);
	
	//根据user 获取sys_user_category 表中关联的category
	public List<Category> getUserCategoryByUser(User user);
	
	//物理删除 sys_user_category表
	public void deleteUserCategory(UserCategory uc);
	
	//插入sys_role_category表 2015-12-30
	public void insertRoleCategory(RoleCategory rc);
	//删除管理的关系
	public void deleteRoleCategory(RoleCategory rc);
	/**
	 * 根据用户查询所具有的特殊权限分类
	 * @return
	 */
	public List<Category> findCategoryRoleByUser(User user);
	
	public List<Category> findCategoryRole();
	/***
	 * 查询特殊的分类 即管理关系表中拥有这个数据的
	 * @param id
	 * @return
	 */
	public List<Category> findCategoryNotSimple();
	
	//end
	
	//add by hefeng
	public String findparentIdsById(String id);
	//end
	
	//add by yinshh3
	public List<String> getDelFlagByCategoryID(List<String> categoryid);
	//end
	public String getMaxImage ();
	public List<Category> parentCategoryIds(List<String> childCategoryIds);
	/**
	 * @yangshw6
	 * 查询最顶级分类 天禾农资集团信息 根据parent_id进行定位 
	 */
	public Office getTopOffice();
	/**
	 * @yangshw6
	 * 取得最大的知识库序号
	 */
	public String getMaxSort();
	//查询得到没有被删除的所有信息
	public List<Category> findALL();
	//查询所有的
	public List<Category> findALLNOflag();
	//批量插入
	public void batchInsert(List<Category> category);
	
	/**
	 * @yangshw6
	 * 查出同一个分类下面的最大值
	 */
	public String getMaxSortInFirst(String parentId);
	
	
	/**
	 * @yangshw6
	 * 取得二级知识分类最大的排序
	 * @Deprecated 取出的数据是错误的
	 */
	@Deprecated
	public String getMaxSortInSecond(String parentId);
	/**
	 * 取得关于圈民知识的知识分类
	 */
	public List<Category> getAppCategory();

	public List<Category> queryAll();
}
