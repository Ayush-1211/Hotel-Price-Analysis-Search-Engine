import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

	public static void options(HashMap<String, Integer> hashMap, Scanner sc) {
		try {
			int userInput;
			do {
				System.out.println("Please enter your choice:");
				System.out.println("1 : Web Crawling");
				System.out.println("2 : Word Completion");
				System.out.println("3 : Spell Checker");
				System.out.println("4 : Search Frequecy");
				System.out.println("5 : Inverted Index and Frequecy Count");
				System.out.println("6 : Page Ranking");
				System.out.println("0 : Back to Main Menu");
				userInput = sc.nextInt();
				switch (userInput) {
					case 1: {
						try {
							Crawler.crawl();
						}
						catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}
					case 2: {
						System.out.print("Provie Search Input: ");
						String keyword = sc.next();
						if (!hashMap.containsKey(keyword)) {
							hashMap.put(keyword, 1);
						}
						else {
							hashMap.put(keyword, hashMap.get(keyword)+1);
						}
						for (String string : TrieTree.auto_Suggest_word(keyword)) {
							System.out.println(string);
						}
						break;
					}
					case 3: {
						try {
							System.out.print("Provie Search Input: ");
							String keyword = sc.next();
							if (!hashMap.containsKey(keyword)) {
								hashMap.put(keyword, 1);  // adding keyword in hashMap
							} 
							else {
								hashMap.put(keyword, hashMap.get(keyword)+1); 
							}
							SpellCorrector corrector = new SpellCorrector();
							corrector.useDictionary("src//TxtFiles//dictonary.txt"); 
							String suggestion = corrector.suggestSimilarWord(keyword);  // return suggested value
							if (suggestion == null) {
								suggestion = "No similar word found";
							}
							System.out.println("Suggestion is: " + suggestion);
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}
					case 4: {
						System.out.println("Here is the list of Search Words and their counts:");
						for (Map.Entry<String, Integer> entry : hashMap.entrySet()) {
							String key = entry.getKey();
							Integer val = entry.getValue();
							System.out.println("Word: "+ key + " Count: "+ val);
						}
					}
					case 5: {
						System.out.println("Enter a word to count its frequency : ");
						String wordToCount = sc.next();
						CountFrequency countFrequency = new CountFrequency();
						try {
							countFrequency.countWords(wordToCount);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
						break;
					}
					case 6: {
						System.out.println("Please enter Search Query: ");
						Scanner sc1 = new Scanner(System.in);
					    String key = sc1.nextLine();
					    System.out.println("Search Result:");
					    File folder = new File("src\\TxtFiles\\");
			            File[] listOfFiles = folder.listFiles();
			            ArrayList<String> files = new ArrayList<>();
			            for (File file : listOfFiles) {
			                if (file.isFile()) {
			                       files.add(file.getName());
			                }
			            }
			            sc1.close();
			            try {
						PageRank.FrqBuilder(files, key.toLowerCase(), 10);
			            }
			            catch(IndexOutOfBoundsException e)
			            {
			            	System.out.println("No Results Found");
			            }
					}
					case 0: {
						break;
					}
					default: {
						System.out.println("Please provide valid input!!!!!!!!!!");
						break;
					}
				}
			} while(userInput != 0);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static void main(String[] args) {
		int userInput;
//		TrieTree.create_dictionary();
		Scanner sc = new Scanner(System.in);
		HashMap<String, Integer> hashMap = new HashMap<>();
		System.out.println("Welcome!!!");
		System.out.println("The system will show hotels based on the location searched by the user.");
		do {
			System.out.println("Please enter your choice:");
			System.out.println("1 : Search Hotel based on location");
			System.out.println("2 : More Options");
			System.out.println("0 : Exit");
			userInput = sc.nextInt();
			switch (userInput) {
				case 1: {
					System.out.println("Enter location (city): ");
				    String keyword = sc.next();
					SpellCorrector corrector = new SpellCorrector();
					try {
						corrector.useDictionary("src//TxtFiles//dictonary.txt");
					} catch (IOException e) {
						e.printStackTrace();
					}
					String suggestion = corrector.suggestSimilarWord(keyword);
					if (suggestion == null) {
						List<String> completionSuggestion = TrieTree.auto_Suggest_word(keyword);
						int i=0;
						System.out.println("Unable to find the word.\nPlease Select one of the suggested words based on your query from below:");
						for (String string : completionSuggestion) {
							i+=1;
							System.out.println(Integer.toString(i)+" - "+string);
							if(i>10)
								break;
						}
						System.out.print("Enter the number corresponding to the option: ");
						i = sc.nextInt();
						if(i>11 || i<1) {
							System.out.println("Incorrect option number\nExiting");
							break;
						}
						suggestion = completionSuggestion.get(i);
					}
					if(suggestion!=keyword) {
						System.out.println("Found spelling mistake in the search query\nFetching results for this autocorrected word '"+suggestion+"'");
					}
				    File folder = new File("src\\TxtFiles\\");
		            File[] listOfFiles = folder.listFiles();
		            ArrayList<String> files = new ArrayList<>();
		            for (File file : listOfFiles) {
		                if (file.isFile()) {
		                	files.add(file.getName());
		                }
		            }
		            try {
		            	PageRank.FrqBuilder(files, suggestion.toLowerCase(), 10);
		            }
		            catch(IndexOutOfBoundsException e)
		            {
//		            	e.printStackTrace();
		            	System.out.println("No Results Found\nCrawling the web for your search query");
		            	SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd");
		            	Calendar c = Calendar.getInstance();
		            	c.setTime(new Date());
            			c.add(Calendar.DATE, 1);
		            	String checkin = formatter.format(c.getTime());
            			c.add(Calendar.DATE, 1);
            			if(!suggestion.toLowerCase().contains("canada")) {
            				suggestion+=" Canada";
            			}
		            	String url = "https://www.booking.com/searchresults.en-gb.html?ss="+suggestion.replace(" ","+")+"&dest_type=city&checkin="+checkin+"&checkout="+formatter.format(c.getTime());
//		            	String url = "https://www.hotelscombined.ca/hotels/"+suggestion.replace(" ","-")+"/"+checkin+"/"+formatter.format(c.getTime())+"/2adults";
		            	System.out.println(url);
		        		Crawler crawler = new Crawler();
						crawler.getPageLinks(url, 0);
			            listOfFiles = folder.listFiles();
			            files = new ArrayList<>();
			            for (File file : listOfFiles) {
			                if (file.isFile()) {
			                	files.add(file.getName());
			                }
			            }
		            	PageRank.FrqBuilder(files, suggestion.toLowerCase(), 10);
		            }
					break;
				}
				case 2: {
					options(hashMap, sc);
					break;
				}
				case 0: {
					break;
				}
				default: {
					System.out.println("Please provide valid input!!!!!!!!!!");
					break;
				}
			}
		} while (userInput != 0);
		System.out.println("Closing our Hotel Web Search Engine!!!!!!!!!!!!!!!");
	}

}
