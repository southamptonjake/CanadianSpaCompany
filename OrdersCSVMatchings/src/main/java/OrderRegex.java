import java.util.ArrayList;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class OrderRegex {
	
	public @Id Long id;
	public String email = "[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z\\.]{2,4}";
	public String phone = "^[0-9 ]+$";
	public String mobile = "^[0-9 ]+$";
	public String firstName = "^[a-zA-Z ]+$";
	public String lastName = "^[a-zA-Z ]+$";
	public String company = ".*";
	public String addr1 = "^[0-9]+ [a-zA-z0-9 ]+$";
	public String addr2 = "^[a-zA-Z ]+$";
	public String city = "^[a-zA-Z ]+$";
	public String country = "^[a-zA-Z ]+$";
	public String state = "^[a-zA-Z ]+$";
	public String zip = "([Gg][Ii][Rr] 0[Aa]{2})|((([A-Za-z][0-9]{1,2})|(([A-Za-z][A-Ha-hJ-Yj-y][0-9]{1,2})|(([A-Za-z][0-9][A-Za-z])|([A-Za-z][A-Ha-hJ-Yj-y][0-9][A-Za-z]?))))\\s?[0-9][A-Za-z]{2})";
	public String quantity = "^[1-9][0-9]*$";
	public String sku;
	public String price;
	public String tax;

}
