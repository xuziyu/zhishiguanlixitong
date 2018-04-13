/**
 * 
 */
package com.yonyou.kms.modules.sys.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yonyou.kms.common.mapper.JsonMapper;
import com.yonyou.kms.common.web.BaseController;
import com.yonyou.kms.modules.cms.entity.Label;
import com.yonyou.kms.modules.cms.entity.UserLabel;
import com.yonyou.kms.modules.cms.service.LabelService;

/**
 * 标签Controller
 * @author hotsum
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/tag")
public class TagController extends BaseController {
	@Autowired
	private LabelService labelservice;
	/**
	 * 树结构选择标签（treeselect.tag）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "treeselect")
	public String treeselect(HttpServletRequest request, Model model) {
		model.addAttribute("url", request.getParameter("url")); 	// 树结构数据URL
		model.addAttribute("extId", request.getParameter("extId")); // 排除的编号ID
		model.addAttribute("checked", request.getParameter("checked")); // 是否可复选
		model.addAttribute("selectIds", request.getParameter("selectIds")); // 指定默认选中的ID
		model.addAttribute("isAll", request.getParameter("isAll")); 	// 是否读取全部数据，不进行权限过滤
		model.addAttribute("module", request.getParameter("module"));	// 过滤栏目模型（仅针对CMS的Category树）
		return "modules/sys/tagTreeselect";
	}
	
	/**
	 * 树结构选择标签（treeselect.tag）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "treeselectlable")
	public String treeselectlable(@ModelAttribute("userlabel")UserLabel usl,HttpServletRequest request, Model model) {
		
		//======
		String newString=JsonMapper.toJsonString(usl.getSelectedTagString());
//		System.out.println("newString"+newString);
		List<String> slist=new ArrayList<String>();
		if(usl.getSelectedTagString()!=null){
			String[] string=usl.getSelectedTagString().split("\\.");
			slist.addAll(Arrays.asList(string));
		}		
//		for(int i=0;i<string.length;i++){
//			System.out.println("newString"+string[i]);
//		}
		//System.out.println("articleId"+articleId);
		String  articleId=usl.getArticleid();
		Label la=new Label();
		la.setLabelvalue(usl.getLabelvalue());
		if(articleId==null || articleId.equals("")){
//			System.out.println("articleId==null || articleId==0");
			List<Label>	label=new ArrayList<Label>();
			label=labelservice.getAllLabel(la);
			List<Label> unexalabel=labelservice.getUnexamineLabel(la);			
			List<UserLabel>	userlabel=new ArrayList<UserLabel>();
			for(int i=0;i<label.size();i++){
				String id=label.get(i).getId();
				//System.out.println("label:"+label.get(i).getLabelvalue());
				for(int j=0;j<slist.size();j++){
					String lid=slist.get(j);
					if(id.equals(lid)){
						label.get(i).setIschecked(1);
					}
				}
				UserLabel ul=new UserLabel();
				ul.setLabelid(label.get(i).getId());
				ul.setLabelvalue(label.get(i).getLabelvalue());
				ul.setIschecked(label.get(i).getIschecked());
				ul.setDelFlag(label.get(i).getDelFlag());
				//System.out.println("userlabel1:"+JsonMapper.toJsonString(ul));
				userlabel.add(ul);
			}
			for(int i=0;i<unexalabel.size();i++){
				String id=unexalabel.get(i).getId();
				//System.out.println("unexalabel:"+unexalabel.get(i).getLabelvalue());
				for(int j=0;j<slist.size();j++){
					String lid=slist.get(j);
					if(id.equals(lid)){
						unexalabel.get(i).setIschecked(1);
					}
				}
				UserLabel ul=new UserLabel();
				ul.setLabelid(unexalabel.get(i).getId());
				StringBuffer sb=new StringBuffer();
				sb.append(unexalabel.get(i).getLabelvalue());
				ul.setLabelvalue(sb.toString());
				ul.setIschecked(unexalabel.get(i).getIschecked());
				ul.setDelFlag(unexalabel.get(i).getDelFlag());
				//System.out.println("userlabel2:"+JsonMapper.toJsonString(ul));
				userlabel.add(ul);
			}
			model.addAttribute("userlabellist", userlabel);
		}else{
			//System.out.println("articleId!=null");
			List<Label>	label=new ArrayList<Label>();
			label=labelservice.getAllLabel(la);
			List<Label> unexalabel=labelservice.getUnexamineLabel(la);
			List<UserLabel>	userlabel=new ArrayList<UserLabel>();
			for(int i=0;i<label.size();i++){
				String id=label.get(i).getId();
				for(int j=0;j<slist.size();j++){
					String lid=slist.get(j);
					if(id.equals(lid)){
						label.get(i).setIschecked(1);
					}
				}
				UserLabel ul=new UserLabel();
				ul.setLabelid(label.get(i).getId());
				ul.setLabelvalue(label.get(i).getLabelvalue());
				ul.setIschecked(label.get(i).getIschecked());
				ul.setDelFlag(label.get(i).getDelFlag());
				//System.out.println("userlabel:"+JsonMapper.toJsonString(ul));
				userlabel.add(ul);
			}
			for(int i=0;i<unexalabel.size();i++){
				String id=unexalabel.get(i).getId();
				for(int j=0;j<slist.size();j++){
					String lid=slist.get(j);
					if(id.equals(lid)){
						unexalabel.get(i).setIschecked(1);
					}
				}
				UserLabel ul=new UserLabel();
				ul.setLabelid(unexalabel.get(i).getId());
				StringBuffer sb=new StringBuffer();
				sb.append(unexalabel.get(i).getLabelvalue());
				ul.setLabelvalue(sb.toString());
				ul.setIschecked(unexalabel.get(i).getIschecked());
				ul.setDelFlag(unexalabel.get(i).getDelFlag());
				//System.out.println("userlabel:"+JsonMapper.toJsonString(ul));
				userlabel.add(ul);
			}
			model.addAttribute("userlabellist",userlabel);
		}
		//======
		model.addAttribute("url", request.getParameter("url")); 	// 树结构数据URL
		model.addAttribute("extId", request.getParameter("extId")); // 排除的编号ID
		model.addAttribute("checked", request.getParameter("checked")); // 是否可复选
		model.addAttribute("selectIds", request.getParameter("selectIds")); // 指定默认选中的ID
		model.addAttribute("isAll", request.getParameter("isAll")); 	// 是否读取全部数据，不进行权限过滤
		model.addAttribute("module", request.getParameter("module"));	// 过滤栏目模型（仅针对CMS的Category树）
		
		return "modules/sys/tagTreeselect2";
	}
	
	/**
	 * 图标选择标签（iconselect.tag）
	 */
	@RequiresPermissions("user")
	@RequestMapping(value = "iconselect")
	public String iconselect(HttpServletRequest request, Model model) {
		model.addAttribute("value", request.getParameter("value"));
		return "modules/sys/tagIconselect";
	}
	
}
