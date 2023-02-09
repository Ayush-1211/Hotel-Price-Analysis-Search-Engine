
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

	private int max_depth = 2;

	String file_path = "src//TxtFiles//urls.txt";

	private HashSet<String> urlLink;

	public Crawler() {
		urlLink = new HashSet<>();
	}
     // funcation to save HTML text content
	public void urlToTextContent(String url) {
		System.out.println("Content Conversion for URL: " + url);
		try {
			String html = Jsoup.connect(url).get().html();  // Use of Jsoup lib 
			Document doc = Jsoup.parse(html.toString());  
			String textContent = doc.text(); 
			String title = doc.title();    
			createFile(title, textContent, url);
			save_html(title, html);
		} 
		catch (IOException e) {}
	}
   
	// to store the text file at specific location
	private void createFile(String title, String textContent, String url) {
		try {
			String[] titlesplit = title.split("\\|"); 
			String newTitle = "";
			for (String s : titlesplit) {
				if(s.equals(""))
					continue;
				newTitle = newTitle + " " + s;
			}
			newTitle = newTitle.replaceAll("Updated 2022 Prices", "Booking");
			File f = new File("src//TxtFiles//" + newTitle + ".txt");   
			f.createNewFile(); 
			PrintWriter pw = new PrintWriter(f);
			pw.println(url);
			pw.println(textContent);
			pw.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TO crawl web pages recursively 
	public void getPageLinks(String URL, int depth) {
		if (URL.contains("#")) {
			String substring = URL.substring(URL.indexOf("#"));
			URL = URL.replace(substring, "");  // replace 
		}
		if (!urlLink.contains(URL.split("\\?")[0]) && (depth < max_depth)) { // split by path  // max depth to control the recursively calling of link
			try {
				if (urlLink.add(URL.split("\\?")[0])) {
					if(!(URL.contains("searchresults.en-gb.html") || URL.contains("index.en-gb.html"))) {						
						try (FileWriter f = new FileWriter(file_path, true); // to append url in index file (urls.txt)
								BufferedWriter b = new BufferedWriter(f);
								PrintWriter p = new PrintWriter(b);) {
							p.println(URL.split("\\?")[0]);
						}
						catch (IOException i) {
							i.printStackTrace();
						}
					}
					else
						urlLink.remove(URL.split("\\?")[0]);
					urlToTextContent(URL);   // Call to save text files
				}
				Document document = Jsoup.connect(URL).execute().parse();  // jsoup lib
				Elements resultSet = document.select("a[href]");  // getting all links of web page
				depth++;
				for (Element element : resultSet) {   // filtering link based on condition
				    String pattern = "(booking|hotelscombined)\\.(com|ca)\\/(hotel|Hotel|Place)(.)+(details)*";
				    Pattern r = Pattern.compile(pattern); 
				    Matcher m = r.matcher(element.attr("abs:href"));
				    
				    // remove links for another language
				    if(element.attr("abs:href").contains("booking.com") && !element.attr("abs:href").contains(".en-gb.html"))
				    	continue;

				    // select links for hotel pages
				    if(!m.find())
				    	continue;
				    
				    // check if it is crawled earlier or not
				    if(urlLink.contains(element.attr("abs:href")))
				    	continue;

				    getPageLinks(element.attr("abs:href"), depth);
				}
			}
			catch (IOException e) {
				e.printStackTrace();
				System.err.println("For '" + URL + "': " + e.getMessage());
			}
		}
	}

	public static void save_html(String title, String d) {
		try {
			String[] titlesplit = title.split("\\|");
			String newTitle = "";
			for (String s : titlesplit) {
				if(s.equals(""))
					continue;
				newTitle = newTitle + " " + s;
			}
			newTitle = newTitle.replaceAll("Updated 2022 Prices", "Booking");
			File f = new File("src//htmlFiles//" + newTitle + ".html");   // save html files
			f.createNewFile();
			PrintWriter pw = new PrintWriter(f);
			pw.println(d);
			pw.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void crawl() throws IOException {
		File myObj = new File("src//TxtFiles//urls.txt");
		if (myObj.createNewFile())   // to create the URLS.txt file if not available
			System.out.println("List of URL File Created !");
		else
			System.out.println("File already exists.");
		Crawler crawler = new Crawler();
		crawler.get_extracted_links_list();
		System.out.println("Hello User!---- Do you want to crawl pages? : Please type 1 for yes or 0 for no");
		Scanner sc = new Scanner(System.in);
		int userInput = sc.nextInt();
		while (userInput==1) {   // get urls from user--> crawl it--> craw the url upto depth of 2
			System.out.println("Please enter URL");
			String url = sc.next();
			crawler.getPageLinks(url, 0);
			System.out.println("Do you want to continue crawl pages? : Please type 1 for yes or 0 for no");  // ask user to crawl other link
			userInput = sc.nextInt();
		}
		System.out.println("Re-creating dictionary because recently crawled data found\nIt might take a minute!");
		TrieTree.create_dictionary();  // create new dictionary 
	}
	// To get list of crawled links
	public void get_extracted_links_list() {
		urlLink = new HashSet<>();
		try {
			FileReader fr = new FileReader(new File(file_path));
			BufferedReader br = new BufferedReader(fr);
			String line;
			while((line = br.readLine()) != null) {
				urlLink.add(line);
			}
			br.close();
			fr.close();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
