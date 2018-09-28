package br.com.experian.cucumber.integration.cucumber.common.utils.RunnerClass;


import java.io.File;

import br.com.experian.cucumber.integration.cucumber.steps.Hooks;
import br.com.experian.cucumber.integration.cucumber.common.utils.PropertiesUtil;
import br.com.experian.cucumber.integration.cucumber.reports.ReportJson;

public class RESTRunner  {

	public static void main(String[] args) {
		String reportDirName = "execution";
		String reportDir = PropertiesUtil.getProperty("report.path") + "/" + reportDirName + "/";
		
		ReportJson.clearReportDir(reportDir);
		
		String execFeaturesFolder = PropertiesUtil.getProperty("feature.source");
		
		String glue = "br.com.experian.cucumber.integration.cucumber.steps";

		String tags = PropertiesUtil.getProperty("cucumber.tags");
		
		String[] plugins = { "pretty", "json:" + reportDir + reportDirName + ".json" };

		String[] arguments = null;
		if (tags != null && !tags.isEmpty()) {
			arguments = new String[] { "-m", "-p", plugins[0], "-p", plugins[1], "-g", glue, "-t", tags,
					execFeaturesFolder };
		} else {
			arguments = new String[] { "-m", "-p", plugins[0], "-p", plugins[1], "-g", glue,
					execFeaturesFolder };
		}
		try {
			cucumber.api.cli.Main.run(arguments, Thread.currentThread().getContextClassLoader());
		} catch (Throwable e) {
			e.printStackTrace();
		}

		ReportJson.generateFinalReport(reportDir, reportDir, "Report " + reportDirName);

		// abrir o relatório no final
		String reportAbsPath = new File(reportDir).getAbsolutePath();
		String report = reportAbsPath + "/cucumber-html-reports/overview-features.html";

		System.out.println("Generated report: ");
		System.out.println(report);

        //try to close browser if it exists
		try {
            Hooks.closeBrowser();
        }catch (Exception e){}
	}
	
}
