import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

public class StatusReport {

	String orderNumber,transactionDate,originalCustomerOrderNumber,status;

	public StatusReport(String orderNumber, String transactionDate,
			String originalCustomerOrderNumber) {
		super();
		this.orderNumber = orderNumber;
		this.transactionDate = transactionDate;
		this.originalCustomerOrderNumber = originalCustomerOrderNumber;
		status = "S10";
	} 
	
	public StatusReport(String orderNumber, String transactionDate,
			String originalCustomerOrderNumber, String status) {
		super();
		this.orderNumber = orderNumber;
		this.transactionDate = transactionDate;
		this.originalCustomerOrderNumber = originalCustomerOrderNumber;
		this.status = status;
	} 

	public static void createStatusReport(ArrayList<StatusReport> listOfReports)
	{
		SimpleDateFormat form = new SimpleDateFormat("dd/MM/yy");

		try {
			InputStream inputStream = new FileInputStream ("Status Update.xls");
			POIFSFileSystem fileSystem = new POIFSFileSystem (inputStream);

			HSSFWorkbook workBook = new HSSFWorkbook (fileSystem);

			HSSFSheet sheet  = workBook.getSheetAt(1);
		
			//find last header value
			String hdr  = sheet.getRow(3).getCell(1).getStringCellValue();
			Integer hdrnum = Integer.valueOf(hdr.substring(3));
			//find C30 and keepem
			for(int i = 0; i < sheet.getPhysicalNumberOfRows() -7;i++)
			{
				Row row = sheet.getRow(i + 7);
				//if(row.getCell(2).getStringCellValue().equals("C30"))
				//{

				if(row.getCell(2).getStringCellValue().equals(""))
				{
					break;
				}
				else
				{

					String orderNumber,transactionDate,originalCustomerOrderNumber,status;
					orderNumber = row.getCell(1).getStringCellValue();
					status = row.getCell(2).getStringCellValue();
					transactionDate = form.format(row.getCell(3).getDateCellValue());
					originalCustomerOrderNumber = row.getCell(6).getStringCellValue();
					StatusReport C30 =  new StatusReport(orderNumber,transactionDate,originalCustomerOrderNumber,status);
					listOfReports.add(C30);
				}



				//}

			}
	
			inputStream.close();
			Collections.reverse(listOfReports);
			inputStream = new FileInputStream ("Status Update Template 2018.xls");
			fileSystem = new POIFSFileSystem (inputStream);

			workBook = new HSSFWorkbook (fileSystem);

			sheet  = workBook.getSheetAt (1);
			Integer newhdrnum = hdrnum + 1;
			String newhdr = "HDR" + String.format ("%04d", newhdrnum);
			String newtlr = "TLR" + String.format ("%04d", newhdrnum);
			sheet.getRow(3).getCell(1).setCellType(Cell.CELL_TYPE_STRING);
			sheet.getRow(3).getCell(1).setCellValue(newhdr);
			sheet.getRow(4).getCell(1).setCellType(Cell.CELL_TYPE_STRING);
			sheet.getRow(4).getCell(1).setCellValue(newtlr);
			sheet.getRow(3).getCell(4).setCellType(Cell.CELL_TYPE_STRING);
			sheet.getRow(3).getCell(4).setCellValue(String.format("%06d", listOfReports.size()));
			sheet.getRow(4).getCell(4).setCellType(Cell.CELL_TYPE_STRING);
			sheet.getRow(4).getCell(4).setCellValue(String.format("%06d", listOfReports.size()));
			int cRow = 7;
			for(int z = 0; z < listOfReports.size(); z ++)
			{
				
				Row row = sheet.getRow(cRow);
				StatusReport cReport = listOfReports.get(z);
				row.getCell(1).setCellType(Cell.CELL_TYPE_STRING);
				row.getCell(1).setCellValue(cReport.orderNumber);
				row.getCell(2).setCellValue(cReport.status);
				try {
					
					row.getCell(3).setCellValue(form.parse(cReport.transactionDate));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
				row.getCell(6).setCellValue(cReport.originalCustomerOrderNumber);

				cRow +=1;

			}

			HSSFFormulaEvaluator.evaluateAllFormulaCells(workBook);
			FileOutputStream fileOut1 = new FileOutputStream("Status Update.xls");
			workBook.write(fileOut1);

			fileOut1.flush();
			fileOut1.close();
			System.out.println("status report created");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
