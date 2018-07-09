
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;

public class ReadHomeBaseInvoice {

	public static void main(String[] args) {

		//change to User wheni export
		//String path = "C:\\Users\\JakeL\\Desktop\\Orders";
		String path = "\\\\ANIQA\\CCentre\\Trays\\In";
		ArrayList<StatusReport> reportsToAdd = new ArrayList<StatusReport>();
		File Folder = new File(path);
		File files[];
		files = Folder.listFiles();
		JOptionPane.showMessageDialog(null, "Starting", "Starting", JOptionPane.INFORMATION_MESSAGE); 
		int countRead = 0;
		int countUpload = 0;
		if(files.length>1)
		{
			for(int i = 0;i<files.length; i++){
				System.out.println(files[i].getPath());
			}
		}
		else{
			System.out.println("found only one file...");
			System.out.println(files);
		}
		JOptionPane.showMessageDialog(null, "Found " + files.length + " files", "Number of Files", JOptionPane.INFORMATION_MESSAGE); 
		for(int i = 0;i<files.length ; i++)
		{
			boolean seen = false;

			try {
				try {

					String readOrders =  Files.readAllLines(new File("readOrders.txt").toPath()).get(0);
					System.out.println(readOrders);
					String[] orders = readOrders.split(",");
					for(String o : orders)
					{
						if (files[i].getName().equals(o))
						{
							seen = true;
						}
					}
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				if(!seen)	
				{

					countRead ++;
					try (Writer writer = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream("readOrders.txt",true), "utf-8"))) {
						writer.write(files[i].getName() + ",");
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					System.out.println("reading " + files[i].getName());
					String email, phone, mobile,firstName, lastName, company,addr1,  addr2, city, country,state, zip;
					email = "Not Given";
					phone = "";
					mobile = "";
					firstName = "";
					lastName = "";
					company = "";
					addr1 = "";
					addr2 = "";
					city = ""; 
					country = "GB";
					state = "";
					zip = "";
					String orderDate = "";
					String orderNum = "";
					String customerOrderNumber = "";
					String storeRef = "";
					ArrayList<String> sku = new ArrayList<String>();
					ArrayList<String> quanity = new ArrayList<String>();
					ArrayList<String> price = new ArrayList<String>();
					ArrayList<String> tax = new ArrayList<String>();


					List<String> lines = Files.readAllLines(files[i].toPath());
					for(String s : lines)
					{
						String[] splitLine = s.split("=");
						if(splitLine[0].equals("DIN"))
						{
							orderDate = splitLine[1].split("\\+")[0];
							orderDate = orderDate.substring(4, 6) + "/" + orderDate.substring(2, 4) + "/20" + orderDate.substring(0, 2);
						}
						else if(splitLine[0].equals("ORD"))
						{
							orderNum = splitLine[1].split(":")[0];
							customerOrderNumber = splitLine[1].split(":")[1].split("\\+")[0];
						}
						else if(splitLine[0].equals("CLO"))
						{
							storeRef = splitLine[1].split(":")[0];
						}
						else if(splitLine[0].equals("DNA"))
						{
							String number = splitLine[1].split("\\+")[0];
							String data = splitLine[1].split("\\+")[3];
							if(number.equals("1"))
							{
								String[] splitData = data.split(":");
								lastName = splitData[1];
								firstName = splitData[2];
								addr1 = splitData[3];
							}
							else if (number.equals("2"))
							{
								String[] splitData = data.split(":");
								city = splitData[0];
								state = splitData[1];
								zip = splitData[2];
								phone = splitData[3];
								mobile = splitData[3];
							}
						}
						else if(splitLine[0].equals("OLD"))
						{
							String orderDetails = splitLine[1].split(":")[2];
							String[] splitOrderDetails = orderDetails.split("\\+");
							try {
								sku.add(SharedInfo.convertToHomeBaseSku(splitOrderDetails[0]));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							quanity.add(splitOrderDetails[2]);

							String penny = splitOrderDetails[3].substring(0, splitOrderDetails[3].length() - 2);
							String pounds = penny.substring(0,penny.length() - 2) + "." +  penny.substring(penny.length() - 2);
							System.out.println(pounds);
							price.add(pounds);
							tax.add("0.2");

						}


					}
					
					
				System.out.println(orderDate);
				System.out.println(orderNum);
				System.out.println(customerOrderNumber);
				
				if(orderDate.equals("") || orderNum.equals("") || customerOrderNumber.equals(""))
				{
					throw new Exception();
				}
				JOptionPane.showMessageDialog(null, "Uploading: " + firstName, "Uploading", JOptionPane.INFORMATION_MESSAGE);
				StatusReport s = new StatusReport(orderNum,orderDate,customerOrderNumber);
				Customer c = new Customer(email, phone, mobile,firstName, lastName, company,addr1,  addr2, city, country,state, zip);
				Order o = new Order(quanity,sku,price,tax, orderNum, storeRef,  customerOrderNumber,  c);
				o.upload();
				countUpload ++;
				JOptionPane.showMessageDialog(null, "Uploaded: " + firstName, "Uploading", JOptionPane.INFORMATION_MESSAGE);
				System.out.println("uploaded " + files[i].getName());
				reportsToAdd.add(s);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	System.out.println(reportsToAdd.size());
	JOptionPane.showMessageDialog(null, "Read " + countRead + " items" + System.lineSeparator() + "Uploaded " + countUpload + " items" , "Finished", JOptionPane.INFORMATION_MESSAGE);
	StatusReport.createStatusReport(reportsToAdd);
	

}


}






