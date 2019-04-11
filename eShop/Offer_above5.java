package components;

public class Offer_above5 implements SimpleOffer{
	public void useOffer(ShoppingCart cart) {
		if(cart.getTotalAmount() > 5) cart.totalAmount -= cart.totalAmount * 0.1;
	}
	
}
