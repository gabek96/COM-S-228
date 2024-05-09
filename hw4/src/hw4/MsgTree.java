package hw4;
import java.util.Iterator;
import java.util.Stack;

/**
 * @author Gabe
 */
public class MsgTree {

	public char payloadChar;
	public MsgTree left;
	public MsgTree right;

	/*
	 * Can use a static char idx to the tree string for recursive solution, but it
	 * is not strictly necessary
	 */
	private static int staticCharIdx = 0;

	/**
	 * Constructor building the tree from a string
	 * 
	 * @param encodingString
	 */
	public MsgTree(String encodingString) {
		Stack<MsgTree> tree = new Stack<>();
		this.payloadChar = '^';
		tree.push(this);

	    // Loop through the characters in the encodingString starting from index 1
		for (int i = 1; i < encodingString.length(); i++) {
			MsgTree current = tree.peek();
	        // Check if the current node's payloadChar is not '^' or both children are not null
			if (current.payloadChar != '^' || (current.left != null && current.right != null)) {
				tree.pop();
				i--;
			} else {
	            // If the conditions are not met, create a new node with the character at index i
				char c = encodingString.charAt(i);
				MsgTree newCurrent = new MsgTree(c);
				tree.push(newCurrent);
				
				// If the current node's left child is null, set it to the new node;
	            // otherwise, set the right child to the new node
				if (current.left == null) {
					current.left = newCurrent;
				} else {
					current.right = newCurrent;
				}
			}
		}
	}

	// Constructor for a single node with null children
	public MsgTree(char payloadChar) {
		this.payloadChar = payloadChar;
		this.left = null;
		this.right = null;
	}

	// method to print characters and their binary codes
	public static void printCharacterCodes(MsgTree root, String code) {
		System.out.println("character code\n-------------------------");
		for (char ch : code.toCharArray()) {
			getCode(root, ch, binCode = "");
			System.out.println("    " + (ch == '\n' ? "\\n" : ch + " ") + "    " + binCode);
		}
	}

	private static String binCode;

	/**
	 * Gets code and recursively calls itself setting the alphabet
	 * 
	 * @param root
	 * @param ch
	 * @param path
	 * @return
	 */
	private static boolean getCode(MsgTree rootnode, char leaf, String path) {
		if (rootnode != null) {
			if (rootnode.payloadChar == leaf) {
				binCode = path;
				return true;
			}
			return getCode(rootnode.left, leaf, path + "0") || getCode(rootnode.right, leaf, path + "1");
		}
		return false;
	}

	/**
	 * method decodes the message and print out the method of the console
	 * 
	 * @param codes
	 * @param msg
	 */
	public void decode(MsgTree codes, String msg) {
		System.out.println("Message:");
		MsgTree current = codes;
		StringBuilder build = new StringBuilder();

		for (int i = 0; i < msg.length(); i++) {
			char ch = msg.charAt(i);
			current = (ch == '0' ? current.left : current.right);
			if (current.payloadChar != '^') {
				getCode(codes, current.payloadChar, binCode = "");
				build.append(current.payloadChar);
				current = codes;
			}
		}
		System.out.println(build.toString());
		stats(msg, build.toString());
	}

	/**
	 * Extra credit statistics. Pulls the encoded and decoded strings data to print
	 * 
	 * @param encode
	 * @param decode
	 */
	private void stats(String encode, String decode) {
		System.out.println("STATISTICS:");
		System.out.println(String.format("Average bits/char:\t%.1f", encode.length() / (double) decode.length()));
		System.out.println("Total Characters:\t" + decode.length());
		System.out.println(String.format("Space Savings:\t%.1f%%", (1d - decode.length() / (double) encode.length()) * 100));
	}
}
