package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import utils.Constants;

public class PatchSet {

	private int sizeInsertions;
	
	private int number;

	private String revision;

	private Account uploader;

	private Account author;
	
	private Date createdOn;

	private ArrayList<Approval> approvals;

	private ArrayList<Account> comments;

	public ArrayList<Account> getComments() {
		return comments;
	}

	public int getSizeInsertions() {
		return sizeInsertions;
	}

	public void setSizeInsertions(int sizeInsertions) {
		this.sizeInsertions = sizeInsertions;
	}

	public void setComments(ArrayList<Account> comments) {
		this.comments = comments;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public Account getUploader() {
		return uploader;
	}

	public void setUploader(Account uploader) {
		this.uploader = uploader;
	}

	public Account getAuthor() {
		return author;
	}

	public void setAuthor(Account author) {
		this.author = author;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(long timestamp) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

		createdOn = new Date(timestamp * Constants.FIX_TIME_ZONE);
	}

	public ArrayList<Approval> getApprovals() {
		return approvals;
	}

	public void setApprovals(ArrayList<Approval> approvals) {
		this.approvals = approvals;
	}

	public static ArrayList<PatchSet> setAllPatchSets(JSONArray patchsets) {

		ArrayList<PatchSet> patchsArray = new ArrayList<PatchSet>();

		for (int i = 0; i < patchsets.length(); i++) {
			JSONObject obj = (JSONObject) patchsets.get(i);
			PatchSet patch = new PatchSet();

			Account author = new Account();
			author.setInfoFromJSON(obj.getJSONObject(Constants.AUTHOR));
			patch.setAuthor(author);

			patch.setNumber(obj.getInt(Constants.NUMBER));
			patch.setCreatedOn(obj.getInt(Constants.CREATED_ON));

			ArrayList<Approval> allApprovals = new ArrayList<Approval>();

			if (obj.has(Constants.APPROVALS)) {
				JSONArray approvalsJSONArray = obj
						.getJSONArray(Constants.APPROVALS);
				allApprovals = Approval.setAllApprovals(approvalsJSONArray);
				patch.setApprovals(allApprovals);
			}

			ArrayList<Account> allComments = new ArrayList<Account>();

			if (obj.has(Constants.COMMENTS)) {
				JSONArray commentsJSONArray = obj
						.getJSONArray(Constants.COMMENTS);
				allComments = setAllReviewers(commentsJSONArray);
				patch.setComments(allComments);
			}
			
			if (obj.has(Constants.SIZE_INSERTIONS)) {
				patch.setSizeInsertions(obj.getInt(Constants.SIZE_INSERTIONS));
			}

			patchsArray.add(patch);
		}
		return patchsArray;
	}

	private static ArrayList<Account> setAllReviewers(
			JSONArray commentsJSONArray) {
		ArrayList<Account> comments = new ArrayList<Account>();

		for (int i = 0; i < commentsJSONArray.length(); i++) {
			JSONObject obj = (JSONObject) commentsJSONArray.get(i);
			Account author = new Account();

			if (obj.has(Constants.REVIEWER)) {
				author.setInfoFromJSON(obj.getJSONObject(Constants.REVIEWER));
				comments.add(author);
			}
		}
		return comments;
	}

	public void showApprovals() {

		if (approvals != null) {
			for (Iterator<Approval> iterator = approvals.iterator(); iterator
					.hasNext();) {
				Approval review = (Approval) iterator.next();
				System.out.println("\n\t By: " + review.getBy().getName());
				System.out.println("\t Granted on: " + review.getGrantedOn());
				System.out.println("\t Value: " + review.getValue());
				System.out.println("\t Type: " + review.getType());
			}
		}
	}

	public void showComments() {
		if (comments != null) {
			System.out.println("\t Num. comments: " + comments.size());
		} else {
			System.out.println("\t Num. comments: 0");
		}
	}
}
