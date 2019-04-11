package components;

import java.util.ArrayList;
import java.util.List;

public class Customer {
	private ShoppingCart cart = new ShoppingCart();
	private List<Order> orders = new ArrayList<>();
	
	public void addProduct(Product p) {
		cart.addProduct(p);
	}
	//oridinary checkout
	//seems logical to clear the shoping cart after a order has been made
	public Order checkout(MyDate date) {
		Order newOrder = new Order(cart, date);
		clearCart();
		orders.add(newOrder);
		return newOrder;	
	}
	public void clearCart() {
		cart.clearCart();
	}
	public List<Order> getOrders() {
		return orders;
	}
	//checkout with an offer
	public Order checkoutWithOffer(MyDate date, SimpleOffer offer) {
		useSimpleOffer(offer);
		return checkout(date);
	}
	private void useSimpleOffer(SimpleOffer offer) {
		offer.useOffer(cart);
	}
	
	public static void main(String[] args) {
		Customer c = new Customer();
		//c.addProduct(new Apple());
		c.addProduct(new Orange());
		c.addProduct(new Orange());
		c.addProduct(new Orange());
		c.checkoutWithOffer(new MyDate(2019, 04, 12),new Offer_ThreeOrangesForOne());
		c.addProduct(new Apple());
		c.addProduct(new Apple());
		c.addProduct(new Apple());
		c.addProduct(new Apple());
		c.addProduct(new Apple());
		c.addProduct(new Apple());
		c.addProduct(new Apple());
		c.addProduct(new Apple());
		c.addProduct(new Apple());
		c.checkoutWithOffer(new MyDate(2019, 04, 12), new Offer_above5());
		//c.checkout(new MyDate(2019, 04, 12));
		List<Order> list = c.getOrders();
		for(Order o : list) {
			System.out.println(o.totalAmount + "|oranges -> " + o.getCart().get("Orange") + "|apples -> " + 
							   o.getCart().get("Apple") + "|date -> " + o.getDeliveryDate());
		}
	}
}
