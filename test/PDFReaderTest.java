package test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.checkerframework.checker.units.qual.m;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class PDFReaderTest {

	static RemoteWebDriver driver;
	static String file = "C:/Users/user/Downloads/CGU2337_5996413756.pdf";
	static String invoiceNo = "5996413756";
	static String billedTo = "CGU2337";

	@BeforeClass
	public void setup() {
		try {
			driver = new ChromeDriver();
			driver.get(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@AfterClass
	public void tearDown() {
		try {
			if (driver != null) {
				driver.quit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public static void pdfReaderTest() throws IOException {
		File pdfFile = new File(file);
		PDDocument pdfDocument = null;

		try {
			pdfDocument = PDDocument.load(pdfFile);

			// Extract text from the document
			PDFTextStripper pdfStripper = new PDFTextStripper();
			String pdfText = pdfStripper.getText(pdfDocument);

			// Check if InvoiceNo is present in the PDF
			if (pdfText.contains(invoiceNo)) {
				System.out.println("Invoice Number '" + invoiceNo + "' found in the PDF.");
			} else {
				System.out.println("Invoice Number '" + invoiceNo + "' not found in the PDF.");
				Assert.fail("Invoice Number not found in the PDF.");
			}

			// Check if BilledTo is present in the PDF
			if (pdfText.contains(billedTo)) {
				System.out.println("BilledTo '" + billedTo + "' found in the PDF.");
			} else {
				System.out.println("BilledTo '" + billedTo + "' not found in the PDF.");
				Assert.fail("BilledTo not found in the PDF.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (pdfDocument != null) {
				pdfDocument.close();
			}
		}
	}

	public static String textSearchInPdf(String filePath, String invoiceNo, String billedTo) {
		
		File pdfFile = new File(filePath);
		PDDocument pdfDocument = null;
		String status = "fail";
		boolean status1 = false;
		boolean status2 = false;
		try {

			pdfDocument = PDDocument.load(pdfFile);

			// Extract text from the document
			PDFTextStripper pdfStripper = new PDFTextStripper();
			String pdfText = pdfStripper.getText(pdfDocument);
			

			 //Check if InvoiceNo is present in the PDF
			if (pdfText.contains(invoiceNo)) {
				System.out.println("Invoice Number '" + invoiceNo + "' found in the PDF.");
				status1 = true;
			} else {
				System.out.println("Invoice Number '" + invoiceNo + "' not found in the PDF.");
			}
			
			
			// Check if BilledTo is present in the PDF
			if (pdfText.contains(billedTo)) {
				System.out.println("BilledTo '" + billedTo + "' found in the PDF.");
				status2 = true;
			} else {
				System.out.println("BilledTo '" + billedTo + "' not found in the PDF.");
			}
			
			if (status1 && status2) {
				status = "SUCCESS";
			}else if (status1 && status2 == false) {
				status = "BilledTo not found in the PDF";
			}else if (status2 && status1 == false) {
				status = "InvoiceNo not found in the PDF";
			}else if (status2 == false && status1 == false) {
				status = "BilledTo and InvoiceNo not found in the PDF";
			} 

			pdfDocument.close();
			pdfDocument = null;

			return status;
		} catch (Exception e) {
			e.printStackTrace();
			return status;
		} finally {
			if (pdfDocument != null) {
				try {
					pdfDocument.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	public static boolean fileWiter(String resultFilePath, String content) {

		File file = new File(resultFilePath);
		try {
			
    		FileWriter writer = new FileWriter(file);
    		writer.write(content);
    		writer.close();
    		return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args) throws IOException {
//      pdfReaderTest();
		String baseDirPath = "E:\\downFile";
		String resultFolder = "result";
		String resultFileName = "result.csv";
		
		String resultFilePath = baseDirPath + File.separatorChar + resultFolder + File.separatorChar + resultFileName;
		
		String content = "1,file1,true\n2,file2,true";
		
//		fileWiter(resultFilePath, content);
		
		String file = "C:/Users/user/Downloads/CGU2337_5996413756.pdf";
		String invoiceNo = "5996413756";
		String billedTo = "CGU2337";

		System.out.println(textSearchInPdf(file, invoiceNo, billedTo));
		
	}

}
