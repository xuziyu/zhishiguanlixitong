package com.yonyou.kms.modules.cms.dao;

import java.util.List;
import java.util.Map;

import com.yonyou.kms.common.persistence.CrudDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.cms.entity.Label;

@MyBatisDao
public interface LabelDao extends CrudDao<Label> {
	// 获取标签排行，按知识数的多少进行排行，取前5条数据
	public List<Label> getHotLabelData();

	// 批量删除标签表中的数据
	public void batchdeleteLabelData(List<String> list);

	// 批量取得标签下的关联文章和用户
	public List<Label> batchgetLabelData(List<String> list);

	// 批量更新标签表中的内容
	public void batchUpdateLabelData(List<Label> list);

	// 获取知识关联标签,在文章浏览界面使用
	// public List<Label> getLabelConnArticle(String id);
	// //插入知识关联的标签,使用标签列表，将标签装载进去
	// public void insertLabelConnArticle(Label insert);
	// //删除知识关联的标签
	// public void deleteLabelConnArticle(Label label);
	// //更新知识关联的标签
	// public void updateLabelConnArticle(Label label);
	// //获取用户关联的标签，在个人中心显示
	// public List<Label> getLabelConnUser(String userid);
	// //插入用户关联的标签
	// public void insertLabelConnUser(Label insert);
	// //删除知识关联的标签
	// public void deleteLabelConnUser(Label label);
	// //更新用户关联的标签
	// public void updateLabelConnUser(Label label);
	// 获取全部标签
	public List<Label> getAllLabel(Label label);

	// 检查是否是重复的标签;
	public String findRepeatLabelName(String labelName);

	// //获取单个标签下的知识id
	// public List<Article> getArticleinLabel(Label label);
	// //插入标签内容
	public int insert(Label label);

	// 更新标签内容
	public int update(Label label);

	// 删除标签
	public int delete(Label label);

	// 批量查询，通过id，查标签的标题
	public List<String> batchget(List<String> labelid);

	// 批量插入数据到缓存表(cms_label_count)
	public void batchinsert(List<Label> list);

	// 批量更新数据到缓存表(cms_label_count)
	public void batchupdate(List<Label> list);

	// 批量删除缓存表中的数据
	public void batchdelete(List<String> list);

	// 获取缓存表中的所有id
	public List<String> getAllid();

	// 读取缓存表中的数据
	public List<Label> getLabelData();

	// 标签的关联用户，知识数
	// public List<Label> getLabelCountData();
	// 是否有当前用户关联知识
	// public String getUserData(String userid);
	// //获取当前用户没有的标签
	// public List<Label> getDiffUserConnData(String userid);
	// 获取当前用户未审批的标签
	public List<Label> getUserUnexamineLabel(Label label);

	// 显示未指定合并的标签
	public List<Label> findUnMergeLabel(String id);

	// 更新关联文章的合并的标签
	public void updateMergeLabelbyArticle(String firstlabelid, String secondlabelid);

	// 更新关联用户的合并的标签
	public void updateMergeLabelbyUser(String firstlabelid, String secondlabelid);

	// 把合并后的新名字更新
	public void updateMergeLabel(String firstid, String newname);

	// 找出拥有全部合并标签的文章(合并标签时重复)
	public List<String> findRepeatMergeLabelByArticle(String firstlabelid, String secondlabelid);

	// 找出拥有全部合并标签的用户(合并标签时重复)
	public List<String> findRepeatMergeLabelByUser(String firstlabelid, String secondlabelid);

	// 在关联文章表删除第二个合并的标签
	public void deleteMergeLabelbyArticle(Map map);

	// 在关联用户表删除第二个合并的标签
	public void deleteMergeLabelbyUser(Map map);

	// 获得标签关联的标签
	public List<Label> findRelationLabel(String labelid);

	// 插入标签关联的标签
	public void insertRelationLabel(List<Label> label);

	// 删除标签关联的标签
	public void deleteRelationLabel(Map map);

	// 获取标签全部的关联标签id
	public List<String> getAllRelationid(String id);

	// 取出形成标签树的数据
	public List<Label> getLabelTreeData();

	public void MergeArticle(String originalcategoryId, String categoryId, String articleId);

	public void MergeCategory(String originalcategoryId, String categoryId);

	// 批量插入cms_label表中数据 add by luqibao 2016-03-04
	public void insertAll(List<Label> list);

	// 标签关联二级知识分类
	public void updateLabelByCategoryId(Map map);

	// 获取关联的二级知识分类的标签
	public List<Label> findLabelByCategoryId(String categroyid);

	public void insertLabelByCategoryId(List<Label> list);

	public void deleteLabelByCategoryId(Map map);

	// 通过原来的分类和标签的关联关系去知识和标签的关联表中查找知识id
	// add by wuwq6
	public List<String> findArticleIdByOld(Map map);

	// 根据分类id查找到所有关联的标签id add by wuwq6
	public List<String> getAllLabeIdByCategoryId(String categoryid);

	// 删除知识和标签旧的关联关系 add by wuwq6
	public void deleteOldRelation(Map map);

	// 插入知识与标签的新关联 add by wuwq6
	public void insertNewRelation(Map<String, Object> newList);

	// 根据分类id从知识和标签的关联表里取得标签id为空的知识id
	public List<String> findArticleIdByCategoryId(String categoryid);

	// 根据知识id删除知识与标签的关联
	public void deleteOldRelationByArticleId(Map<String, Object> articleIdList);

	// 根据分类id在知识表里找关联的知识id(原来分类下不带标签的情况)
	public List<String> findArticleIdByCategoryIdFromArticle(String categoryid);

	// 根据分类id删除旧的知识和标签的关联关系((原来分类下不带标签的情况))
	public void deleteOldRelationByCategoryIdAndLabelId(String categoryid);

	// 查找分类和标签关联表里是否有重复数据，返回条数
	public List<String> findDoubleData1(Map<String, Object> articleIdList);

	// 删除分类和标签关联表中的重复数据
	public void deleteDoubleData1(Map<String, Object> articleIdList);

	// 查找知识和标签关联表里是否有重复数据，返回条数
	public List<String> findDoubleData2(Map<String, Object> articleIdList);

	// 删除知识和标签关联表中的重复数据
	public void deleteDoubleData2(Map<String, Object> articleIdList);
	
	/**************************2017-11-08新增********************************/
	public List<Label> getLabelParentIds(String parentId);
}
