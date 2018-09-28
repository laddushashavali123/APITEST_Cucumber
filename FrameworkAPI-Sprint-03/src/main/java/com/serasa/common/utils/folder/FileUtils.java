package com.serasa.common.utils.folder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;

public class FileUtils {

	public static List<String> getFolderPathFiles(String pathToFolder){		
		try(Stream<Path> paths = Files.walk(Paths.get(pathToFolder))) {
		    return paths.filter(Files::isRegularFile)
		    			.map(String::valueOf)
		    			//.map(e -> getFileName(e))
		    			.collect(Collectors.toList());
		}catch (Exception e){			
			throw new IllegalStateException("Não foi possível localizar a pasta no caminho" + pathToFolder);
		}
	}
	
	public static String getFileName(String fullPath){		
		return FilenameUtils.getBaseName(fullPath);
	}
	
	
	
}
