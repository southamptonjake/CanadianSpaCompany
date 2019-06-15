package entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class HomebaseOrder {
	
	@Id
	public Long id;
	public int stage;
	public String customerEmail;
	
	public HomebaseOrder(Long id, int stage, String customerEmail) {
		super();
		this.id = id;
		this.stage = stage;
		this.customerEmail = customerEmail;
	}

	public HomebaseOrder() {
	}
	

}
