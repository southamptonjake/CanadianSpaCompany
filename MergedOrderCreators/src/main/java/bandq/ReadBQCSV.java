package bandq;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import shared.Customer;
import shared.Order;


public class ReadBQCSV {

	public ArrayList<Order> listOfOrders = new ArrayList<Order>();

	public void readCSV()
	{
		String csvFile = "orders.csv";
        String line = "";
        String cvsSplitBy = ",";

        
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	String cOrderNum = "firstOrder";
        	br.readLine();
            while ((line = br.readLine()) != null) {

            	line = line.replace("\"", "");
                // use comma as separator
                String[] orderDetails = line.split(cvsSplitBy);
                //if new order
                if(!cOrderNum.equals(orderDetails[0]))
                {
                	
                
                	cOrderNum = orderDetails[0];
                	String buyerName = orderDetails[8];
                	String poType = orderDetails[2];
                	String salesOrderNum = orderDetails[23];
                	String fullName = orderDetails[14];
                	String firstName = fullName.split(" ")[1];
                	String lastName = fullName.split(" ")[2];
                	String siteCode = orderDetails[13];	
                	Customer c = new Customer(orderDetails[21],orderDetails[19],orderDetails[20],firstName,lastName,"",orderDetails[15],"",orderDetails[16],orderDetails[17],"",orderDetails[18]);
                	ArrayList<String> sku = new ArrayList<String>();
                	ArrayList<String> quanity = new ArrayList<String>();
                	ArrayList<String> price = new ArrayList<String>();
                	ArrayList<String> tax = new ArrayList<String>();
                	//cell A (0)
                	String orderNumber = orderDetails[0];
                	sku.add(orderDetails[29]);
                	quanity.add(orderDetails[31]);
                	price.add(orderDetails[34]);
                	tax.add("0.2");
                	
                	String notes = orderNumber + " " + salesOrderNum + " " + siteCode;
                	String billingID = findBillingID(poType,buyerName);
                	
                	
                	listOfOrders.add(new Order(quanity,sku,price,tax,notes,c,billingID,"48307"));
                	
                }
                // if continuing order
                else
                {
                	listOfOrders.get(listOfOrders.size() -1).quanity.add(orderDetails[31]);
                	listOfOrders.get(listOfOrders.size() -1).sku.add(orderDetails[29]);
                	listOfOrders.get(listOfOrders.size() -1).price.add(orderDetails[34]);
                	listOfOrders.get(listOfOrders.size() -1).tax.add("0.2");
                }
                

            }
            System.out.println("done");

        } catch (IOException e) {
            e.printStackTrace();
        }
       
	}
	
	
	public String findBillingID(String poType, String buyerName)
	{
		if(poType.equals("DC Manual PO"))
		{
			return "23678611";
		}
		else
		{
			if(buyerName.equals("B&Q Limited"))
			{
				return "23657440";
			}
			else if(buyerName.equals("B&Q Ireland Ltd"))
			{
				return "23677719";
			}
			else if(buyerName.equals("B&Q (Retail) Jersey Ltd"))
			{
				return "23677785";
			}
			else if(buyerName.equals("B&Q (Retail) Guernsey Ltd"))
			{
				return "23677822";
			}
		}
		return "-1";
		
	}

	public static void main(String[] args) {
		try {
			ReadBQCSV Co = new ReadBQCSV();
			Co.readCSV();
			for(Order o: Co.listOfOrders)
			{
				o.uploadOrder();
			}
			JOptionPane optionPane = new JOptionPane();
			optionPane.setMessage("Closing");
			JDialog dialog = optionPane.createDialog("Orders Uploaded");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane optionPane = new JOptionPane();
			JDialog dialog = optionPane.createDialog("Orders Not Uploaded");
			optionPane.setMessage("Closing");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);

		}

	}

}
