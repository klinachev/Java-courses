import java.util.Arrays;
import java.io.*;
public class Scanner implements AutoCloseable {
    private Reader reader;

    private boolean hasline = false;
	
	private char last = 0;

	private StringBuilder next, line;
    
    public Scanner(InputStream in) {
        reader = new InputStreamReader(in);
    }
    
    public Scanner(Reader r) {
        reader = r;
    }
    
    public Scanner(File r) {
        try {
            reader = new FileReader(r);
        } catch (FileNotFoundException e) {
			System.out.println("Can not find file" + e.getMessage());
		}
    }
    
    public Scanner(String s) {
        reader = new StringReader(s);
    }

    public void close() {
        try {
            reader.close();
        } catch (IOException e) {
            System.out.println("Can not close reader" + e.getMessage());
        }
    }

	private boolean isWord(char ch) {
        return (Character.isAlphabetic(ch) || ch == '\'' || Character.getType(ch) == Character.DASH_PUNCTUATION);
    }
	
	private byte whatChar(char ch) {
		if (isWord(ch)) {
			return 1;
		}
		if (Character.isDigit(ch)) {
			return 0;
		}
		return -1;
    }
    
    public Integer nextInt() throws IOException {
        take((byte)0);
        if (next.length() == 0) {
			return null;
		}
        return Integer.parseInt(next.toString());
    }

    public String nextWord() throws IOException {
        take((byte)1);
        return next.toString();
    }
	
    private byte take(byte vid) throws IOException {
		next = new StringBuilder();
		char ch = last;
		last = 0;
		int c = 0;
		while(whatChar(ch) != vid && ch != '-') {
			c = reader.read();
			if (c == -1) {
				return -1;
			}
			ch = (char) c;
		}
		while(vid == whatChar(ch) || ch == '-') {
			next.append(ch);
			c = reader.read();
			if (c == -1) {
				return vid;
			}
			ch = (char) c;
		} 
        return vid;
    }
	
	public boolean noIntInLine() throws IOException {
        return noInLine((byte)0);
    }
	
	public boolean noWordInLine() throws IOException {
        return noInLine((byte)1);
    }
    
    public boolean noInLine(byte vid) throws IOException {
		if (whatChar(last) == vid) {
			return false;
		}
		char ch = ' ';
		int c = 0;
		while(whatChar(ch) != vid && ch != '-' && ch != '\n') {
			c = reader.read();
			if (c == -1) {
				return false;
			}
			ch = (char) c;
		}
		if (ch == '\n') {
			return true;
		}
		last = ch;
		return false;
	}
}
