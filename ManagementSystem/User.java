package managment.system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

public class User {
	
	public static void main(String[] args) {
		User me = new User("me");
		System.out.println(me.isEnrolled("DS"));
	}
	
	String username, position;
	List<Course> courses;
	
	public User(String accName) {
		courses = new ArrayList<>();
		username = accName;
		try {
			BufferedReader reader = new BufferedReader(new FileReader("usersInfo\\" + username + ".txt"));
			position = reader.readLine();
			String line;
			while( (line = reader.readLine()) != null) {
				courses.add(new Course(line));
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public boolean createCourse(String courseName) {
		if(courseExists(courseName) || position.compareTo("Student") == 0)
			return false;
		courses.add(new Course(courseName));
		return true;	
	}
	public void commitChanges() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter("usersInfo\\" + username + ".txt"));
			writer.write(position);
			for(Course course : courses) {
				writer.newLine();
				writer.write(course.getName());
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(Course course : courses)
			course.commitChanges();
	}
	public Course getCourse(String courseName) {
		for(int i=0; i<courses.size(); ++i)
			if(courseName.compareTo(courses.get(i).getName()) == 0)
				return courses.get(i);
		return null;
	}
	public boolean isEnrolled(String courseName) {
		Course cur = new Course(courseName);
		for(Course course : courses) {
			if(cur.getName().compareTo(course.getName()) == 0)
				return true;
		}
		return false;
	}
	public boolean enroll(String courseName) {
		if(!Course.exist(courseName) || isEnrolled(courseName))
			return false;
		courses.add(new Course(courseName));
		return true;
	}
	public List<Course> getAllCourses() {
		return courses;
	}
	private boolean courseExists(String courseName) {
		File file = new File("courses\\" + courseName + ".txt");
		if(file.exists()) return true;
		return false;
	}
}
