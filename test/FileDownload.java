/**
 * 
 */
package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

public class FileDownload {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String baseDirPath = "E:\\downFile";
		
		String FILE_URL = "https://mkonnect.havells.com:8443/xmwcsdealermkonnect/xmwdwn?dtype=invoice&fname=5533058244";
		
		String FILE_NAME = baseDirPath + File.separatorChar + "file1.pdf";
		System.out.println(FILE_NAME);
		
		
		System.out.println(downloadFile(FILE_URL, FILE_NAME));
//		try {
//			BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
//			FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME);
//			
//			    byte dataBuffer[] = new byte[1024];
//			    int bytesRead;
//			    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
//			        fileOutputStream.write(dataBuffer, 0, bytesRead);
//			    }
//			    
//			    fileOutputStream.close();
//			    fileOutputStream = null;
//			    in.close();
//			    in = null;
//			System.out.println("exit");
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
	}
	
	public static boolean downloadFile(String FILE_URL, String FILE_NAME) {
		try {
			BufferedInputStream in = new BufferedInputStream(new URL(FILE_URL).openStream());
			FileOutputStream fileOutputStream = new FileOutputStream(FILE_NAME);
			
			    byte dataBuffer[] = new byte[1024];
			    int bytesRead;
			    while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
			        fileOutputStream.write(dataBuffer, 0, bytesRead);
			    }
			    
			    fileOutputStream.close();
			    fileOutputStream = null;
			    in.close();
			    in = null;
			System.out.println("  DN exit");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
