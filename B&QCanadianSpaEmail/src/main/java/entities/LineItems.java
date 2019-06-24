package entities;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class LineItems {
	
	@Id
	public Long id;
	public @Index String[] productTitles;
	public @Index int[] quantities;
	public LineItems(String[] productTitles, int[] quantities) {
		this.productTitles = productTitles;
		this.quantities = quantities;
	}
	public LineItems() {
		
	}
	
	
	
	
	
}
