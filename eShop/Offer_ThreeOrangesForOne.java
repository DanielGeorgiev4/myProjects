package components;

public class Offer_ThreeOrangesForOne implements SimpleOffer{
	public void useOffer(ShoppingCart shCart) {
		shCart.cart.replace("Orange", shCart.cart.get("Orange") + shCart.cart.get("Orange")/3);
	}

}
