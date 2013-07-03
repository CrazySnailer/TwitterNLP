package isiteam.TwitterNLP.database.bean;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * News entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "news", catalog = "twittercrawler", uniqueConstraints = @UniqueConstraint(columnNames = "url_md5"))
public class News implements java.io.Serializable {

	// Fields

	private Integer id;
	private String uuid;
	private String title;
	private String content;
	private String sourceUrl;
	private String site;
	private Timestamp pubtime;
	private String repub;
	private Integer genus;
	private String keywords;
	private Integer keywordsId;
	private String picture;
	private Timestamp insertTime;
	private Integer templateId;
	private Integer channelId;
	private Short language;
	private Integer websiteId;
	private String urlMd5;

	// Constructors

	/** default constructor */
	public News() {
	}

	/** minimal constructor */
	public News(String title, String content, String sourceUrl, String site,
			Timestamp pubtime, Integer genus, Timestamp insertTime,
			Integer templateId, Short language, Integer websiteId) {
		this.title = title;
		this.content = content;
		this.sourceUrl = sourceUrl;
		this.site = site;
		this.pubtime = pubtime;
		this.genus = genus;
		this.insertTime = insertTime;
		this.templateId = templateId;
		this.language = language;
		this.websiteId = websiteId;
	}

	/** full constructor */
	public News(String uuid, String title, String content, String sourceUrl,
			String site, Timestamp pubtime, String repub, Integer genus,
			String keywords, Integer keywordsId, String picture,
			Timestamp insertTime, Integer templateId, Integer channelId,
			Short language, Integer websiteId, String urlMd5) {
		this.uuid = uuid;
		this.title = title;
		this.content = content;
		this.sourceUrl = sourceUrl;
		this.site = site;
		this.pubtime = pubtime;
		this.repub = repub;
		this.genus = genus;
		this.keywords = keywords;
		this.keywordsId = keywordsId;
		this.picture = picture;
		this.insertTime = insertTime;
		this.templateId = templateId;
		this.channelId = channelId;
		this.language = language;
		this.websiteId = websiteId;
		this.urlMd5 = urlMd5;
	}

	// Property accessors
	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "uuid", length = 36)
	public String getUuid() {
		return this.uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	@Column(name = "title", nullable = false, length = 65535)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "content", nullable = false)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "source_url", nullable = false, length = 4096)
	public String getSourceUrl() {
		return this.sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	@Column(name = "site", nullable = false)
	public String getSite() {
		return this.site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	@Column(name = "pubtime", nullable = false, length = 19)
	public Timestamp getPubtime() {
		return this.pubtime;
	}

	public void setPubtime(Timestamp pubtime) {
		this.pubtime = pubtime;
	}

	@Column(name = "repub", length = 128)
	public String getRepub() {
		return this.repub;
	}

	public void setRepub(String repub) {
		this.repub = repub;
	}

	@Column(name = "genus", nullable = false)
	public Integer getGenus() {
		return this.genus;
	}

	public void setGenus(Integer genus) {
		this.genus = genus;
	}

	@Column(name = "keywords")
	public String getKeywords() {
		return this.keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	@Column(name = "keywords_id")
	public Integer getKeywordsId() {
		return this.keywordsId;
	}

	public void setKeywordsId(Integer keywordsId) {
		this.keywordsId = keywordsId;
	}

	@Column(name = "picture", length = 9096)
	public String getPicture() {
		return this.picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	@Column(name = "insert_time", nullable = false, length = 19)
	public Timestamp getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(Timestamp insertTime) {
		this.insertTime = insertTime;
	}

	@Column(name = "template_id", nullable = false)
	public Integer getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}

	@Column(name = "channel_id")
	public Integer getChannelId() {
		return this.channelId;
	}

	public void setChannelId(Integer channelId) {
		this.channelId = channelId;
	}

	@Column(name = "language", nullable = false)
	public Short getLanguage() {
		return this.language;
	}

	public void setLanguage(Short language) {
		this.language = language;
	}

	@Column(name = "website_id", nullable = false)
	public Integer getWebsiteId() {
		return this.websiteId;
	}

	public void setWebsiteId(Integer websiteId) {
		this.websiteId = websiteId;
	}

	@Column(name = "url_md5", unique = true, length = 32)
	public String getUrlMd5() {
		return this.urlMd5;
	}

	public void setUrlMd5(String urlMd5) {
		this.urlMd5 = urlMd5;
	}

}