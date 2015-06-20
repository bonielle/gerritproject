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
	private static final String allAprovalsFile = "C:\\Users\\Gerusa\\workspace\\GerritData\\ginga_approvals.txt";

	public static void main(String[] args) throws RowsExceededException,
			WriteException, IOException {
		
		ArrayList<Changes> changes = UtilResults.returnChanges(allAprovalsFile);

		UtilResults.setChangesValues(changes);

		// clean changes with time <=0
		UtilResults.selectPatchsets(changes);
		Changes.setOwnerPosition();

		UtilResults.countApprovalsByLevel(changes);

		UtilResults.removeOwnerFromList("Tomohiro Shirane",
				Changes.ownersPosition);

		UtilResults.setNivelSixSigma(changes);

		ExcelData.writeExcel(changes);

		//UtilResults.showGeneralInformationChange(changes, searchChange);

	}
}
