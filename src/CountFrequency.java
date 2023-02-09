
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CountFrequency {

	public void countWords(String word) throws FileNotFoundException, IOException {
		try {
			int wordCount = 0;
			File file = new File("src\\TxtFiles\\dictonary.txt");  // read the file with specific path
			InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file), "utf-8"); 
			BufferedReader bufferReader = new BufferedReader(inputStreamReader);
			StringBuffer lines = new StringBuffer();   
			String line = null;
			while ((line = bufferReader.readLine()) != null) {  // retunr bool  // if line is not null
				lines.append(line.toLowerCase());  // add data in String
			}
			Pattern p = Pattern.compile(word.toLowerCase()); // create regex of User Input
			Matcher m = p.matcher(lines);   
			while (m.find()) {  // find return boolean
				wordCount++;  // increament the word count
			}
			bufferReader.close();
			System.out.println("Word : " + word + "\nWord Frequency Count : " + wordCount); 

		}
		catch (IOException error) {
			error.printStackTrace();
		}
	}
}
