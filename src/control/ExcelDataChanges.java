package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import model.Approval;
import model.Changes;
import model.PatchSet;
import utils.Constants;
import utils.UtilResults;

public class ExcelDataChanges {

	private final int changeIdColumm = 0;

	private final int value_negative_two_columm = 1;

	private final int value_negative_one_columm = 2;

	private final int value_negative = 1;

	private final int value_media_negative = 2;

	private final int value_positive_two_columm = 3;

	private final int value_positive_one_columm = 4;

	private final int qnt_comments_columm = 5;

	private final int time_review_columm = 6;

	private final int qnt_reviewer_columm = 7;

	private final int qnt_patch_columm = 8;

	private final int owner_columm = 9;

	private int qntReviewJr = 10;

	private int qntReviewPl = 11;

	private int qntReviewSr = 12;

	private int sizeInsertions = 13;

	private WritableWorkbook workbook;

	private final String changesResults;

	private WritableSheet sheetChangeResults;

	private WritableSheet sheetJunior;

	private WritableSheet sheetIntern;

	private WritableSheet sheetPleno;

	private WritableSheet sheetMaster;

	private WritableSheet sheetSenior;

	private WritableSheet sheetApprovals;

	private static ExcelDataChanges sSingleton;

	private ExcelDataChanges(List<Changes> data, String fileName) throws IOException,
			RowsExceededException, WriteException {
		super();
		changesResults = Changes.class.getName();
		readFile(fileName);
		initializesChangeSheet();
		initializesPatchSetSheet(sheetIntern);
		initializesPatchSetSheet(sheetJunior);
		initializesPatchSetSheet(sheetSenior);
		initializesPatchSetSheet(sheetPleno);
		initializesPatchSetSheet(sheetMaster);

		initializesApprovals(sheetApprovals);

		readChangeResult(data);
		workbook.close();
	}

	public static void writeExcel(List<Changes> data, String fileName)
			throws RowsExceededException, WriteException, IOException {
		sSingleton = new ExcelDataChanges(data,fileName);
	}

	private void readChangeResult(List<Changes> data)
			throws RowsExceededException, WriteException, IOException {

		for (int i = 0; i < data.size(); i++) {
			writeChangeResult(data.get(i), (i + 1));
		}

		readPatchSetInfo(data);

		workbook.write();
	}

	private void writeChangeResult(Changes data, int row) throws IOException,
			RowsExceededException, WriteException {
		Label owner = new Label(owner_columm, row, data.getOwner().getName());

		Label changeId = new Label(changeIdColumm, row, data.getChangeId());

		Number value_positive_one = new Number(value_positive_one_columm, row,
				data.getQntValueOnePositive());
		Number value_negative_one = new Number(value_negative_one_columm, row,
				data.getQntValueOneNegative());
		Number value_positive_two = new Number(value_positive_two_columm, row,
				data.getQntValueTwoPositive());
		Number value_negative_two = new Number(value_negative_two_columm, row,
				data.getQntValueTwoNegative());
		Number total_comments = new Number(qnt_comments_columm, row,
				data.getTotalComments());
		Number review_time = new Number(time_review_columm, row,
				data.getReviewTime());

		Number qnt_reviewer_approval = new Number(qnt_reviewer_columm, row,
				data.getTotalApprovals());

		Number qnt_patch_sets = new Number(qnt_patch_columm, row,
				data.getReworkPatchsets());

		Number qntJrApproval = new Number(qntReviewJr, row,
				data.getQntReviewJr());

		Number qntSrApproval = new Number(qntReviewSr, row,
				data.getQntReviewSr());

		Number qntPlApproval = new Number(qntReviewPl, row,
				data.getQntReviewPl());

		Number size_insertions = new Number(sizeInsertions, row,
				data.getChangeSizeInsertions());

		sheetChangeResults.addCell(size_insertions);

		sheetChangeResults.addCell(qntJrApproval);
		sheetChangeResults.addCell(qntSrApproval);
		sheetChangeResults.addCell(qntPlApproval);

		sheetChangeResults.addCell(owner);
		sheetChangeResults.addCell(qnt_patch_sets);
		sheetChangeResults.addCell(qnt_reviewer_approval);
		sheetChangeResults.addCell(changeId);
		sheetChangeResults.addCell(value_positive_one);
		sheetChangeResults.addCell(value_negative_one);
		sheetChangeResults.addCell(value_positive_two);
		sheetChangeResults.addCell(value_negative_two);
		sheetChangeResults.addCell(total_comments);
		sheetChangeResults.addCell(review_time);
	}

	public void initializesChangeSheet() throws RowsExceededException,
			WriteException {

		Label owner_label = new Label(owner_columm, 0, "Owner");
		Label change_label = new Label(changeIdColumm, 0, "Change Id");
		Label value_positive_one_label = new Label(value_positive_one_columm,
				0, "PatchSets +1");
		Label value_negative_one_label = new Label(value_negative_one_columm,
				0, "PatchSets -1");
		Label value_positive_two_label = new Label(value_positive_two_columm,
				0, "PatchSets +2");
		Label value_negative_two_label = new Label(value_negative_two_columm,
				0, "PatchSets -2");
		Label qnt_comments_change = new Label(qnt_comments_columm, 0,
				"Qnt comments");
		Label review_time = new Label(time_review_columm, 0, "Review time");

		Label qnt_reviewer_approval = new Label(qnt_reviewer_columm, 0,
				"Num reviewed");

		Label qnt_jr_approval = new Label(qntReviewJr, 0, "Qnt Jr approval");

		Label qnt_pl_approval = new Label(qntReviewPl, 0, "Qnt Pl approval");

		Label qnt_sr_approval = new Label(qntReviewSr, 0, "Qnt Sr approval");

		Label size_insertions = new Label(sizeInsertions, 0, "Size Insertions");

		Label qnt_patch_sets = new Label(qnt_patch_columm, 0, "Qnt Patchsets");

		sheetChangeResults.addCell(size_insertions);
		sheetChangeResults.addCell(qnt_jr_approval);
		sheetChangeResults.addCell(qnt_pl_approval);
		sheetChangeResults.addCell(qnt_sr_approval);
		sheetChangeResults.addCell(owner_label);
		sheetChangeResults.addCell(qnt_patch_sets);
		sheetChangeResults.addCell(change_label);
		sheetChangeResults.addCell(value_positive_one_label);
		sheetChangeResults.addCell(value_negative_one_label);
		sheetChangeResults.addCell(value_positive_two_label);
		sheetChangeResults.addCell(value_negative_two_label);
		sheetChangeResults.addCell(qnt_comments_change);
		sheetChangeResults.addCell(review_time);
		sheetChangeResults.addCell(qnt_reviewer_approval);
	}

	private void readFile(String fileName) throws IOException {
		workbook = Workbook.createWorkbook(new File(fileName));
		sheetChangeResults = workbook.createSheet(changesResults, 0);
		sheetIntern = workbook.createSheet(Constants.INTERNSHIP, 1);
		sheetJunior = workbook.createSheet(Constants.JUNIOR, 2);
		sheetPleno = workbook.createSheet(Constants.PLENO, 3);
		sheetSenior = workbook.createSheet(Constants.SENIOR, 4);
		sheetMaster = workbook.createSheet(Constants.MASTER, 5);
		sheetApprovals = workbook.createSheet(Constants.APPROVALS_BY_DEV, 6);
	}

	private void writePatchInfo(int r, Changes change, WritableSheet sheet)
			throws RowsExceededException, WriteException, IOException {

		Label change_label = new Label(changeIdColumm, r, change.getChangeId());
		Label owner = new Label(owner_columm, r, change.getOwner().getName());

		Number value_positive_one_label = new Number(value_positive_one_columm,
				r, change.getQntValueOnePositive());
		Number value_negative_one_label = new Number(value_negative_one_columm,
				r, change.getQntValueOneNegative());
		Number value_positive_two_label = new Number(value_positive_two_columm,
				r, change.getQntValueTwoPositive());
		Number value_negative_two_label = new Number(value_negative_two_columm,
				r, change.getQntValueTwoNegative());
		Number qnt_comments_change = new Number(qnt_comments_columm, r,
				change.getTotalComments());
		Number review_time = new Number(time_review_columm, r,
				change.getReviewTime());

		Number qnt_reviewer_approval = new Number(qnt_reviewer_columm, r,
				change.getTotalApprovals());

		Number qnt_patch_sets = new Number(qnt_patch_columm, r,
				change.getReworkPatchsets());

		sheet.addCell(qnt_patch_sets);
		sheet.addCell(owner);
		sheet.addCell(change_label);
		sheet.addCell(value_positive_one_label);
		sheet.addCell(value_negative_one_label);
		sheet.addCell(value_positive_two_label);
		sheet.addCell(value_negative_two_label);
		sheet.addCell(qnt_comments_change);
		sheet.addCell(review_time);
		sheet.addCell(qnt_reviewer_approval);
	}

	private void readApprovals(List<Changes> data)
			throws RowsExceededException, WriteException, IOException {

		ArrayList<Changes> changesOnlyUploader = UtilResults
				.changesOnlyOnUploader(data);
		Map<String, String> ownersPosition = Changes.ownersPosition;

		int qntNegaviveOneApprovalJr = 0;

		int qntNegaviveOneApprovalPl = 0;

		int qntNegaviveOneApprovalSr = 0;

		for (Iterator iterator = changesOnlyUploader.iterator(); iterator
				.hasNext();) {
			Changes changes = (Changes) iterator.next();
			ArrayList<PatchSet> allPatchsets = changes.getPatchSets();

			if (!allPatchsets.isEmpty() && allPatchsets != null) {
				for (Iterator iterator2 = allPatchsets.iterator(); iterator2
						.hasNext();) {
					PatchSet patchSet = (PatchSet) iterator2.next();
					ArrayList<Approval> allApprovals = patchSet.getApprovals();

					if (allApprovals != null && !allApprovals.isEmpty()) {

						for (Iterator iterator3 = allApprovals.iterator(); iterator3
								.hasNext();) {
							Approval approval = (Approval) iterator3.next();

							if (approval.getValue() == -1) {
								String approvalBy = approval.getBy().getName();

								if (ownersPosition.containsKey(approvalBy)) {

									String ownerPosition = ownersPosition
											.get(approvalBy);

									if (ownerPosition.equals(Constants.JUNIOR)) {
										qntNegaviveOneApprovalJr++;

									} else if (ownerPosition
											.equals(Constants.PLENO)) {
										qntNegaviveOneApprovalPl++;

									} else if (ownerPosition
											.equals(Constants.SENIOR)) {
										qntNegaviveOneApprovalSr++;
									}
								}

							}

						}
					}
				}
			}

		}

		Label junior = new Label(0, 1, "Junior");
		Label pleno = new Label(0, 2, "Pleno");
		Label senior = new Label(0, 3, "Senior");

		Number junior_qnt = new Number(1, 1, qntNegaviveOneApprovalJr);
		Number pleno_qnt = new Number(1, 2, qntNegaviveOneApprovalPl);
		Number senior_qnt = new Number(1, 3, qntNegaviveOneApprovalSr);

		sheetApprovals.addCell(junior);
		sheetApprovals.addCell(pleno);
		sheetApprovals.addCell(senior);
		sheetApprovals.addCell(junior_qnt);
		sheetApprovals.addCell(pleno_qnt);
		sheetApprovals.addCell(senior_qnt);

		workbook.write();
	}

	private void readPatchSetInfo(List<Changes> data)
			throws RowsExceededException, WriteException, IOException {
		ArrayList<Changes> changesOnlyUploader = UtilResults
				.changesOnlyOnUploader(data);
		Changes.getAllOwners();
		Changes.setOwnerPosition();
		Map<String, String> ownersPosition = Changes.ownersPosition;
		int contIntern = 1;
		int contJr = 1;
		int contPl = 1;
		int contSr = 1;
		int contMs = 1;

		for (Iterator<Changes> iterator = changesOnlyUploader.iterator(); iterator
				.hasNext();) {

			Changes change = (Changes) iterator.next();
			String owner = change.getOwner().getName();

			if (ownersPosition.containsKey(owner)) {

				String ownerPosition = ownersPosition.get(owner);
				if (ownerPosition.equals(Constants.INTERNSHIP)) {
					writePatchInfo(contIntern, change, sheetIntern);
					contIntern++;
				} else if (ownerPosition.equals(Constants.JUNIOR)) {
					writePatchInfo(contJr, change, sheetJunior);
					contJr++;
				} else if (ownerPosition.equals(Constants.PLENO)) {
					writePatchInfo(contPl, change, sheetPleno);
					contPl++;
				} else if (ownerPosition.equals(Constants.SENIOR)) {
					writePatchInfo(contSr, change, sheetSenior);
					contSr++;
				} else if (ownerPosition.equals(Constants.MASTER)) {
					writePatchInfo(contMs, change, sheetMaster);
					contMs++;
				}
			}
		}

		readApprovals(data);
		workbook.write();
	}

	public void initializesApprovals(WritableSheet sheet)
			throws RowsExceededException, WriteException {

		Label change_label = new Label(changeIdColumm, 0, "Dev level");
		Label value_positive_one_label = new Label(value_negative, 0,
				"Qnt. Negative");
		Label value_negative_one_label = new Label(value_media_negative, 0,
				"Media negative");

		sheet.addCell(change_label);
		sheet.addCell(value_negative_one_label);
		sheet.addCell(value_positive_one_label);

	}

	public void initializesPatchSetSheet(WritableSheet sheet)
			throws RowsExceededException, WriteException {

		Label change_label = new Label(changeIdColumm, 0, "Change Id");
		Label value_positive_one_label = new Label(value_positive_one_columm,
				0, "PatchSets +1");
		Label value_negative_one_label = new Label(value_negative_one_columm,
				0, "PatchSets -1");
		Label value_positive_two_label = new Label(value_positive_two_columm,
				0, "PatchSets +2");
		Label value_negative_two_label = new Label(value_negative_two_columm,
				0, "PatchSets -2");
		Label qnt_comments_change = new Label(qnt_comments_columm, 0,
				"Qnt comments");
		Label review_time = new Label(time_review_columm, 0, "Review time");

		Label qnt_reviewer_approval = new Label(qnt_reviewer_columm, 0,
				"Num reviewed");

		Label qnt_patch_sets = new Label(qnt_patch_columm, 0, "Qnt Patchsets");

		Label owner = new Label(owner_columm, 0, "Owner");

		sheet.addCell(qnt_patch_sets);
		sheet.addCell(change_label);
		sheet.addCell(value_positive_one_label);
		sheet.addCell(value_negative_one_label);
		sheet.addCell(value_positive_two_label);
		sheet.addCell(value_negative_two_label);
		sheet.addCell(qnt_comments_change);
		sheet.addCell(review_time);
		sheet.addCell(owner);
		sheet.addCell(qnt_reviewer_approval);
	}
}
