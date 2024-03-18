package test;

import java.io.IOException;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;

public class PDFReaderTestTest {
	static RemoteWebDriver driver;
	static String file = "C:/Users/user/Downloads/AAACU2414K.pdf";
	static String targetPAN = "AAACU2414K";
  @BeforeMethod
  public void setup() {
		driver = new ChromeDriver();
		driver.get(file);
	}
  @AfterMethod
 	public void tearDown() {
 		driver.quit();
 	}
  @Test
  public static void pdfReaderTest() throws IOException {
	    File pdfFile = new File(file);
	    PDDocument pdfDocument = PDDocument.load(pdfFile);

	    try {
	        // number of pages
	        int pageCount = pdfDocument.getNumberOfPages();
	        System.out.println("Number of pages in the PDF: " + pageCount);

	        // Extract text from the document
	        PDFTextStripper pdfStripper = new PDFTextStripper();
	        String pdfText = pdfStripper.getText(pdfDocument);
	        System.out.println(pdfText);
	        Assert.assertTrue(pdfText.contains("Metrics to be Impacted:"));
	        
	        //set the page number and get the text
//	        pdfStripper.setStartPage(1);
//	        String pdfText = pdfStripper.getText(pdfDocument);
//	        System.out.println(pdfText);
//	        Assert.assertTrue(pdfText.contains("Metrics to be Impacted:"));
	        
	        // Compare text with PAN number
          if (pdfText.contains(targetPAN)) {
              System.out.println("Status: Pass");
          } else {
              System.out.println("Status: Fail");
          }
	    } finally {
	        pdfDocument.close(); 
	    }
	}

  public static void main(String[] args) throws IOException{
		pdfReaderTest();	
	}
}
