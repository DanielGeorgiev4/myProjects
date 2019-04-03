package testingPackage;

import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

class Test {

	static public void main(String[] args) throws IOException {
		final JFXPanel fxPanel = new JFXPanel();
		String[] fileName = {"data\\chapter 1\\file_e.txt","data\\chapter 1\\file_k.txt"};
		Dictionary dic = new Dictionary(fileName);
		Audio audio = new Audio();
		JFrame frame = new JFrame();
		
		//main button 
		JButton button = new JButton("Show");
		button.setBounds(20, 20, 80, 30);
		frame.add(button);
		
		//repeat button
		JButton repeatButton = new JButton("Repeat");
		repeatButton.setBounds(120, 20, 80, 30);
		frame.add(repeatButton);
		
		//search online button
		JButton searchButton = new JButton("Search");
		searchButton.setBounds(220, 20, 80, 30);
		frame.add(searchButton);
		
		//label
		JLabel english = new JLabel();
		JLabel korean = new JLabel();
		english.setBounds(20, 60, 400, 60);
		korean.setBounds(20, 120, 400, 60);
		english.setFont(new Font(null,1,30));
		korean.setFont(new Font(null,1,30));
		frame.add(english);
		frame.add(korean);
		
		//comboBox
		List<String> chapters = new LinkedList<>();
		for(int i=1; i<=6; ++i) chapters.add(Integer.toString(i));
		JComboBox<Object> cBox = new JComboBox<>(chapters.toArray());
		cBox.setBounds(20, 180, 90, 20);
		frame.add(cBox);
		
		//frame parameters
		frame.setSize(500, 300);
		frame.setLocation(400, 100);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//button click action
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(fileName[0].compareTo("data\\chapter " + cBox.getSelectedItem().toString() + "\\file_e.txt") != 0){
					fileName[0] = "data\\chapter " + cBox.getSelectedItem().toString() + "\\file_e.txt";
					fileName[1] = "data\\chapter " + cBox.getSelectedItem().toString() + "\\file_k.txt";
					try {
						dic.load(fileName);
					} catch (IOException e1) {
						System.out.println("fileName error");
						e1.printStackTrace();
					}
					korean.setVisible(true);
				}
				if(english.isVisible() && korean.isVisible()) {
					dic.nextElement();
					korean.setVisible(false);
					english.setText(dic.getEnglishWord());
					korean.setText(dic.getKoreanWord());
					String audioName = "data\\chapter " + cBox.getSelectedItem().toString() + "\\" +
							  							  dic.getEnglishWord() + ".mp3";
					audio.loadAudio(audioName);
				}
				else {
					korean.setVisible(true);
					audio.play();
				}
			}
		});	
	
		//repeatButton action
		repeatButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			audio.repeat();
			}
		});
		
		//searchButton action
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Desktop.getDesktop().browse(new URI("www.google.com"));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
	}
}