import java.util.ArrayList;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Matchings {
	
	
	public @Id Long id;
	public ArrayList<String> email;
	public ArrayList<String> phone;
	public ArrayList<String> mobile;
	public ArrayList<String> firstName;
	public ArrayList<String> lastName;
	public ArrayList<String> company;
	public ArrayList<String> addr1;
	public ArrayList<String> addr2;
	public ArrayList<String> city;
	public ArrayList<String> country;
	public ArrayList<String> state;
	public ArrayList<String> zip;
	public ArrayList<String> quantity;
	public ArrayList<String> sku;
	public ArrayList<String> price;
	public ArrayList<String> tax;
}
