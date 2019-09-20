package range;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import shared.Customer;
import shared.Order;

public class ReadRangeCSV {


	public ArrayList<Order> listOfOrders = new ArrayList<Order>();

	String code2sku = "315840,KA-10007,40\n" + 
			"315833,KA-10012,6.00\n" + 
			"315828,KF-10033,200.00\n" + 
			"315836,KA-10015,6.00\n" + 
			"315869,KC-10195,250.00\n" + 
			"315883,KC-10198,250.00\n" + 
			"315831,KA-10020,60.00\n" + 
			"315858,KF-10002,250.00\n" + 
			"315862,KA-10005,50.00\n" + 
			"315811,KA-10089,87.50\n" + 
			"315838,KA-10017,6.00\n" + 
			"315877,KL-10031,225.00\n" + 
			"315817,KL-10011,600.00\n" + 
			"315844,KH-10025,5500.00\n" + 
			"315874,KC-10202,250.00\n" + 
			"315818,KF-10058,100.00\n" + 
			"315807,KA-10027,87.50\n" + 
			"315849,KY-10008,1250.00\n" + 
			"315822,KF-10027,150.00\n" + 
			"315842,KA-10011,15.00\n" + 
			"315805,KH-10037,3400.00\n" + 
			"315819,KF-10024,500.00\n" + 
			"315843,KH-10021,3,500.00\n" + 
			"315851,KY-10010,2000.00\n" + 
			"315880,KC-10190,250.00\n" + 
			"315813,KA-10039,30.00\n" + 
			"315856,KF-10004,75.00\n" + 
			"315865,KH-10030,5000.00\n" + 
			"315806,KH-10019,7000.00\n" + 
			"315823,KF-10028,150.00\n" + 
			"315834,KA-10013,6.00\n" + 
			"315841,KA-10009,175.00\n" + 
			"315814,KA-10003,16.00\n" + 
			"315845,KH-10032,5700.00\n" + 
			"315863,KC-10051,250.00\n" + 
			"315889,KC-10201,250.00\n" + 
			"315824,KF-10029,300.00\n" + 
			"315876,KA-10109,60.00\n" + 
			"315885,KC-10199,250.00\n" + 
			"315829,KF-10034,225.00\n" + 
			"315816,KS-10002,15000.00\n" + 
			"315860,KF-10006,750.00\n" + 
			"315881,KC-10197,250.00\n" + 
			"315854,KS-10003,18000.00\n" + 
			"315855,KF-10005,150.00\n" + 
			"315888,KC-10194,250.00\n" + 
			"315827,KF-10032,175.00\n" + 
			"315852,KY-10011,1700.00\n" + 
			"315825,KF-10030,400.00\n" + 
			"315872,KA-10032,85.00\n" + 
			"315875,KA-10108,45.00\n" + 
			"315808,KA-10026,87.50\n" + 
			"315846,KF-10009,3500.00\n" + 
			"315866,KA-10122,20.00\n" + 
			"315809,KP-10008,900.00\n" + 
			"315861,KH-10029,4500.00\n" + 
			"315884,KC-10192,250.00\n" + 
			"315864,KH-10041,2800.00\n" + 
			"315887,KC-10200,250.00\n" + 
			"315810,KA-10088,40.00\n" + 
			"315878,KS-10007,30000.00\n" + 
			"315847,KL-10020,100.00\n" + 
			"315890,KA-10035,4.00\n" + 
			"315848,KY-10007,1000.00\n" + 
			"315886,KC-10193,250.00\n" + 
			"315835,KA-10014,6.00\n" + 
			"315850,KY-10009,1500.00\n" + 
			"315873,KC-10196,250.00\n" + 
			"315815,KS-10001,12000.00\n" + 
			"315821,KF-10026,200.00\n" + 
			"315832,KA-10021,75.00\n" + 
			"315859,KF-10001,300.00\n" + 
			"315871,KA-10082,40.00\n" + 
			"315830,KA-10022,60.00\n" + 
			"315839,KA-10004,50.00\n" + 
			"315867,KA-10034,18.00\n" + 
			"315826,KF-10031,150.00\n" + 
			"315857,KF-10003,75.00\n" + 
			"315882,KC-10191,250.00\n" + 
			"315820,KF-10025,400.00\n" + 
			"315853,KP-10014,350.00\n" + 
			"315868,KC-10189,250.00\n" + 
			"315870,KA-10033,18.00\n" + 
			"315837,KA-10016,6.00\n" + 
			"315879,KA-10019,12.50\n" + 
			"315812,KA-10010,30.00";


	public String[] sku2code(String code)
	{

		String[] returner = new String[2];
		String[] lines = code2sku.split("\n");

		for(String s: lines)
		{
			String[] line = s.split(",");
			if(line[0].equals(code))
			{
			    returner[0] = line[1];
			    returner[1] = line[2];
			}
		}

		return returner;
	}

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
				String[] orderDetails = line.replace("\"", "").split(cvsSplitBy);
				//if new order
				if(!cOrderNum.equals(orderDetails[0]))
				{
					cOrderNum = orderDetails[0];

					String fname = orderDetails[1].split(" ")[0];
					String lname = orderDetails[1].split(" ")[1];

					String addr1 = orderDetails[3] + " " + orderDetails[5];
					Customer c = new Customer(orderDetails[10],orderDetails[9],orderDetails[9],fname,lname,orderDetails[4],addr1,"",orderDetails[6],orderDetails[8],orderDetails[7],orderDetails[2]);
					ArrayList<String> sku = new ArrayList<String>();
					ArrayList<String> quanity = new ArrayList<String>();
					ArrayList<String> price = new ArrayList<String>();
					ArrayList<String> tax = new ArrayList<String>();


					String[] result = sku2code(orderDetails[11]);
					String notes = cOrderNum;
					sku.add(result[0]);
					quanity.add(orderDetails[14]);
					price.add(result[1]);
					tax.add("0.2");

					listOfOrders.add(new Order(quanity,sku,price,tax,notes,c,"24689412","55732"));

				}
				// if continuing order
				else
				{
					String[] result = sku2code(orderDetails[11]);

					listOfOrders.get(listOfOrders.size() -1).quanity.add(result[0]);
					listOfOrders.get(listOfOrders.size() -1).sku.add(orderDetails[14]);
					listOfOrders.get(listOfOrders.size() -1).price.add(result[1]);
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
			ReadRangeCSV Co = new ReadRangeCSV();
			Co.readCSV();
			for(Order o: Co.listOfOrders)
			{
				//o.uploadOrder();
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
