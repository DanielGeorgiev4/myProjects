package managment.system;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLNonTransientConnectionException;
import java.time.temporal.JulianFields;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.metal.MetalInternalFrameTitlePane;

import authentication.SimpleAuthentication;


public class ManagementSystem {

	public static SimpleAuthentication auth = new SimpleAuthentication("account.txt");
	
	public static void main(String[] args) {
		loggin();
	}
	
	private static void initMinPanels(JPanel panel, JFrame frame, int x, int y, int width, int heigth, Color color, boolean toHaveLayout) {
		panel.setBounds(x, y, width, heigth);
		if(toHaveLayout) panel.setLayout(null);
		panel.setBackground(color);
		frame.add(panel);
	}
	private static void initMainButtons (JButton button, JPanel panel, int x, int y, int width, int heigth, Color color, Font font) {
		button.setFocusable(false);
		button.setFont(font);
		button.setBounds(x, y, width, heigth);
		button.setBackground(color);
		panel.add(button);
	}
	private static List<Course> getAllCourses(){
		File folder = new File("courses");
		File[] listOfFiles = folder.listFiles();
		List<Course> result = new ArrayList<>();
		for(File file : listOfFiles) {
			result.add(new Course(file.getName().replaceAll(".txt", "")));
		}
		return result;
	}
	private static void mainFrame(String username, String password) {
		User user = new User(username);
		JFrame mainFrame = new JFrame();
		int width = 1000, heigth = 600;
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		int mainPanelsSize = 220;
		Color panelColor = Color.LIGHT_GRAY;
		Font font = new Font(null, 1, 22);
		JPanel topPanel = new JPanel(),
			   bottomPanel = new JPanel(),
			   menuPanel = new JPanel(),
			   myCoursesPanel = new JPanel(),
			   allCoursesPanel = new JPanel();
		JButton myCourses = new JButton("My courses"),
			    createCourse = new JButton("Create course"),
			    viewAllCourses = new JButton("View all courses"),
			    backToMenu = new JButton("Menu"),
			    enroll = new JButton("Enroll");
		initMainButtons(myCourses, menuPanel, 120, 100, mainPanelsSize, mainPanelsSize, panelColor, font);
		initMainButtons(createCourse, menuPanel, 390, 100,  mainPanelsSize, mainPanelsSize, panelColor, font);
		initMainButtons(viewAllCourses, menuPanel, 660, 100,  mainPanelsSize, mainPanelsSize, panelColor, font);
		initMainButtons(backToMenu, topPanel, 20, 25, 150, 50, panelColor, font);
		initMainButtons(enroll, topPanel, width - 185, 25, 150, 50, panelColor, font);
		
		//viewAllCourses button click action
		viewAllCourses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuPanel.setVisible(false);
				allCoursesPanel.setVisible(true);
				enroll.setVisible(true);
				allCoursesPanel.removeAll();
				
				List<Course> allCourses = getAllCourses();
				String[][] data = new String[allCourses.size()][3]; 
			    String[] columnNames = { "Course name", "Number of participants", "Enrolled" }; 
			    for(int i=0; i<allCourses.size(); ++i) {
			    	data[i][0] = allCourses.get(i).getName();
			    	data[i][1] = Integer.toString(allCourses.get(i).getNumerOfEnrolled());
			    	data[i][2] = user.isEnrolled(allCourses.get(i).getName()) ? "yes" : "";
			    }
			    JTable j = new JTable(data, columnNames); 
			    ListSelectionModel select= j.getSelectionModel();  
	            select.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
	            select.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						int row = j.getSelectedRow(),
							column = j.getSelectedColumn();
					}
				});
			    j.setCellSelectionEnabled(true);
			    JScrollPane sp = new JScrollPane(j);
			    allCoursesPanel.add(sp);
			}
		});
		
		enroll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					JFrame newCourse = new JFrame();
					
					JLabel label = new JLabel("Course name", SwingConstants.CENTER);
					label.setBounds(70, 40, 150, 30);
					label.setFont(font);
					
					JTextField textField = new JTextField();
					textField.setBounds(17, 80, 250, 30);
					textField.setFont(font);
					
					JButton button = new JButton("Join");
					button.setFont(font);
					button.setBounds(70, 140, 150, 40);
					button.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							user.enroll(textField.getText());
							user.commitChanges();
							myCourses.doClick();
							newCourse.dispose();
						}
					});
					
					newCourse.add(label);
					newCourse.add(textField);
					newCourse.add(button);
					newCourse.setSize(300, 250);
					newCourse.setLocationRelativeTo(null);
					newCourse.setLayout(null);
					newCourse.setVisible(true);
				}
		});
		
		//cerateCourse button click action
		createCourse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame newCourse = new JFrame();
				
				JLabel label = new JLabel("Course name", SwingConstants.CENTER);
				label.setBounds(70, 40, 150, 30);
				label.setFont(font);
				
				JTextField textField = new JTextField();
				textField.setBounds(17, 80, 250, 30);
				textField.setFont(font);
				
				JButton button = new JButton("Create");
				button.setFont(font);
				button.setBounds(70, 140, 150, 40);
				button.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if( user.createCourse(textField.getText()) ) {
							JOptionPane.showMessageDialog(newCourse, "A course with name " + textField.getText() + " has been created",
																	 "Success", JOptionPane.INFORMATION_MESSAGE);
							user.commitChanges();
							newCourse.dispose();
						}
						else {
							JOptionPane.showMessageDialog(newCourse, "Permission denied - must be a Teacher or course with the name "
																	 + textField.getText() + " already exists", 
																	 "taken username", JOptionPane.WARNING_MESSAGE);
						}
					}
				});
				
				newCourse.add(label);
				newCourse.add(textField);
				newCourse.add(button);
				newCourse.setSize(300, 250);
				newCourse.setLocationRelativeTo(null);
				newCourse.setLayout(null);
				newCourse.setVisible(true);
			}
		});
		
		//back to menu button click action
		backToMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuPanel.setVisible(true);
				myCoursesPanel.setVisible(false);
				allCoursesPanel.setVisible(false);
				enroll.setVisible(false);
			}
		});

	    //myCourses button click action
		myCourses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuPanel.setVisible(false);
				myCoursesPanel.setVisible(true);
				myCoursesPanel.removeAll();
				
				List<Course> userCourses = user.getAllCourses();
				String[][] data = new String[userCourses.size()][2]; 
			    String[] columnNames = { "Course name", "Number of participants" }; 
			    for(int i=0; i<userCourses.size(); ++i) {
			    	data[i][0] = userCourses.get(i).getName();
			    	data[i][1] = Integer.toString(userCourses.get(i).getNumerOfEnrolled());
			    }
			    JTable j = new JTable(data, columnNames); 
			    JScrollPane sp = new JScrollPane(j);
			    myCoursesPanel.add(sp);
			}
		});
		
		myCoursesPanel.setVisible(false);
		allCoursesPanel.setVisible(false);
		enroll.setVisible(false);
		
		//initialize all the panels
		initMinPanels(myCoursesPanel, mainFrame, 0, 100, width, 420, null, false);
		initMinPanels(allCoursesPanel, mainFrame, 0, 100, width, 420, null, false);
		initMinPanels(topPanel, mainFrame, 0, 0, width, 100, Color.GREEN, true);
		initMinPanels(menuPanel, mainFrame, 0, 100, width, 420, null, true);
		initMinPanels(bottomPanel, mainFrame, 0, 520, width, 50, Color.GREEN, true);
		//-----------------------------------------------------------------------------------
		mainFrame.setSize(width, heigth);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}

	public static void loggin() {
		JFrame logginFrame = new JFrame();
		logginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//username label
		JLabel usernameLable = new JLabel("Username:");
		usernameLable.setBounds(50, 50, 100, 30);
		usernameLable.setFont(new Font(null, 1, 14));
		logginFrame.add(usernameLable);
		
		//password label
		JLabel passwordLable = new JLabel("Password:");
		passwordLable.setBounds(50, 100, 100, 30);
		passwordLable.setFont(new Font(null, 1, 14));
		logginFrame.add(passwordLable);
		
		//username textfield
		JTextField usernameTextField = new JTextField();
		usernameTextField.setVisible(true);
		usernameTextField.setBounds(150, 50, 130, 30);
		usernameTextField.setFont(new Font(null, 1, 14));
		logginFrame.add(usernameTextField);
		
		//password field
		JPasswordField passwordField = new JPasswordField();
		passwordField.setVisible(true);
		passwordField.setBounds(150, 100, 130, 30);
		passwordField.setFont(new Font(null, 1, 14));
		logginFrame.add(passwordField);
		
		//button for registration
		JButton regButton = new JButton("sign up");
		regButton.setVisible(true);
		regButton.setBounds(110, 150, 100, 30);
		logginFrame.add(regButton);
		
		//button for logIn
		JButton logInButton = new JButton("log in	");
		logInButton.setVisible(true);
		logInButton.setBounds(160, 150, 110, 30);
		//logginFrame.add(logInButton);
		
		//logginFrame properties
		logginFrame.setLayout(null);
		logginFrame.setSize(350, 230);
		logginFrame.setLocationRelativeTo(null);
		logginFrame.setVisible(true);
		
		//reg button action
		regButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFrame regFrame = new JFrame();
				
				//username label
				JLabel rusernameLable = new JLabel("Username:");
				rusernameLable.setBounds(50, 50, 100, 30);
				rusernameLable.setFont(new Font(null, 1, 14));
				regFrame.add(rusernameLable);
				
				//password label
				JLabel rpasswordLable = new JLabel("Password:");
				rpasswordLable.setBounds(50, 100, 100, 30);
				rpasswordLable.setFont(new Font(null, 1, 14));
				regFrame.add(rpasswordLable);
				
				//type label
				JLabel rposLable = new JLabel("Position:");
				rposLable.setBounds(50, 150, 100, 30);
				rposLable.setFont(new Font(null, 1, 14));
				regFrame.add(rposLable);
				
				//username textfield
				JTextField rusernameTextField = new JTextField();
				rusernameTextField.setVisible(true);
				rusernameTextField.setBounds(150, 50, 130, 30);
				rusernameTextField.setFont(new Font(null, 1, 14));
				regFrame.add(rusernameTextField);
				
				//password field
				JPasswordField rpasswordField = new JPasswordField();
				rpasswordField.setVisible(true);
				rpasswordField.setBounds(150, 100, 130, 30);
				rpasswordField.setFont(new Font(null, 1, 14));
				regFrame.add(rpasswordField);
				
				//password field
				String[] positions = {"Student","Teacher"};
				JComboBox<String> posComboBox = new JComboBox<>(positions);
				//posComboBox.setEditable(false);
				posComboBox.setVisible(true);
				posComboBox.setBounds(150, 150, 130, 30);
				posComboBox.setFont(new Font(null, 1, 14));
				regFrame.add(posComboBox);
				
				//button for confirmation
				JButton rregButton = new JButton("confirm");
				rregButton.setVisible(true);
				rregButton.setBounds(110, 230, 110, 30);
				regFrame.add(rregButton);
				
				//logginFrame properties
				regFrame.setLayout(null);
				regFrame.setSize(350, 330);
				regFrame.setLocationRelativeTo(null);
				regFrame.setVisible(true);
				
				rregButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String username = rusernameTextField.getText(),
								   password = new String(rpasswordField.getPassword());
						if(auth.isCorrectFormat(username) && auth.isCorrectFormat(password)) {
							if(auth.isUsernameFree(username)) {
								auth.signUp(username, password);
								auth.commitChanges();
								createAccount(username, posComboBox.getSelectedItem().toString());
								JOptionPane.showMessageDialog(regFrame, "Successfully created account!");
								regFrame.dispose();
							}
							else {
								JOptionPane.showMessageDialog(regFrame, "Username has already been taken", "taken username", JOptionPane.WARNING_MESSAGE);
							}
						}
						else {
							JOptionPane.showMessageDialog(regFrame, "The username/password should contain only digits(0-9) and letters(a-z/A-Z)",
															"incorrect format", JOptionPane.WARNING_MESSAGE);
						}
					}
				});
			}
		});
		
		logInButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logginTryRespose(usernameTextField, passwordField, logginFrame);
			}
		});
		
		//enter action
		passwordField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					logginTryRespose(usernameTextField, passwordField, logginFrame);
				}
			}
		});
		
		usernameTextField.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					logginTryRespose(usernameTextField, passwordField, logginFrame);
				}
			}
		});
	}

	private static void createAccount(String username, String position) {
		try {
			new File("usersInfo").mkdir();
			BufferedWriter writer = new BufferedWriter(new FileWriter("usersInfo\\" + username + ".txt"));
			writer.write(position);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void logginTryRespose(JTextField usernameTextField, JPasswordField passwordField, JFrame logginFrame) {
		if( auth.logIn(usernameTextField.getText(), new String(passwordField.getPassword())) ) {
			logginFrame.dispose();
			mainFrame(usernameTextField.getText(), new String(passwordField.getPassword()));
		}
		else {
			JOptionPane.showMessageDialog(logginFrame, "Incorrect username or passowrd","access denied", JOptionPane.WARNING_MESSAGE);
			makeEmpty(usernameTextField, passwordField);
			usernameTextField.grabFocus();
		}
	}
	public static void makeEmpty(JTextField c1, JPasswordField c2) {
		c1.setText(null);
		c2.setText(null);
	}
}
