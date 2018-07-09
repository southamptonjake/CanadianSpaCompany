import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;

public class StoreCode {

	Double code;

	public StoreCode(Double code) {
		this.code = code;
	}

	public String removeStartingSpace(String s)
	{
		while(s.charAt(0) == ' ')
		{
			s.substring(1);
		}
		return s;
	}

	public Double hCodeToCCode() 
	{
		try {
			InputStream inputStream = new FileInputStream ("HToCCodes.xls");
			POIFSFileSystem fileSystem = new POIFSFileSystem (inputStream);

			HSSFWorkbook workBook = new HSSFWorkbook (fileSystem);

			HSSFSheet sheet  = workBook.getSheetAt(0);

			for(int i = 0; i < sheet.getPhysicalNumberOfRows();i++)
			{
				Row row = sheet.getRow(i);
				try {
					if(row.getCell(1).getNumericCellValue() == code)
					{

						return row.getCell(14).getNumericCellValue();
					}
				} catch (Exception e) {
				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return code;
	}
	public String codeToAddress()
	{
		code = hCodeToCCode();
		String address = "";
		try {
			InputStream inputStream = new FileInputStream ("CCodesToAddress.xls");
			POIFSFileSystem fileSystem = new POIFSFileSystem (inputStream);

			HSSFWorkbook workBook = new HSSFWorkbook (fileSystem);

			HSSFSheet sheet  = workBook.getSheetAt(0);

			for(int i = 0; i < sheet.getPhysicalNumberOfRows();i++)
			{
				Row row = sheet.getRow(i);

				try {
					if(row.getCell(0).getNumericCellValue() == code)
					{

						address += row.getCell(1).getStringCellValue() + System.lineSeparator();
						address += row.getCell(5).getStringCellValue() + System.lineSeparator();
						address += row.getCell(6).getStringCellValue() + System.lineSeparator();
						address += row.getCell(7).getStringCellValue() + System.lineSeparator();
						address += row.getCell(8).getStringCellValue() + System.lineSeparator();
						address += row.getCell(9).getStringCellValue() + System.lineSeparator();
						address += row.getCell(10).getStringCellValue() + System.lineSeparator();
						address += " " + System.lineSeparator() + "Contact" + System.lineSeparator();
						address += row.getCell(3).getStringCellValue() + System.lineSeparator();
						address += row.getCell(2).getStringCellValue() + System.lineSeparator();
						address += row.getCell(4).getStringCellValue();
						address = address.replaceAll(System.lineSeparator() + System.lineSeparator(), System.lineSeparator());



					}
				} catch (Exception e) {

				}

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return address;
	}



}
