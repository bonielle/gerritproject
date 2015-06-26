package control;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.rmi.CORBA.Util;

import model.Changes;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import utils.Constants;
import utils.UtilResults;

public class Main {

	public static String searchChange = "I09052027acd847acd45b798cce1fbfb3ab069c4d";
	private static final String dataToReadGinga = "C:\\Users\\Gerusa\\Documents\\GitHub\\gerritproject\\ginga_approvals.txt";
	private static final String dataToReadResearch = "C:\\Users\\Gerusa\\Documents\\GitHub\\gerritproject\\codeReviewMeasure.txt";
	private static final String dataResultGinga = "ProjectGinga.xls";
	private static final String dataResultResearch = "ProjectResearch.xls";
	private static final String sheetResearch = "C:\\Users\\Gerusa\\Documents\\GitHub\\gerritproject\\Result.xls";

	public static void main(String[] args) throws RowsExceededException,
			WriteException, IOException {

		UtilResults.createDataExcelFileByChanges(dataToReadGinga,
				dataResultGinga, true);
		UtilResults.createDataExcelFileByPatch(dataToReadResearch,
				dataResultResearch, sheetResearch);

		// UtilResults.showGeneralInformationChange(changes, searchChange);

	}
}
