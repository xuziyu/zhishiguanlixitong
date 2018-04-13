package com.yonyou.kms.modules.cms.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yonyou.kms.common.utils.BaseImg64;
import com.yonyou.kms.common.utils.HtmlFilterUtil;
import com.yonyou.kms.common.utils.IdGen;
import com.yonyou.kms.common.utils.PageUtil;
import com.yonyou.kms.common.utils.ReturnApp;
import com.yonyou.kms.common.utils.StandardData;
import com.yonyou.kms.common.utils.UtilParseJsontoPsw;
import com.yonyou.kms.common.web.BaseController;
import com.yonyou.kms.modules.cms.entity.Article;
import com.yonyou.kms.modules.cms.entity.Category;
import com.yonyou.kms.modules.cms.entity.Comment;
import com.yonyou.kms.modules.cms.entity.FeaturePackage;
import com.yonyou.kms.modules.cms.entity.Label;
import com.yonyou.kms.modules.cms.entity.Share;
import com.yonyou.kms.modules.cms.entity.Thumbs;
import com.yonyou.kms.modules.cms.service.ArticleCountService;
import com.yonyou.kms.modules.cms.service.ArticleLabelService;
import com.yonyou.kms.modules.cms.service.ArticleService;
import com.yonyou.kms.modules.cms.service.CommentService;
import com.yonyou.kms.modules.cms.service.FeaturePackageService;
import com.yonyou.kms.modules.cms.service.LabelService;
import com.yonyou.kms.modules.cms.service.ShareService;
import com.yonyou.kms.modules.cms.service.ThumbsService;
import com.yonyou.kms.modules.sys.entity.User;
import com.yonyou.kms.modules.sys.utils.FileStorageUtils;

import net.sf.json.JSONObject;

/**
 * 
 * app后台接口
 * 
 * @author yangshw6
 *
 */
@Controller
@RequestMapping(value = "${adminPath}/app")
public class phoneController extends BaseController {

	@Autowired
	private ArticleService articleService;
	@Autowired
	private ThumbsService thumbsService;
	@Autowired
	private ShareService shareService;
	@Autowired
	private FeaturePackageService featurePackageService;
	@Autowired
	private ArticleLabelService articleLabelService;
	@Autowired
	private ArticleCountService countService;
	@Autowired
	private CommentService commentService;

	@Autowired
	private LabelService labelService;

	@Value("${web.app.token}")
	private String token;

	@Value("${web.img.basepath}")
	private String imgBasePath;

	@Value("${web.img.realpath}")
	private String realpath;

	@ResponseBody
	@RequestMapping(value = "textAppHttp", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public String textAppHttp(HttpServletRequest request) {

		System.out.println(request.getParameter("text"));
		System.out.println(request.getParameter("data"));
		JSONObject json = new JSONObject();
		json.put("text", "111");
		return json.toString();
	}

	/**
	 * 取出可同步到app的所有专题
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/api/getAllSpeiclByApp.do", method = RequestMethod.POST)
	public ReturnApp getAllSpeiclByApp() {
		ReturnApp returnApp = new ReturnApp();
		List<FeaturePackage> data = new ArrayList<FeaturePackage>();
		try {
			data = featurePackageService.getFeaturePackageByApp();
		} catch (Exception e) {
			return new ReturnApp("1", e.getMessage());
		}
		returnApp.setData(data);
		return returnApp;
	}

	/**
	 * 取出专题(可同步到app)下的可分享的知识
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/api/getListByCondition.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ReturnApp getListBySpecial(@RequestBody StandardData sd) {
		// @RequestParam("key")String key,@RequestParam("condition")String
		// condition 默认不会转码
		/*
		 * String character="^[a-zA-Z]+$";//全字母匹配 String
		 * chinese="^[\u4E00-\u9FA5]+$";//全中文匹配 String key=request.getParameter("key");
		 * String condition=request.getParameter("condition");
		 */
		ReturnApp returnApp = new ReturnApp();
		boolean flag = UtilParseJsontoPsw.parseJson(JSONObject.fromObject(sd));
		if (flag) {
			JSONObject j = JSONObject.fromObject(sd);
			JSONObject k = JSONObject.fromObject(j.get("data"));
			String key = k.optString("key");
			String condition = k.optString("condition");
			JSONObject param = JSONObject.fromObject(condition);
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			String message = "";
			if (key == null || key.equals("") || !key.equals(token)) {
				message = "身份验证失败,无法取得数据";
				return new ReturnApp("1", message);
			}
			int pageSize = 10;
			int pageNo = Integer.valueOf(param.optString("pageNo"));
			int beginnum = (pageNo - 1) * pageSize + 1;
			int endnum = pageNo * pageSize;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("specialid", param.has("specialid") ? param.optString("specialid") : "");
			map.put("label", param.has("label") ? param.optString("label") : "");
			map.put("beginnum", beginnum);
			map.put("endnum", endnum);
			String title = param.has("title") ? param.optString("title") : "";
			if (!title.equals("")) {
				map.put("title", "%" + title + "%");
			}
			try {
				data = articleService.getListByCondition(map);
			} catch (Exception e) {
				e.printStackTrace();
				message = e.getMessage();
				return new ReturnApp("1", message);
			}
			List<Map<String, Object>> lists = new ArrayList<>();
			if (data != null && data.size() > 0) {
				for (Map<String, Object> result : data) {
					String content = String.valueOf(result.get("image"));
					int position = content.indexOf("<img");
					if (position != -1) {
						int lastposition = content.indexOf("/>", position);
						String img = content.substring(position, lastposition + 2);
						int srcfront = img.indexOf("src");
						int srclast = img.indexOf("\"", srcfront + 5);
						img = img.substring(srcfront + 5, srclast);
						// System.out.println(img);
						result.put("image", img);
					} else {
						result.put("image", "");
					}
					// 增加摘要
					String digest = GetDocContent(content);
					if (digest.length() > 200) {
						digest = digest.substring(0, 199);
					}
					result.put("digest", digest);
					Object createtime1 = result.get("createtime") == "" ? "" : result.get("createtime");
					if (null != createtime1 && createtime1 != "") {
						Date date = new Date(String.valueOf(result.get("createtime")));
						SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
						String createtime = dateFm.format(date);
						result.put("createtime", createtime);
					}
					lists.add(result);
				}
			}
			if (lists.size() > 0) {
				returnApp.setData(lists);
			} else {
				return new ReturnApp("1", "没有查询到更多专题");
			}
		} else {
			return new ReturnApp("1", "数据校验失败");
		}
		return returnApp;
	}

	/**
	 * 圈民分享查询接口
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "api/getShareList.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ReturnApp getShareList(@RequestBody StandardData sd) {
		ReturnApp returnApp = new ReturnApp();
		boolean flag = UtilParseJsontoPsw.parseJson(JSONObject.fromObject(sd));
		if (flag) {
			JSONObject j = JSONObject.fromObject(sd);
			JSONObject k = JSONObject.fromObject(j.get("data"));
			String key = k.optString("key");
			String condition = k.optString("condition");
			JSONObject param = JSONObject.fromObject(condition);
			String label = param.optString("label");
			String message = "";
			if (key == null || key.equals("") || !key.equals(token)) {
				message = "身份验证失败,无法取得数据";
				return new ReturnApp("1", message);
			}
			int pageSize = 10;
			int pageNo = Integer.valueOf(param.optString("pageNo"));
			int totalPage = PageUtil.getTotalPage(pageSize);
			if (pageNo > totalPage) {
				pageNo = totalPage;
				returnApp.setErrorMsg("没有更多数据了");
			}
			int beginnum = (pageNo - 1) * pageSize + 1;
			int endnum = pageNo * pageSize;
			List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
			List<Map<String, Object>> resData = new ArrayList<Map<String, Object>>();
			Map<String, Object> map = new HashMap<String, Object>();
			if (null != label && !"".equals(label)) {
				map.put("keyword", "%" + label + "%");
			}
			map.put("beginnum", beginnum);
			map.put("endnum", endnum);
			/* map.put("catogory", endnum); */
			if (null != param.optString("searchkey") && !"".equals(param.optString("searchkey"))) {
				map.put("searchkey", "%" + param.optString("searchkey") + "%");
			}
			try {
				lists = shareService.getListByCondition(map);
			} catch (Exception e) {
				e.printStackTrace();
				message = e.getMessage();
				return new ReturnApp("1", message);
			}
			if (null != lists && lists.size() > 0) {
				for (Map<String, Object> result : lists) {
					Object shareDate = result.get("shareDate");
					String sharedate = shareDate.toString();
					sharedate = sharedate.substring(0, 10);
					Object sharecount = result.get("shareCount");
					Object title = result.get("title");
					Object titilId = result.get("titilId");
					int thumbsNum = countService.getThumbsNumByArticleId(titilId.toString(), title.toString());
					result.put("thumbsNum", thumbsNum);
					result.put("sharedate", sharedate);
					result.put("sharecount", sharecount);
					result.put("title", title);
					result.put("titilId", titilId);
					resData.add(result);
				}
			} else {
				if (pageNo == 1) {
					return new ReturnApp("1", "查询不到数据");
				} else {
					returnApp.setErrorMsg("查询不到数据了");
				}
			}
			returnApp.setData(resData);
		} else {
			return new ReturnApp("1", "数据校验失败");
		}
		return returnApp;
	}

	/**
	 * 知识详情接口
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "api/getArticleById.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ReturnApp getArticleById(@RequestBody StandardData sd) {
		ReturnApp returnApp = new ReturnApp();
		boolean flag = UtilParseJsontoPsw.parseJson(JSONObject.fromObject(sd));
		if (flag) {
			JSONObject j = JSONObject.fromObject(sd);
			JSONObject k = JSONObject.fromObject(j.get("data"));
			String key = k.optString("key");
			String condition = k.optString("condition");
			JSONObject param = JSONObject.fromObject(condition);
			String message = "";
			if (key == null || key.equals("") || !key.equals(token)) {
				message = "身份验证失败,无法取得数据";
				return new ReturnApp("1", message);
			}
			String id = param.optString("id");
			Article article = new Article();
			String copyfrom = new String();
			List<Label> labels = new ArrayList<Label>();
			StringBuffer sbBuffer = new StringBuffer();
			try {
				articleService.updateHitsAddOne(id);
				article = articleService.get(new Article(id));
				List<Comment> comments = commentService.findArticleComment(article.getId());
				String content = "";
				if (comments.size() > 0) {
					for (Comment comment : comments) {
						content = HtmlFilterUtil.getTextFromHtml(comment.getContent());
						System.out.println(content);
						sbBuffer.append(content + "/"
								+ new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(comment.getCreateDate())
								+ "<br/>");
					}
				} else {
					sbBuffer.append("");
				}
				labels = articleLabelService.findLabelByArticle(id);
				copyfrom = article.getArticleData().getCopyfrom();
				if (copyfrom != null && !copyfrom.equals("")) {
					if (!copyfrom.equals("1")) {
						articleService.updateCopyFrom(id, "1");
					}
				} else {
					copyfrom = "0";
					articleService.updateCopyFrom(id, "1");
				}
			} catch (Exception e) {
				return new ReturnApp("1", e.getMessage());
			}
			Thumbs thumbs = thumbsService.getEntity(id, param.optString("userId"));
			if (null == thumbs) {
				thumbs = new Thumbs(article.getId());
				thumbs.setIsThumbs("1");
			}
			Map<String, Object> map = new HashMap<String, Object>();
			if (!returnApp.getErrorCode().equals("1")) {
				// 这里的替换是把ueditor中对iframe的处理腾讯视频的？被转义的转义回来。。
				map.put("content", article.getArticleData().getContent().replaceAll("\\&\\^\\^", "?"));
				map.put("title", article.getTitle());
				map.put("id", article.getId());
				map.put("authorid", article.getCreateBy().getId());
				map.put("authorname", article.getCreateBy().getName());
				map.put("clicknum", article.getHits());
				map.put("comment", sbBuffer.toString());
				String content = String.valueOf(article.getArticleData().getContent());
				int position = content.indexOf("<img");
				if (position != -1) {
					int lastposition = content.indexOf("/>", position);
					String img = content.substring(position, lastposition + 2);
					int srcfront = img.indexOf("src");
					int srclast = img.indexOf("\"", srcfront + 5);
					img = img.substring(srcfront + 5, srclast);
					// System.out.println(img);
					map.put("image", img);
				} else {
					map.put("image", "");
				}
				// 增加摘要
				String digest = GetDocContent(content);
				if (digest.length() > 200) {
					digest = digest.substring(0, 199);
				}
				map.put("digest", digest);
				SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
				Date createDate = article.getCreateDate();
				String createtime = "";
				if (null != createDate) {
					createtime = dateFm.format(createDate);
				}
				map.put("createtime", createtime);
				map.put("isapp", copyfrom);
				// 增加标签
				if (labels != null && labels.size() > 0) {
					StringBuffer sb = new StringBuffer();
					int size = labels.size();
					for (int i = 0; i < size; i++) {
						Label label = labels.get(i);
						if (i == size - 1) {
							sb.append(label);
						} else {
							sb.append(label + ",");
						}
					}
					map.put("label", sb.toString().trim());
				} else {
					map.put("label", "");
				}
				map.put("thumbs", thumbs.getIsThumbs());
				int thumbsNum = countService.getThumbsNumByArticleId(article.getId(), article.getTitle());
				List<Object> resData = new ArrayList<>();
				resData.add(map);
				resData.add(thumbsNum);
				returnApp.setData(resData);
			}
		} else {
			return new ReturnApp("1", "数据校验失败");
		}
		return returnApp;
	}

	/**
	 * 
	 * 更新专题
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/api/saveSpecial.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ReturnApp saveSpecial(@RequestBody StandardData sd) {
		ReturnApp returnApp = new ReturnApp();
		boolean flag = UtilParseJsontoPsw.parseJson(JSONObject.fromObject(sd));
		if (flag) {
			JSONObject j = JSONObject.fromObject(sd);
			JSONObject k = JSONObject.fromObject(j.get("data"));
			String key = k.optString("key");
			String condition = k.optString("condition");
			JSONObject param = JSONObject.fromObject(condition);
			String message = "";
			if (key == null || key.equals("") || !key.equals(token)) {
				message = "身份验证失败,无法取得数据";
				return new ReturnApp("1", message);
			}
			FeaturePackage featurePackage = featurePackageService.get(param.getString("id"));
			featurePackage.setName(param.getString("title"));
			featurePackage.setCanShare(param.has("issync") ? param.getString("issync") : "");

			try {
				featurePackageService.saveFeaturePackage(featurePackage, "0");
			} catch (Exception e) {
				return new ReturnApp("1", e.getMessage());
			}
		} else {
			return new ReturnApp("1", "数据校验失败");
		}
		return returnApp;
	}

	/**
	 * @throws Exception
	 * @throws JSONException
	 * @yangshw6 保存app端的文章到圈民分享中
	 */
	@ResponseBody
	@RequestMapping(value = "/api/reprintArticleByApp.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ReturnApp saveArticleByApp(@RequestBody StandardData sd) {
		ReturnApp returnApp = new ReturnApp();

		JSONObject j = JSONObject.fromObject(sd);
		JSONObject k = JSONObject.fromObject(j.get("data"));
		String key = k.optString("key");
		String condition = k.optString("condition");
		JSONObject param = JSONObject.fromObject(condition);
		String message = "";
		String label = param.optString("label");
		List<String> labelList = new ArrayList<>();
		if (null != label && !"".equals(label)) {
			labelList.add(label);
		}
		if (key == null || key.equals("") || !key.equals(token)) {
			message = "身份验证失败,无法取得数据";
			return new ReturnApp("1", message);
		}
		Article article = new Article();
		article.setId(param.optString("id"));
		article.getArticleData().setContent(param.optString("content"));
		article.setHits(param.optInt("clicknum"));
		article.setTitle(param.optString("title"));
		article.setCreateBy(new User(param.optString("republish")));
		Article ar = articleService.get(article);

		// 数据库有无记录,有记录则存入我的分享,没有记录则暂存
		if (ar == null) {
			article.setDelFlag("2");
			article.setIsOriginal("0");
			article.setCreateBy(new User("1"));
			// article.setExaminer(new User("1"));
			article.getArticleData().setContent(param.optString("content"));
			article.setCategory(new Category("4"));
			article.setOriginalreason("转载于app圈民分享");
			article.getArticleData().setCopyfrom("0");
			try {
				articleService.saveByApp(article, labelList);
			} catch (Exception e) {
				return new ReturnApp("1", e.getMessage());
			}

		} else {
			Share share = new Share();
			share.setTitle(ar.getTitle());
			share.setTitleId(ar.getId());
			share.setCreateBy(ar.getCreateBy());
			share.setOwnlib(ar.getCategory().getId());
			share.setAllowShare("1");
			share.setCategory(ar.getCategory());
			share.setShareDate(new Date());
			try {
				shareService.save(share);
			} catch (Exception e) {
				return new ReturnApp("1", e.getMessage());
			}
		}

		return returnApp;
	}

	/**
	 * @throws Exception
	 * @throws JSONException
	 * @Candy 上传图片
	 */
	@ResponseBody
	@RequestMapping(value = "/api/upLoadImg.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ReturnApp upLoadImg(@RequestBody StandardData sd, HttpServletRequest request) {
		ReturnApp returnApp = new ReturnApp();
		JSONObject j = JSONObject.fromObject(sd);
		// System.out.println(imgBasePath);
		JSONObject k = JSONObject.fromObject(j.get("data"));

		String shareImg = k.optString("shareImg");
		if (StringUtils.isNotBlank(shareImg)) {
			String[] image = shareImg.split(",");
			String suffix = image[0];
			if (suffix.indexOf("data:image/") != -1) {
				suffix = suffix.replace("data:image/", "");
			}
			if (suffix.indexOf(";base64") != -1) {
				suffix = suffix.replace(";base64", "");
			}
			// 即将上传阿里云的临时目录
			String path = FileStorageUtils.kms_tempfile_path;
			// System.out.println(path);
			File files = new File(path);
			if (!files.exists()) {
				try {
					files.mkdir();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// base64写入临时目录形成file文件
			String ss = BaseImg64.generateImage(image[1], files.getPath(), suffix);
			/**************************************************************/
			String imgUrl = "";
			String fileName1 = IdGen.uuid() + "." + suffix;
			try {
				// 调用阿里云上传图片的方法
				imgUrl = FileStorageUtils.putObject(fileName1, files.getPath() + "\\" + ss);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			// System.out.println(imgUrl);
			/*
			 * if (StringUtils.isNotBlank(ss)) { returnApp.setData(realUploadPath+"/" + ss);
			 * }else{ return new ReturnApp("1","上传失败"); }
			 */

			/**************************************************************/
			if (StringUtils.isNotBlank(imgUrl)) {
				returnApp.setData(imgUrl);
			} else {
				returnApp.setErrorCode("1");
				returnApp.setErrorMsg("上传失败");
			}
			// 删除上传临时目录下的临时文件
			FileStorageUtils.deleteFile(files.getPath() + "\\" + ss);
		} else {
			return new ReturnApp("1", "请选择图片");
		}
		return returnApp;
	}

	/**
	 * 文章点赞
	 * 
	 * @throws Exception
	 * @throws JSONException
	 * @Candy
	 */
	@ResponseBody
	@RequestMapping(value = "/api/articelThumbsUp.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ReturnApp articlThumbsUp(@RequestBody StandardData sd) {
		ReturnApp returnApp = new ReturnApp();

		JSONObject j = JSONObject.fromObject(sd);
		JSONObject k = JSONObject.fromObject(j.get("data"));
		String key = k.optString("key");
		String condition = k.optString("condition");
		JSONObject param = JSONObject.fromObject(condition);
		String message = "";
		if (key == null || key.equals("") || !key.equals(token)) {
			message = "身份验证失败,无法取得数据";
			return new ReturnApp("1", message);
		}
		String articleId = param.optString("articleId");
		String userId = param.optString("userId");
		Thumbs thumbs = thumbsService.getEntity(articleId, userId);
		if (null != thumbs) {
			String isThumbs = thumbs.getIsThumbs();
			if ("1".equals(isThumbs)) {
				thumbs.setIsThumbs("0");
				thumbsService.updateEntity(thumbs.getIsThumbs(), thumbs.getThumbsId(), thumbs.getUserId());
			} else if ("0".equals(isThumbs)) {
				thumbs.setIsThumbs("1");
				thumbsService.updateEntity(thumbs.getIsThumbs(), thumbs.getThumbsId(), thumbs.getUserId());
			}
		} else {
			thumbs = new Thumbs(articleId, userId, "0");
			thumbsService.save(thumbs);
		}
		List<Object> resData = new ArrayList<>();
		Article article = articleService.get(new Article(articleId));
		int thumbsNum = countService.getThumbsNumByArticleId(articleId, article.getTitle());
		resData.add(thumbs);
		resData.add(thumbsNum);
		returnApp.setData(resData);
		return returnApp;
	}

	/**
	 * 生成随机token码
	 * 
	 */
	public String createToken(String userId) {
		StringBuffer sb = new StringBuffer(userId);
		long date = new Date().getTime();
		sb.append(date);
		return sb.toString();
	}

	// 从html标签中 截取正文内容
	private static String GetDocContent(String html) {
		html = html.replace("&nbsp;", " ");
		Document doc = Jsoup.parse(html);
		Elements divs = doc.body().getElementsByTag("p");
		int max = -1;
		StringBuffer content = new StringBuffer();
		for (int i = 0; i < divs.size(); i++) {
			Element div = (Element) divs.get(i);
			String divContent = div.text();// GetDivContent(div);
			if (divContent.length() > 0) {
				content.append(divContent);
			}
		}
		return content.toString();
	}

	private static String GetDivContent(Element div) {
		StringBuilder sb = new StringBuilder();
		// 考虑div里标签内容的顺序，对div子树进行深度优先搜索
		Stack<Element> sk = new Stack<Element>();
		sk.push(div);
		while (!sk.empty()) {
			// //
			Element e = sk.pop();
			// //对于div中的div过滤掉
			// if (e != div && e.tagName().equals("div")) continue;
			// //考虑正文被包含在p标签中的情况，并且p标签里不能含有a标签
			// if (e.tagName().equals("p") && e.getElementsByTag("a").size() ==
			// 0) {
			// String className = e.className();
			// if (className.length() != 0 && className.equals("pictext"))
			// continue;
			sb.append(e.text());
			// //sb.append("\n");
			// continue;
			// } else if (e.tagName().equals("td")) {
			// //考虑正文被包含在td标签中的情况
			// if (e.getElementsByTag("div").size() != 0) continue;
			// sb.append(e.text());
			// //sb.append("\n");
			// continue;
			//
			// }
			// //将孩子节点加入栈中
			// Elements children = e.children();
			// for (int i=children.size()-1; i>=0; i--) {
			// sk.push((Element)children.get(i));
			// }
		}

		return sb.toString();
	}

	/********************************
	 * 2017-11-07新增加的方法
	 *********************************/

	/**
	 * 取出所有相关标识下的所有专题
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/api/getAllRelatedPackage.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ReturnApp getAllSpeiclPackage(@RequestBody StandardData sd, HttpServletRequest request) {
		ReturnApp returnApp = new ReturnApp();
		boolean flag = UtilParseJsontoPsw.parseJson(JSONObject.fromObject(sd));
		if (flag) {
			JSONObject j = JSONObject.fromObject(sd);
			JSONObject k = JSONObject.fromObject(j.get("data"));
			String related = k.optString("related");
			if (StringUtils.isNotEmpty(related)) {
				List<FeaturePackage> data = new ArrayList<FeaturePackage>();
				try {
					data = featurePackageService.getcurrentFeaturePackage(Integer.valueOf(related));
				} catch (Exception e) {
					return new ReturnApp("1", e.getMessage());
				}
				List<FeaturePackage> lists = new ArrayList<>();
				if (data.size() > 0) {
					for (FeaturePackage featurePackage : data) {
						String imgpath = imgBasePath + "/static/packagecover/" + related + "/"
								+ featurePackage.getName() + ".jpg";
						String imgpath2 = request.getSession().getServletContext().getRealPath("/")
								+ "/static/packagecover/" + related + "/" + featurePackage.getName() + ".jpg";
						File file = new File(imgpath2);
						if (file.exists()) {
							featurePackage.setImage(imgpath);
						}
						lists.add(featurePackage);
					}
				}
				returnApp.setData(lists);

			} else {
				return new ReturnApp("1", "该目录下无内容");
			}
		} else {
			return new ReturnApp("1", "数据校验失败");
		}

		return returnApp;
	}

	/**
	 * 取出知识圈对应专题(可同步到app)下的可分享的知识
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/api/getKnowledgeListByCondition.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ReturnApp getKnowledgeListByCondition(@RequestBody StandardData sd) {
		ReturnApp returnApp = new ReturnApp();
		boolean flag = UtilParseJsontoPsw.parseJson(JSONObject.fromObject(sd));
		if (flag) {
			JSONObject j = JSONObject.fromObject(sd);
			JSONObject k = JSONObject.fromObject(j.get("data"));
			String key = k.optString("key");
			String condition = k.optString("condition");
			JSONObject param = JSONObject.fromObject(condition);
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			String message = "";
			if (key == null || key.equals("") || !key.equals(token)) {
				message = "身份验证失败,无法取得数据";
				return new ReturnApp("1", message);
			}
			int pageSize = 10;
			int pageNo = Integer.valueOf(param.optString("pageNo"));
			int beginnum = (pageNo - 1) * pageSize + 1;
			int endnum = pageNo * pageSize;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("specialid", param.has("specialid") ? param.optString("specialid") : "");
			map.put("breedType", param.has("breedType") ? "%" + param.optString("breedType") + "%" : "");
			map.put("articleType", param.has("articleType") ? "%" + param.optString("articleType") + "%" : "");
			map.put("beginnum", beginnum);
			map.put("endnum", endnum);
			String title = param.has("title") ? param.optString("title") : "";
			if (!title.equals("")) {
				map.put("title", "%" + title + "%");
			}
			try {
				data = articleService.getKnowledgeListByCondition(map);
			} catch (Exception e) {
				e.printStackTrace();
				message = e.getMessage();
				return new ReturnApp("1", message);
			}
			List<Map<String, Object>> lists = new ArrayList<>();
			if (data != null && data.size() > 0) {
				for (Map<String, Object> result : data) {
					String image = String.valueOf(result.get("image"));
					if (null != image && StringUtils.isNotBlank(image) 
							&& !"null".equals(image)) {
						result.put("image", image);
					} else {
						result.put("image", "");
					}
					// 增加摘要
					String description = String.valueOf(result.get("description"));
					String content = String.valueOf(result.get("content"));
					String digest = "";
					if (null != description && StringUtils.isNotBlank(description) 
							&& !"null".equals(description)) {
						digest = description;
					} else if (null != content && StringUtils.isNotBlank(content) 
							&& !"null".equals(content)) {
						digest = GetDocContent(content);
					}
					if (digest.length() > 200) {
						digest = digest.substring(0, 199);
					}
					result.put("digest", digest);
					Object createtime1 = result.get("createtime") == "" ? "" : result.get("createtime");
					if (null != createtime1 && !"".equals(createtime1)) {
						Date date = new Date(String.valueOf(result.get("createtime")));
						SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
						String createtime = dateFm.format(date);
						result.put("createtime", createtime);
					} else {
						result.put("createtime", "");
					}
					result.remove("content");
					lists.add(result);
				}
			}
			if (lists.size() > 0) {
				returnApp.setData(lists);
			} else {
				if (pageNo > 1) {
					return new ReturnApp("1", "没有下一页啦");
				} else {
					return new ReturnApp("1", "没有查询到相关数据");
				}
			}
		} else {
			return new ReturnApp("1", "数据校验失败");
		}
		return returnApp;
	}

	// 取出知识圈养殖品种和文章分类下的对应名称下的分类
	@ResponseBody
	@RequestMapping(value = "/api/getClassUnderKnowledgeCategory.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ReturnApp getClassUnderKnowledgeCategory(@RequestBody StandardData sd) {
		ReturnApp returnApp = new ReturnApp();
		boolean flag = UtilParseJsontoPsw.parseJson(JSONObject.fromObject(sd));
		if (flag) {
			JSONObject j = JSONObject.fromObject(sd);
			JSONObject k = JSONObject.fromObject(j.get("data"));
			String name = k.optString("name");
			// f1ee1e0c8058469ea7c2910e7c20c293 养殖品种
			// b2cb11a4299240cf91994f49273f3f09 文章类型
			List<Object> resData = new ArrayList<>();
			List<Label> labelList = labelService.getLabelParentIdsByName("f1ee1e0c8058469ea7c2910e7c20c293", name);
			if (labelList.size() > 0) {
				resData.add(labelList);
			} else {
				return new ReturnApp("1", "查询养殖分类失败");
			}
			List<Label> labels = labelService.getLabelParentIds("b2cb11a4299240cf91994f49273f3f09");
			if (labels.size() > 0) {
				resData.add(labels);
			} else {
				return new ReturnApp("1", "查询知识分类失败");
			}
			returnApp.setData(resData);
		} else {
			return new ReturnApp("1", "数据校验失败");
		}
		return returnApp;
	}

	/**
	 * 取出产品大全对应专题(可同步到app)下的可分享的知识
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/api/getProductListByCondition.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ReturnApp getProductListByCondition(@RequestBody StandardData sd) {
		ReturnApp returnApp = new ReturnApp();
		boolean flag = UtilParseJsontoPsw.parseJson(JSONObject.fromObject(sd));
		if (flag) {
			JSONObject j = JSONObject.fromObject(sd);
			JSONObject k = JSONObject.fromObject(j.get("data"));
			String key = k.optString("key");
			String condition = k.optString("condition");
			JSONObject param = JSONObject.fromObject(condition);
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			String message = "";
			if (key == null || key.equals("") || !key.equals(token)) {
				message = "身份验证失败,无法取得数据";
				return new ReturnApp("1", message);
			}
			int pageSize = 10;
			int pageNo = Integer.valueOf(param.optString("pageNo"));
			int beginnum = (pageNo - 1) * pageSize + 1;
			int endnum = pageNo * pageSize;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("specialid", param.has("specialid") ? param.optString("specialid") : "");
			map.put("label", param.has("label") ? "%" + param.optString("label") + "%" : "");
			map.put("beginnum", beginnum);
			map.put("endnum", endnum);
			String title = param.has("title") ? param.optString("title") : "";
			if (!title.equals("")) {
				map.put("title", "%" + title + "%");
			}
			try {
				data = articleService.getProductListByCondition(map);
			} catch (Exception e) {
				e.printStackTrace();
				message = e.getMessage();
				return new ReturnApp("1", message);
			}
			List<Map<String, Object>> lists = new ArrayList<>();
			if (data != null && data.size() > 0) {
				for (Map<String, Object> result : data) {
					String image = String.valueOf(result.get("image"));
					if (null != image && StringUtils.isNotBlank(image) 
							&& !"null".equals(image)) {
						result.put("image", image);
					} else {
						result.put("image", "");
					}
					// 增加摘要
					String description = String.valueOf(result.get("description"));
					String content = String.valueOf(result.get("content"));
					String digest = "";
					if (null != description && StringUtils.isNotBlank(description) 
							&& !"null".equals(description)) {
						digest = description;
					} else if (null != content && StringUtils.isNotBlank(content) 
							&& !"null".equals(content)) {
						digest = GetDocContent(content);
					}
					if (digest.length() > 200) {
						digest = digest.substring(0, 199);
					}
					result.put("digest", digest);
					Object createtime1 = result.get("createtime") == "" ? "" : result.get("createtime");
					if (null != createtime1 && !"".equals(createtime1)) {
						Date date = new Date(String.valueOf(result.get("createtime")));
						SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
						String createtime = dateFm.format(date);
						result.put("createtime", createtime);
					} else {
						result.put("createtime", "");
					}
					lists.add(result);
				}
			}
			if (lists.size() > 0) {
				returnApp.setData(lists);
			} else {
				if (pageNo > 1) {
					return new ReturnApp("1", "没有下一页啦");
				} else {
					return new ReturnApp("1", "没有查询到相关数据");
				}
			}
		} else {
			return new ReturnApp("1", "数据校验失败");
		}
		return returnApp;
	}

	// 取出产品分类下的对应名称下的分类
	@ResponseBody
	@RequestMapping(value = "/api/getClassUnderProductCategory.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ReturnApp getClassUnderProductCategory(@RequestBody StandardData sd) {
		ReturnApp returnApp = new ReturnApp();
		boolean flag = UtilParseJsontoPsw.parseJson(JSONObject.fromObject(sd));
		if (flag) {
			JSONObject j = JSONObject.fromObject(sd);
			JSONObject k = JSONObject.fromObject(j.get("data"));
			String name = k.optString("name");
			// c0ead07126884c18bdc3d062e874dc45 产品品种
			List<Label> labelList = labelService.getLabelParentIdsByName("c0ead07126884c18bdc3d062e874dc45", name);
			if (labelList.size() > 0) {
				returnApp.setData(labelList);
			} else {
				return new ReturnApp("1", "查询分类失败");
			}
		} else {
			return new ReturnApp("1", "数据校验失败");
		}
		return returnApp;
	}

	/**
	 * 取出业务知识对应专题(可同步到app)下的可分享的知识
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "/api/getBusinessListByCondition.do", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
	public ReturnApp getBusinessListByCondition(@RequestBody StandardData sd) {
		ReturnApp returnApp = new ReturnApp();
		boolean flag = UtilParseJsontoPsw.parseJson(JSONObject.fromObject(sd));
		if (flag) {
			JSONObject j = JSONObject.fromObject(sd);
			JSONObject k = JSONObject.fromObject(j.get("data"));
			String key = k.optString("key");
			String condition = k.optString("condition");
			JSONObject param = JSONObject.fromObject(condition);
			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			String message = "";
			if (key == null || key.equals("") || !key.equals(token)) {
				message = "身份验证失败,无法取得数据";
				return new ReturnApp("1", message);
			}
			int pageSize = 10;
			int pageNo = Integer.valueOf(param.optString("pageNo"));
			int beginnum = (pageNo - 1) * pageSize + 1;
			int endnum = pageNo * pageSize;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("specialid", param.has("specialid") ? param.optString("specialid") : "");
			map.put("beginnum", beginnum);
			map.put("endnum", endnum);
			String title = param.has("title") ? param.optString("title") : "";
			if (!title.equals("")) {
				map.put("title", "%" + title + "%");
			}
			try {
				data = articleService.getBusinessListByCondition(map);
			} catch (Exception e) {
				e.printStackTrace();
				message = e.getMessage();
				return new ReturnApp("1", message);
			}
			List<Map<String, Object>> lists = new ArrayList<>();
			if (data != null && data.size() > 0) {
				for (Map<String, Object> result : data) {
					String image = String.valueOf(result.get("image"));
					if (null != image && StringUtils.isNotBlank(image) 
							&& !"null".equals(image)) {
						result.put("image", image);
					} else {
						result.put("image", "");
					}
					// 增加摘要
					String description = String.valueOf(result.get("description"));
					String content = String.valueOf(result.get("content"));
					String digest = "";
					if (null != description && StringUtils.isNotBlank(description) 
							&& !"null".equals(description)) {
						digest = description;
					} else if (null != content && StringUtils.isNotBlank(content) 
							&& !"null".equals(content)) {
						digest = GetDocContent(content);
					}
					if (digest.length() > 200) {
						digest = digest.substring(0, 199);
					}
					result.put("digest", digest);
					Object createtime1 = result.get("createtime") == "" ? "" : result.get("createtime");
					if (null != createtime1 && !"".equals(createtime1)) {
						Date date = new Date(String.valueOf(result.get("createtime")));
						SimpleDateFormat dateFm = new SimpleDateFormat("yyyy-MM-dd");
						String createtime = dateFm.format(date);
						result.put("createtime", createtime);
					} else {
						result.put("createtime", "");
					}
					lists.add(result);
				}
			}
			if (lists.size() > 0) {
				returnApp.setData(lists);
			} else {
				if (pageNo > 1) {
					return new ReturnApp("1", "没有下一页啦");
				} else {
					return new ReturnApp("1", "没有查询到相关数据");
				}
			}
		} else {
			return new ReturnApp("1", "数据校验失败");
		}
		return returnApp;
	}

}
