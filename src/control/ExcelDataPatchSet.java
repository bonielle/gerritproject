package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import model.Approval;
import model.Changes;
import model.CodeReviewSheet;
import model.PatchSet;

import org.apache.poi.ss.util.SheetUtil;

import utils.Constants;
import utils.UtilResults;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;

public class ExcelDataPatchSet {

	private WritableWorkbook workbook;

	private String codeReviewResults;

	private static int columm_change_id = 0;

	private static int columm_patch_number = 1;

	private static int columm_line_count = 2;

	private static int columm_time_review = 3;

	private static int columm_dev = 4;

	private static int columm_complexity = 5;

	private static int columm_project_name = 6;

	private WritableSheet patchSetsReviewTime;

	private int cont = 1;

	private static ExcelDataPatchSet sSingleton;

	private ExcelDataPatchSet(ArrayList<Changes> data, String fileName,
			ArrayList<CodeReviewSheet> codeReviewSeet) throws IOException,
			RowsExceededException, WriteException {
		super();
		codeReviewResults = Changes.class.getName();
		readFile(fileName);
		initializesChangeSheet();
		readChangeResult(data, codeReviewSeet);
		workbook.close();
	}

	private void readChangeResult(ArrayList<Changes> data,
			ArrayList<CodeReviewSheet> codeReviewSeet)
			throws RowsExceededException, WriteException, IOException {

		for (int i = 0; i < codeReviewSeet.size(); i++) {
			readPatchSet(data, codeReviewSeet.get(i), cont);
		}

		workbook.write();
	}

	public void readPatchSet(ArrayList<Changes> data, CodeReviewSheet patchSet,
			int row) {

		Changes change = UtilResults
				.getChangeById(patchSet.getChangeId(), data);
		PatchSet patch = null;
		if (change != null) {
			patch = UtilResults.getPatchSetByNumber(change,
					patchSet.getPatchSet());
		}

		if (change != null && patch != null) {
			cont++;
			Label changeId = new Label(columm_change_id, row,
					change.getChangeId());
			Number patchNumber = new Number(columm_patch_number, row,
					patchSet.getPatchSet());
			Number linesCount = new Number(columm_line_count, row,
					patch.getSizeInsertions());
			Number timeReview = new Number(columm_time_review, row,
					patchSet.getReviewTime());
			Label dev = new Label(columm_dev, row, patchSet.getDev());
			Label complexity = new Label(columm_complexity, row,
					patchSet.getComplexity());
			Label project = new Label(columm_project_name, row,
					patchSet.getProject());
			try {
				patchSetsReviewTime.addCell(changeId);
				patchSetsReviewTime.addCell(patchNumber);
				patchSetsReviewTime.addCell(linesCount);
				patchSetsReviewTime.addCell(timeReview);
				patchSetsReviewTime.addCell(dev);
				patchSetsReviewTime.addCell(complexity);
				patchSetsReviewTime.addCell(project);
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static void writeExcel(ArrayList<Changes> data, String fileName,
			ArrayList<CodeReviewSheet> codeReviewSeet)
			throws RowsExceededException, WriteException, IOException {
		sSingleton = new ExcelDataPatchSet(data, fileName, codeReviewSeet);
	}

	private void readFile(String fileName) throws IOException {
		workbook = Workbook.createWorkbook(new File(fileName));
		patchSetsReviewTime = workbook.createSheet(codeReviewResults, 0);
	}

	public void initializesChangeSheet() {
		Label changeId = new Label(columm_change_id, 0, "Change Id");
		Label patchNumber = new Label(columm_patch_number, 0, "Patch Number");
		Label linesCount = new Label(columm_line_count, 0, "Line Count");
		Label timeRiview = new Label(columm_time_review, 0, "Time Code review (min)");
		Label dev = new Label(columm_dev, 0, "Dev level");
		Label complex = new Label(columm_complexity, 0, "Complexity");
		Label project = new Label(columm_project_name, 0, "Project");

		try {
			patchSetsReviewTime.addCell(changeId);
			patchSetsReviewTime.addCell(patchNumber);
			patchSetsReviewTime.addCell(linesCount);
			patchSetsReviewTime.addCell(timeRiview);
			patchSetsReviewTime.addCell(dev);
			patchSetsReviewTime.addCell(complex);
			patchSetsReviewTime.addCell(project);
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
