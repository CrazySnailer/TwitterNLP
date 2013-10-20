package isiteam.TwitterNLP.database.bean;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Testset entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "testset", catalog = "twittercrawler")
public class Testset implements java.io.Serializable {

	// Fields

	private Integer id;
	private String tittle;
	private String content;
	private Timestamp pubTime;
	private String userId;
	private String posContent;
	private String optimalPosContent;
	private String termFrequence;
	private String termVector;
	private Integer type;
	private Timestamp insertTime;

	// Constructors

	/** default constructor */
	public Testset() {
	}

	/** minimal constructor */
	public Testset(Timestamp insertTime) {
		this.insertTime = insertTime;
	}

	/** full constructor */
	public Testset(String tittle, String content, Timestamp pubTime,
			String userId, String posContent, String optimalPosContent,
			String termFrequence, String termVector, Integer type,
			Timestamp insertTime) {
		this.tittle = tittle;
		this.content = content;
		this.pubTime = pubTime;
		this.userId = userId;
		this.posContent = posContent;
		this.optimalPosContent = optimalPosContent;
		this.termFrequence = termFrequence;
		this.termVector = termVector;
		this.type = type;
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

	@Column(name = "tittle", length = 1000)
	public String getTittle() {
		return this.tittle;
	}

	public void setTittle(String tittle) {
		this.tittle = tittle;
	}

	@Column(name = "content")
	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "pubTime", length = 19)
	public Timestamp getPubTime() {
		return this.pubTime;
	}

	public void setPubTime(Timestamp pubTime) {
		this.pubTime = pubTime;
	}

	@Column(name = "userId", length = 30)
	public String getUserId() {
		return this.userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "posContent")
	public String getPosContent() {
		return this.posContent;
	}

	public void setPosContent(String posContent) {
		this.posContent = posContent;
	}

	@Column(name = "optimalPosContent")
	public String getOptimalPosContent() {
		return this.optimalPosContent;
	}

	public void setOptimalPosContent(String optimalPosContent) {
		this.optimalPosContent = optimalPosContent;
	}

	@Column(name = "termFrequence")
	public String getTermFrequence() {
		return this.termFrequence;
	}

	public void setTermFrequence(String termFrequence) {
		this.termFrequence = termFrequence;
	}

	@Column(name = "termVector")
	public String getTermVector() {
		return this.termVector;
	}

	public void setTermVector(String termVector) {
		this.termVector = termVector;
	}

	@Column(name = "type")
	public Integer getType() {
		return this.type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "insertTime", nullable = false, length = 19)
	public Timestamp getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(Timestamp insertTime) {
		this.insertTime = insertTime;
	}

}