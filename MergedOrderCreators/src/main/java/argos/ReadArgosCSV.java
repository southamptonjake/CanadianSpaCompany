package argos;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import shared.Customer;
import shared.Order;


public class ReadArgosCSV {

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

                // use comma as separator
                String[] orderDetails = line.split(cvsSplitBy);
                //if new order
                if(!cOrderNum.equals(orderDetails[0]))
                {
                	
                	String fname = orderDetails[70].split(" ")[0];
					String lname = orderDetails[70].split(" ")[1];
                	cOrderNum = orderDetails[0];
                	System.out.println(orderDetails[38]);
                	Customer c = new Customer(orderDetails[9],orderDetails[26],orderDetails[38],fname,lname,orderDetails[12],orderDetails[13],orderDetails[14],orderDetails[21],orderDetails[24],orderDetails[23],orderDetails[20]);
                	ArrayList<String> sku = new ArrayList<String>();
                	ArrayList<String> quanity = new ArrayList<String>();
                	ArrayList<String> price = new ArrayList<String>();
                	ArrayList<String> tax = new ArrayList<String>();
                	
                	
                	//cell A (0)
                	String orderNumber = orderDetails[0];
                	//cell BE (56)
                	String orderRef = orderDetails[56];
                	//cell BL (63)
                	String customerSKU = orderDetails[63];
                	
                	
                	String notes = orderNumber + " " + orderRef + " " + customerSKU;
                	sku.add(orderDetails[42]);
                	quanity.add(orderDetails[47]);
                	price.add(orderDetails[46]);
                	tax.add("0.2");
                	
                	listOfOrders.add(new Order(quanity,sku,price,tax,notes,c,"15223361","46116"));
                	
                }
                // if continuing order
                else
                {
                	listOfOrders.get(listOfOrders.size() -1).quanity.add(orderDetails[47]);
                	listOfOrders.get(listOfOrders.size() -1).sku.add(orderDetails[42]);
                	listOfOrders.get(listOfOrders.size() -1).price.add(orderDetails[46]);
                	listOfOrders.get(listOfOrders.size() -1).tax.add("0.2");
                }
                

            }
            System.out.println("done");

        } catch (IOException e) {
            e.printStackTrace();
        }
       
	}

	public static void main(String[] args) {
		try {
			ReadArgosCSV Co = new ReadArgosCSV();
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
			System.out.println(e.getMessage());
			JOptionPane optionPane = new JOptionPane();
			JDialog dialog = optionPane.createDialog("Orders Not Uploaded");
			optionPane.setMessage("Closing");
			dialog.setAlwaysOnTop(true);
			dialog.setVisible(true);

		}

	}

}
