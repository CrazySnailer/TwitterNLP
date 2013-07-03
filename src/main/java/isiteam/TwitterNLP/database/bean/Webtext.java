package isiteam.TwitterNLP.database.bean;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Webtext entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "webtext", catalog = "twittercrawler")
public class Webtext implements java.io.Serializable {

	// Fields

	private Integer id;
	private String title;
	private String content;
	private String segwords;
	private Timestamp pubTime;
	private Integer category;
	private String description;
	private Timestamp insertTime;

	// Constructors

	/** default constructor */
	public Webtext() {
	}

	/** full constructor */
	public Webtext(String title, String content, String segwords,
			Timestamp pubTime, Integer category, String description,
			Timestamp insertTime) {
		this.title = title;
		this.content = content;
		this.segwords = segwords;
		this.pubTime = pubTime;
		this.category = category;
		this.description = description;
		this.insertTime = insertTime;
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

	@Column(name = "title", length = 65535)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "content", length = 65535)
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "segwords", length = 65535)
	public String getSegwords() {
		return this.segwords;
	}

	public void setSegwords(String segwords) {
		this.segwords = segwords;
	}

	@Column(name = "pubTime", length = 19)
	public Timestamp getPubTime() {
		return this.pubTime;
	}

	public void setPubTime(Timestamp pubTime) {
		this.pubTime = pubTime;
	}

	@Column(name = "category")
	public Integer getCategory() {
		return this.category;
	}

	public void setCategory(Integer category) {
		this.category = category;
	}

	@Column(name = "description", length = 65535)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "insertTime", length = 19)
	public Timestamp getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(Timestamp insertTime) {
		this.insertTime = insertTime;
	}

}