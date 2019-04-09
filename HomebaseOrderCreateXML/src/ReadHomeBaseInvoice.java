
import java.io.BufferedWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.File;

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


		String path = "./xmldocs/";
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

					File orderfile = new File("./readOrders.txt");

					String readOrders = "";
					if(Files.readAllLines(orderfile.toPath()).size() > 0)
					{
						readOrders =  Files.readAllLines(orderfile.toPath()).get(0);
					}
					String[] orders = readOrders.split(",");
					for(String o : orders)
					{
						String a = files[i].getName();


						if (files[i].getName().equals(o))
						{

							seen = true;
						}
					}
				} catch (IOException e2) {
				}
				if(!seen)	
				{
					System.out.println(i);
					countRead ++;
					try (Writer writer = new BufferedWriter(new OutputStreamWriter(
							new FileOutputStream("readOrders.txt",true), "utf-8"))) {
						writer.write((files[i].getName() + ","));
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

					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(files[i]);



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
				
					NodeList textData = doc.getElementsByTagName("TextData");

					Element el = (Element) textData.item(0);
					NodeList addressNodes = el.getElementsByTagName("TextLine");

					firstName = addressNodes.item(1).getTextContent();
					lastName = addressNodes.item(2).getTextContent();
					addr1 = addressNodes.item(3).getTextContent();
					city = addressNodes.item(4).getTextContent();
					state = addressNodes.item(5).getTextContent();
					zip = addressNodes.item(6).getTextContent();
					mobile = addressNodes.item(7).getTextContent();
					phone = addressNodes.item(8).getTextContent();

					
					String orderDate = "";
					orderDate = doc.getElementsByTagName("Date").item(0).getTextContent();
					System.out.println(orderDate);
					
					
					@SuppressWarnings("unused")
					String orderNum = "";
					@SuppressWarnings("unused")
					String customerOrderNumber = "";
					@SuppressWarnings("unused")
					String storeRef = "";
					
					NodeList references = doc.getElementsByTagName("References");

					el = (Element) references.item(0);
					NodeList orderNumbers = el.getElementsByTagName("Reference");

					for(int p = 0; p < orderNumbers.getLength(); p ++)
					{
						Node number = orderNumbers.item(p);
						String owner = number.getAttributes().getNamedItem("owner").getTextContent();
						if(owner.equals("Company"))
						{
							customerOrderNumber = number.getTextContent();
						}
						else if(owner.equals("Partner"))
						{
							orderNum = number.getTextContent();
						}
					}

					NodeList entities = doc.getElementsByTagName("Entity");

					for(int p = 0; p < entities.getLength(); p ++)
					{
						Node entity = entities.item(p);
						String function = entity.getAttributes().getNamedItem("function").getTextContent();

						if(function.equals("ShipTo"))
						{
							el = (Element) entity;

							NodeList identify = el.getElementsByTagName("Identifier");
							for(int z = 0; z < identify.getLength(); z ++)
							{
								Node identidy = identify.item(z);
								String function2 = identidy.getAttributes().getNamedItem("function").getTextContent();
								if(function2.equals("GLN"))
								{
									storeRef = identidy.getTextContent();
								}
							}
						}
					}
					
					

					ArrayList<String> sku = new ArrayList<String>();
					ArrayList<String> quantity = new ArrayList<String>();	
					ArrayList<String> price = new ArrayList<String>();
					ArrayList<String> tax = new ArrayList<String>();
					
					NodeList products = doc.getElementsByTagName("Product");
					
					

					for(int p = 0; p < products.getLength(); p ++)
					{
						Node product = products.item(p);
						String xrefMode = product.getAttributes().getNamedItem("xrefMode").getTextContent();
						
						if(xrefMode.equals("Target"))
						{
							el = (Element) product;

							//find sku
							NodeList identify = el.getElementsByTagName("Identifier");
							for(int z = 0; z < identify.getLength(); z ++)
							{
								Node identidy = identify.item(z);
								String function2 = identidy.getAttributes().getNamedItem("function").getTextContent();
								if(function2.equals("ProductCode"))
								{
									String owner = identidy.getAttributes().getNamedItem("owner").getTextContent();
									if(owner.equals("Company"))
									{
										sku.add(identidy.getTextContent());
									}
								
								}
							}
							//find quant
							NodeList quantitys = el.getElementsByTagName("UnitQuantity");
							quantity.add(quantitys.item(0).getTextContent());
							System.out.println(quantitys.item(0).getTextContent());
							
							//find price
						
							NodeList values = el.getElementsByTagName("Value");
							price.add(values.item(0).getTextContent());
							System.out.println(values.item(0).getTextContent());
							
							tax.add("0.2");
							
						}
						
						
					}





					
				if(orderDate.equals("") || orderNum.equals("") || customerOrderNumber.equals(""))
				{
					throw new Exception();
				}
				JOptionPane.showMessageDialog(null, "Uploading: " + firstName, "Uploading", JOptionPane.INFORMATION_MESSAGE);
				StatusReport s = new StatusReport(orderNum,orderDate,customerOrderNumber);
				Customer c = new Customer(email, phone, mobile,firstName, lastName, company,addr1,  addr2, city, country,state, zip);
				Order o = new Order(quantity,sku,price,tax, orderNum, storeRef,  customerOrderNumber,  c);
				//o.upload();
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






