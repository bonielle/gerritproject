package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import javax.swing.plaf.TextUI;
import javax.swing.text.ChangedCharSetException;

import org.json.JSONArray;
import org.json.JSONObject;

import control.Main;
import utils.Constants;

public class Changes {

	private int changeSizeInsertions;
	
	private final int POSITIVE_ONE = 1;

	private final int NEGATIVE_ONE = -1;

	private final int POSITIVE_TWO = 2;

	private final int NEGATIVE_TWO = -2;

	private int qntReviewJr;

	private int qntReviewPl;

	private int qntReviewSr;

	private int qntValueOnePositive;

	private int qntValueOneNegative;

	private int qntValueTwoNegative;

	private int qntValueTwoPositive;

	private int totalComments;

	private int totalApprovals;

	private String changeId;

	private String subject;

	private Account owner;

	private Date createdOn;

	private Date lastUpdated;

	private Boolean openToReview;

	private double reviewTime;

	// NEW, DRAFT, SUBMITTED, MERGED, ABANDONED
	private String status;

	private ArrayList<PatchSet> patchSets;

	private Date firstApproval;
	
	private Date datePatchSetApproval;


	private String dataString;

	private static HashSet<String> allOwners = new HashSet<String>();

	public static String[] allOwnersString = {};

	public static Map<String, String> ownersPosition = new HashMap<String, String>();


	public Date getDatePatchSetApproval() {
		return datePatchSetApproval;
	}
	
	public int getQntReviewJr() {
		return qntReviewJr;
	}

	public int getQntReviewPl() {
		return qntReviewPl;
	}

	public int getQntReviewSr() {
		return qntReviewSr;
	}

	public static void showAllOwners() {
		System.out.println(allOwners);
	}

	public int getQntValueOnePositive() {
		return qntValueOnePositive;
	}

	public int getQntValueOneNegative() {
		return qntValueOneNegative;
	}

	public int getQntValueTwoNegative() {
		return qntValueTwoNegative;
	}

	public int getQntValueTwoPositive() {
		return qntValueTwoPositive;
	}

	public double getReviewTime() {
		return reviewTime;
	}

	public String getDataString() {
		return dataString;
	}

	public void setDataString(String dataString) {
		this.dataString = dataString;
	}

	public Date getFirstApproval() {
		return firstApproval;
	}

	public void setFirstApproval(Date firstApproval) {
		this.firstApproval = firstApproval;
	}

	public String getChangeId() {
		return changeId;
	}

	public void setChangeId(Object changeId) {
		if (changeId instanceof String) {
			this.changeId = (String) changeId;
		} else {
			this.changeId = null;
		}
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Account getOwner() {
		return owner;
	}

	public void setOwner(JSONObject owner) {

		Account user = new Account();
		final String name = owner.getString(Constants.NAME);
		final String email = owner.getString(Constants.EMAIL);
		final String userName = owner.getString(Constants.USERNAME);
		user.setEmail(email);
		user.setName(name);
		user.setUsername(userName);
		this.owner = user;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(long timestamp) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		createdOn = new Date(timestamp * Constants.FIX_TIME_ZONE);
	}

	public Date getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(long timestamp) {
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		lastUpdated = new Date(timestamp * Constants.FIX_TIME_ZONE);
	}

	public Boolean getOpenToReview() {
		return openToReview;
	}

	public void setOpenToReview(Boolean openToReview) {
		this.openToReview = openToReview;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<PatchSet> getPatchSets() {
		return patchSets;
	}
	
	public int getReworkPatchsets(){
		return patchSets.size() -1;
	}

	public void setPatchSetsByJSON(JSONArray patchsets) {
		patchSets = PatchSet.setAllPatchSets(patchsets);
	}

	public void setPatchSetsArray(ArrayList<PatchSet> patchs) {
		patchSets = patchs;
	}

	public void showPatchSets() {
		for (Iterator<PatchSet> iterator = patchSets.iterator(); iterator
				.hasNext();) {
			PatchSet patchSet = (PatchSet) iterator.next();
			System.out.println("\n\b Number: " + patchSet.getNumber());
			System.out.println("\b Author: " + patchSet.getAuthor().getName());
			System.out.println("\b Created on: " + patchSet.getCreatedOn());
			System.out.println("\b SizeInsetions: " + patchSet.getSizeInsertions());
			
			System.out.println("\b Approval: ");
			patchSet.showApprovals();
			patchSet.showComments();

		}
	}

	public void setTimeReviewChange() {
		firstApproval = Calendar.getInstance().getTime();

		for (Iterator<PatchSet> iteratorPatch = patchSets.iterator(); iteratorPatch
				.hasNext();) {
			PatchSet patchSet = (PatchSet) iteratorPatch.next();
			ArrayList<Approval> approvals = patchSet.getApprovals();

			if (approvals != null) {
				for (Iterator<Approval> iterator = approvals.iterator(); iterator
						.hasNext();) {
					Approval approval = (Approval) iterator.next();
					Date date = approval.getGrantedOn();

					if (firstApproval.after(date)) {
						firstApproval = date;
						datePatchSetApproval=patchSet.getCreatedOn();
					}
				}
			}

		}

		long milliseconds;

		milliseconds = lastUpdated.getTime() - firstApproval.getTime();

		// if (milliseconds < 0) {
		// milliseconds= lastUpdated.getTime() - createdOn.getTime();
		// }

		reviewTime = (float) TimeUnit.MILLISECONDS.toMinutes(milliseconds);
		reviewTime = reviewTime / 60;

	}

	public void setNumApprovals() {
		for (Iterator<PatchSet> iterator = patchSets.iterator(); iterator
				.hasNext();) {
			PatchSet patchs = (PatchSet) iterator.next();
			ArrayList<Approval> approvals = patchs.getApprovals();

			if (approvals != null ) {						
				totalApprovals +=approvals.size();
			}
		}
	}

	public int getTotalApprovals() {
		return totalApprovals;
	}

	public void setNumComments() {
		for (Iterator<PatchSet> iterator = patchSets.iterator(); iterator
				.hasNext();) {
			PatchSet patchSet = (PatchSet) iterator.next();
			ArrayList<Account> commentsOnReviews = patchSet.getComments();

			if (commentsOnReviews != null) {
				totalComments = totalComments + commentsOnReviews.size();
			}
		}
	}

	public int getTotalComments() {
		return totalComments;
	}

	public void setQntReviewValues() {

		for (Iterator<PatchSet> iteratorPatch = patchSets.iterator(); iteratorPatch
				.hasNext();) {
			PatchSet patchSet = (PatchSet) iteratorPatch.next();
			ArrayList<Approval> approvals = patchSet.getApprovals();

			if (approvals != null) {

				for (Iterator<Approval> iteratorApproval = approvals.iterator(); iteratorApproval
						.hasNext();) {
					Approval patchSetApproval = (Approval) iteratorApproval
							.next();

					switch (patchSetApproval.getValue()) {
					case POSITIVE_ONE:
						qntValueOnePositive++;
						break;

					case NEGATIVE_ONE:
						qntValueOneNegative++;
						break;

					case POSITIVE_TWO:
						qntValueTwoPositive++;
						break;

					case NEGATIVE_TWO:
						qntValueTwoNegative++;
						break;

					default:
						break;
					}
				}
			}
		}
	}

	public static void setAllOwners(String name) {
		allOwners.add(name);
	}

	public static String[] getAllOwners() {
		allOwnersString = allOwners.toArray(allOwnersString);
		return allOwnersString;
	}

	public static void setOwnerPosition() {
		ownersPosition.put(allOwnersString[0], Constants.JUNIOR);
		ownersPosition.put(allOwnersString[1], Constants.PLENO);
		ownersPosition.put(allOwnersString[2], Constants.SENIOR);
		ownersPosition.put(allOwnersString[3], Constants.JUNIOR);
		ownersPosition.put(allOwnersString[4], Constants.PLENO);
		ownersPosition.put(allOwnersString[5], Constants.PLENO);
		ownersPosition.put(allOwnersString[6], Constants.PLENO);
		ownersPosition.put(allOwnersString[7], Constants.SENIOR);
		ownersPosition.put(allOwnersString[8], Constants.SENIOR);
		ownersPosition.put(allOwnersString[9], Constants.SENIOR);
	}

	public void countApprovalsByLevel() {
		for (Iterator<PatchSet> iterator = patchSets.iterator(); iterator
				.hasNext();) {
			PatchSet patchs = (PatchSet) iterator.next();
			ArrayList<Approval> approvals = patchs.getApprovals();

			if (approvals != null) {

				for (Iterator<Approval> iterator2 = approvals.iterator(); iterator2
						.hasNext();) {
					Approval approval = (Approval) iterator2.next();
					String name = approval.getBy().getName();

					if (ownersPosition.containsKey(name)) {

						if (ownersPosition.get(name).equals(Constants.JUNIOR)) {
							qntReviewJr++;
						} else if (ownersPosition.get(name).equals(Constants.PLENO)) {
							qntReviewPl++;
						} else if (ownersPosition.get(name).equals(Constants.SENIOR)) {
							qntReviewSr++;
						}					
						
					}
				}
			}
			
		}
	}
	
	public void countSizeInsertions(){
		for (Iterator<PatchSet> iterator = patchSets.iterator(); iterator
				.hasNext();) {
			PatchSet patchs = (PatchSet) iterator.next();
			changeSizeInsertions+=patchs.getSizeInsertions();			
		}
	}

	public int getChangeSizeInsertions() {
		return changeSizeInsertions;
	}
}
