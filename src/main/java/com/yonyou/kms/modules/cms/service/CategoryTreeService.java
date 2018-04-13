package com.yonyou.kms.modules.cms.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yonyou.kms.common.mapper.JsonMapper;
import com.yonyou.kms.common.service.CrudService;
import com.yonyou.kms.modules.cms.dao.CategoryTreeDao;
import com.yonyou.kms.modules.cms.entity.CategoryTree;
/**
 * 
 * 首页动态知识库树Service
 * @author yangshiwei
 *
 */
@Service
@Transactional(readOnly = false)
public class CategoryTreeService extends CrudService<CategoryTreeDao, CategoryTree>{
	@Autowired
	private CategoryTreeDao categorytreedao;
	
	
	/**
	 * 
	 * 将数据取出并进行封装，方便前台调用数据
	 * @param categoryidlist:二级分类id集合.fircatelist:一级分类集合
	 * @return 返回的是包含三级结构的list.
	 * 
	 */
	public List<Map<String,Object>>  getCategoryTreeData(List<String> categoryidlist,List<String> fircatelist,List<String> libcatelist){
//		List<String>	fircatelist=new ArrayList<String>();
//		fircatelist.add("94fdd583c8ac40b580a93c04951bb6b5");
		List<CategoryTree> fcl=new ArrayList<CategoryTree>();//一级分类数据
		List<CategoryTree> lcl=new ArrayList<CategoryTree>();//库级数据
		List<CategoryTree>  firstTosecond=new ArrayList<CategoryTree>();
		List<CategoryTree> libToFirst=new ArrayList<CategoryTree>();
		if(fircatelist !=null && fircatelist.size()!=0){
		fcl=categorytreedao.batchget(fircatelist);
		}
		if(libcatelist !=null && libcatelist.size()!=0){
			lcl=categorytreedao.batchget(libcatelist);
		}
		//取出一级分类id,name和对应的二级分类id,name
		if(categoryidlist!=null&&categoryidlist.size()>0){
			firstTosecond=categorytreedao.getcategoryData(categoryidlist);
		}		
		//取出一级分类id，对getcategoryData进行再次查询，取出对应知识库的id，name
		//JsonMapper.toJsonString(firstTosecond);
		List<String> firstcategoryid=new ArrayList<String>();
		for(int i=0;i<firstTosecond.size();i++){
			String id=new String();
			id=firstTosecond.get(i).getId();
			firstcategoryid.add(id);
			//System.out.println("一级分类数据为："+firstTosecond.get(i).getId()+"--"+firstTosecond.get(i).getName());
		//System.out.println("二级分类"+firstTosecond.get(i).getCategoryChild().getId()+"--"+firstTosecond.get(i).getCategoryChild().getName());
		}
		//把那些没有下级的一级知识分类添加到一级知识分类集合中
		for(int i=0;i<fcl.size();i++){
			String id=fcl.get(i).getId();
			int count=0;
			for(int j=0;j<firstcategoryid.size();j++){
				String cid=firstcategoryid.get(j);
				if(id.equals(cid)){
					count++;
					break;
				}
			}
			if(count==0){
				firstcategoryid.add(fcl.get(i).getId());
			}	
		}
		
		//System.out.println("一级分类的长度为:"+firstcategoryid.size());
		//取出知识库和一级分类
		if(firstcategoryid!=null&&firstcategoryid.size()>0){
			libToFirst=categorytreedao.getcategoryData(firstcategoryid);
		}
//		for(int i=0;i<libToFirst.size();i++){
//			//System.out.println("知识库数据为："+libToFirst.get(i).getId()+"--"+libToFirst.get(i).getName());
//			//System.out.println("一级分类"+libToFirst.get(i).getCategoryChild().getId()+"--"+libToFirst.get(i).getCategoryChild().getName());
//		}
		//将一级分类和二级分类整合,一级分类下有其对应的二级分类
		List<Map<String,Object>>  firstlist=new ArrayList<Map<String,Object>>();
		//取出所有的不带重复的一级分类数据
//		for(int i=0;i<categoryidlist.size();i++){
//			System.out.println("categoryidlist:"+JsonMapper.toJsonString(categoryidlist.get(i)));
//		}
		List<CategoryTree> firstdata=new ArrayList<CategoryTree>();
		if(categoryidlist!=null&&categoryidlist.size()>0){
			firstdata=categorytreedao.getAllFather(categoryidlist);
		}
		for(int i=0;i<fcl.size();i++){
			String id=fcl.get(i).getId();
			int count=0;
			for(int j=0;j<firstdata.size();j++){
				String cid=firstdata.get(j).getId();
				if(id.equals(cid)){
					count++;
					break;
				}
			}
			if(count==0){
				firstdata.add(fcl.get(i));
			}	
		}
		//System.out.println("firstdata..."+firstdata.toString()+firstdata.size());
		for(int i=0;i<firstdata.size();i++){
			//System.out.println("firstdata:"+JsonMapper.toJsonString(firstdata.get(i)));
			Map<String,Object> firstmap=new HashMap<String,Object>();
			List<CategoryTree> secondlist=new ArrayList<CategoryTree>();
			firstmap.put("id",firstdata.get(i).getId());
			firstmap.put("name", firstdata.get(i).getName());
			firstmap.put("secondlist",secondlist);
			String firstid2=firstdata.get(i).getId();
			for(int j=0;j<libToFirst.size();j++){
				//System.out.println("firstid2....."+firstid2);
				String parentid=libToFirst.get(j).getId();//知识库编号
				String firstid1=libToFirst.get(j).getCategoryChild().getId();//一级知识分类id
				//System.out.println("firstid1....."+firstid1);
				if(firstid1.equals(firstid2)){
					firstmap.put("parentid", parentid);
//					System.out.println("parentid,....."+parentid);
				}
			}
			firstlist.add(firstmap);
		}
		//此循环把二级分类装填到对应的一级分类下面
		for(int i=0;i<firstlist.size();i++){
			//System.out.println("firstlist1:"+JsonMapper.toJsonString(firstlist.get(i)));
			String id=new String();
			id=(String)firstlist.get(i).get("id");
			List<CategoryTree> second=(List<CategoryTree>)firstlist.get(i).get("secondlist");
			for(int j=0;j<firstTosecond.size();j++){
				String firstid=new String();
				firstid=firstTosecond.get(j).getId();				
				if(firstid.equals(id)){
					second.add(firstTosecond.get(j).getCategoryChild());	
				}
			}
			//System.out.println("firstlist2:"+JsonMapper.toJsonString(firstlist.get(i)));
		}
		
		/*System.out.println("firstlist..."+firstlist.toString());*/
		//把所有的不带重复的知识库数据取出
		List<CategoryTree> libdata=new ArrayList<CategoryTree>();
		if(firstcategoryid!=null&&firstcategoryid.size()>0){
			libdata=categorytreedao.getAllFather(firstcategoryid);
		}
		
		for(int i=0;i<lcl.size();i++){
			String id=lcl.get(i).getId();
			int count=0;
			for(int j=0;j<libdata.size();j++){
				String cid=libdata.get(j).getId();
				if(id.equals(cid)){
					count++;
					break;
				}
			}
			if(count==0){
				libdata.add(lcl.get(i));
			}	
		}
		//System.out.println("firstcategoryid..."+firstcategoryid.toString());
		//System.out.println("libdata..."+libdata.toString()+libdata.size());
		//将一级分类与知识库分类数据整合
		List<Map<String,Object>>  liblist=new ArrayList<Map<String,Object>>();
		for(int i=0;i<libdata.size();i++){
			Map<String,Object> libmap=new HashMap<String,Object>();
			List<Map<String,Object>> first=new ArrayList<Map<String,Object>>();
			libmap.put("id",libdata.get(i).getId());
			libmap.put("name", libdata.get(i).getName());
			libmap.put("image", libdata.get(i).getImage());
			libmap.put("firstlist",first);
			libmap.put("sort",libdata.get(i).getSort());
			liblist.add(libmap);
		}
		//此循环是一级分类装填到对应的知识库中
		/*System.out.println("firstlist..."+firstlist.size());*/
		for(int i=0;i<liblist.size();i++){
			//System.out.println("liblist1:"+JsonMapper.toJsonString(liblist.get(i)));
			String libid=new String();
			libid=(String)liblist.get(i).get("id");
			List<Map<String,Object>> first=(List<Map<String,Object>>)liblist.get(i).get("firstlist");
			for(int j=0;j<firstlist.size();j++){
				String parentid=(String)firstlist.get(j).get("parentid"); //从libToFirst中取得知识库id			
				if(libid.equals(parentid)){
					Map<String,Object> map=new HashMap<String,Object>();
					map=firstlist.get(j);
					first.add(map);
				}
			}
			//System.out.println("liblist2:"+JsonMapper.toJsonString(liblist.get(i)));
		}
		Collections.sort(liblist,new Comparator<Map<String,Object>>(){
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				if(o1.get("sort") ==null || o1.get("sort").equals("")){
					if(o2.get("sort") ==null || o2.get("sort").equals("")){
						return 0;
					}
					return -1;
				}
				if(o2.get("sort") ==null || o2.get("sort").equals("")){
					return 1; 
				}
				int sort1=Integer.valueOf(String.valueOf(o1.get("sort")));
				int sort2=Integer.valueOf(String.valueOf(o2.get("sort")));
				if(sort1 <sort2){
					return -1;
				}else if(sort1 > sort2){
					return 1;
				}else{
					return 0;
				}
			}
		});
		return liblist;
	}
}
