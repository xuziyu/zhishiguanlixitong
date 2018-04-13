/**
 * 
 */
package com.yonyou.kms.modules.cms.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.yonyou.kms.common.config.Global;
import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.service.TreeService;
import com.yonyou.kms.common.utils.IdGen;
import com.yonyou.kms.modules.cms.dao.ArticleDao;
import com.yonyou.kms.modules.cms.dao.CategoryDao;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.ECategory;
import com.yonyou.kms.modules.cms.entity.RoleCategory;
import com.yonyou.kms.modules.cms.entity.Site;
import com.yonyou.kms.modules.cms.entity.UserCategory;
import com.yonyou.kms.modules.cms.utils.CmsUtils;
import com.yonyou.kms.modules.sys.dao.OfficeDao;
import com.yonyou.kms.modules.sys.dao.RoleDao;
import com.yonyou.kms.modules.sys.dao.UserDao;
import com.yonyou.kms.modules.sys.entity.Office;
import com.yonyou.kms.modules.sys.entity.Role;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.utils.UserUtils;
import com.yonyou.rmiservice.entity.RmiEntity;

/**
 * 栏目Service
 * 
 * @author hotsum
 * @version 2013-5-31
 */
@Service
@Transactional(readOnly = true)
public class CategoryService extends TreeService<CategoryDao, Category> {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	public static final String DEL_FLAG = "0";
	public static final String CACHE_CATEGORY_LIST = "categoryList";
	private Map<String,String> orgmap=new HashMap<String, String>();
	public List<UserCategory> uclist=new ArrayList<UserCategory>();
	private String listname="";
	private String likename="";
	@Autowired
	private CategoryDao categorydao;
	private Category entity = new Category();
	@Autowired
	private ArticleDao articleDao;
	@Autowired
	private ArticleCountService articlecountService;
	@Autowired
	private OfficeDao officeDao;

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;

	/**
	 * @yangshw6 取得最顶级分类 天禾农资集团的信息
	 * @see com.yonyou.kms.modules.sys.service.OfficeService.findTopOffice(User)
	 */
	@Deprecated
	@Transactional(readOnly = false)
	public Office findTopOffice() {
		Office office = new Office();
		office = officeDao.getTopOffice();
		return office;
	}

	/**
	 * @yangshw6
	 * 取得知识各级分类最大的排序 sort
	 * type:1 库级分类 2:一级分类 3:二级分类
	 * parentId 如果有父类分类的话
	 */
	public String getSort(int type,String parentId){
		//String sort=null;
//		switch(type){
//		case 1:sort=categorydao.getMaxSort();break;
//		case 2:sort=categorydao.getMaxSortInFirst(parentId);break;
//		case 3:sort=categorydao.getMaxSortInSecond(parentId);break;
//		//默认为50
//		default :sort="50";
//		}
		
		String sort=categorydao.getMaxSortInFirst(parentId);
		if(StringUtils.isBlank(sort)){
			
			sort="50";
		}
		
		return sort;
	}
	/**
	 * 
	 * @param isCurrentSite
	 *            true
	 * @param module
	 *            article
	 * @param place
	 *            表示在哪里调这个服务 参数是sys/user
	 * @return
	 */
	public List<Category> findByUser(boolean isCurrentSite, String module,String place,User...users){
		List<String> categoryAll=Lists.newArrayList();
		User user=null;
		if(users==null||users.length<=0){
			user = UserUtils.getUser();
		}else{
			user = users[0];
		}
		// List<Category> list =
		// (List<Category>)UserUtils.getCache(CACHE_CATEGORY_LIST);
		List<Category> list = null;
		List<String> tempList = Lists.newArrayList();
		if (list == null) {
			Category category = new Category();
			category.setOffice(new Office());

			category.setSite(new Site());
			category.setParent(new Category());

			if (CATEGORY_PLACE_SYS.equals(place)) {
				category.getSqlMap().put("dsf", dataScopeFilter(user, "o", ""));
				list = dao.findList(category);
				//除了原有的权限分类,再加上那些被分配的权限
				List<Category> cs=Lists.newArrayList();
				cs=findByUser2(true, "article", CATEGORY_PLACE_SYS,user);
				list.addAll(cs);
			}else{
				try {
					category.getSqlMap().put("dsf", dataScopeFilter1(user, "o", ""));
				} catch (Exception e) {
					e.printStackTrace();
				}
				list = dao.findList(category);
				// add by luqibao 前端除去具有角色的分类 2015-12-30
				boolean flag = false;
				List<Role> roles = user.getRoleList();
				for (Role r : roles) {
					if (Role.DATA_SCOPE_ALL.equals(r.getDataScope())) {
						flag = true;
					}
				}
				// 如果是系统管理员或者是超级管理员 还是走原来的逻辑
				if (!user.isAdmin()) {
					if (!flag) {
						// 将那些特殊的分类全部移除掉
						List<Category> categorys = dao.findCategoryNotSimple();
						if (categorys != null) {
							list.removeAll(categorys);
						}
						// 再上本人具有特殊权限的分类
						categorys = dao.findCategoryRoleByUser(user);
						for (Category c : categorys) {
							if (!list.contains(c)) {
								list.add(c);
							}
						}
					}

				}

				// end
			}

			for (Category c : list) {
				tempList.add(c.getId());
			}
			// add by luqibao 修改代码 能够显示
			Set<String> set = new TreeSet<String>();
			for (Category category1 : list) {
				String parentIds[] = category1.getParentIds().split(",");
				for (String str : parentIds) {
					if (!"0".equals(str)) {
						set.add(str);
					}
				}
			}
			// test start
			// 查询用户所具有的特殊权限的分类
			// List<Category>
			// categorys=categorydao.findCategoryRoleByUser(UserUtils.getUser());
			// for(Category c:categorys){
			// System.out.println(c.getName());
			// }
			// //查询所有的特殊分类权限
			// List<Category> categorys1=categorydao.findCategoryNotSimple();
			// for(Category c:categorys1){
			// System.out.println(c.getName());
			// }
			// test end
			// CmsUtils.hasPermission(null);
			/*******************************************************************
			 * categoryAll=findCategoryIdByUser1(user); for(String
			 * str:categoryAll){ set.add(str); }
			 ******************************************************************/
			categoryAll.clear();
			for (String str : set) {
				categoryAll.add(str);
			}
			List<Category> list1 = null;
			// 判空操作 huangmj 2015 11 23 satrt
			if (categoryAll.size() > 0) {
				list1 = categorydao.findAllByIds(categoryAll);
			}
			// 判空操作 huangmj 2015 11 23 end
			if (list1 != null && list1.size() > 0) {
				list.addAll(list1);
			}
			List<Category> temp = Lists.newArrayList();
			for (Category c : list) {
				if (!temp.contains(c)) {
					temp.add(c);
				}
			}
			list = temp;
			for (int i = 0; i < list.size(); i++) {
				if (tempList.contains(list.get(i).getId())) {
					list.get(i).setIsAdmin("1");
				} else {
					list.get(i).setIsAdmin("0");
				}
			}
			// end
			// 将没有父节点的节点，找到父节点
			Set<String> parentIdSet = Sets.newHashSet();
			for (Category e : list) {
				if (e.getParent() != null
						&& StringUtils.isNotBlank(e.getParent().getId())) {
					boolean isExistParent = false;
					for (Category e2 : list) {
						if (e.getParent().getId().equals(e2.getId())) {
							isExistParent = true;
							break;
						}
					}
					if (!isExistParent) {
						parentIdSet.add(e.getParent().getId());
					}
				}
			}
			if (parentIdSet.size() > 0) {
				// FIXME 暂且注释，用于测试
				// dc = dao.createDetachedCriteria();
				// dc.add(Restrictions.in("id", parentIdSet));
				// dc.add(Restrictions.eq("delFlag", Category.DEL_FLAG_NORMAL));
				// dc.addOrder(Order.asc("site.id")).addOrder(Order.asc("sort"));
				// list.addAll(0, dao.find(dc));
			}
		}
		if (isCurrentSite) {
			List<Category> categoryList = Lists.newArrayList();
			for (Category e : list) {
				if (Category.isRoot(e.getId())
						|| (e.getSite() != null && e.getSite().getId() != null && e
								.getSite().getId().equals(
										Site.getCurrentSiteId()))) {
					if (StringUtils.isNotEmpty(module)) {
						if (module.equals(e.getModule())
								|| "".equals(e.getModule())) {
							categoryList.add(e);
						}
					} else {
						categoryList.add(e);
					}
				}
			}
			return categoryList;
		}

		return list;
	}

	/**
	 * 表示具有权限的分类的集合 这个指的是管理权限 不是现实树结构的那些分类
	 * 
	 * @param isCurrentSite
	 *            true
	 * @param module
	 *            article
	 * @param place
	 *            CATEGORY_PLACE_SYS
	 * @return
	 */
	public List<Category> findByUser2(boolean isCurrentSite, String module,String place,User...users){
		List<String> categoryAll=Lists.newArrayList();
		User user=null;
		if(users==null||users.length<=0){
			user = UserUtils.getUser();
		}else{
			user = userDao.get(users[0]);
		}
		// List<Category> list =
		// (List<Category>)UserUtils.getCache(CACHE_CATEGORY_LIST);
		List<Category> list = null;
		List<String> tempList = Lists.newArrayList();
		if (list == null) {
			Category category = new Category();
			category.setOffice(new Office());

			category.setSite(new Site());
			category.setParent(new Category());

			if (CATEGORY_PLACE_SYS.equals(place)) {
				category.getSqlMap().put("dsf", dataScopeFilter(user, "o", ""));
				list = Lists.newArrayList();
			} else {
				category.getSqlMap()
						.put("dsf", dataScopeFilter1(user, "o", ""));
				list = dao.findList(category);
				List<Category> categorys=getCategoryByUser(user);
				for(Category c:categorys){
					if(!list.contains(c)){
						list.add(c);
					}
				}

			}
			// list = dao.findList(category);
			// for(Category c:list){
			// System.out.println(c.getName());
			// }
			// 全部的

			// add by luqibao 这里单独处理后台管理新增
			try {
				if (CATEGORY_PLACE_SYS.equals(place)) {
					list = categorydao.findCategorysByUser(user);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 判断是否是超级管理员 如果是的话 走原来的体系
			boolean flag = false;
			List<Role> roles = user.getRoleList();
			for (Role r : roles) {
				if (Role.DATA_SCOPE_ALL.equals(r.getDataScope())) {
					flag = true;
				}
			}

			// 如果是系统管理员 还是走原来的逻辑
			if (user.isAdmin() | flag) {
				category.getSqlMap().put("dsf", dataScopeFilter(user, "o", ""));
				list = dao.findList(category);
			}

			// end 这里单独处理后台管理新增

			for (Category c : list) {
				tempList.add(c.getId());
			}
			// add by luqibao 修改代码 能够显示
			Set<String> set = new TreeSet<String>();
			for (Category category1 : list) {
				String parentIds[] = category1.getParentIds().split(",");
				for (String str : parentIds) {
					if (!"0".equals(str)) {
						set.add(str);
					}
				}
			}

			/*******************************************************************
			 * categoryAll=findCategoryIdByUser1(user); for(String
			 * str:categoryAll){ set.add(str); }
			 ******************************************************************/
			categoryAll.clear();
			for (String str : set) {
				categoryAll.add(str);
			}
			List<Category> list1 = null;
			// 判空操作 huangmj 2015 11 23 satrt
			if (categoryAll.size() > 0) {
				list1 = categorydao.findAllByIds(categoryAll);
			}
			// 判空操作 huangmj 2015 11 23 end
			if (list1 != null && list1.size() > 0) {
				list.addAll(list1);
			}
			List<Category> temp = Lists.newArrayList();
			for (Category c : list) {
				if (!temp.contains(c)) {
					temp.add(c);
				}
			}
			list = temp;
			for (int i = 0; i < list.size(); i++) {
				if (tempList.contains(list.get(i).getId())) {
					list.get(i).setIsAdmin("1");
				} else {
					list.get(i).setIsAdmin("0");
				}
			}
			// end
			// 将没有父节点的节点，找到父节点
			Set<String> parentIdSet = Sets.newHashSet();
			for (Category e : list) {
				if (e.getParent() != null
						&& StringUtils.isNotBlank(e.getParent().getId())) {
					boolean isExistParent = false;
					for (Category e2 : list) {
						if (e.getParent().getId().equals(e2.getId())) {
							isExistParent = true;
							break;
						}
					}
					if (!isExistParent) {
						parentIdSet.add(e.getParent().getId());
					}
				}
			}
			if (parentIdSet.size() > 0) {
				// FIXME 暂且注释，用于测试
				// dc = dao.createDetachedCriteria();
				// dc.add(Restrictions.in("id", parentIdSet));
				// dc.add(Restrictions.eq("delFlag", Category.DEL_FLAG_NORMAL));
				// dc.addOrder(Order.asc("site.id")).addOrder(Order.asc("sort"));
				// list.addAll(0, dao.find(dc));
			}
		}
		if (isCurrentSite) {
			List<Category> categoryList = Lists.newArrayList();
			for (Category e : list) {
				if (Category.isRoot(e.getId())
						|| (e.getSite() != null && e.getSite().getId() != null && e
								.getSite().getId().equals(
										Site.getCurrentSiteId()))) {
					if (StringUtils.isNotEmpty(module)) {
						if (module.equals(e.getModule())
								|| "".equals(e.getModule())) {
							categoryList.add(e);
						}
					} else {
						categoryList.add(e);
					}
				}
			}
			return categoryList;
		}

		return list;
	}

	public List<Category> findByParentId(String parentId, String siteId) {
		Category parent = new Category();
		parent.setId(parentId);
		entity.setParent(parent);
		Site site = new Site();
		site.setId(siteId);
		entity.setSite(site);
		return dao.findByParentIdAndSiteId(entity);
	}

	public Page<Category> find(Page<Category> page, Category category) {
		// DetachedCriteria dc = dao.createDetachedCriteria();
		// if (category.getSite()!=null &&
		// StringUtils.isNotBlank(category.getSite().getId())){
		// dc.createAlias("site", "site");
		// dc.add(Restrictions.eq("site.id", category.getSite().getId()));
		// }
		// if (category.getParent()!=null &&
		// StringUtils.isNotBlank(category.getParent().getId())){
		// dc.createAlias("parent", "parent");
		// dc.add(Restrictions.eq("parent.id", category.getParent().getId()));
		// }
		// if (StringUtils.isNotBlank(category.getInMenu()) &&
		// Category.SHOW.equals(category.getInMenu())){
		// dc.add(Restrictions.eq("inMenu", category.getInMenu()));
		// }
		// dc.add(Restrictions.eq(Category.FIELD_DEL_FLAG,
		// Category.DEL_FLAG_NORMAL));
		// dc.addOrder(Order.asc("site.id")).addOrder(Order.asc("sort"));
		// return dao.find(page, dc);
		// page.setSpringPage(dao.findByParentId(category.getParent().getId(),
		// page.getSpringPage()));
		// return page;
		category.setPage(page);
		category.setInMenu(Global.SHOW);
		page.setList(dao.findModule(category));
		return page;
	}

	@Override
	@Transactional(readOnly = false)
	public String save(Category category) {
		String id = "";
		if (category.getParent() == null
				|| org.apache.commons.lang3.StringUtils.isBlank(category
						.getParentId()) || "0".equals(category.getParentId())
				|| "1".equals(category.getParentId())) {
			/*
			 * if(categorydao.getMaxImage()!=null){ String Maximage=new
			 * String(categorydao.getMaxImage());
			 * System.out.println("------------Maximage------"+Maximage);
			 * Integer image=Integer.valueOf(Maximage);
			 * System.out.println("------------image------"+image);
			 * category.setImage(String.valueOf((image+(int)Math.random()*9)%10)); }
			 */
			// 取一个3-9的随机数,在新建知识库category时,赋给image字段,测试可用了.(默认的0,1,2分配给技术,作物,岗位)
			// int image=0;
			// //System.out.println("------------image------"+image);
			// category.setImage(String.valueOf(image));
		}

		category.setSite(new Site(Site.getCurrentSiteId()));
		if (StringUtils.isNotBlank(category.getViewConfig())) {
			category.setViewConfig(StringEscapeUtils.unescapeHtml4(category
					.getViewConfig()));
		}
		id = super.save(category);
		UserUtils.removeCache(CACHE_CATEGORY_LIST);
		CmsUtils.removeCache("mainNavList_" + category.getSite().getId());
		return id;
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(Category category) {
		super.delete(category);
		UserUtils.removeCache(CACHE_CATEGORY_LIST);
		CmsUtils.removeCache("mainNavList_" + category.getSite().getId());
	}

	/**
	 * 通过编号获取栏目列表
	 */
	public List<Category> findByIds(String ids) {
		List<Category> list = Lists.newArrayList();
		String[] idss = StringUtils.split(ids, ",");
		if (idss.length > 0) {
			// List<Category> l = dao.findByIdIn(idss);
			// for (String id : idss){
			// for (Category e : l){
			// if (e.getId().equals(id)){
			// list.add(e);
			// break;
			// }
			// }
			// }
			for (String id : idss) {
				Category e = dao.get(id);
				if (null != e) {
					// System.out.println("e.id:"+e.getId()+",e.name:"+e.getName());
					list.add(e);
				}
				// list.add(dao.get(id));

			}
		}
		return list;
	}

	// add by luqibao 重新findByIds,之前的性能有问题
	public List<Category> findByIds(List<String> ids) {
		List<Category> list = Lists.newArrayList();
		if (ids == null) {
			ids = Lists.newArrayList();
		}
		list = categorydao.findAllByIds(ids);
		if (list == null) {
			list = Lists.newArrayList();
		}
		return list;
	}

	// end
	// add by luqibao
	// 获取到当前用户具有的查询权限的知识分类id集合
	/**
	 * 
	 * @param
	 * @return 当前用户具有查看权限的知识分类的id
	 */
	public List<String> findCategoryIdByUser(User user) {
		List<String> classifys = null;
		// 如果传入空值 那么就使用当前用户
		if (user == null) {
			user = UserUtils.getUser();
		}
		// 如果当前用户获取不到 那么就返回空值
		if (user.getId() == null) {
			return Lists.newArrayList();
		}
		
		//classifys=(List<String>) UserUtils.getCache("classifysCache");
		
		
//		if(classifys!=null){
//			return classifys;
//		}
			classifys=Lists.newArrayList();
			List<Category> categorys=findByUser(true,"article",CATEGORY_PLACE_USER,user);
			if(categorys!=null&&categorys.size()>0){
				for(Category c:categorys){
					if("2".equals(c.getId()) || "3".equals(c.getId()) || "4".equals(c.getId())){
						continue;
					}else{
						classifys.add(c.getId());
					}
				}
			}
			//user.getSqlMap().put("dsf", dataScopeFilter(user.getCurrentUser(), "o", "u"));
			//classifys=categorydao.findCategoryIds(user);
			if(classifys==null){
				return Lists.newArrayList();
			}
			//UserUtils.putCache("classifysCache",classifys);
		return classifys;
	}

	/**
	 * 
	 * @param
	 * @return 当前用户具有查询权限的知识分类的id
	 */
	@SuppressWarnings("unused")
	private List<String> findCategoryIdByUser1(User user) {
		List<String> classifys = null;
		// 如果传入空值 那么就使用当前用户
		if (user == null) {
			user = UserUtils.getUser();
		}
		// 如果当前用户获取不到 那么就返回空值
		if (user.getId() == null) {
			return Lists.newArrayList();
		}
		
		//classifys=(List<String>) UserUtils.getCache("classifysCache1");
		
		
		if(classifys!=null){
			return classifys;
		}

		classifys = Lists.newArrayList();
		user.getSqlMap().put("dsf",
				dataScopeFilter(user.getCurrentUser(), "o", "u"));
		classifys = categorydao.findCategoryIds(user);
		if (classifys == null) {
			return Lists.newArrayList();
		}
		UserUtils.putCache("classifysCache1", classifys);
		return classifys;
	}

	// 根据传入进来的用户 查询该用户所管理的知识库id集合
	// 如果该用户不是知识库分类管理员，返回null
	/**
	 * 
	 * 
	 * @param user
	 *            当前的用户
	 * @return 管理的知识库id
	 */
	public List<String> findCategoryAsAdmin(User user) {
		return null;
	}

	// 根据知识库或者知识分类的id 获取下级
	public List<Category> getChildsByCategoryId(String id) {
		List<Category> list = Lists.newArrayList();
		Category category = new Category();
		category.setId(id);
		if (categorydao.get(category) != null) {
			list = categorydao.getChildsByCategoryId(id);
		}
		;
		return list != null ? list : new ArrayList<Category>();
	}

	public List<Category> findAllCategory(Category category) {
		List<Category> list = Lists.newArrayList();
		if (category == null || category.getSite() == null) {
			category = new Category();
			Site site = new Site();
			Category parent = new Category();
			category.setParent(parent);
			category.setSite(site);
		}
		category.setDelFlag("0");
		list = categorydao.findList(category);
		if (list == null) {
			list = Lists.newArrayList();
		}
		return list;
	}

	// end
	// 通过库id得到一级分类id
	public String getFirstCategoryIdByLibId(String libid) {
		List<String> list = Lists.newArrayList();
		if (libid != null && !"".equals(libid)) {
			list = categorydao.getChildIdByCategoryId(libid);
		}
		;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			sb.append(list.get(i) + ",");
		}
		return sb.toString();
	}

	/**
	 * yinshh3根据一级分类id获取二级分分类id集合(仿照上面)
	 * 
	 * @param 一级分类id的list集合
	 */
	public String getSecondCategoryIdByFirstId(String firstIdLists) {
		List<String> secondIdList = Lists.newArrayList();
		List<List<String>> allSecondIdList = Lists.newArrayList();
		if (firstIdLists != null && !"".equals(firstIdLists)) {
			// 单独一个一级id对应的二级id集合
			String[] strs = firstIdLists.split(",");
			for (String s : strs) {
				secondIdList = categorydao.getChildIdByCategoryId(s);
				allSecondIdList.add(secondIdList);
			}
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < allSecondIdList.size(); i++) {
			for (int j = 0; j < allSecondIdList.get(i).size(); j++) {
				sb.append(allSecondIdList.get(i).get(j) + ",");
			}
		}
		return sb.toString();
	}

	/**
	 * 
	 * @param flag,判断是库id,一级分类id,还是二级分类id的flag
	 * @return 二级分类用,分隔的String
	 */
	public String getSecondIdByFlag(String categoryids) {
		String secondids = new String();
		int flag = getCategoryFlagByID(categoryids);
		if (categoryids != null) {
			// 判断得到库id
			if (flag == 0) {
				// 库id得到一级id集合
				String firstIds = new String(
						getFirstCategoryIdByLibId(categoryids));
				secondids = getSecondCategoryIdByFirstId(firstIds);
			} else if (flag == 1) {
				// 判断得到一级id
				secondids = getSecondCategoryIdByFirstId(categoryids);
			} else {
				// 判断得到二级id
				secondids = categoryids;
			}
		}
		return secondids;
	}

	// add hefeng
	/**
	 * 通过id获得parentIds
	 */
	public String findparentIdsById(String id) {
		return dao.findparentIdsById(id);
	}

	// end

	// add by yinshh3
	/*
	 * 将传进来的categoryid进行分类获取,确保他传出去的是2级分类的结合.
	 */
	public List<String> getSecondCategoryID(List<String> categoryidlist) {
		List<String> finallist = new ArrayList<String>();
		for (int i = 0; i < categoryidlist.size(); i++) {
			Category category = new Category();
			category = categorydao.get(categoryidlist.get(i));
			// System.out.println("category.........."+JsonMapper.toJsonString(category));
			String parentids = category.getParentIds();
			// System.out.println("category..getParentIds........"+JsonMapper.toJsonString(parentids));
			if (parentids == null) {
				// System.out.println("=======parentids为空=============");
			}
			int count = 0;
			for (int j = 0; j < parentids.length(); j++) {
				if (parentids.charAt(j) == ',') {
					count++;
					// System.out.println("count........"+JsonMapper.toJsonString(count));
				}
			}
			// 如果parentids含有4个,.则确定它是底层的categoryid,就添加到最终的list中去.
			if (count == 4) {
				finallist.add(categoryidlist.get(i));
			}
		}
		return finallist;
	}

	/**
	 * 
	 * @param categoryList
	 * @return categoryMap,用户具有查询的库id的List,一级知识分类的List,二级知识分类id的List.最终装在一个大的List<Map>中
	 */
	public Map<String, Object> categoryMap(List<String> categoryList) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> libList = Lists.newArrayList();
		List<String> firList = Lists.newArrayList();
		List<String> secList = Lists.newArrayList();
		List<Category> parentids = new ArrayList<Category>();
		if (categoryList != null && categoryList.size() > 0) {
			parentids = dao.parentCategoryIds(categoryList);
		}
		for (Category category : parentids) {
			if (category.getParentIds().split(",").length == 2) {
				libList.add(category.getId());
			}
			if (category.getParentIds().split(",").length == 3) {
				firList.add(category.getId());
			}
			if (category.getParentIds().split(",").length == 4) {
				secList.add(category.getId());
			}
		}
		map.put("libList", libList);
		map.put("firList", firList);
		map.put("secList", secList);
		return map;
	}

	// 根据传入的当前用户具有的二级分类ID集合,找到对应的delflag的集合
	public List<String> getDelFlagByCategoryID(List<String> secondcategoryid) {
		List<String> DelFlagList = Lists.newArrayList();
		// 如果这个secondid集合为空,就不传入数据库查询
		if (secondcategoryid.size() == 0) {
			return DelFlagList;
		}
		DelFlagList = categorydao.getDelFlagByCategoryID(secondcategoryid);
		// 从数据库返回为空的数据,内容并不是null,用下面的进行判断.
		/*
		 * if(DelFlagList==null){
		 * System.out.println("-----DelFlagList==null---"+JsonMapper.toJsonString(DelFlagList));
		 * DelFlagList.add("0");
		 * System.out.println("-----DelFlagList==null---"+JsonMapper.toJsonString(DelFlagList));
		 *  }
		 */
		// 如果为空,添加一条非审核状态的数据.(只要不等于2就行)
		// 后来发现不用处理,他的长度等于0,在调用方法的时候,不进入循环,count就不会增加.就是初始值0
		/*
		 * if(DelFlagList.size()==0){
		 * System.out.println("-----DelFlagList.size==0的条件---"+JsonMapper.toJsonString(DelFlagList)+"----DelFlagList.size()---"+DelFlagList.size());
		 * 
		 * System.out.println("-----DelFlagList.size==0的条件---"+JsonMapper.toJsonString(DelFlagList)+"----DelFlagList.size()---"+DelFlagList.size());
		 *  }
		 */
		return DelFlagList;
	}

	// end

	// add by yinshh3
	/**
	 * 得到categoryid是库id，一级id，还是二级id
	 * 
	 * @param categoryId
	 *            传入的categoryid
	 * @return flag(0：库id;1：一级id;2：二级id;)
	 */
	public Integer getCategoryFlagByID(String categoryId) {
		int flag = 0;
		if (categoryId != null && !"".equals(categoryId)) {
			Category category = CmsUtils.getCategory(categoryId);
			if (category == null) {
				return flag;
			}
			String parentids = category.getParentIds();
			int count = 0;
			for (int i = 0; i < parentids.length(); i++) {
				if (parentids.charAt(i) == ',') {
					count++;
				}
			}
			// if(parentids.length()==4){
			// count=2;
			// }
			// if(parentids.length()>4&&parentids.length()<25){
			// count=3;
			// }
			// if(parentids.length()>53){
			// count=4;
			// }
			if (count == 2) {
				// 库id
				flag = 0;
			} else if (count == 3) {
				// 一级id
				flag = 1;
			} else {
				// 二级id
				flag = 2;
			}
		}
		return flag;
	}

	// add by luqibao
	@Transactional(readOnly = false)
	public String changeDelFlag(Article article) {
		// add by yangshw6 当知识发布,下架，弃审以后，更新数据到知识统计表里
		if (article.getDelFlag().equals("0")) {
			articlecountService.insertSingleData(article.getId());
			articleDao.updateExaminerId(article);
		}
		else if (article.getDelFlag().equals("1")||"3".equals(article.getDelFlag())) {
			articlecountService.deleteSingleData(article.getId());
			articleDao.updateExaminerId(article);
			articleDao.changeReason(article);
		}
		else if (article.getDelFlag().equals("2")) {
			articlecountService.deleteSingleData(article.getId());
			article.getExaminer().setId("");
			articleDao.updateExaminerId(article);
		}
		else{
			
		}
		// end by yangshw6
		articleDao.updateDel(article);
		String categoryId = CmsUtils.getArticlecid(article.getId());
		return categoryId == null ? "" : categoryId;
	}
	/**
	 * 获取知识分类下有可以分享文章的知识分类的id
	 * 
	 * @return
	 */
	public List<String> findCategoryIsAllowShare() {
		List<String> list = null;
		list = articleDao.findAllowShareCategoryIds();
		if (list == null) {
			list = Lists.newArrayList();
		}
		return list;
	}

	/**
	 * 插入管理知识分类关联关系
	 * 
	 * @param user
	 * @param category
	 * @return
	 */
	@Transactional
	public boolean insertUserCategory(List<User> users, Category category) {

		// c.setId("687c121212af491483ffc11935324855");
		// end
		// 删除 start
		// categorydao.deleteUserCategory(user);
		// end
		// 增加 start
		// 因为增加的管理员不可能很多 所有for循环进行插入
		try {
			for (User user : users) {
				UserCategory uc = new UserCategory();
				uc.setCategory(category);
				uc.setUser(user);
				uc.preInsert();
				categorydao.insertUserCategory(uc);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	
	
	
	/**
	 * 删除管理用户
	 * 
	 * @param user
	 * @return
	 */
	@Transactional
	public boolean deleteUserCategory(User user, Category c) {
		if (user == null | user.getId() == null) {
			return false;
		}
		try {
			UserCategory uc = new UserCategory();
			uc.setUser(user);
			uc.setCategory(c);
			categorydao.deleteUserCategory(uc);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 获取管理这个分类的所有人员
	 * 
	 * @param category
	 * @return
	 */
	@Transactional
	public List<User> getUserByCategory(Category category) {
		Category c = categorydao.get(category);
		List<User> users = Lists.newArrayList();
		if (c == null) {
			return users;
		}

		if (category == null) {
			return users;
		}
		users = userDao.findUsersByCategory(category);

		// for(User u:users){
		// System.out.println(u.getName());
		// }
		return users;
	}

	/**
	 * 获取用户所管理的知识分类
	 * 
	 * @return
	 */
	@Transactional
	public List<Category> getCategoryByUser(User user) {
		List<Category> list = Lists.newArrayList();
		list = categorydao.findCategorysByUser(user);
		if (list == null) {
			return Lists.newArrayList();
		}
		return list;
	}

	/***************************************************************************
	 * 插入sys_role_category记录
	 */
	@Transactional
	public boolean insertRoleCategory(List<Role> roles, Category category) {

		try {
			for (Role role : roles) {
				RoleCategory rc = new RoleCategory();
				rc.setRole(role);
				rc.setCategory(category);
				categorydao.insertRoleCategory(rc);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 删除sys_role_category表中的记录
	 * 
	 * @param role
	 * @param c
	 * @return
	 */
	@Transactional
	public boolean deleteRoleCategory(Role role, Category c) {
		if (role == null | role.getId() == null) {
			return false;
		}
		try {
			RoleCategory rc = new RoleCategory();
			rc.setRole(role);
			rc.setCategory(c);
			categorydao.deleteRoleCategory(rc);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public List<Category> findCategoryRole() {
		List<Category> list = categorydao.findCategoryRole();
		return list;
	}

	/**
	 * 获取这个分类下面的所有角色
	 * 
	 * @param category
	 * @return
	 */
	@Transactional
	public List<Role> getRoleByCategory(Category category) {
		Category c = categorydao.get(category);
		List<Role> roles = Lists.newArrayList();
		if (c == null) {
			return roles;
		}

		if (category == null) {
			return roles;
		}
		roles = roleDao.findRoleByCategory(category);

		// for(User u:users){
		// System.out.println(u.getName());
		// }
		return roles;
	}

	// 查询所有的角色 除去系统的
	public List<Role> findAllRole() {
		List<Role> tempRole = Lists.newArrayList();
		Role r = new Role();
		r.setUseable("1");
		r.setDelFlag("0");
		List<Role> roles = roleDao.findList(r);
		for (Role r1 : roles) {
			if (r1.getDataScope() != null) {
				if (!r1.getSysData().equals("1") && !r1.getId().equals("6")) {
					tempRole.add(r1);
				}
			}
		}
		return tempRole;
	}

	// end
	
	
	/**
	 * 去除掉excel中自己重复的数据
	 * 
	 * @author luochp3
	 * @param Ecategorylist 导入文件中的数据组
	 * @return
	 */
	public List<ECategory> deletecho(List<ECategory> Ecategorylist){
		List<String> stringList=Lists.newArrayList();
		List<ECategory> ecList=Lists.newArrayList();
		for(int i=0;i<Ecategorylist.size();i++){
			if(!StringUtils.isBlank(Ecategorylist.get(i).getFirstCategory())){
				String name=Ecategorylist.get(i).getFirstCategory()+",";
				Ecategorylist.get(i).setIndex(1);//设置层级数
				if(!StringUtils.isBlank(Ecategorylist.get(i).getSecondCategory())){
					name=name+Ecategorylist.get(i).getSecondCategory()+",";
					Ecategorylist.get(i).setIndex(2);//设置层级数
					if(!StringUtils.isBlank(Ecategorylist.get(i).getThirdCategroy())){
						name=name+Ecategorylist.get(i).getThirdCategroy()+",";
						Ecategorylist.get(i).setIndex(3);//设置层级数
					}
				}
				if(!stringList.contains(name)){
					stringList.add(name);
					ecList.add(Ecategorylist.get(i));
				}else{
					likename=likename+","+Ecategorylist.get(i).getNum();
				}
			}
		}
		return ecList;
	}
	
	/**
	 * 封装execl中要导入的数据
	 * 
	 * @author luochp3
	 * @param ec    一条execl中的数据
	 * @param pid   该条数据的父id
	 * @param pids  该条数据的父ids
	 * @param name  知识分类名称
	 * @return
	 */
	public Category setCategoryList(ECategory ec,String pid,String pids,String name){
		 //判断存储所属机构的map是否存在
		if(orgmap.isEmpty()){
			SetOrgMap();
		}
		UserCategory uc=new UserCategory();
		User createBy=UserUtils.getUser();
		User updateBy=createBy;
		Date createDate=new Date();
		Date updateDate=createDate;
		Category category = new Category();
		if(ec.getAllowComment().trim().equals("是")){
			category.setAllowComment("1");
		}else {
			category.setAllowComment("0");
		}
		category.setId(IdGen.uuid());
		category.setName(name);
		category.setParent(new Category(pid));
		category.setParentIds(pids);
		
		category.setCreateBy(createBy);
		category.setUpdateBy(updateBy);
		category.setCreateDate(createDate);
		category.setUpdateDate(updateDate);
		category.setSite(new Site(Site.getCurrentSiteId()));
		
		category.setKeywords(ec.getKeywords()!=null ? ec.getKeywords() :"");
		category.setSort(ec.getSort()!=null ? Integer.valueOf(ec.getSort()) : 0);
		category.setOffice(new Office(orgmap.get(ec.getOffice())));
		if(pids.split(",").length == 4){
			category.setModule("article");
			uc.setId(IdGen.uuid());
			uc.setUser(createBy);
			uc.setCategory(category);
			uclist.add(uc);
		}
		
		return category;
		
	}
	/**
	 * 去除掉excel中和数据库中重复的数据
	 * 
	 * @author luochp3
	 * @param Ecategorylist excel中的数据组
	 * @param index 知识分类层级
	 * @return
	 */
	public List<Category> deletechodata(List<ECategory> Ecategorylist,int index){
		//获取未被删除的知识分类列表
		List<Category> categoryList = categorydao.findALL();
		List<Category> firstList=Lists.newArrayList();
		List<Category> secondList=Lists.newArrayList();
		List<Category> thirdList=Lists.newArrayList();
		List<Category> result=Lists.newArrayList();
		Category cg=null;
		//分出第一级,第二级,第三级
		for(Category c:categoryList){
			if(c.getParentIds().split(",").length == 2){
				firstList.add(c);
			}
			if(c.getParentIds().split(",").length == 3){
				secondList.add(c);
			}
			if(c.getParentIds().split(",").length == 4){
				thirdList.add(c);
			}
		}
		
		//对比数据
		switch(index){
		case 1:
			for(ECategory ec:Ecategorylist){
				Map<String,String> map=serchdata("1",ec.getFirstCategory(),firstList);
				if(map.get("flag").equals("N")){
					cg=setCategoryList(ec,"1","0,1,",ec.getFirstCategory());
					result.add(cg);
				}else{
					likename=likename+","+ec.getNum();
				}
			}
			break;
		case 2:
			for(ECategory ec:Ecategorylist){
				Map<String,String> map1=serchdata("1",ec.getFirstCategory(),firstList);
				if(!map1.get("flag").equals("N")){
					Map<String,String> map2=serchdata(map1.get("id"),ec.getSecondCategory(),secondList);
					if(map2.get("flag").equals("N")){
						cg=setCategoryList(ec,map1.get("id"),map1.get("pids")+map1.get("id")+",",ec.getSecondCategory());
						result.add(cg);
					}else{
						likename=likename+","+ec.getNum();
					}
				}else{
					listname=listname+","+ec.getNum();
				}
			}
			break;
		case 3:
			for(ECategory ec:Ecategorylist){
				Map<String,String> map1=serchdata("1",ec.getFirstCategory(),firstList);
				if(!map1.get("flag").equals("N")){
					Map<String,String> map2=serchdata(map1.get("id"),ec.getSecondCategory(),secondList);
					if(!map2.get("flag").equals("N")){
						Map<String,String> map3=serchdata(map2.get("id"),ec.getThirdCategroy(),thirdList);
						if(map3.get("flag").equals("N")){
							cg=setCategoryList(ec,map2.get("id"),map2.get("pids")+map2.get("id")+",",ec.getThirdCategroy());
							result.add(cg);
						}else{
							likename=likename+","+ec.getNum();
						}
					}else{
						listname=listname+","+ec.getNum();
					}
				}else{
					listname=listname+","+ec.getNum();
				}
			}
			break;
			default:break;
		}
		
		
		return result;
		
	}
	
	/**
	 * 将所有的code和部门ID用map存储.
	 * @author luochp3
	 */
	public void SetOrgMap(){
		List<Office> officeList=officeDao.findAll();
		orgmap.clear();
		for(Office o:officeList){
			orgmap.put(o.getCode(), o.getId());
		}
	}
	/**
	 * 查找相同的分类名称
	 * 
	 * @author luochp3
	 * @param pid            父id
	 * @param name           excel中知识分类名称
	 * @param Categorylist   数据库中的数据
	 * @return
	 */
	public Map<String,String> serchdata(String pid,String name,List<Category> Categorylist){
		Map<String,String> map=new HashMap<String, String>();
		//匹配分类名称
		for(Category c:Categorylist){
			if(c.getName().trim().equals(name)){
				if(c.getParentId().equals(pid)){
					map.put("flag", "Y");
					map.put("id", c.getId());
					map.put("pid", c.getParentId());
					map.put("pids", c.getParentIds());
					return map;
				}
			}
		}
		map.put("flag", "N");
		return map;
		
	}
	/**
	 * 过滤要插入到数据库的集合
	 * 
	 * @author luochp3
	 * @param Ecategorylist 导入文件中的数据组
	 * @return
	 */
	public JSONObject getlist(List<ECategory> Ecategorylist) {
		
		
		JSONObject json=new JSONObject();
		listname="";
		likename="";
    	String flag="";
		int index=0;
		int num=Ecategorylist.size();
		//去除掉excel中自己重复的数据
		Ecategorylist=deletecho(Ecategorylist);
		//对excel中数据分三类
		List<ECategory> oneEcategory=new ArrayList<ECategory>();
		List<ECategory> twoEcategory=new ArrayList<ECategory>();
		List<ECategory> threeEcategory=new ArrayList<ECategory>();
		for(ECategory ec:Ecategorylist){
			if(ec.getIndex()==1){
				oneEcategory.add(ec);
			}else if(ec.getIndex()==2){
				twoEcategory.add(ec);
			}else if(ec.getIndex()==3){
				threeEcategory.add(ec);
			}
		}
		//清空数据
		uclist.clear();
		//去除掉excel中和数据库中重复的数据
		List<Category> CategoryList=deletechodata(oneEcategory,1);
		
		//改变序号.
		setCategorySort(1,CategoryList);
		index=index+CategoryList.size();
		//批量插入数据
		flag=batchInsert(CategoryList,uclist);
		if(flag.equals("succeed")){
			CategoryList.clear();
			uclist.clear();
			CategoryList=deletechodata(twoEcategory,2);
			setCategorySort(2,CategoryList);
			index=index+CategoryList.size();
			flag=batchInsert(CategoryList,uclist);
			if(flag.equals("succeed")){
				CategoryList.clear();
				uclist.clear();
				CategoryList=deletechodata(threeEcategory,3);
				setCategorySort(3,CategoryList);
				index=index+CategoryList.size();
				flag=batchInsert(CategoryList,uclist);
				if(flag.equals("succeed")){
					json.put("flag", "导入成功");
		    		json.put("msg", "成功导入"+index+"条数据,有"+(num-index)+"条数据存在冲突");
		    		if(num-index == 0){
		    			json.put("why", "");
		    		}else{
		    			json.put("why", "数据重复或没按要求填写数据");
		    		}
		    		
		    		listname=listname!=""?listname.substring(1):"";
		    		likename=likename!=""?likename.substring(1):"";
		    		json.put("list", listname);
		    		json.put("likename", likename);
				}else{
					json.put("flag", "导入失败");
		    		json.put("msg", "导入三级知识分类失败");
		    		json.put("why", "未知");
		    		json.put("list", "");
		    		json.put("likename", "");
				}
			}else{
				json.put("flag", "导入失败");
	    		json.put("msg", "导入二级知识分类失败");
	    		json.put("why", "未知");
	    		json.put("list", "");
	    		json.put("likename", "");
			}
		}else{
			json.put("flag", "导入失败");
    		json.put("msg", "导入一级知识分类失败");
    		json.put("why", "未知");
    		json.put("list", "");
    		json.put("likename", "");
		}
		return json;
		
	}
	
	// 对知识分类插入序号.
	public void setCategorySort(int CategorySize,List<Category> categoryList){
		//int sort = 0;	
		
//		switch(CategorySize){
//		case 1:sort=Integer.valueOf(categorydao.getMaxSort());break;
//		case 2:sort=Integer.valueOf(categorydao.getMaxSortInFirst());break;
//		case 3:sort=Integer.valueOf(categorydao.getMaxSortInSecond());break;
//		default:break;
//		}
		
		//add by luqibao
		//只要查询出分类序号最大值就OK了
		int sort=Integer.valueOf(categorydao.getMaxSort());
				
		for(int i = 0;i<categoryList.size();i++){
			categoryList.get(i).setSort(sort+i+1);
		}
	}
	/**
	 * 
	 * @param categoryList
	 * @param category
	 * @return 从给入的List找到传入category的子category
	 */
	public List<Category> getchildCategory(List<Category> categoryList,Category category){
		List<Category> newList=Lists.newArrayList();
		for(Category c:categoryList){
			if(c.getParentId().equals(category.getId())){
				newList.add(c);
			}
		}
		return newList;
	};
	
	@Transactional
	public String batchInsert(List<Category> category,List<UserCategory> uclist){
		String json="succeed";
		if(category.size()>0){
		    try{
				categorydao.batchInsert(category);
				if(uclist.size()>0){
					categorydao.batchinsertUserCategory(uclist);
				}
			}catch(Exception e){
				return json="fail";
			}
		}
		return json;
	}
	/**
	 * @yangshw6
	 * 取出关于圈民知识(app)的知识分类
	 * 
	 */
	public List<Category> getAppCategory(){
		return categorydao.getAppCategory();
	}
	
	/**
	 * @yangshw6
	 * 批量插入用户与分类的关联关系 
	 * 
	 */
	@Transactional
	public boolean batchInsertUserCategory(List<String> categorylist,User user){
		UserCategory u=null;
		Category ca=null;
		List<UserCategory>   uc=new ArrayList<UserCategory>();
		if(/*categorylist==null&&*/categorylist.size()==0){
			//未选分类节点
			u=new UserCategory();
			u.setUser(user);
			categorydao.deleteUserCategory(u);
			return false;
		} else{
			//选了分类节点
			u=new UserCategory();
			u.setUser(user);
			categorydao.deleteUserCategory(u);
			for(int i=0;i<categorylist.size();i++){
				u=new UserCategory();
				ca=new Category(categorylist.get(i));
				u.preInsert();
				u.setCategory(ca);
				u.setUser(user);
				uc.add(u);
			}
			categorydao.batchinsertUserCategory(uc);
			return true;
		}
	}
	/**
	 * 获取用户(id)下 sys_user_category中的数据
	 * 
	 */
	
	public List<Category> getUserCategoryByUser(User user){
		List<Category> list=new ArrayList<Category>();
		list=categorydao.getUserCategoryByUser(user);
		return list;
	}
	
	/**
	 * 查找指定用户具有管理权限的分类列表
	 */
	public List<Category> findCategoryCanAudit(User user){
		
		if(user==null||user.getId()==null){
			throw new RuntimeException("用户不能为空");
		}
		//1.管理的所有知识的Id
		List<String> articleIds = articleDao.findArticleNeedAuditByUserId(user.getId());
		//2.根据这些id查找他们的归属分类
		// ①查找所有的知识和分类
		List<Article> articles = articleDao.findListNoCondition();
		Category c=new Category();
		Site s=new Site();
		c.setSite(s);
		c.setParent(c);
		Map<String, Category> map2category = map2category(categorydao.findList(c));
		// ②筛选
		List<Category> categorys=Lists.newArrayList();
		if(articles!=null&&articles.size()>0&&articleIds!=null){
			for(Article article:articles){
				if(articleIds.contains(article.getId())){
					categorys.add(map2category.get(article.getCategory().getId()));
				}
			}
		}
		//3 将父级的分类都查找到
		warpIfNecessary(categorys);
		return categorys;
	}
	/**
	 * 帮助分类找到父级  构成完整的树
	 * @param categorys
	 */
	private void warpIfNecessary(List<Category> categorys){
		if(categorys==null||categorys.size()<=0)return;
		Set<String> ids=Sets.newTreeSet(); //防止Id重复
		Category c=new Category();
		Site s=new Site();
		c.setSite(s);
		c.setParent(c);
		Map<String, Category> map2category = map2category(categorydao.findList(c));
		
		//将本分类的ID和族谱id都加到ids中 
		for(Category category:categorys){
			if(category==null)continue;
			ids.add(category.getId());
			if(category.getParentIds()!=null){
				ids.addAll(Arrays.asList(category.getParentIds().split(",")));
			}
		}
		
		categorys.clear();
		for(String id:ids){
			if(map2category.get(id)!=null){
				categorys.add(map2category.get(id));
			}
		}
		
	}
	
	private Map<String,Category> map2category(List<Category> categorys){
		Map<String,Category> maps=Maps.newHashMap();
		if(categorys!=null&&categorys.size()>=0){
			for(Category category:categorys){
				maps.put(category.getId(), category);
			}
		}
		return maps;
	}
	
	/**
	 * 查询所有知识库中的三级分类
	 * @return
	 */
	public List<RmiEntity> queryAll() {
		List<RmiEntity> list = new ArrayList<>();
		List<Category> lists = categorydao.queryAll();
		if (lists.size() > 0) {
			for (Category category : lists) {
				if(category.getParentIds().split(",").length >=4){
					RmiEntity entity = new RmiEntity();
					entity.setId(category.getId());
					entity.setName(category.getName());
					list.add(entity);
				}
			}
		}
		return list;
	}
	
}
