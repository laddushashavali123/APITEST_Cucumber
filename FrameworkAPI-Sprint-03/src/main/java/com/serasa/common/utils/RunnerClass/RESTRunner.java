package com.serasa.common.utils.RunnerClass;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.serasa.reports.ReportJson;

public class RESTRunner {
	
	public static void main(String[] args) {
		
		// generate report dir name
		DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		Date date = new Date();
		String reportDirName = "Run " + sdf.format(date);
		String reportRoot = "../REPORTS/";
		String reportDir = reportRoot + reportDirName + "/";

		String execFeaturesFolder = "src/test/resources/features/HEADER_MENU.feature";
		
		String glue = "com.serasa.steps";

		String[] plugins = { "pretty", "json:" + reportDir + reportDirName + ".json" };
		String[] arguments = { "-m", "-p", plugins[0], "-p", plugins[1], "-g", glue,
								execFeaturesFolder };
		try {
			cucumber.api.cli.Main.run(arguments, Thread.currentThread().getContextClassLoader());
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		
		ReportJson.generateFinalReport(reportDir, reportDir, "Report " + reportDirName);

		// abrir o relatório no final
		String reportAbsPath = new File(reportDir).getAbsolutePath();
		String report = reportAbsPath + "/cucumber-html-reports/overview-features.html";
		
		System.out.println("REPORT GENERATED: ");
		System.out.println(report);
		
	}

}
