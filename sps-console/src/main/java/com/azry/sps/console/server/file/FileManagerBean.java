package com.azry.sps.console.server.file;

import com.azry.sps.systemparameters.model.SystemParameterType;
import com.azry.sps.systemparameters.model.sysparam.Parameter;
import com.azry.sps.systemparameters.model.sysparam.SysParam;
import org.apache.commons.io.FileUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

@Stateless
public class FileManagerBean implements FileManager{
	@Inject
	@SysParam(type = SystemParameterType.STRING, code = "service-icon-path")
	Parameter<String> directoryPath;

	@Override
	public String uploadIcon(String source, byte[] data) throws IOException {
		String directory = directoryPath.getValue() + "/" + source + "/";
		String fileName = getRandomName(10);
		forceCreateFile(directory, fileName, data);
		return source + "/" + fileName;
	}

	@Override
	public byte[] downloadIcon(String filePath) throws IOException {
		if (filePath == null) {
			filePath = "";
		}

		File file = new File(directoryPath.getValue() + "/" + filePath);
		return FileUtils.readFileToByteArray(file);

	}

	private String getRandomName(int size) {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		Random random = new Random();

		StringBuilder generatedString = new StringBuilder();

		for(int i = 0; i < size; i ++){
			char c = (char)(leftLimit + random.nextInt(rightLimit - leftLimit));
			generatedString.append(c);
		}
		return generatedString.toString();
	}

	private void forceCreateFile(String dirPath, String fileName, byte[] buffer) throws IOException {

		File file = new File(dirPath);
		boolean res = file.mkdirs();
		boolean update = true;
		file = new File(dirPath + "/" + fileName);
		if (file.exists()) {
			byte[] current = FileUtils.readFileToByteArray(file);
			if (Arrays.equals(current, buffer)) {
				update = false;
			}
		}
		if (update) {
			//log.info("Updated file in file system: " + fullPath);
			FileUtils.writeByteArrayToFile(file, buffer);
		}
	}
}
