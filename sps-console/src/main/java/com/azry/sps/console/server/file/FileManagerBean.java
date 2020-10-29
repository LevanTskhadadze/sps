package com.azry.sps.console.server.file;

import org.apache.commons.io.FileUtils;

import javax.ejb.Stateless;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

@Stateless
public class FileManagerBean implements FileManager{

	@Override
	public String uploadIcon(String source, byte[] data) throws IOException {
		String directory = "/sps/files/" + source + "/";
		String fileName = getRandomName(10);
		forceCreateFile(directory, fileName, data);
		return directory + fileName;
	}

	@Override
	public byte[] downloadIcon(String path) throws IOException {
		if (path == null) {
			path = "";
		}

		File file = new File(path);
		return FileUtils.readFileToByteArray(file);

	}

	private String getRandomName(int size) {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		Random random = new Random();

		StringBuilder generatedString = new StringBuilder();

		for(int i = 0; i < size; i ++){
			char c = (char)(leftLimit + random.nextInt(rightLimit - leftLimit) - 1);
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
