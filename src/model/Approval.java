package model;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.Constants;

public class Approval {

	private String type;

	private int value;

	private Date grantedOn;

	private Account by;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public Date getGrantedOn() {
		return grantedOn;
	}

	public void setGrantedOn(int timestamp) {
		grantedOn = new Date(timestamp * Constants.FIX_TIME_ZONE);
	}

	public Account getBy() {
		return by;
	}

	public void setBy(Account by) {
		this.by = by;
	}

	public static ArrayList<Approval> setAllApprovals(JSONArray approvals) {
		ArrayList<Approval> approvalsArray = new ArrayList<Approval>();

		for (int i = 0; i < approvals.length(); i++) {
			JSONObject obj = (JSONObject) approvals.get(i);
			Approval review = new Approval();
			Account author = new Account();

			author.setInfoFromJSON(obj.getJSONObject(Constants.BY_REVIEW));
			String reviewer = author.getName();

			if (!reviewer.equals(Constants.HUDSON_REVIEWER)) {
				review.setGrantedOn(obj.getInt(Constants.GRATED_ON));
				review.setValue(obj.getInt(Constants.VALUE_REVIEW));
				review.setType(obj.getString(Constants.TYPE_REVIEW));
				review.setBy(author);
				approvalsArray.add(review);
			}
		}

		return approvalsArray;
	}

}
