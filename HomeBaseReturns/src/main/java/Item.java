
public class Item {
	
	String sku;
	String productName;
	public Item(String sku, String productName) {
		
		this.sku = sku;
		this.productName = productName;
	}
	@Override
	public String toString() {
		return sku + "		" + productName;
	}
	
	

}
