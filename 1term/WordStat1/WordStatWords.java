import java.io.*;
import java.util.*;

public class WordStatWords{
    public static void main(String[] args) throws IOException {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
		int c = 0;
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(
                new FileInputStream(args[0]),
                "UTF-8"
        ));) {
			while (c != -1) {
				StringBuilder line = new StringBuilder();
				c = reader.read();
				char ch = ' ';
				if (c >= 0) {
					ch = (char) c;
				}
				while (c >= 0 && !(Character.isAlphabetic(ch) ||
						ch == '\'' 
						|| Character.getType(ch) == Character.DASH_PUNCTUATION)){
					c = reader.read();
					ch = (char) c;
				}
				while (c >= 0 && (Character.isAlphabetic(ch) ||
						ch == '\'' 
						|| Character.getType(ch) == Character.DASH_PUNCTUATION)) {
					line.append(ch);
					System.out.println(ch);
					c = reader.read();
					ch = (char) c;
				}
				if (line.length() > 0) {
					String str = (line.toString()).toLowerCase();
					boolean f = true;
					if ((map.containsKey(str))) {
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
			) 
			);) {
			Map<String, Integer> map2 = new TreeMap<String, Integer>(map); 
			for (Map.Entry<String, Integer> entry: map2.entrySet()) {
				 writer.write(entry.getKey() + " " + entry.getValue() + System.getProperty("line.separator"));
			}
        } catch (FileNotFoundException e) {
            System.out.println("Output file not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Output file error: " + e.getMessage());
        }
    }
}
