import java.io.*;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Comparator;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;

public class WordStatSortedLastIndex {
    public static void main(String[] args) throws IOException {
        
		Map<String, IntList> map = new LinkedHashMap<String, IntList>();
		int sch = 0, lineNom = 0;
        try (Scanner reader = new Scanner(
			new InputStreamReader(
				new FileInputStream(args[0]),
				"UTF-8"
			));
		) {
			while (true) {
				while (reader.noWordInLine()) {
					lineNom++;
					sch = 0;
				}
				String line = reader.nextWord();
				if (line.length() == 0) {
					break;
				}
				sch++;
				String str = line.toLowerCase();
				if (map.containsKey(str)) {
					IntList curr = map.get(str);
					curr.addLast(sch, lineNom);
				} else {
					IntList curr = new IntList(sch, lineNom);
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
			List<Map.Entry<String, IntList>> list = new LinkedList<Map.Entry<String, IntList>>(map.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, IntList>>() {
				public int compare(Map.Entry<String, IntList> a, Map.Entry<String, IntList> b) {
					return a.getKey().compareTo(b.getKey());
				}
			});
			for (Map.Entry<String, IntList> current : list) {
                writer.write(current.getKey() + " " + current.getValue());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Output file not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("Output error: " + e.getMessage());
        }
    }
}
