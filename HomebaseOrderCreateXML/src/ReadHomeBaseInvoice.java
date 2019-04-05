
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



	private static void printNote(NodeList nodeList) {

		for (int count = 0; count < nodeList.getLength(); count++) {

			System.out.println("node list:" + count);
			Node tempNode = nodeList.item(count);

			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {

				// get node name and value
				System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");

				if (tempNode.hasAttributes()) {

					// get attributes names and values
					NamedNodeMap nodeMap = tempNode.getAttributes();

					for (int i = 0; i < nodeMap.getLength(); i++) {

						System.out.println("attr list:" + i);
						Node node = nodeMap.item(i);
						System.out.println("attr name : " + node.getNodeName());
						System.out.println("attr value : " + node.getNodeValue());

					}

				}

				if (tempNode.hasChildNodes()) {

					// loop again if has child nodes
					//printNote(tempNode.getChildNodes());

				}

				System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");

			}

		}

	}


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

							seen = false;
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
					String orderDate = "";
					String orderNum = "";
					String customerOrderNumber = "";
					String storeRef = "";
					ArrayList<String> sku = new ArrayList<String>();
					ArrayList<String> quanity = new ArrayList<String>();
					
					
					ArrayList<String> price = new ArrayList<String>();
					ArrayList<String> tax = new ArrayList<String>();
					
					NodeList header = doc.getElementsByTagName("Header").item(0).getChildNodes();
					
					
					
					

					System.out.println(header.item(7).getTextContent());

					System.out.println(orderDate);
					System.out.println(orderNum);
					System.out.println(customerOrderNumber);

					/*
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
					 */
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






