/**
 * 
 */
package com.yonyou.kms.modules.sys.service;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.rpc.ServiceException;

import nc.itf.kms.ISynchroNCInfo.ISynchroNCInfoLocator;
import nc.itf.kms.ISynchroNCInfo.ISynchroNCInfoPortType;
import nc.vo.kms.entity.NCOffice;
import nc.vo.kms.entityN.OuterSystemRetVO;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yonyou.kms.common.service.TreeService;
import com.yonyou.kms.modules.cms.dao.ArticleDao;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.service.ArticleService;
import com.yonyou.kms.modules.cms.utils.CmsUtils;
import com.yonyou.kms.modules.sys.dao.OfficeDao;
import com.yonyou.kms.modules.sys.dao.StatusDao;
import com.yonyou.kms.modules.sys.dao.UserDao;
import com.yonyou.kms.modules.sys.entity.Area;
import com.yonyou.kms.modules.sys.entity.Audit;
import com.yonyou.kms.modules.sys.entity.Office;
import com.yonyou.kms.modules.sys.entity.Status;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.utils.SynchroNCUtil;
import com.yonyou.kms.modules.sys.utils.UserUtils;

/**
 * 机构Service
 * 
 * @author hotsum
 * @version 2014-05-16
 */
@Service
@Transactional(readOnly = true)
public class OfficeService extends TreeService<OfficeDao, Office> {

	/**
	 * 数据正常
	 */
	public static String DATA_NORMAL = "0";

	/**
	 * 数据异常
	 */
	public static String DATA_EXCEPTION = "1";

	/**
	 * 数据为空
	 */
	public static String DATA_NULL = "2";
	
	private Gson gson = new Gson();
	
	@Autowired
	private StatusDao statusDao;
	//注入数据源
	@Value("${nc.datasource}")
	private String dataSource;
	//nc远程连接口的信息
	@Value("${nc.romateService}")
	private String romateService;
	@Autowired
	private OfficeDao officeDao;
	@Autowired
	private ArticleService articleService;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ArticleDao articleDao;
	
	public List<Office> findAll() {
		return UserUtils.getOfficeList();
	}
	
	
	public List<Office> findList(Boolean isAll) {
		if (isAll != null && isAll) {
			return UserUtils.getOfficeAllList();
		} else {
			return UserUtils.getOfficeList();
		}
	}
	
	public List<User> findAllAudit(String officeId){
		
		return officeDao.findAuditUsers(officeId);
	}
	/**
	 * @yangshw6
	 * 取得最顶级分类 天禾农资集团的信息
	 */
	@Transactional(readOnly = false)
	public Office findTopOffice(User user){
		if(user==null||StringUtils.isBlank(user.getId()))return null;
		//Office office=new Office();
		//office=officeDao.getTopOffice();
		//return office;
		Office office = officeDao.findUserOffice(user.getId());
		if(office==null)
			return null;
		else
		{
			String topOfficeId="";
			int length = office.getParentIds().split(",").length;
			if(length==1)
				topOfficeId=office.getId();
			else
				topOfficeId=office.getParentIds().split(",")[1];
			
			return officeDao.get(topOfficeId);
		}
		
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<Office> findList(Office office) {
		office.setParentIds(office.getParentIds() + "%");
		return dao.findByParentIdsLike(office);
	}
	
	public List<Audit> findAllAudit(){
		return officeDao.findAllAudits();
	}
	
	@Override
	@Transactional(readOnly = false)
	public String save(Office office) {
		super.save(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
		return "";
	}
	
	@Override
	@Transactional(readOnly = false)
	public void delete(Office office) {
		super.delete(office);
		UserUtils.removeCache(UserUtils.CACHE_OFFICE_LIST);
	}
	
	@Transactional(readOnly = false)
	public void assignUser2Office(String ids,String officeId) throws Exception{

		try {
			//1 清除原来的数据
			officeDao.clearOfficeAudit(officeId);
			if(StringUtils.isBlank(ids)){
				return;
			}
			//2 重新分配的数据
			List<String> userIds = CmsUtils.transform(ids, ",");
			Map<String,Object> maps=new HashMap<String, Object>();
			maps.put("list", userIds);
			maps.put("officeId", officeId);
			officeDao.saveOfficeAduits(maps);
			//在分配完知识审核员之后 需要重新分配每个人不同的知识
			doAfterAssign();
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void doAfterAssign(){
		//1 ① 查找所有的机构 作为一个映射 KEY= officeId  VALUE=office.parent
		final Map<String, Office> mapParents = articleService.mapOffice2Parent(officeDao.findAll());
		//  ② 查找所有的用户映射
		final Map<String, User> mapUsers =articleService.mapUser2Id(userDao.findList(new User()));
		//  ③ 查找所有的审核信息 
		final List<Audit> audits = officeDao.findAllAudits();
		// 
		//机构和审核管理员的映射
		final Map<String,List<Audit>> auditMapOffice=new HashMap<String, List<Audit>>();
		List<Audit> singleOffice=null;
		for(Audit audit:audits){
			if(auditMapOffice.containsKey(audit.getOfficeId())){
				singleOffice = auditMapOffice.get(audit.getOfficeId());
				singleOffice.add(audit);
			}else{
				singleOffice=Lists.newArrayList();
				singleOffice.add(audit);
				auditMapOffice.put(audit.getOfficeId(), singleOffice);
			}
		}
		
		Article article=new Article();
		//-1 代表所有的
		article.setDelFlag("-1");
		//所有的知识
		final List<Article> articles = articleDao.findListNoCondition();
		//需要执行的个数
		final CountDownLatch jobCount=new CountDownLatch(articles.size());
		
		final List<Audit> fullAudit=Lists.newArrayList();
		class AssginHelper{
			
			private int NCPU=Runtime.getRuntime().availableProcessors();
			private int accessCount=(int) Math.ceil(1/(0.1+0.9/NCPU));  //scala
			private final ExecutorService executors = Executors.newFixedThreadPool(accessCount+1);
			public AssginHelper(){}
			
			public void execute(){
				
				for(final Article article:articles){
					//将任务进行分割
					executors.execute(new Runnable() {
						
						public void run() {
							try {
								User user = mapUsers.get(article.getCreateBy().getId());
								if(user!=null){
									Office office = user.getOffice();
									List<String> userIds = articleService.findAuditUser(office, mapParents, auditMapOffice);
									List<Audit> audits=Lists.newLinkedList();
									if(userIds!=null&&userIds.size()>0){
										Audit audit=null;
										for(String id:userIds){
											if(id==null)continue;
											audit=new Audit();
											audit.setUserId(id);
											//注意这里  这里用的是officeId map articleId
											audit.setOfficeId(article.getId());
											audits.add(audit);
										}
									}
									fullAudit.addAll(audits);
								}
							}finally{
								jobCount.countDown();
							}
						}
					});
				}
			}
		}
		
		AssginHelper helper=new AssginHelper();
		helper.execute();
		try {
			jobCount.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
			Thread.currentThread().interrupt();
		}
		//做一个去重
		Set<Audit> dbInsert=Sets.newLinkedHashSet();
		for(Audit audit:fullAudit){
			dbInsert.add(audit);
		}
		fullAudit.clear();
		for(Audit audit:dbInsert){
			if(audit==null)continue;
			fullAudit.add(audit);
		}
		//将以前的数据全部清理
		articleDao.clearAudits();
		//插入新的数据
		articleDao.saveAudits(fullAudit);
		
	}
	
	
	
	
	@Transactional(readOnly = false)
	public void synchroNCInfo() throws ServiceException {
		Logger logger = LoggerFactory.getLogger(getClass());
		List<Office> allOffice = null;//从数据库中找出所有数据
		try {
			allOffice = dao.findAll();//从数据库中找出所有数据
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			/** ************获取同步记录表数据(用于判断是否第一次同步)************** */
			String maxTs = statusDao.selectStatus("ORG_GROUP");
			// 存储key:机构id // value:机构的所有parentId
			Map<String, String> relations = new HashMap<String, String>();

			List<Office> addOfficeList = new ArrayList<Office>();
			List<Office> updateOfficeList = new ArrayList<Office>();
			List<Status> statusList = new ArrayList<Status>();
			
			//集团同步记录
			Status groupStatus = null;
			//业务单元同步记录
			Status orgStatus = null;
			//部门同步记录
			Status deptStatus = null;
			//连接nc远程服务   调取数据
			ISynchroNCInfoLocator service = new ISynchroNCInfoLocator();
			URL url=new URL(romateService);
			if (StringUtils.isEmpty(maxTs)) {// 第一次同步，所取数据为新增的数据
				
				ISynchroNCInfoPortType client =service.getISynchroNCInfoSOAP11port_http(url);
				/** *******************获取NC集团************** */
				OuterSystemRetVO group = client.getNCGroupInfo(dataSource,"1999-01-01");//调用接口getNCGroupInfo获得数据
				logger.debug("============结束调用NC远程接口,返回GROUP数据" + group.getData()+ "========================");
				List<NCOffice> groupAddList = new ArrayList<NCOffice>();
				// 调用接口成功且数据正常
				if ((group.isSuccess())
						&& (DATA_NORMAL.equals(group.getDataState()))) {//判断取得数据的状态
					// 获取传过来的集团数据
					groupAddList = gson.fromJson(group.getData(),
							new TypeToken<List<NCOffice>>() {
							}.getType());
					// 将NC集团数据转换成Office并设置父子关系
					ncOffice2Office(groupAddList, addOfficeList, relations);
					//记录同步时间
					groupStatus = new Status();
					groupStatus.setId(""+System.currentTimeMillis()+Math.random());
					groupStatus.setTs(group.getMaxTs());//获得数据中的最大时间
					groupStatus.setFromSys("ORG_GROUP");//"设置从ORG_GROUP这个表获得的数据"
				} else {
					return;
				}

				/** *************获取NC业务单元************** */
				OuterSystemRetVO org = client.getNCOrgInfo(dataSource,"1999-01-01");
				logger.debug("============结束调用NC远程接口,返回ORG数据:" + org.getData()+ "========================");
				List<NCOffice> orgAddList = new ArrayList<NCOffice>();
				// 调用接口成功且数据正常
				if ((org.isSuccess())
						&& (DATA_NORMAL.equals(org.getDataState()))) {
					// 获取传过来的业务数据
					orgAddList = gson.fromJson(org.getData(),
							new TypeToken<List<NCOffice>>() {
							}.getType());

					// 将NC业务单元数据转换成Office
					ncOffice2Office(orgAddList, addOfficeList, relations);
					//记录同步时间
					orgStatus = new Status();
					orgStatus.setId(""+System.currentTimeMillis()+Math.random());
					orgStatus.setTs(org.getMaxTs());
					orgStatus.setFromSys("ORG_ORGS");
				} else {
					return;
				}

				/** *************获取NC部门************** */
				OuterSystemRetVO dept = client.getNCDeptInfo(dataSource,"1999-01-01");
				logger.debug("============结束调用NC远程接口,返回DEPT数据:" + dept.getData()+ "========================");
				List<NCOffice> deptAddList = new ArrayList<NCOffice>();
				// 调用接口成功且数据正常
				if ((dept.isSuccess())
						&& (DATA_NORMAL.equals(dept.getDataState()))) {
					// 获取传过来的部门数据
					deptAddList = gson.fromJson(dept.getData(),
							new TypeToken<List<NCOffice>>() {
							}.getType());

					// 将NC部门数据转换成Office
					ncOffice2Office(deptAddList, addOfficeList, relations);
					//记录同步时间
					deptStatus = new Status();
					deptStatus.setId(System.currentTimeMillis()+Math.random()+"");
					deptStatus.setTs(dept.getMaxTs());
					deptStatus.setFromSys("ORG_DEPT");
				}

			} else {
				// 第N次同步，所取数据为两种：新增的数据、修改的数据
				/** ************获取知识库所有机构数据************** */

				/** ************获取office父子关系************** */
				
				Map<String, Office> officeRelation = new HashMap<String, Office>();
				for (int i = 0; i < allOffice.size(); i++) {
					relations.put(allOffice.get(i).getId(), allOffice.get(i)
							.getParentIds());//key id;value pid
					officeRelation.put(allOffice.get(i).getId(), allOffice
							.get(i));//key  id;vlaue 这条数据
				}

				/** *******************获取NC集团************** **/
				
				//获取上次同步记录
				
				ISynchroNCInfoPortType client =service.getISynchroNCInfoSOAP11port_http(url);
				String groupTs =statusDao.selectStatus("ORG_GROUP");
				OuterSystemRetVO group =client.getNCGroupInfo(dataSource,groupTs); ;//调用接口getNCGroupInfo
				List<NCOffice> groupList = new ArrayList<NCOffice>();
				logger.debug("============结束调用NC远程接口,返回ORG_GROUP数据:" + group.getData()+ "========================");
				// 调用接口成功且数据正常
				if (group.isSuccess()
						&& (DATA_NORMAL.equals(group.getDataState()))) {
					// 获取传过来的集团数据
					groupList = gson.fromJson(group.getData(),
							new TypeToken<List<NCOffice>>() {
							}.getType());

					// 将NC集团数据转换成Office
					ncOffice2Office(groupList, addOfficeList, updateOfficeList,
							relations, officeRelation);
					//记录同步时间
					deptStatus = new Status();
					deptStatus.setId(System.currentTimeMillis()+Math.random()+"");
					deptStatus.setTs(group.getMaxTs());
					deptStatus.setFromSys("ORG_GROUP");
				}

				/** *******************获取业务单元************** */
				String orgTs=statusDao.selectStatus("ORG_ORGS");
				OuterSystemRetVO org = client.getNCOrgInfo(dataSource,orgTs);
				List<NCOffice> orgList = new ArrayList<NCOffice>();
				logger.debug("============结束调用NC远程接口,返回ORG_ORGS数据:" + org.getData()
						+ "========================");
				// 调用接口成功且数据正常
				if ((org.isSuccess())
						&& (DATA_NORMAL.equals(org.getDataState()))) {
					// 获取传过来的业务单元数据
					orgList = gson.fromJson(org.getData(),
							new TypeToken<List<NCOffice>>() {
							}.getType());

					// 将NC业务单元数据转换成Office
					ncOffice2Office(orgList, addOfficeList, updateOfficeList,
							relations, officeRelation);
					//记录同步时间
					deptStatus = new Status();
					deptStatus.setId(System.currentTimeMillis()+Math.random()+"");
					deptStatus.setTs(org.getMaxTs());
					deptStatus.setFromSys("ORG_ORGS");
				}

				/** *******************获取部门************** */
				String deptTs=statusDao.selectStatus("ORG_DEPT");
				OuterSystemRetVO dept = client.getNCDeptInfo(dataSource,deptTs);
				List<NCOffice> deptList = new ArrayList<NCOffice>();
				logger.debug("============结束调用NC远程接口,返回DEPT数据:" + dept.getData()
						+ "========================");
				// 调用接口成功且数据正常
				if ((dept.isSuccess())
						&& (DATA_NORMAL.equals(dept.getDataState()))) {
					// 获取传过来的业务单元数据
					deptList = gson.fromJson(dept.getData(),
							new TypeToken<List<NCOffice>>() {
							}.getType());

					// 将NC业务单元数据转换成Office
					ncOffice2Office(deptList, addOfficeList, updateOfficeList,
							relations, officeRelation);
					//记录同步时间
					deptStatus = new Status();
					deptStatus.setId(System.currentTimeMillis()+Math.random()+"");
					deptStatus.setTs(dept.getMaxTs());
					deptStatus.setFromSys("ORG_DEPT");
				}

			}

			/** ************组织新增数据的parent_ids************** */
			Map<String, String> newRelations = new HashMap<String, String>(); // 存储key:机构id
			// value:机构的所有parentIds","分割
			String parentIds = "";

			for (Office temp : addOfficeList) {
				parentIds = SynchroNCUtil.getParentIds(temp.getParentId(),
						relations);
				temp.setParentIds(parentIds);
				newRelations.put(temp.getId(), parentIds);
			}
			
			/** ************NC同步过来的数据保存************** */
			List<Office> tempList=Lists.newArrayList();
			if((addOfficeList != null) && (!addOfficeList.isEmpty())){
				//调用Office批量插入接口
				
				//增加这一步操作,将重复的和增加了两次的数据给排除掉
				
				for(Office o:addOfficeList){
					if(!allOffice.contains(o)&&!tempList.contains(o)){
						tempList.add(o);
					}
				}
				
				//dao.batchInsert(tempList);
				for(Office office:tempList){
					dao.insert(office);
				}
			}
			
			if((updateOfficeList != null) && (!updateOfficeList.isEmpty())){
				//调用Office批量更新接口
				for(Office office:updateOfficeList){
					dao.update(office);
				}
			}
			
			
			
			/** ************保存同步记录************** */

			//保存状态
			if(groupStatus!=null){statusList.add(groupStatus);}
			if(deptStatus!=null){statusList.add(deptStatus);}
			if(orgStatus!=null){statusList.add(orgStatus);}
			if(!statusList.isEmpty()){
				statusDao.batchInsert(statusList);
			}

		} catch (RemoteException e) {
			logger.error("调用NC远程接口getNCOrgInfo()失败");
			throw new ServiceException("调用NC远程接口getNCOrgInfo()失败:"
					+ e.getStackTrace());
		} catch (ServiceException e) {
			logger
					.error("获取远程接口失败:ISynchroNCInfoPortType client = service.getISynchroNCInfoSOAP11port_http()");
			throw new ServiceException(
					"获取远程接口失败:ISynchroNCInfoPortType client = service.getISynchroNCInfoSOAP11port_http():"
							+ e.getStackTrace());
		}catch(Exception e){
			 e.printStackTrace();
		}

	}

	/**
	 * * 获取新增数据
	 * 
	 * @param ncOfficeList
	 *            来源NC端的数据
	 * @param officeList
	 *            新增的机构
	 * @param relations
	 */
	private void ncOffice2Office(List<NCOffice> ncOfficeList,
			List<Office> officeList, Map<String, String> relations) {
		Office office = null;
		Office parent = null;
		NCOffice ncOffice = null;
		User user=new User();
		user.setId("1");
		
		for (int i = 0; i < ncOfficeList.size(); i++) {

			ncOffice = ncOfficeList.get(i);
			office = new Office();
			office.setId(ncOffice.getId());

			parent = new Office();
			parent.setId(ncOffice.getParent_id());
			office.setParent(parent);

			office.setCode(ncOffice.getCode());
			office.setName(ncOffice.getName());

			office.setParentIds(ncOffice.getParent_id());
			office.setSort(10);

			Area area = new Area();
			area.setId("1");
		office
			.setUseable("2".equals(ncOffice.getUseable()) ? "1"
					: "0");
			office.setArea(area);
			office.setType("1");
			office.setGrade("1");
//			office.setCreateBy(UserUtils.getUser());
			office.setCreateBy(user);
			office.setCreateDate(new Date());
//			office.setUpdateBy(UserUtils.getUser());
			office.setUpdateBy(user);
			office.setUpdateDate(new Date());

			// 设置在上下级关系
			relations.put(office.getId(), ncOffice.getParent_id());//
			officeList.add(office);
		}
	}

	/**
	 * 获取新增、修改Office数据
	 * 
	 * @param ncOfficeList
	 *            来源NC端的数据
	 * @param addOfficeList
	 *            新增的机构
	 * @param updateOfficeList
	 *            更新的机构
	 * @param relations
	 * @param officeRelation
	 */
	private void ncOffice2Office(List<NCOffice> ncOfficeList,
			List<Office> addOfficeList, List<Office> updateOfficeList,
			Map<String, String> relations, Map<String, Office> officeRelation) {// nc传过来的集团信息，新增到知识库的信息，修改的信息，父子关系，知识库关系
		Office office = null;
		Office parent = null;
		NCOffice ncOffice = null;
		User user=new User();
		user.setId("1");
		
		for (int i = 0; i < ncOfficeList.size(); i++) {

			ncOffice = ncOfficeList.get(i);

			// 判断数据是新增还是更新
			if (officeRelation.containsKey(ncOffice.getId())) {// 更新的数据
				office = officeRelation.get(ncOffice.getId());

				office.setCode(ncOffice.getCode());
				office.setName(ncOffice.getName());
				office.setDelFlag(ncOffice.getDel_flag());
				office
						.setUseable("2".equals(ncOffice.getUseable()) ? "1"
								: "0");

				updateOfficeList.add(office);
			} else {// 新增的数据
				office = new Office();
				office.setId(ncOffice.getId());

				parent = new Office();
				parent.setId(ncOffice.getParent_id());
				office.setParent(parent);

				office.setCode(ncOffice.getCode());
				office.setName(ncOffice.getName());

				office.setParentIds(ncOffice.getParent_id());
				office.setSort(10);

				Area area = new Area();
				area.setId("1");

				office.setArea(area);
				office.setType("1");
				office.setGrade("1");
				//office.setCreateBy(UserUtils.getUser());
				office.setCreateBy(user);
				office.setCreateDate(new Date());
				//office.setUpdateBy(UserUtils.getUser());
				office.setUpdateBy(user);
				office.setUpdateDate(new Date());
				office.setDelFlag(ncOffice.getDel_flag());
				office
						.setUseable("2".equals(ncOffice.getUseable()) ? "1"
								: "0");
				// 设置在上下级关系
				relations.put(office.getId(), ncOffice.getParent_id());
				addOfficeList.add(office);
			}

		}
	}
}
