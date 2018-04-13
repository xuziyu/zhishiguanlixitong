/**
 * 
 */
package com.yonyou.kms.modules.cms.entity;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;

import com.google.common.collect.Lists;
import com.yonyou.kms.common.persistence.DataEntity;
import com.yonyou.kms.modules.cms.utils.CmsUtils;
import com.yonyou.kms.modules.sys.entity.User;

/**
 * 文章Entity
 * @author hotsum
 * @version 2013-05-15
 */
public class Article extends DataEntity<Article> {

    public static final String DEFAULT_TEMPLATE = "frontViewArticle";
	
	private static final long serialVersionUID = 1L;
	private Category category;// 分类编号
	private String title;	// 标题
    private String link;	// 外部链接
	private String color;	// 标题颜色（red：红色；green：绿色；blue：蓝色；yellow：黄色；orange：橙色）
	private String image;	// 文章图片
	private String keywords;// 关键字
	private String description;// 描述、摘要
	private Integer weight;	// 权重，越大越靠前
	private Date weightDate;// 权重期限，超过期限，将weight设置为0
	private Integer hits;	// 点击数
	private String posid;	// 推荐位，多选（1：首页焦点图；2：栏目页文章推荐；）
    private String customContentView;	// 自定义内容视图
   	private String viewConfig;	// 视图参数
   	private String attfilenumber;
	private ArticleData articleData;	//文章副表
	//add by luqibao
	private String path;				//文章的全路径
	private Store store;				//文章收藏的信息
	
	//end
	private String isOriginal; 	//是否原创 1:是 0:不是 为转载
	private Date beginDate;	// 开始时间
	private Date endDate;	// 结束时间
	
	private User user;
	
	private User examiner;
	
	//add by linj
	private FeaturePackage featurePackage;//专题包
	
	//add by wuwq
	private String originalreason;    //转载出处
	private String reason;    //下架弃审原因
	/*
	 * 三期需求  需要设置每一篇知识的审核管理员
	 * 
	 * */
	private String auditUserId;
    
	public Article() {
		
		super();
		this.weight = 0;
		this.hits = 0;
		this.posid = "";
		this.delFlag = DEL_FLAG_AUDIT;//默认待审核
		this.isOriginal="1";
		this.examiner=new User();
		this.articleData=new ArticleData();
		//System.out.println("Article().");
	}

	public Article(String id){
		
		this();
		this.id = id;
		//System.out.println("Article(String id)..");
	}
	
	public Article(Category category){
		this();
		this.delFlag = DEL_FLAG_NORMAL;
		this.category = category;
		//System.out.println("Article(Category category)...");
	}
	
	
	
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getOriginalreason() {
		return originalreason;
	}

	public void setOriginalreason(String originalreason) {
		this.originalreason = originalreason;
	}

	public User getExaminer() {
		return examiner;
	}

	public void setExaminer(User examiner) {
		this.examiner = examiner;
	}

	public String getIsOriginal() {
		return isOriginal;
	}

	public void setIsOriginal(String isOriginal) {
		this.isOriginal = isOriginal;
	}

	public void prePersist(){
		//TODO 后续处理，暂不知有何用处
		//super.prePersist();
		articleData.setId(this.id);
	}
	public String getAttfilenumber() {
		return attfilenumber;
	}

	public void setAttfilenumber(String attfilenumber) {
		this.attfilenumber = attfilenumber;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    @Length(min=0, max=255)
    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

	@Length(min=0, max=50)
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Length(min=0, max=255)
	public String getImage() {
		return image;
	}

	public void setImage(String image) {
        this.image = image;//CmsUtils.formatImageSrcToDb(image);
	}

	@Length(min=0, max=255)
	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Length(min=0, max=255)
	public String getDescription() {
		return description;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@NotNull
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Date getWeightDate() {
		return weightDate;
	}

	public void setWeightDate(Date weightDate) {
		this.weightDate = weightDate;
	}

	public Integer getHits() {
		return hits;
	}

	public void setHits(Integer hits) {
		this.hits = hits;
	}

	@Length(min=0, max=10)
	public String getPosid() {
		return posid;
	}

	public void setPosid(String posid) {
		this.posid = posid;
	}

    public String getCustomContentView() {
        return customContentView;
    }

    public void setCustomContentView(String customContentView) {
        this.customContentView = customContentView;
    }

    public String getViewConfig() {
        return viewConfig;
    }

    public void setViewConfig(String viewConfig) {
        this.viewConfig = viewConfig;
    }

	public ArticleData getArticleData() {
		return articleData;
	}

	public void setArticleData(ArticleData articleData) {
		this.articleData = articleData;
	}

	public List<String> getPosidList() {
		List<String> list = Lists.newArrayList();
		if (posid != null){
			for (String s : StringUtils.split(posid, ",")) {
				list.add(s);
			}
		}
		return list;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setPosidList(List<String> list) {
		posid = ","+StringUtils.join(list, ",")+",";
	}

   	public String getUrl() {
        return CmsUtils.getUrlDynamic(this);
   	}

   	public String getImageSrc() {
        return CmsUtils.formatImageSrcToWeb(this.image);
   	}

	public FeaturePackage getFeaturePackage() {
		return featurePackage;
	}

	public void setFeaturePackage(FeaturePackage featurePackage) {
		this.featurePackage = featurePackage;
	}

	public String getAuditUserId() {
		return auditUserId;
	}

	public void setAuditUserId(String auditUserId) {
		this.auditUserId = auditUserId;
	}

	

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Article other = (Article) obj;
		
		return other.getId().equals(this.id) ;
	}

}


