package components;

public class MyDate {
	private int year, month, day;
	
	public MyDate(int year, int month, int day) {
		this.year = year;
		this.month = month;
		this.day = day;
	}
	public int getDay() {
		return day;
	}
	public int getMonth() {
		return month;
	}
	public int getYear() {
		return year;
	}
	//not correct - not working for dates before the current
	public int compareTo(MyDate currentDate) {
		if(year == currentDate.getYear() && month == currentDate.getMonth() && day - currentDate.getDay() == 1)
			return 1;
		else return currentDate.getDay() - day;
	}
}
