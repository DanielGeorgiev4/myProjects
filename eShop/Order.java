package components;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Order extends ShoppingCart{
	private MyDate deliveryDate;
	private double deliveryPrice;
	
	public Order(ShoppingCart shCart, MyDate date) {
		cart = shCart.cart;
		totalAmount = shCart.totalAmount;
		deliveryDate = date;
		deliveryPrice = date.compareTo(getCurrentDate()) == 1 ? 2 : 1; 
	}
	//get the current date
	private MyDate getCurrentDate() {
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		return new MyDate(Integer.valueOf(timeStamp.substring(0, 4)),
						  Integer.valueOf(timeStamp.substring(4, 6)),
						  Integer.valueOf(timeStamp.substring(6, 8)));
	}
	//int date format in yyyymmdd
	public int getDeliveryDate() {
		return deliveryDate.getYear()*10000 + deliveryDate.getMonth()*100 + deliveryDate.getDay();
	}
	public double getFinalAmount() {
		return totalAmount + deliveryPrice;
	}
	public double getDeliveryPrice() {
		return deliveryPrice;
	}
}
