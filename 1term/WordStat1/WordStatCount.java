import java.io.*;
import java.util.*;

public class WordStatCount {
	public static boolean isWord(char ch) {
        return (Character.isAlphabetic(ch) || ch == '\'' || Character.getType(ch) == Character.DASH_PUNCTUATION);
    }
    public static void main(String[] args) throws IOException {
        
		Map<String, Integer> map = new LinkedHashMap<String, Integer>();
		int c = 0;
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(
                new FileInputStream(args[0]),
                "UTF-8"
			));
		) {
			while (c != -1) {
				StringBuilder line = new StringBuilder();
				c = reader.read();
				char ch = ' ';
				if (c >= 0) {
					ch = (char) c;
				}
				while (c >= 0 && !(isWord(ch))){
					c = reader.read();
					ch = (char) c;
				}
				while (c >= 0 && isWord(ch)) {
					line.append(ch);
					c = reader.read();
					ch = (char) c;
				}
				if (line.length() > 0) {
					String str = (line.toString()).toLowerCase();
					boolean f = true;
					if (map.containsKey(str)) {
							map.put(str, map.get(str) + 1);
							f = false;
					}
					if (f) {
						map.put(str,1);
					}
				}
            }
		} catch (FileNotFoundException e) {
            System.out.println("Input file not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Input file error: " + e.getMessage());
        }
		
		try (BufferedWriter writer = new BufferedWriter(
			new OutputStreamWriter(
					new FileOutputStream(args[1]),
					"UTF-8"
			));
		) {
			List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
				public int compare(Map.Entry<String, Integer> a, Map.Entry<String, Integer> b) {
					return a.getValue() - b.getValue();
				}
			});
			for (Map.Entry<String, Integer> current : list) {
                writer.write(current.getKey() + ' ' + String.valueOf(current.getValue()) + '\n');
            }
        } catch (FileNotFoundException e) {
            System.out.println("Output file not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Output file error: " + e.getMessage());
        }
    }
}
