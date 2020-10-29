package com.azry.sps.console.server.file;

import javax.ejb.Local;
import java.io.IOException;

@Local
public interface FileManager {

	String uploadIcon(String source, byte[] data) throws IOException;

	byte[] downloadIcon(String path) throws IOException;
}
