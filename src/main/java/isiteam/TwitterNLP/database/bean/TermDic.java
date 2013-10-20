package isiteam.TwitterNLP.database.bean;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * TermDic entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "termDic", catalog = "twittercrawler")
public class TermDic implements java.io.Serializable {

	// Fields

	private Integer id;
	private Integer termId;
	private String term;
	private String termTag;
	private Integer docCount;
	private Double idf;
	private Integer twitterCount;
	private Integer newsCount;
	private Timestamp insertTime;

	// Constructors

	/** default constructor */
	public TermDic() {
	}

	/** minimal constructor */
	public TermDic(Timestamp insertTime) {
		this.insertTime = insertTime;
	}

	/** full constructor */
	public TermDic(Integer termId, String term, String termTag,
			Integer docCount, Double idf, Integer twitterCount,
			Integer newsCount, Timestamp insertTime) {
		this.termId = termId;
		this.term = term;
		this.termTag = termTag;
		this.docCount = docCount;
		this.idf = idf;
		this.twitterCount = twitterCount;
		this.newsCount = newsCount;
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

	@Column(name = "termId")
	public Integer getTermId() {
		return this.termId;
	}

	public void setTermId(Integer termId) {
		this.termId = termId;
	}

	@Column(name = "term", length = 500)
	public String getTerm() {
		return this.term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	@Column(name = "termTag", length = 50)
	public String getTermTag() {
		return this.termTag;
	}

	public void setTermTag(String termTag) {
		this.termTag = termTag;
	}

	@Column(name = "docCount")
	public Integer getDocCount() {
		return this.docCount;
	}

	public void setDocCount(Integer docCount) {
		this.docCount = docCount;
	}

	@Column(name = "idf", precision = 11, scale = 0)
	public Double getIdf() {
		return this.idf;
	}

	public void setIdf(Double idf) {
		this.idf = idf;
	}

	@Column(name = "twitterCount")
	public Integer getTwitterCount() {
		return this.twitterCount;
	}

	public void setTwitterCount(Integer twitterCount) {
		this.twitterCount = twitterCount;
	}

	@Column(name = "newsCount")
	public Integer getNewsCount() {
		return this.newsCount;
	}

	public void setNewsCount(Integer newsCount) {
		this.newsCount = newsCount;
	}

	@Column(name = "insertTime", nullable = false, length = 19)
	public Timestamp getInsertTime() {
		return this.insertTime;
	}

	public void setInsertTime(Timestamp insertTime) {
		this.insertTime = insertTime;
	}

}