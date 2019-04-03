package testingPackage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Dictionary {

	private List<String[]> words = new LinkedList<>();
	private String[] pair;
	private int i;
	
	public Dictionary(String[] fileName) throws IOException {
		pair = new String[2];
		load(fileName);
	}
	public String getEnglishWord() {
		return pair[0];
	}
	public String getKoreanWord() {
		return pair[1];
	}
	public void nextElement() {
		++i;
		if(i == words.size()) {
			i = 0;
			shuffle();
		}
		pair[0] = words.get(i)[0];
		pair[1] = words.get(i)[1];
	}
	public void shuffle() {
		Collections.shuffle(words);
	}
	public void load(String[] fileName) throws IOException {
		i = -1;
		words.clear();
		BufferedReader eng = new BufferedReader(new FileReader(fileName[0]));
		BufferedReader kor = new BufferedReader(new FileReader(fileName[1]));
		while(eng.ready()) {
			String[] element = {eng.readLine(), kor.readLine()};
			words.add(element);
		}
		eng.close();
		kor.close();
		//shuffle();
	}
}
