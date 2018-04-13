package com.yonyou.kms.modules.cms.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.itf.kms.ISynchroNC.ISynchroNCInfoLocator;
import nc.itf.kms.ISynchroNC.ISynchroNCInfoPortType;
import nc.itf.kms.ISynchroNC.OuterSystemRetVO;
import nc.vo.kms.entityN.NCLabel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.common.utils.IdGen;
import com.yonyou.kms.modules.cms.dao.LabelDao;
import com.yonyou.kms.modules.cms.entity.Label;
import com.yonyou.kms.modules.cms.utils.ContributionUtil;
import com.yonyou.kms.modules.sys.dao.StatusDao;
import com.yonyou.kms.modules.sys.entity.Status;
import com.yonyou.kms.modules.sys.utils.SynchroNCUtil;
import com.yonyou.kms.modules.sys.utils.UserUtils;

/**
 * 
 * 标签的Service
 * 
 * @author yangshiwei
 * 
 */
@Service
@Transactional(readOnly = false)
public class LabelService extends CrudService<LabelDao, Label> {
	@Autowired
	private LabelDao labeldao;

	// nc远程连接口的信息
	@Value("${nc.romateService}")
	private String romateService;
	// 注入数据源
	@Value("${nc.datasource}")
	private String dataSource;
	@Autowired
	private StatusDao statusDao;
	@Autowired
	private LabelDao labelDao;

	public static final String LABELTAB = "label";

	/***
	 * 
	 * 
	 * */
	public List<Label> FindAllLabel() {
		List<Label> labelList = Lists.newArrayList();
		Label label = new Label();
		labelList = labeldao.findList(label);
		;
		return labelList;
	};

	/*
	 * 取出标签关联知识表中的前5条数据，用于首页显示
	 */
	public List<Label> getHotLabelData() {
		return labeldao.getLabelData();
	}

	/*
	 * 显示所有的标签
	 */
	public List<Label> getAllLabel(Label la) {
		List<Label> label = new ArrayList<Label>();
		label = labeldao.getAllLabel(la);
		return label;
	}

	/**
	 * 保存更新标签关联二级知识分类
	 * 
	 * @param ids
	 *            标签集合 categoryid 二级知识分类
	 * @return true 更新成功 false 更新失败
	 */
	public boolean saveLabelByCategoryId(List<String> ids, String categoryid) {
		List<Label> persistlist = labeldao.findLabelByCategoryId(categoryid);
		List<Label> insertlist = new ArrayList<Label>();
		List<String> deletelist = new ArrayList<String>();
		Map<String, Object> map = new HashMap<String, Object>();

		// add by wuwq6
		Map<String, Object> map2 = new HashMap<String, Object>();
		List<String> oldlabelIdList = new ArrayList<String>();
		List<String> articleIdList = new ArrayList<String>();
		List<String> newlabelIdList = new ArrayList<String>();
		Map<String, Object> newMap = new HashMap<String, Object>();

		oldlabelIdList = labeldao.getAllLabeIdByCategoryId(categoryid);
		if (oldlabelIdList.size() > 0) {
			map2.put("categoryid", categoryid);
			map2.put("oldlabelIdList", oldlabelIdList);
			articleIdList = labeldao.findArticleIdByOld(map2);
			if (articleIdList.size() > 0) {
				labeldao.deleteOldRelation(map2);
			}
		} else {
			// 原来该分类下没有标签
			// Map<String, Object> map3 = new HashMap<String, Object>();
			// articleIdList = labeldao.findArticleIdByCategoryId(categoryid);
			// if (articleIdList.size() > 0) {
			// map3.put("articleIdList", articleIdList);
			// labeldao.deleteOldRelationByArticleId(map3);
			articleIdList = labeldao.findArticleIdByCategoryIdFromArticle(categoryid);

		}

		int count = 0;
		int firstNum = persistlist.size();
		int num = 0;
		for (int i = 0; i < firstNum; i++) {
			count = 0;
			String lid = persistlist.get(num).getId();
			for (int j = 0; j < ids.size(); j++) {
				String id = ids.get(j);
				if (lid.equals(id)) {
					count++;
					num++;
					ids.remove(j);
					break;
				}
			}
			if (count == 0) {
				try {
					deletelist.add(lid);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				persistlist.remove(num);
			}
		}

		if (deletelist != null && deletelist.size() > 0) {
			map.put("categoryid", categoryid);
			map.put("ids", deletelist);
			labeldao.deleteLabelByCategoryId(map);
		}
		if (persistlist == null || persistlist.size() == 0) {
			// 原来选的和现在选的完全不一样
			if (ids != null && ids.size() > 0) {
				for (int i = 0; i < ids.size(); i++) {
					Label label = new Label();
					label.preInsert();
					label.setRelationid(ids.get(i));
					label.setCategoryid(categoryid);
					insertlist.add(label);
				}
				labeldao.insertLabelByCategoryId(insertlist);
			}
			/* return true; */
		} else {
			// 原先选的标签里有这次选的
			if (ids == null || ids.size() == 0) {
				// 原来选的包含所有现在选的，所以不做操作
				/*
				 * for (Label ls : persistlist) ids.add(ls.getId());
				 */
				/*
				 * for (int i = 0; i < persistlist.size(); i++) {
				 * ids.add(persistlist.get(i).getId()); }
				 */
				// map.put("ids", ids);
				// map.put("categoryid", categoryid);
				// labeldao.deleteLabelByCategoryId(map);
				/* return true; */
			} else {
				// 原来选的包含部分现在选的
				if (ids != null && ids.size() > 0) {
					/*
					 * for (int i = 0; i < persistlist.size(); i++) {
					 * ids.add(persistlist.get(i).getId()); }
					 */
					for (int i = 0; i < ids.size(); i++) {
						Label label = new Label();
						label.preInsert();
						label.setRelationid(ids.get(i));
						label.setCategoryid(categoryid);
						insertlist.add(label);
					}
					try {
						labeldao.insertLabelByCategoryId(insertlist);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}

		// add by wuwq6
		newlabelIdList = labeldao.getAllLabeIdByCategoryId(categoryid);
		Map<String, Object> map3 = new HashMap<String, Object>();
		map3.put("newlabelIdList", newlabelIdList);
		map3.put("categoryid", categoryid);
		int doubledata = 0;

		if (newlabelIdList.size() > 0) {

			/*
			 * doubledata = (labeldao.findDoubleData1(map3)).size();
			 * if(doubledata != newlabelIdList.size()) {
			 * labeldao.deleteDoubleData1(map3); } doubledata = 0;
			 */
			if (articleIdList.size() > 0) {
				for (int i = 0; i < articleIdList.size(); i++) {
					for (int j = 0; j < newlabelIdList.size(); j++) {
						newMap.put("id", IdGen.uuid());
						newMap.put("categoryid", categoryid);
						newMap.put("articleid", articleIdList.get(i));
						newMap.put("labelid", newlabelIdList.get(j));
						doubledata = (labeldao.findDoubleData2(newMap)).size();
						if (doubledata == 0) {
							try {
								labeldao.insertNewRelation(newMap);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
				}
			}
		}

		return true;
	}

	/**
	 * 获取关联二级知识分类的标签
	 * 
	 * @param categoryid
	 *            二级知识分类
	 * @return 标签集合
	 */
	public List<Label> findLabelBycategoryId(String categoryid) {
		List<Label> label = Lists.newArrayList();
		label = labeldao.findLabelByCategoryId(categoryid);
		return label;
	}

	/*
	 * 获取当前用户增加的未审批的标签
	 */
	public List<Label> getUnexamineLabel(Label la) {
		List<Label> label = new ArrayList<Label>();
		if (la.getUserid() == null || la.getUserid().equals("")) {
			la.setUserid(UserUtils.getUser().getId());
		}
		label = labeldao.getUserUnexamineLabel(la);
		return label;
	}

	/*
	 * 
	 * 批量删除标签表中的数据
	 */
	public void batchdeleteLabelData(List<String> list) {
		labeldao.batchdeleteLabelData(list);
	}

	/*
	 * 批量查找指定标签下的关联数据
	 */
	public List<Label> batchgetLabelData(List<String> list) {
		List<Label> getlist = new ArrayList<Label>();
		getlist = labeldao.batchgetLabelData(list);
		return getlist;
	}

	/***
	 * 
	 * 批量更新标签表的数据
	 */
	public void batchUpdate(List<Label> list) {
		labeldao.batchUpdateLabelData(list);
	}

	/*
	 * 显示所有标签和对应的关联数
	 */
	// public List<Label> getLabelCountData(){
	// return labeldao.getLabelCountData();
	// }
	// 删除标签
	public void delete(Label label) {
		labeldao.delete(label);
	}

	// 修改标签
	public void update(Label label) {
		label.preUpdate();
		labeldao.update(label);
	}

	// 插入标签
	public void insert(Label label) {
		label.preInsert();
		// System.out.println("id"+label.getId());
		labeldao.insert(label);
	}

	// //取出关联知识的标签
	// public List<Label> getLabelConnArticle(String id){
	// List<Label> label=new ArrayList<Label>();
	// label=labeldao.getLabelConnArticle(id);
	// return label;
	// }
	// //插入关联知识的标签
	// public void insertLabelConnArticle(List<String> labelvalue,String
	// articleid,String categoryid){
	// StringBuffer labellist=new StringBuffer();
	// for(int i=0;i<labelvalue.size();i++){
	// labellist.append(labelvalue.get(i)+",");
	// }
	// System.out.println("插入将标签转化为列表为"+labellist);
	// Label label=new Label();
	// label.setArticleid(articleid);
	// label.setLabellist(labellist.toString());
	// label.setCategoryid(categoryid);
	// labeldao.insertLabelConnArticle(label);
	// }
	// //更新关联知识的标签
	// public void updateLabelConnArticle(List<String> labelvalue,String
	// articleid){
	// StringBuffer labellist=new StringBuffer();
	// for(int i=0;i<labelvalue.size();i++){
	// labellist.append(labelvalue.get(i)+",");
	// }
	// System.out.println("更新将标签转化为列表为"+labellist);
	// Label label=new Label();
	// label.setArticleid(articleid);
	// label.setLabellist(labellist.toString());
	// labeldao.updateLabelConnArticle(label);
	// }
	// //删除关联知识的单个标签，即为将剩下的标签更新到本标签
	// public void deleteLabelConnArticle(List<String> labelvalue,String
	// articleid){
	// StringBuffer labellist=new StringBuffer();
	// for(int i=0;i<labelvalue.size();i++){
	// labellist.append(labelvalue.get(i)+",");
	// }
	// System.out.println("删除将标签转化为列表为"+labellist);
	// Label label=new Label();
	// label.setArticleid(articleid);
	// label.setLabellist(labellist.toString());
	// labeldao.updateLabelConnArticle(label);
	// }
	// 取出用户关联的标签,分页显示
	// public Page<Label> getLabelConnUser(Page<Label> page,Label label){
	// return findPage(page,label);
	// }
	// public List<Label> getLabelConnUser(String userid){
	// return labeldao.getLabelConnUser(userid);
	// }
	// public Page<Label> findPage(Page<Label> page,Label entity) {
	// entity.setPage(page);
	// page.setList(dao.findList(entity));
	// return page;
	// }

	// 取出对应标签下的，权限下的知识id,知识标题
	// public List<Article> getArticleinLabel(Label label,List<String>
	// categoryid){
	// List<Article> articlelist=new ArrayList<Article>();
	// articlelist=labeldao.getArticleinLabel(label);
	// List<Article> newlist=new ArrayList<Article>();
	// for(int i=0;i<articlelist.size();i++){
	// String id=articlelist.get(i).getCategory().getId();
	// String title=articlelist.get(i).getTitle();
	// for(int j=0;j<categoryid.size();j++){
	// String category=categoryid.get(j);
	// if(category.equals(id)){
	// Article article=new Article();
	// article.setId(articlelist.get(i).getId());
	// article.setTitle(title);
	// newlist.add(article);
	// }
	// }
	// }
	// return newlist;
	// }
	// 插入用户关联的标签

	// /*用户增加关注标签，显示用户没有的标签(分页显示)
	// * @param page:分页对象
	// * @param label:将userid传入
	// */
	// public Page<Label> findDiffrentLabel(Page<Label> page,Label label){
	// label.setPage(page);
	// page.setList(dao.getDiffUserConnData(label.getUserid()));
	// return page;
	// }

	public void MergeArticle(String originalcategoryId, String categoryId, String articleId) {
		dao.MergeArticle(originalcategoryId, categoryId, articleId);
	}

	public void MergeCategory(String originalcategoryId, String categoryId) {
		dao.MergeCategory(originalcategoryId, categoryId);
	}

	/*
	 * 
	 * 将标签的数据存入缓存表(cms_label_count)中,用于首页显示
	 */
	public void saveData() {
		List<Label> list = new ArrayList<Label>();
		List<Label> insertlist = new ArrayList<Label>();
		List<Label> updatelist = new ArrayList<Label>();
		list = labeldao.getHotLabelData();// 取出计算好的数据
		List<String> idfromCount = new ArrayList<String>();
		idfromCount = labeldao.getAllid();
		List<String> idfromlist = new ArrayList<String>();
		for (Label label : list) {
			idfromlist.add(label.getId());
			int count = 0;
			String id = label.getId();
			for (int j = 0; j < idfromCount.size(); j++) {
				String did = idfromCount.get(j);
				if (id.equals(did)) {
					count++;
					break;
				}
			}
			if (count == 0) {
				// System.out.println("count==0");
				insertlist.add(label);
				// System.out.println("insertlistpc的值为"+label.toString());
			} else if (count == 1) {
				// System.out.println("count==1");
				updatelist.add(label);
				// System.out.println("insertlistpc的值为"+label.toString());
			}

		}
		// 查出差异的id，批量删除，校正数据正误
		List<String> diff = ContributionUtil.getDiffrent(idfromCount, idfromlist);
		if (diff.size() > 0) {
			labeldao.batchdelete(diff);
		}
		if (insertlist.size() > 0) {
			// System.out.println("计算LabelCount出的插入数据为:"+insertlist.toString()+"-"+insertlist.size());
			labeldao.batchinsert(insertlist);
		}
		if (updatelist.size() > 0) {
			// System.out.println(updatelist.size()+"---"+"计算LabelCount出的更新数据为:"+updatelist.toString()+"-"+updatelist.size());
			labeldao.batchupdate(updatelist);
		}
	}

	// 检查是否是重复的标签名,重复为:true,不重复为:false
	public boolean findRepeatLabelName(String labelName) {
		boolean flag = false;
		String judge = labeldao.findRepeatLabelName(labelName);
		if (judge == null) {
			return flag;
		} else {
			if (judge.equals(labelName)) {
				flag = true;
				return flag;
			}
			return flag;
		}
	}

	public boolean findRepeatLabelName(String labelName, String labelid) {
		boolean flag = false;
		Label la = labeldao.get(new Label(labelid));
		if (labelName.equals(la.getLabelvalue())) {
			return flag;
		}
		String judge = labeldao.findRepeatLabelName(labelName);
		if (judge == null) {
			return flag;
		} else {
			if (judge.equals(labelName)) {
				flag = true;
				return flag;
			}
			return flag;
		}
	}

	public boolean findRepeatLabelByMerge(String labelName, String id) {
		boolean flag = false;
		Label label = labeldao.get(new Label(id));
		String judge = labeldao.findRepeatLabelName(labelName);
		if (judge == null) {
			return flag;
		} else if (judge.equals(label.getLabelvalue())) {
			return flag;
		} else {
			if (judge.equals(labelName)) {
				flag = true;
				return flag;
			}
			return flag;
		}
	}

	// 取得未指定的合并的标签
	public List<Label> findUnMergeLabel(String labelid) {
		List<Label> labellist = new ArrayList<Label>();
		labellist = labeldao.findUnMergeLabel(labelid);
		return labellist;
	}

	// 合并标签
	public void MergeLabel(String secondid, String firstid, String newname) {
		List<String> arlist = labeldao.findRepeatMergeLabelByArticle(firstid, secondid);
		List<String> userlist = labeldao.findRepeatMergeLabelByUser(firstid, secondid);
		if (arlist != null && arlist.size() > 0) {
			Map<String, Object> artmap = new HashMap<String, Object>();
			artmap.put("labelid", secondid);
			artmap.put("articleids", arlist);
			labeldao.deleteMergeLabelbyArticle(artmap);
		}
		if (userlist != null && userlist.size() > 0) {
			Map<String, Object> usermap = new HashMap<String, Object>();
			usermap.put("labelid", secondid);
			usermap.put("userids", userlist);
			labeldao.deleteMergeLabelbyUser(usermap);
		}
		labeldao.updateMergeLabelbyArticle(firstid, secondid);
		labeldao.updateMergeLabelbyUser(firstid, secondid);
		labeldao.updateMergeLabel(firstid, newname);
		labeldao.delete(new Label(secondid));
	}

	/*
	 * 查出标签关联的标签
	 */
	public List<Label> findRelationLabel(String labelid) {
		List<Label> relalist = new ArrayList<Label>();
		relalist = this.labeldao.findRelationLabel(labelid);
		return relalist;
	}

	/*
	 * 
	 * 插入标签关联的标签
	 * 
	 * @param 关联的标签集合
	 */
	public void insertRelationLabel(String labelid, List<String> ids) {
		List<Label> list = new ArrayList<Label>();
		for (int i = 0; i < ids.size(); i++) {
			Label la = new Label();
			la.preInsert();
			la.setMainid(labelid);
			la.setRelationid(ids.get(i));
			list.add(la);
		}
		labeldao.insertRelationLabel(list);
	}

	/*
	 * 删除标签关联的标签
	 */
	public void deleteRelationLabel(String labelid, List<String> ids) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("labelid", labelid);
		map.put("relaids", ids);
		labeldao.deleteRelationLabel(map);
	}

	/*
	 * 保存(更新，修改，删除)标签关联的标签
	 */
	public void savaRelationData(String labelid, List<String> ids) {
		List<String> insertlist = new ArrayList<String>();
		List<String> deletelist = new ArrayList<String>();
		List<String> list = new ArrayList<String>();
		list = labeldao.getAllRelationid(labelid);
		if (ids == null || ids.size() == 0) {
			if (list == null || list.size() == 0) {
				return;
			} else {
				deletelist.addAll(list);
				deleteRelationLabel(labelid, deletelist);
				return;
			}
		}
		for (int i = 0; i < ids.size(); i++) {
			String id = ids.get(i);
			int count = 0;
			for (int j = 0; j < list.size(); j++) {
				String lid = list.get(j);
				if (id.equals(lid)) {
					count++;
					list.remove(j);
					break;
				}
			}
			if (count == 0) {
				insertlist.add(id);
			}
		}
		if (list != null && list.size() > 0) {
			deletelist.addAll(list);
		}
		if (insertlist != null && insertlist.size() > 0)
			insertRelationLabel(labelid, insertlist);
		if (deletelist != null && deletelist.size() > 0)
			deleteRelationLabel(labelid, deletelist);

	}

	/*
	 * 获取标签树的数据
	 */
	public List<Label> getLabelTreeData() {
		List<Label> list = new ArrayList<Label>();
		list = labeldao.getLabelTreeData();
		return list;
	}

	/**
	 * nc->kms 同步标签库
	 */
	public void importLable() {

		boolean isNew = false; // 是否为第一次同步
		Gson gson = new Gson();
		String maxTs = statusDao.selectStatus(LABELTAB); // 获取数据库中上次同步标签的时间
		List<String> alls = labelDao.getAllid(); // 保存所以的标签id
		List<Label> addList = Lists.newArrayList(); // 新增的数据
		List<Label> updateList = Lists.newArrayList(); // 更新的数据

		try {

			URL url = new URL(romateService);
			ISynchroNCInfoLocator is = new ISynchroNCInfoLocator();
			ISynchroNCInfoPortType http = is.getISynchroNCInfoSOAP11port_http(url);

			// 如果是空的话,那么就是第一次导入,时间设置为最小
			if (StringUtils.isBlank(maxTs)) {
				maxTs = "1999-10-10";
				isNew = true;
			}

			OuterSystemRetVO lableInfo = http.getLable(dataSource, maxTs);

			if (!lableInfo.getSuccess()) {
				return;
			}
			// nc中的标签数据集合
			List<NCLabel> lables = Lists.newArrayList();

			lables = gson.fromJson(lableInfo.getData(), new TypeToken<List<NCLabel>>() {
			}.getType());

			// Map<String, String> relations = new HashMap<String, String>();
			// 知识库标签的数据集合
			List<Label> knowsList = Lists.newArrayList();
			Label label = null;
			// 如果没有数据,那么直接返回
			if (lables == null || lables.size() <= 0) {
				return;
			}
			for (NCLabel ncl : lables) {
				label = new Label();

				label.setLabelvalue(ncl.getLableValue());
				label.setLabelcontent(ncl.getLableValue());
				if (ncl.getParentId().equals("0")) {
					label.setPid("3");
				} else {
					label.setPid(ncl.getParentId());
				}
				label.setId(ncl.getLabelId());
				label.setDelFlag(ncl.getDelFlag());
				label.setIssys("0");
				label.setCreateDate(new Date());
				label.setUserid("1");

				knowsList.add(label);
			}
			// System.out.println(knowsList.size());
			Map<String, String> newRelations = new HashMap<String, String>(); // 存储key:机构id
			// value:机构的所有parentIds","分割
			String parentIds = "";
			// 设置上下级
			for (Label temp : knowsList) {
				parentIds = SynchroNCUtil.getParentIds(temp.getPid(), newRelations);
				temp.setPids(parentIds);
				newRelations.put(temp.getId(), parentIds);
				// System.out.println(temp.getId()+":"+temp.getLabelvalue()+":"+temp.getParentId()+":"+temp.getParentIds());
				// System.out.println(JsonMapper.toJsonString(temp));
			}

			// 对于第一次导入和第n次导入数据,区分处理
			if (isNew) {

				// 全部都为新数据
				for (Label l : knowsList) {
					if (!addList.contains(l)) {
						addList.add(l);
					}
				}

			} else {
				for (Label l : knowsList) {
					// 如果包含这个id,那么就是更新的
					if (alls.contains(l.getId())) {
						if (!updateList.contains(l)) {
							updateList.add(l);
						}
					} else {
						// 为新增加的
						if (!addList.contains(l)) {
							addList.add(l);
						}
					}
				}

			}

			// 储存更新的时间 下次更新从这个时间戳开始更新数据
			Status labelStatus = new Status();
			labelStatus.setId(IdGen.uuid());
			labelStatus.setTs(lableInfo.getMaxTs());
			labelStatus.setFromSys(LABELTAB);
			statusDao.insertStatus(labelStatus);

			// System.out.println(" 增加数量:"+addList.size()+" :"+addList.toString());
			// System.out.println(" 更新数量:"+updateList.size()+" :"+updateList.toString());
			//
			// 批量插入数据
			if (addList.size() > 0) {
				labelDao.insertAll(addList);
			}
			// 批量更新数据
			if (updateList.size() > 0) {
				labelDao.batchUpdateLabelData(updateList);
			}

			// //System.out.println(knowsList.size());
			// Map<String, String> newRelations = new HashMap<String, String>();
			// // 存储key:机构id
			// //value:机构的所有parentIds","分割
			// String parentIds = "";
			// //设置上下级
			// for (Label temp : knowsList) {
			// parentIds = SynchroNCUtil.getParentIds(temp.getPid(),
			// newRelations);
			// temp.setPids(parentIds);
			// newRelations.put(temp.getId(), parentIds);
			// //System.out.println(temp.getId()+":"+temp.getLabelvalue()+":"+temp.getParentId()+":"+temp.getParentIds());
			// //System.out.println(JsonMapper.toJsonString(temp));
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置上级的一个方案
	 * 
	 * @author Hotusm
	 * @since 2016-03-08
	 * @see SynchroNCUtil
	 */

	public void importLable1() {

		List<Label> updateList = Lists.newArrayList(); // 更新的数据

		try {
			Map<String, String> relations = new HashMap<String, String>();
			// 知识库标签的数据集合

			// 这里存放的是一批需要设置上下级的vo（必须具有pid）
			List<Label> lables = Lists.newArrayList();
			List<Label> knowsList = Lists.newArrayList();
			Label label = null;
			lables = labeldao.getLabelTreeData();
			for (Label l : lables) {
				relations.put(l.getId(), l.getPid());
			}

			System.out.println(knowsList.size());
			Map<String, String> newRelations = new HashMap<String, String>(); // 存储key:机构id
			// value:机构的所有parentIds","分割
			String parentIds = "";
			// 设置上下级
			for (Label temp : lables) {
				parentIds = SynchroNCUtil.getParentIds(temp.getPid(), relations);
				temp.setPids(parentIds);
				relations.put(temp.getId(), temp.getPid());
				// newRelations.put(temp.getId(), parentIds);
			}

			for (Label l : lables) {
				System.out.println(l.getPids());
				labelDao.update(l);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/*******************************2017-11-08新增**********************************/
	public List<Label> getLabelParentIds(String parentId) {
		List<Label> relalist = new ArrayList<Label>();
		relalist = this.labeldao.getLabelParentIds(parentId);
		return relalist;
	}

	public List<Label> getLabelParentIdsByName(String parentId, String name) {
		List<Label> relalist1 = new ArrayList<Label>();
		List<Label> relalist2 = new ArrayList<Label>();
		relalist1 = this.labeldao.getLabelParentIds(parentId);
		for (Label label : relalist1) {
			if(StringUtils.isNotBlank(label.getLabelvalue()) && 
				StringUtils.isNotBlank(name)){
				if((label.getLabelvalue()).equals(name)){
					relalist2 = this.labeldao.getLabelParentIds(label.getId());
				}
			}
		}
		return relalist2;
	}
}
