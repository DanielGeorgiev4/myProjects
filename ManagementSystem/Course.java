package managment.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Course {
	
	private String name;
	private int numberOfEnrolled;
	private List<String> data;
	public Course(String courseName) {
		name = courseName;
		data = new ArrayList<>();
		if( new File("courses\\" + courseName + ".txt").exists() ) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader("courses\\" + courseName + ".txt"));
				numberOfEnrolled = Integer.parseInt(reader.readLine());
				String line;
				while( (line = reader.readLine()) != null ) {
					data.add(line);
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			numberOfEnrolled = 1;
		}
	}
	public static boolean exist(String courseName) {
		return new File("courses\\" + courseName + ".txt").exists();
	}
	public String getName() {
		return name;
	}
	public void addData(String newData) {
		data.add(newData);
	}
	public void commitChanges() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("courses\\" + name + ".txt"));
			writer.write(Integer.toString(numberOfEnrolled));
			for(String line : data) {
				writer.newLine();
				writer.write(line);
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public int getNumerOfEnrolled() {
		return numberOfEnrolled;
	}
	public List<String> getData() {
		return data;
	}
	
}
