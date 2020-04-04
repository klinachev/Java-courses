package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Stack;

import static java.lang.Math.abs;

public class Md2Html {
	private static StringBuilder[] ans = new StringBuilder[10];

	private static int anssize = 0;

	private static int[] mas = new int[10];

	private static Stack<Integer> stack = new Stack<>();

	private static String line = "";

	private static byte whatChar(int it, String line) {
		char fir = line.charAt(it);
		boolean b = (it + 2 > line.length()) || fir != line.charAt(it + 1);
		if (fir == '`' && (b)) return 1;
		if ((fir == '_') && (b)
				&& (it < 1 || line.charAt(it - 1) != '\\')) return 2;
        if ((fir == '*') && (b)
                && (it < 1 || line.charAt(it - 1) != '\\')) return -2;
		if (it + 2 > line.length()) return 0;
		char sec = line.charAt(it + 1);
		if ((fir == '_' || fir == '*') && fir == sec) return 4;
		if (fir == '-' && fir == sec) return 5;
		return 0;
	}

	private static boolean isLineEnded(BufferedReader reader, int it) throws IOException {
		if (it == line.length()) {
			line = reader.readLine();
			ans[anssize].append('\n');
			return true;
		}
		return false;
	}

	private static void takeAns(boolean f) {
		for (int i = 1; i <= anssize; i++) {
			if (i < anssize - 1 || f) {
				if (mas[i - 1] > 0) {
					ans[0].append("_");
				} else {
					ans[0].append("*");
				}
			}
			ans[0].append(ans[i]);
		}
		anssize = 0;
	}

	private static boolean checkChar(int c1) {
		byte koch = whatChar(c1, line);
		boolean f = koch > 2;
		if (line.charAt(c1) == '\\') {
			return f;
		}
		if (c1 >  1 && line.charAt(c1 - 1) == '\\') {
			ans[anssize].append(line.charAt(c1));
			return f;
		}
		switch (line.charAt(c1)) {
			case ('<'):
				ans[anssize].append("&lt;");
				return f;
			case ('>'):
				ans[anssize].append("&gt;");
				return f;
			case ('&'):
				ans[anssize].append("&amp;");
				return f;
		}
		if (koch == 0) {
			ans[anssize].append(line.charAt(c1));
			return f;
		}
		if (abs(koch) == 2) {
		    if (anssize > 0 &&  abs(mas[anssize - 1]) == stack.size() && koch * mas[anssize - 1] > 0) {
				ans[anssize - 1].append("<em>");
				ans[anssize].append("</em>");
                takeAns(false);
                mas[0] = 0;
            } else {
				if (koch > 0) {
					mas[anssize++] = stack.size();
				} else {
					mas[anssize++] = -stack.size();
				}
				ans[anssize] = new StringBuilder();
            }
			return f;
		}

		if (!stack.empty() && stack.peek() == koch) {
			ans[anssize].append("</");
			stack.pop();
		} else {
			ans[anssize].append("<");
			stack.push((int) koch);
		}
		switch (koch) {
			case (1):
				ans[anssize].append("code>");
				break;
			case (4):
				ans[anssize].append("strong>");
				break;
			case (5):
				ans[anssize].append("s>");
				break;
		}
		return f;
	}



	private static int start(int it) {
		while (it < line.length() && line.charAt(it) == '#') {
			++it;
		}
		System.out.print(it);
		if (it < line.length() && line.charAt(it) == ' ' && it > 0) {
			ans[anssize].append("<h").append(it).append(">");
			stack.push(10 + it);
			it++;
		} else {
			stack.push(10);
			ans[anssize].append("<p>");
			if (line.charAt(it) != ' ') {
				ans[anssize].append("#".repeat(it));
			}
		}
		return it;
	}

	private static void end() {
		if (!stack.empty()) {
			int nl = stack.pop();
			if (nl > 10) {
				ans[anssize].append("</h").append(nl - 10).append(">");
			} else {
				ans[anssize].append("</p>");
			}
		}
	}

	public static void main(String[] args) {
		boolean newLine = true, noEmptyLine = true, wasFirstLine = false;
		ans[anssize] = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(
						new FileInputStream(args[0]),
						StandardCharsets.UTF_8
				))
		) {
			try (BufferedWriter writer = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(args[1]),
							StandardCharsets.UTF_8
					))
			) {
				while ((line = reader.readLine()) != null) {
					if (line.length() == 0) {
						if (noEmptyLine && wasFirstLine) {
							end();
							takeAns(true);
							writer.write(ans[0].toString());
							ans[0] = new StringBuilder();
						}
						newLine = true;
						noEmptyLine = false;
					} else {
						if (wasFirstLine) {
							ans[anssize].append('\n');
						}
						wasFirstLine = true;
						noEmptyLine = true;
						int it = 0;
						if (newLine) {
							newLine = false;
							it = start(it);
						}
						for (; it < line.length(); ++it) {
							if (line.charAt(it) == '!' && line.charAt(it + 1) == '[') {
                                ans[anssize].append("<img alt='");
								it += 2;
								while (line.charAt(it) != ']') {
									ans[anssize].append(line.charAt(it));
									++it;
									if (isLineEnded(reader, it)) {
										it = 0;
									}
								}
                                ans[anssize].append("' src='");
								while (line.charAt(it) != '(') {
									it++;
								}
								it++;
								while (line.charAt(it) != ')') {
									ans[anssize].append(line.charAt(it++));
									if (isLineEnded(reader, it)) {
										it = 0;
									}
								}
								ans[anssize].append("'>");
							} else {
								if (checkChar(it)) {
									++it;
								}
							}
						}
					}
				}
				end();
				takeAns(true);
				writer.write(ans[0].toString());
				ans[0] = new StringBuilder();
			} catch (FileNotFoundException e) {
				System.out.println("Output file not found: " + e.getMessage());
			} catch (IOException e) {
				System.out.println("Output file error: " + e.getMessage());
			}
		} catch (FileNotFoundException e) {
			System.out.println("Input file not found: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Input file error: " + e.getMessage());
		}
	}
}
