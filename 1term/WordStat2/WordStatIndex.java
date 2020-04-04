import java.io.*;
//~ import java.util.*;
import java.util.Map;
import java.util.LinkedHashMap;

public class WordStatIndex {
    public static void main(String[] args) throws IOException {
        
		Map<String, IntList> map = new LinkedHashMap<String, IntList>();
		int sch = 0;
        try (Scanner reader = new Scanner(
			new InputStreamReader(
				new FileInputStream(args[0]),
				"UTF-8"
			));
		) {
			while (true) {
				String line = reader.nextWord();
				if (line.length() == 0) {
					break;
				}
				sch++;
				String str = line.toLowerCase();
				if (map.containsKey(str)) {
					IntList curr = map.get(str);
					curr.add(sch);
				} else {
					IntList curr = new IntList(sch, 0);
					map.put(str, curr);
				}
			}
		}
		
		try (BufferedWriter writer = new BufferedWriter(
			new OutputStreamWriter(
					new FileOutputStream(args[1]),
					"UTF-8"
			));
		) {
			for (Map.Entry<String, IntList> curr : map.entrySet()) {
				IntList element = curr.getValue();
				writer.write(curr.getKey() + ' ');
				writer.write(String.valueOf(element));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Output file not found: " + e.getMessage());
            return;
        } catch (IOException e) {
            System.out.println("Output error: " + e.getMessage());
        }
    }
}
