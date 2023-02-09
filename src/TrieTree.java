import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TrieTree {
	public class TrieNode {
		Map<Character, TrieNode> c;
		char a;
		boolean isWordComplete;

		public TrieNode(char a) {
			this.a = a;
			c = new HashMap<>();
		}

		public TrieNode() {
			c = new HashMap<>();
		}

		public void insert(String w) {
			if (w == null || w.isEmpty())
				return;
			char char_one = w.charAt(0);
			TrieNode child = c.get(char_one);
			if (child == null) {
				child = new TrieNode(char_one);
				c.put(char_one, child);
			}

			if (w.length() > 1) {
				child.insert(w.substring(1));
			}
			else {
				child.isWordComplete = true;
			}
		}
	}

	TrieNode r;
	public static TrieTree createdTree = null;

	public TrieTree(List<String> words) {
		r = new TrieNode();
		for (String w : words) {
			r.insert(w);
		}
	}

	public void suggest_helper_Funcation(TrieNode r, List<String> list, StringBuffer cur) {
		if (r.isWordComplete) {
			list.add(cur.toString());
		}

		if (r.c == null || r.c.isEmpty())
			return;

		for (TrieNode child : r.c.values()) {
			suggest_helper_Funcation(child, list, cur.append(child.a));
			cur.setLength(cur.length() - 1);
		}
	}

	public List<String> suggest(String p) {
		List<String> list = new ArrayList<>();
		TrieNode lastNode = r;
		StringBuffer cur = new StringBuffer();
		for (char a : p.toCharArray()) {
			lastNode = lastNode.c.get(a);
			if (lastNode == null) {
				return list;
			}
			cur.append(a);
		}
		suggest_helper_Funcation(lastNode, list, cur);
		return list;
	}

	public static void create_dictionary() {
		try {
			File folder = new File("src\\TxtFiles\\");
			File[] listOfFiles = folder.listFiles();
			List<String> words = new ArrayList<>();
			for (File file : listOfFiles) {
				if (file.isFile()) {
					try (Scanner input = new Scanner(file)) {
						while (input.hasNext()) {
							String word = input.next();
							words.add(word);
						}
					}
				}
			}
			File myobj = new File("src\\TxtFiles\\dictonary.txt");
			if(myobj.exists())
				myobj.delete();
			if (myobj.createNewFile()) {
				FileOutputStream fos = new FileOutputStream(myobj);
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
				for (String string : words) {
					if (!string.startsWith("http")) {
						Pattern pattern = Pattern.compile("[^A-Za-z]", Pattern.CASE_INSENSITIVE);
						Matcher m = pattern.matcher(string);
						boolean b = m.find();
						if (!b) {
							bw.write(string);
							bw.newLine();
						}
					}
				}
				bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<String> auto_Suggest_word(String keyword) {

		if(TrieTree.createdTree!=null) {
			List<String> relevantWord = TrieTree.createdTree.suggest(keyword);
			return relevantWord;
		}
		else {
			try {
				File folder = new File("src\\TxtFiles\\");
				File[] listOfFiles = folder.listFiles();
				List<String> words = new ArrayList<>();
				for (File file : listOfFiles) {
					if (file.isFile()) {
						try (Scanner input = new Scanner(file)) {
							while (input.hasNext()) {
								String word = input.next();
								words.add(word);
							}
						}
					}
				}
				File myobj = new File("src\\TxtFiles\\dictonary.txt");
				try {
					if (myobj.createNewFile()) {
						FileOutputStream fos = new FileOutputStream(myobj);
						BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
						for (String string : words) {
							if (!string.startsWith("http")) {
								Pattern pattern = Pattern.compile("[^A-Za-z]", Pattern.CASE_INSENSITIVE);
								Matcher m = pattern.matcher(string);
								boolean b = m.find();
								if (!b) {
									bw.write(string);
									bw.newLine();
								}
							}
						}
						bw.close();
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}

				TrieTree trie = new TrieTree(words);
				if(TrieTree.createdTree==null)
					TrieTree.createdTree = trie;

				List<String> relevantWord = trie.suggest(keyword);
				return relevantWord;
			}
			catch (FileNotFoundException e) {
				System.out.println("Exception : " + e + e.getMessage());
			}
			return null;
		}
	}
}