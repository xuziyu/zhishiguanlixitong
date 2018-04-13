/**
 * 
 */
package com.yonyou.kms.modules.cms.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.yonyou.kms.common.mapper.JsonMapper;
import com.yonyou.kms.common.persistence.BaseEntity;
import com.yonyou.kms.common.persistence.Page;
import com.yonyou.kms.common.utils.StringUtils;
import com.yonyou.kms.common.web.BaseController;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.Comment;
import com.yonyou.kms.modules.cms.entity.Label;
import com.yonyou.kms.modules.cms.service.ArticleCountService;
import com.yonyou.kms.modules.cms.service.CommentService;
import com.yonyou.kms.modules.oa.service.OaNotifyService;
import com.yonyou.kms.modules.sys.utils.DictUtils;
import com.yonyou.kms.modules.sys.utils.UserUtils;

/**
 * 评论Controller
 * @author hotsum
 * @version 2013-3-23
 */
@Controller
@RequestMapping(value = "${adminPath}/cms/comment")
public class CommentController extends BaseController {
	@Autowired
	private CommentService commentService;
	@Autowired
	private ArticleCountService articlecountService;
	@Autowired
	private OaNotifyService oaNotifyService;
	@ModelAttribute
	public Comment get(@RequestParam(required=false) String id) {
		if (org.apache.commons.lang3.StringUtils.isNotBlank(id)){
			return commentService.get(id);
		}else{
			return new Comment();
		}
	}
	@RequiresPermissions("cms:comment:view")
	@RequestMapping(value = {"list", ""})
	public String list(Comment comment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Comment> page = commentService.findPage(new Page<Comment>(request, response), comment); 
        model.addAttribute("page", page);
		return "modules/cms/commentList";
	}
	//add hefeng(初始化 评论管理的方法)
	@RequestMapping(value = "initcomment")
	public String initcomment(@ModelAttribute("comment")Comment comment, HttpServletRequest request, HttpServletResponse response, Model model) {
		Page<Comment> page = commentService.findInitComment(new Page<Comment>(request, response), comment);
//		List<String> categoryids=new ArrayList<String>();
//		if (comment.getCategory()!=null && org.apache.commons.lang3.StringUtils.isNotBlank(comment.getCategory().getId()) && !Category.isRoot(comment.getCategory().getId())){
//			Category category = commentService.getCategory(comment.getCategory().getId());
//			if (category==null){
//				category = new Category();
//			}
//			category.setParentIds(category.getId());
//			category.setSite(category.getSite());
//			categoryids.add(0, category.getId());
//			comment.setCategory(category);
//		}
//		else{
//			comment.setCategory(new Category());
//			categoryids=commentService.commentList(UserUtils.getUser());
//		}
//		comment.getSqlMap().put("dsf", commentService.dataScopeFilter(comment.getCurrentUser(), "o", "u"));//获取当前user
//		comment.setCategoryids(categoryids);
//		Page<Comment> page=commentService.findPage(new Page<Comment>(request, response), comment);
        model.addAttribute("page", page);
		return "modules/cms/commentList";
	}
	//end hefeng
	@RequiresPermissions("cms:comment:edit")
	@RequestMapping(value = "save")
	public String save(Comment comment, RedirectAttributes redirectAttributes) {
		if (beanValidator(redirectAttributes, comment)){
			if (comment.getAuditUser() == null){
				comment.setAuditUser(UserUtils.getUser());
				comment.setAuditDate(new Date());
			}
			comment.setDelFlag(BaseEntity.DEL_FLAG_NORMAL);
			commentService.save(comment);
			//add hefeng
			oaNotifyService.save(oaNotifyService.PretreatmentMsgBeforeSave(comment));
			//end hefeng
			addMessage(redirectAttributes, DictUtils.getDictLabel(comment.getDelFlag(), "cms_del_flag", "保存")
					+"评论'" + StringUtils.abbr(StringUtils.replaceHtml(comment.getContent()),50) + "'成功");
		}
		return "redirect:" + adminPath + "/cms/comment/initcomment?repage&delFlag=2";
	}
	@RequiresPermissions("cms:comment:edit")
	@RequestMapping(value = "delete")
	public String delete(Comment comment, @RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes) {
		commentService.delete(comment, isRe);
		addMessage(redirectAttributes, (isRe!=null&&isRe?"删除":"删除")+"评论成功");
		return "redirect:" + adminPath + "/cms/comment/initcomment?repage&delFlag=2";
	}
	//add zhengyu
	@RequiresPermissions("cms:comment:edit")
	@RequestMapping(value = "change")
	public String change(Comment comment, @RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes) {
		commentService.change(comment, isRe);
		addMessage(redirectAttributes, (isRe!=null&&isRe?"恢复审核":"删除")+"评论成功");
		articlecountService.updateSingleData(4,0,comment.getContentId());
		return "redirect:" + adminPath + "/cms/comment/initcomment?repage&delFlag=0";
	}
	//add hefeng
	@RequestMapping(value = "deleteUserComment")
	public String deleteUserComment(Comment comment, @RequestParam(required=false) Boolean isRe, RedirectAttributes redirectAttributes) {
		commentService.deleteUserComment(comment, isRe);
		addMessage(redirectAttributes, (isRe!=null&&isRe?"删除":"删除")+"评论成功");
		return "redirect:" + adminPath + "/sys/user/userComment/?repage&delFlag="+comment.getDelFlag();
	}
	//end hefeng

}
