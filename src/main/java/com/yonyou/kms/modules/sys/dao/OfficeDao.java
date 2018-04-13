/**
 * 
 */
package com.yonyou.kms.modules.sys.dao;

import java.util.List;
import java.util.Map;

import com.yonyou.kms.common.persistence.TreeDao;
import com.yonyou.kms.common.persistence.annotation.MyBatisDao;
import com.yonyou.kms.modules.sys.entity.Audit;
import com.yonyou.kms.modules.sys.entity.Office;
import com.yonyou.kms.modules.sys.entity.User;

/**
 * 机构DAO接口
 * 
 * @author hotsum
 * @version 2014-05-16
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {

	/**
	 * 批量增加机构
	 * 
	 * @author xiongbo
	 * @param list
	 * @return
	 */
	public int batchInsert(List<Office> list);

	/**
	 * 批量更新
	 * 
	 * @author xiongbo
	 * @param list
	 * @return
	 */
	public int batchUpdate(List<Office> list);

	/**
	 * 查询所有机构
	 * 
	 * @return
	 */
	public List<Office> findAll();
	/**
	 * @yangshw6
	 * 查询最顶级分类 天禾农资集团信息 根据parent_id进行定位 
	 * 废弃 会查询出多个
	 */
	@Deprecated
	public Office getTopOffice();
	
	/**
	 * 
	 * @param users 	用户  
	 * @param officeId  部门ID
	 * @return
	 */
	public void saveOfficeAduits(Map<String,Object> maps);
	
	/**
	 * 
	 * @param userId  	用户ID
	 * @param officeId  机构ID
	 */
	public void deleteOfficeAduit(String userId,String officeId);
	/**
	 * 
	 * @param officeId 查找指定机构的审核管理员
	 * @return
	 */
	public List<User> findAuditUsers(String officeId);
	/**
	 * 
	 * @param officeId 清除指定机构id的所有审核员
	 * 
	 */
	public void clearOfficeAudit(String officeId);
	/**
	 * 查找所有的审核信息
	 * @return
	 */
	public List<Audit> findAllAudits();
	/**
	 * 
	 * @param userId 
	 * @return 查询出用户的部门
	 */
	public Office findUserOffice(String userId);
	
}
