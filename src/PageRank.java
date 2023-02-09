

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PageRank {
		public static String[] wordsFromFile(File f) {  // get list of words for particular text file
			In in = new In(f);   // type of memory management
			String text = in.readAll();

			text = text.replaceAll("[^a-zA-Z0-9\\s]", ""); // regex 
			String[] words = text.split(" ");
			return words;

		}

		// returning URL
		public static String getURL(File f) {
			In in = new In(f);
			String url = in.readLine();

			return url;
		}

		// get price based on location and pattern of price in text files
		public static String getprice(File f) {
			In in = new In(f);
			String url = in.readLine();
			String text = in.readLine();
			if(f.getName().toLowerCase().contains("booking")) {		 // for booking.com
			    String pattern = "CAD [0-9\\.]+";
			    Pattern r = Pattern.compile(pattern); // apply pattern matching and create regex
			    Matcher m = r.matcher(text); // matcher 
			    String price;
			    if(m.find()) {
			    	price = m.group(0);
//			    	System.out.println(price);
			    }
			    else {
			    	for(String el:text.split("CAD ")) {
			    		System.out.println(el);
			    	}
			    	price = " ";
			    }
				return price;
			}
			else {  // for hotelscombined.com
				String price = "C$"+text.split("C\\$")[1].split("guests")[0]+"guests";
				return price;
			}
		}

		// Frequency Builder for Each file and compare it with search queries.
		public static void FrqBuilder(ArrayList<String> as, String key, int result_count) {
			HashMap<String, Integer> wordMap = new HashMap<>();
			for (String fileName : as) {
				String[] wordlist = wordsFromFile(new File("src\\TxtFiles\\" + fileName)); // array of words form files
				for (String word : wordlist) {
					if (word.toLowerCase().equals(key.split("\\W+")[0])) {
						if (wordMap.containsKey(fileName)) {
							wordMap.put(fileName, wordMap.get(fileName) + 1);
						} else {
							wordMap.put(fileName, 1);
						}
					}
				}
			}
			sortValuesInverse(wordMap, result_count);
		}

		//sort result descending order based on occurance of word in files
		
		private static void sortValuesInverse(HashMap<String, Integer> map, int result_count) {
			List list = new LinkedList(map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
	            @Override
				public int compare(Map.Entry<String, Integer> o1,Map.Entry<String, Integer> o2) {
	                return (o1.getValue()).compareTo(o2.getValue());
	            }
	        });
			Collections.reverse(list);
			System.out.println("Top 10 Results using Frequency analysis:");

			for (int i = 1; i <= result_count; i++) {
				String name = list.get(i).toString();
				name = name.substring(0, name.lastIndexOf('.'));
				String Occu = list.get(i).toString();

				int pos = Occu.lastIndexOf("=");

				String txtURL = name + ".txt";
				System.out.println(i + " : URL: " + getURL(new File("src\\TxtFiles\\" + txtURL)));
				System.out.println("Hotel and Website name: " + name);
				System.out.println("Price : " + getprice(new File("src\\TxtFiles\\" + txtURL)));
				System.out.println("Total number of Occurences in given file is :" + Occu.substring(pos, Occu.length()));
				System.out.println();

			}

		}


}