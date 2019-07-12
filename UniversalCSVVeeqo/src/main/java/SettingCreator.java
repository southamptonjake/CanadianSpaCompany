import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.lang3.StringEscapeUtils;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class SettingCreator {

	public void readCSV()
	{
		String csvFile = "orders.csv";
		String line = "";
		String cvsSplitBy = ",";


		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

			String[] header = br.readLine().split(",");
			
			

			




		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		try {
			SettingCreator Co = new SettingCreator();
			Co.readCSV();

		} catch (Exception e) {
			System.out.println(e.getMessage());

		}

	}

}



