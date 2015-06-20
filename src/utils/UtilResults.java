package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.Changes;
import model.PatchSet;

import org.json.JSONArray;
import org.json.JSONObject;

public class UtilResults {

	public static ArrayList<Changes> returnChanges(String filePath) {
		BufferedReader br = null;
		String sCurrentLine = null;
		ArrayList<Changes> changes = new ArrayList<Changes>();

		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((sCurrentLine = br.readLine()) != null
					&& sCurrentLine.contains("project")) {
				changes.add(getAllApprovalsValues(sCurrentLine));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		return changes;
	}

	public static Changes getAllApprovalsValues(String json_string) {

		JSONObject my_obj = new JSONObject(json_string);

		Object changeId = my_obj.get(Constants.CHANGE_ID);
		String subject = my_obj.getString(Constants.SUBJECT);
		JSONObject owner = my_obj.getJSONObject(Constants.OWNER);
		String status = my_obj.getString(Constants.STATUS);
		Long createdOn = my_obj.getLong(Constants.CREATED_ON);
		Long lastUpdated = my_obj.getLong(Constants.LAST_UPDATED);
		Boolean openToReview = my_obj.getBoolean(Constants.OPEN_TO_REVIEW);
		JSONArray patchSets = my_obj.getJSONArray(Constants.PATCH_SETS);

		Changes change = new Changes();
		change.setChangeId(changeId);
		change.setSubject(subject);
		change.setOwner(owner);
		change.setStatus(status);
		change.setCreatedOn(createdOn);
		change.setLastUpdated(lastUpdated);
		change.setOpenToReview(openToReview);
		change.setPatchSetsByJSON(patchSets);

		return change;
	}

	public static String[] getValuesJSON(String filePath) {
		String[] qntValues = null;

		BufferedReader br = null;
		String sCurrentLine = null;

		try {
			br = new BufferedReader(new FileReader(filePath));
			if ((sCurrentLine = br.readLine()) != null) {
				JSONObject my_obj = new JSONObject(sCurrentLine);
				qntValues = JSONObject.getNames(my_obj);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		for (int i = 0; i < qntValues.length; i++) {
			System.out.println("Json Value: " + qntValues[i]);
		}
		return qntValues;
	}

	public static ArrayList<Changes> changesOnlyOnUploader(List<Changes> data) {
		ArrayList<Changes> resultChanges = new ArrayList<Changes>();

		for (Iterator<Changes> iterator = data.iterator(); iterator.hasNext();) {
			Changes changes = (Changes) iterator.next();
			ArrayList<PatchSet> patchSets = changes.getPatchSets();
			HashSet<String> uploaders = new HashSet<String>();

			for (Iterator<PatchSet> iterator2 = patchSets.iterator(); iterator2
					.hasNext();) {
				PatchSet patchSet = (PatchSet) iterator2.next();
				uploaders.add(patchSet.getAuthor().getName());
			}

			if (uploaders.size() == 1) {
				resultChanges.add(changes);
			}
		}

		return resultChanges;
	}

	public static ArrayList<Changes> selectPatchsets(ArrayList<Changes> changes) {
		ArrayList<Changes> changesCleaned = cleanChanges(changes);

		for (Iterator<Changes> iterator = changesCleaned.iterator(); iterator
				.hasNext();) {
			Changes change = (Changes) iterator.next();
			ArrayList<PatchSet> oldPatchs = change.getPatchSets();
			ArrayList<PatchSet> newPatchs = new ArrayList<PatchSet>();

			for (Iterator<PatchSet> iterator2 = oldPatchs.iterator(); iterator2
					.hasNext();) {
				PatchSet patchSet = (PatchSet) iterator2.next();

				if (!patchSet.getCreatedOn().before(
						change.getDatePatchSetApproval())) {
					newPatchs.add(patchSet);
				}
			}
			change.setPatchSetsArray(newPatchs);
		}
		return changesCleaned;
	}

	public static ArrayList<Changes> cleanChanges(ArrayList<Changes> changes) {
		ArrayList<Changes> changesCleaned = new ArrayList<Changes>();

		for (Iterator<Changes> iterator = changes.iterator(); iterator
				.hasNext();) {
			Changes change = (Changes) iterator.next();

			if (change.getReviewTime() > 0) {
				changesCleaned.add(change);
			}

		}
		return changesCleaned;
	}

	public static void removeOwnerFromList(String owner,
			Map<String, String> list) {

		if (list.containsKey(owner)) {
			list.remove(owner);
		}
	}
	

	public static void setNivelSixSigma(ArrayList<Changes> changes) {

		int kpiTime = 0;
		int kpiPatch = 0;

		for (Iterator<Changes> iterator = changes.iterator(); iterator
				.hasNext();) {
			Changes data = (Changes) iterator.next();

			if (data.getReviewTime() <= 50) {
				kpiTime++;
			}

			if (data.getReworkPatchsets() <= 3) {
				kpiPatch++;
			}
		}

		double nivelSigmaTimeReview = ((double) kpiTime / (double) changes
				.size()) * (double) 100;

		double nivelSigmaTimePatch = ((double) kpiPatch / (double) changes
				.size()) * (double) 100;

		System.out.println("% Nivel sigma by Time Review : "
				+ nivelSigmaTimeReview);
		System.out.println("% Nivel sigma by Patchsets : "
				+ nivelSigmaTimePatch);
	}

	public static void countApprovalsByLevel(ArrayList<Changes> change) {
		for (Iterator<Changes> iterator = change.iterator(); iterator.hasNext();) {
			Changes data = (Changes) iterator.next();
			data.countApprovalsByLevel();
		}
	}

	public static void setChangesValues(ArrayList<Changes> changes) {

		for (Iterator<Changes> iterator = changes.iterator(); iterator
				.hasNext();) {
			Changes data = (Changes) iterator.next();
			data.setTimeReviewChange();
			data.setNumApprovals();
			data.setNumComments();
			data.setQntReviewValues();
			data.countSizeInsertions();
			Changes.setAllOwners(data.getOwner().getName());
			Changes.getAllOwners();
		}
	}
	

	public static void showGeneralInformationChange(List<Changes> changes,
			String change) {

		for (int selectChange = 0; selectChange < changes.size(); selectChange++) {
			String id = changes.get(selectChange).getChangeId();

			if (id.equals(change)) {
				System.out.println("Subject: "
						+ changes.get(selectChange).getSubject());
				System.out.println("Size Insertions: "
						+ changes.get(selectChange).getChangeSizeInsertions());
				System.out.println("ChangeId: "
						+ changes.get(selectChange).getChangeId().toString());
				System.out.println("Owner: "
						+ changes.get(selectChange).getOwner().getName());
				System.out.println("CreatedOn: "
						+ changes.get(selectChange).getCreatedOn());
				System.out.println("LastUpdated: "
						+ changes.get(selectChange).getLastUpdated());
				System.out.println("FirstReview: "
						+ changes.get(selectChange).getFirstApproval());
				System.out.println("Status: "
						+ changes.get(selectChange).getStatus());
				System.out.println("Patchsets rework: "
						+ changes.get(selectChange).getPatchSets().size());
				System.out.println("Time review: "
						+ changes.get(selectChange).getReviewTime());
				System.out.println("Total approvals: "
						+ changes.get(selectChange).getTotalApprovals());
				System.out.println("Total +1: "
						+ changes.get(selectChange).getQntValueOnePositive());
				System.out.println("Total -1: "
						+ changes.get(selectChange).getQntValueOneNegative());
				System.out.println("Total +2: "
						+ changes.get(selectChange).getQntValueTwoPositive());
				System.out.println("Total -2: "
						+ changes.get(selectChange).getQntValueTwoNegative());
				changes.get(selectChange).showPatchSets();

			}
		}
	}
}
