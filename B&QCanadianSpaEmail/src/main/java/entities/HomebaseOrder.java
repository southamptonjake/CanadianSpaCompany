package entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class HomebaseOrder {
	
	@Id
	public Long id;
	public @Index int stage;
	public @Index String customerEmail;
	public @Index String customerPhone;
	public @Index int allocationsUsed;
	public @Index LineItems lineItems;
	
	

	public HomebaseOrder(Long id, String customerEmail, String customerPhone, LineItems lineItems) {
		super();
		this.id = id;
		this.stage = 0;
		this.customerEmail = customerEmail;
		this.customerPhone = customerPhone;
		this.allocationsUsed = 0;
		this.lineItems = lineItems;
	}



	public HomebaseOrder() {
	}
	

}
