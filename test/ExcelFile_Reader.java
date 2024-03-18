package test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.ss.usermodel.*;

public class ExcelFile_Reader {

	public static void excelFileReaderExcution(String excelFilePath) {
		
		String baseDirPath = "E:\\downFile";
		
		String resultFolder = "result";
		String resultFileName = "result.csv";
		String resultFilePath = baseDirPath + File.separatorChar + resultFolder + File.separatorChar + resultFileName;
		
		
		StringBuilder contBuilder = new StringBuilder();
		contBuilder.append("Sr.NO");
		contBuilder.append(",");
		contBuilder.append("FileName");
		contBuilder.append(",");
		contBuilder.append("billedTo");
		contBuilder.append(",");
		contBuilder.append("invoiceNo");
		contBuilder.append(",");
		contBuilder.append("Status");
		contBuilder.append(",");
		contBuilder.append("StatusDis");
		contBuilder.append("\n");
		
		String billedTo = null;
		String invoiceNo = null;
		String pdfUrl = null;
		
		String fileName = null;
		String filePath = null;
		
		String statuStr = null;
		
		
		try {
			
			FileInputStream fileInputStream = new FileInputStream(excelFilePath);
			
			Workbook workbook = WorkbookFactory.create(fileInputStream);
			Sheet sheet = workbook.getSheetAt(0);
			
			for (Row row : sheet) {
				
				if (row.getRowNum() > 0) {
					
					System.out.println("CurrentRow: " + row.getRowNum());
					
					for (Cell cell : row) {
						System.out.println(" CurrentColumn: " + cell.getColumnIndex());
						if (cell.getColumnIndex() == 0) {
							billedTo = cell.getStringCellValue().trim();
							System.out.println("  kunnrCellValue:- " + billedTo);
						} else if (cell.getColumnIndex() == 1) {
							if (cell.getCellType().equals(CellType.NUMERIC)) {
								invoiceNo = String.valueOf(Double.valueOf(cell.getNumericCellValue()).longValue());
							}else if (cell.getCellType().equals(CellType.STRING)) {
								invoiceNo = cell.getStringCellValue().trim();
							}
							System.out.println("  vbelnCellValue:- " + invoiceNo);
						} else if (cell.getColumnIndex() == 2) {
							pdfUrl = cell.getStringCellValue().trim();
							System.out.println("  pdfUrlCellValue:- " + pdfUrl);
						} else {
							break;
						}
					}
					
					fileName = billedTo + "_" +invoiceNo + ".pdf";
					filePath = baseDirPath + File.separatorChar + fileName;
					
					boolean fileExit = false;
					File file = new File(filePath);
					
					if (file.exists()) {
						fileExit = true;
					}else {
						fileExit = downloadFile(pdfUrl, filePath);
					}
					
					if (fileExit) {
						statuStr = textSearchInPdf(filePath, invoiceNo, billedTo);
						
						if (statuStr.equals("SUCCESS")) {
							contBuilder.append(row.getRowNum());
							contBuilder.append(",");
							contBuilder.append(fileName);
							contBuilder.append(",");
							contBuilder.append(billedTo);
							contBuilder.append(",");
							contBuilder.append(invoiceNo);
							contBuilder.append(",");
							contBuilder.append(true);
							contBuilder.append(",");
							contBuilder.append(statuStr);
							contBuilder.append("\n");
							
						}else {
							contBuilder.append(row.getRowNum());
							contBuilder.append(",");
							contBuilder.append(fileName);
							contBuilder.append(",");
							contBuilder.append(billedTo);
							contBuilder.append(",");
							contBuilder.append(invoiceNo);
							contBuilder.append(",");
							contBuilder.append(false);
							contBuilder.append(",");
							contBuilder.append(statuStr);
							contBuilder.append("\n");
						}
						
					}
					
					
				}

//				if (row.getRowNum() == 10) {
//					break;
//				}
			}
			
			getFileWiter(resultFilePath, contBuilder.toString());

			workbook.close();
			workbook = null;

			fileInputStream.close();
			fileInputStream = null;

		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public static String textSearchInPdf(String filePath, String invoiceNo, String billedTo) {

		File pdfFile = new File(filePath);
		PDDocument pdfDocument = null;
		String status = "FAIL";
		boolean status1 = false;
		boolean status2 = false;
		try {

			pdfDocument = PDDocument.load(pdfFile);

			// Extract text from the document
			PDFTextStripper pdfStripper = new PDFTextStripper();
			String pdfText = pdfStripper.getText(pdfDocument);

			// Check if InvoiceNo is present in the PDF
			if (pdfText.contains(invoiceNo)) {
				System.out.println("  Invoice Number '" + invoiceNo + "' found in the PDF.");
				status1 = true;
			} else {
				System.out.println("  Invoice Number '" + invoiceNo + "' not found in the PDF.");
			}

			// Check if BilledTo is present in the PDF
			if (pdfText.contains(billedTo)) {
				System.out.println("  BilledTo '" + billedTo + "' found in the PDF.");
				status2 = true;
			} else {
				System.out.println("  BilledTo '" + billedTo + "' not found in the PDF.");
			}

			if (status1 && status2) {
				status = "SUCCESS";
				System.out.println("  status: "+ status);
			} else if (status1 && status2 == false) {
				status = "BilledTo not found in the PDF";
				System.out.println("  status: "+ status);
			} else if (status2 && status1 == false) {
				status = "InvoiceNo not found in the PDF";
				System.out.println("  status: "+ status);
			} else if (status2 == false && status1 == false) {
				status = "BilledTo and InvoiceNo not found in the PDF";
				System.out.println("  status: "+ status);
			}else {
				System.out.println("  status: "+ status);
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

	public static boolean getFileWiter(String resultFilePath, String content) {

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

	public static void main(String[] args) {

		String filePath = "C:\\Users\\user\\Downloads\\invoices_verification.xlsx";

		excelFileReaderExcution(filePath);

	}

}
