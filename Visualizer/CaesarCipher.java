package Visualizer;

public class CaesarCipher {

	private static final int start = 32;
	private static final int end = 126;
	private static final int range = end - start + 1;


	public String encrypt(String plaintext) {
		int key = 10;
		StringBuilder output = new StringBuilder();
			for (int i = 0; i < plaintext.length(); i++) {
				char temp = plaintext.charAt(i);

				if (temp >= start && temp <= end) {
					int shifted = (temp - start + key + range) % range + start;
					output.append((char) shifted);
				} else {
					output.append(temp);
				}
			}

			return output.toString();
		}
	}

