import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

public class Order {

	ArrayList<Item> listOfItems;
	StoreCode storeCode;
	String number;
	ArrayList<String> listOfPo;
	

	public Order(ArrayList<Item> listOfItems, StoreCode storeCode, ArrayList<String> listOfPo) {
		this.listOfItems = listOfItems;
		this.storeCode = storeCode;
		this.number = number;
		this.listOfPo = listOfPo;
	}


	public void output()
	{
		for(int i = 0; i < listOfPo.size(); i ++)
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(listOfPo.get(i) + ".txt",false), "utf-8"))) {
			writer.write("Address" + System.lineSeparator());
			writer.write(storeCode.codeToAddress() + System.lineSeparator() + System.lineSeparator());
			writer.write(listOfItems.get(i).toString() + System.lineSeparator());
		
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
	}


	public static void main(String[] args) {
		String path = "\\\\ANIQA\\CCentre\\Trays\\In";
		//String path = "C:\\Users\\JakeL\\Desktop\\In";
		File Folder = new File(path);
		File files[];
		files = Folder.listFiles();
		JOptionPane.showMessageDialog(null, "Starting", "Starting", JOptionPane.INFORMATION_MESSAGE); 
		if(files.length>1)
		{

		}
		else{
			System.out.println("found only one file...");
			System.out.println(files);
		}
		JOptionPane.showMessageDialog(null, "Found " + files.length + " files", "Number of Files", JOptionPane.INFORMATION_MESSAGE); 
		for(int i = 0;i<files.length ; i++)
		{
			if(files[i].getPath().contains("HBT") && files[i].getPath().contains("rx"))
			{
				System.out.println(files[i].getName());
				List<String> lines;
				String storeCodeString = "";
				try {
					lines = Files.readAllLines(files[i].toPath());
					for(String s : lines)
					{
						String[] splitLine = s.split("=");
						if(splitLine[0].equals("CLO"))
						{
							storeCodeString = splitLine[1];
							storeCodeString = storeCodeString.substring(0, storeCodeString.length() - 1);

						}
					}
					
					StoreCode storeCode = new StoreCode(Double.parseDouble(storeCodeString));
					System.out.println(storeCode.codeToAddress());
					ArrayList<Item> listOfItems = new ArrayList<Item>();
					ArrayList<String> listOfPo = new ArrayList<String>();
					for(String s : lines)
					{
						String[] splitLine = s.split("=");
						if(splitLine[0].equals("CLD"))
						{
							String sku;
							String productName;
							splitLine = s.split(":");
							sku = splitLine[1];
							sku = sku.replace("+", "");
							productName = splitLine[6];
							productName = productName.replace("+", " ");
							productName = productName.substring(0, productName.length() - 1);
							Item readItem = new Item(sku,productName);
							listOfItems.add(readItem);
						}
						else if(splitLine[0].equals("CRF"))
						{
							String po = splitLine[1].substring(0, splitLine[1].indexOf("+"));
							listOfPo.add(po);
						}

					}
					
					Order order = new Order(listOfItems,storeCode,listOfPo);
					order.output();

				} catch (IOException e) {
				}
			}


		}
		JOptionPane.showMessageDialog(null, "Done", "Done", JOptionPane.INFORMATION_MESSAGE); 




	}

}
