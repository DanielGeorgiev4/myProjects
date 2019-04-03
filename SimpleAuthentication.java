package authentication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class SimpleAuthentication {

	public static void main(String[] args) {
		SimpleAuthentication auth = new SimpleAuthentication("accounts.txt");
		System.out.println(auth.logIn("re434e", "123cat"));
		//auth.deleteAccount("user1");
		auth.commitChanges();
		//System.out.println(auth.isCorrectFormat("*/"));
	}
	
	private Map<String,String> hashTable = new HashMap<>();
	private String filename;
	
	public SimpleAuthentication(String filename){
		File file = new File(filename);
		try {
			file.createNewFile();
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line;
			while( (line = reader.readLine()) != null ) {
				hashTable.put(line, reader.readLine());
			}
			reader.close();
			this.filename = filename;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("An error has occurred");
		}
	}
	public void signUp(String username, String password) {
		if(!isCorrectFormat(username) || !isCorrectFormat(password)) {
			System.out.println("The username/password should contain only digits(0-9) and letters(a-z/A-Z)");
		}
		else if(hashTable.containsKey(username)) {
			System.out.println("The username has already be taken");
		}
		else {
			String hashedPassword = hashPassword(password);
			hashTable.put(username, hashedPassword);
			System.out.println("The account <" + username + "> has been created successfully");
			//open the file, seek the end, add the account, close
		}
	}
	public boolean isUsernameFree(String str) {
		return !hashTable.containsKey(str);
	}
	public void commitChanges() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
			boolean isFirst = true;
			for(String key : hashTable.keySet()) {
				if(isFirst)
					isFirst = false;
				else
					writer.newLine();
				writer.write(key);
				writer.newLine();
				writer.write(hashTable.get(key));
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void deleteAccount(String username) {
		hashTable.remove(username);
	}
	public boolean isCorrectFormat(String str) {
		if(str.length() == 0) return false;
		for(int i=0; i<str.length(); ++i) {
			char sym = str.charAt(i);
			if( !(sym >= '0' && sym <= '9' || sym >= 'a' && sym <= 'z' || sym >= 'A' && sym <= 'Z') ) {
				return false;
			}
		}
		return true;
	}
	private String hashPassword(String password) {
		try {
			MessageDigest diges = MessageDigest.getInstance("SHA-256");
			byte[] hash = diges.digest(password.getBytes(StandardCharsets.UTF_8));
			return Base64.getEncoder().encodeToString(hash);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public boolean logIn(String username, String password) {
		boolean isAuthenticated = false;
		String hashedPassword = hashPassword(password);
		if(hashTable.containsKey(username) && hashedPassword.compareTo(hashTable.get(username)) == 0) {
			isAuthenticated = true;
		}
		return isAuthenticated;
	}
}
