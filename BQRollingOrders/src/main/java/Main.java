import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;

public class Main {

	public static void main(String[] args) {

		HashMap<Integer,Date> poToDate = new HashMap<Integer,Date>();

		try {
			JOptionPane.showMessageDialog(null, "Reading Invoice Sheet", "", JOptionPane.INFORMATION_MESSAGE); 
			InputStream inputStream = new FileInputStream ("B&Q Invoicing.xls");
			POIFSFileSystem fileSystem = new POIFSFileSystem (inputStream);

			HSSFWorkbook workBook = new HSSFWorkbook (fileSystem);

			HSSFSheet sheet  = workBook.getSheetAt(0);



			for(int i = 0; i < sheet.getPhysicalNumberOfRows();i++)
			{

				Row row = sheet.getRow(i);
				try {
					Double dpo = row.getCell(4).getNumericCellValue();
					int po = dpo.intValue();
					if(po == 0)
					{
						throw new Exception();
					}
					Date date = row.getCell(6).getDateCellValue();
					if(date == null)
					{
						throw new Exception();
					}
					poToDate.put(po, date);

				} catch (Exception e) {

				}

			}

			inputStream.close();
			
			System.out.println(poToDate.size());
			JOptionPane.showMessageDialog(null, "Reading Rolling Report", "", JOptionPane.INFORMATION_MESSAGE); 

			inputStream = new FileInputStream ("Overdue Rolling Report.xls");
			fileSystem = new POIFSFileSystem (inputStream);

			workBook = new HSSFWorkbook (fileSystem);

			sheet  = workBook.getSheetAt(2);
			
			for(int i = 0; i < sheet.getPhysicalNumberOfRows();i++)
			{

				Row row = sheet.getRow(i);
				try {
					Double dpo = row.getCell(3).getNumericCellValue();
					int po = dpo.intValue();
					if(po == 0)
					{
						throw new Exception();
					}
					if(poToDate.containsKey(po))
					{
						System.out.println(po + " " + poToDate.get(po));
						row.createCell(24);
						row.getCell(24).setCellValue(poToDate.get(po));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			inputStream.close();
			JOptionPane.showMessageDialog(null, "Writing Rolling Report", "", JOptionPane.INFORMATION_MESSAGE); 
			FileOutputStream fileOut1 = new FileOutputStream("Overdue Rolling Report.xls");
			workBook.write(fileOut1);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JOptionPane.showMessageDialog(null, "Finished", "Finished", JOptionPane.INFORMATION_MESSAGE); 


	}

}
