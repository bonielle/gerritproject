package model;

import java.sql.Time;
import java.util.Date;

public class CodeReviewSheet {
	private int patchSet;
	private String changeId;
	private int reviewTime;
	private String dev;
	private String project;
	private Date dateReview;
	private String complexity;

	public int getPatchSet() {
		return patchSet;
	}

	public void setPatchSet(int patchSet) {
		this.patchSet = patchSet;
	}

	public String getChangeId() {
		return changeId;
	}

	public void setChangeId(String changeId) {
		this.changeId = changeId;
	}

	public int getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Time time) {
		reviewTime = time.getMinutes();
	}

	public String getDev() {
		return dev;
	}

	public void setDev(String dev) {
		this.dev = dev;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Date getDateReview() {
		return dateReview;
	}

	public void setDateReview(Date dateReview) {
		this.dateReview = dateReview;
	}

	public String getComplexity() {
		return complexity;
	}

	public void setComplexity(String complexity) {
		this.complexity = complexity;
	}

}
