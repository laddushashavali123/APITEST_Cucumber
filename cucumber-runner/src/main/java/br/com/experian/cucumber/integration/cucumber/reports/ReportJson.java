package br.com.experian.cucumber.integration.cucumber.reports;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import net.masterthought.cucumber.Configuration;
import net.masterthought.cucumber.ReportBuilder;
import net.masterthought.cucumber.Reportable;


public class ReportJson {
	
	public static void clearReportDir(String jsonInputDir){
		try{
	        Path pp = Paths.get(new File(jsonInputDir).getAbsolutePath());
	        Files.walk(pp)
	        .filter(path -> !Files.isDirectory(path))
	        .forEach(path -> {
	            path.toFile().delete();
	        });
	        
	        Path directories = Paths.get(new File(jsonInputDir).getAbsolutePath());
	        Files.walk(directories)
	        .filter(path -> Files.isDirectory(path))
	        .forEach(path -> {
	            path.toFile().delete();
	        });

		} catch (Exception e) {
			System.err.println(e);
		}
	}
	
	public static void generateFinalReport(String jsonInputDir, String reportOutputDir, String projectName){
		
		List<String> jsonFiles = new ArrayList<>();
		
		try{
	        Path pp = Paths.get(new File(jsonInputDir).getAbsolutePath());
	        Files.walk(pp)
	        .filter(path -> !Files.isDirectory(path))
	        .forEach(path -> {
	            jsonFiles.add(path.toAbsolutePath().toString());
	        });

		} catch (Exception e) {
			System.err.println(e);
		}
		
		Configuration configuration = new Configuration(new File(reportOutputDir), projectName);
		
		ReportBuilder reportBuilder = new ReportBuilder(jsonFiles, configuration);
		Reportable result = reportBuilder.generateReports();
	}
}
