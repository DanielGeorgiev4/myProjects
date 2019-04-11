package components;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
	protected Map<String,Integer> cart = new HashMap<>();
	protected double totalAmount;
	
	public void addProduct(Product p) {
		String productName = p.getName();
		if(cart.containsKey(productName))
			cart.replace(productName, cart.get(productName) + 1);
		else
			cart.put(productName, 1);
		totalAmount += p.getPrice();
		totalAmount*=100;								//round to 2 numbers after .
		totalAmount = Math.round(totalAmount);			//round to 2 numbers after .
		totalAmount/=100;								//round to 2 numbers after .
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public Map<String, Integer> getCart() {
		return cart;
	}
	public void clearCart() {
		cart = new HashMap<>();
		totalAmount = 0;
	}
}
