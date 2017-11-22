package isa;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
	static IsaSim decoder = new IsaSim();
			
	public static void main(String[] arguments) throws IOException {

			/*Integer[] in = readIn();
			while(decoder.PC <= in.length * 4) {
				decoder.decodeInstruction(in[decoder.PC/4]);
				System.out.println();
			}
			*/
			
			RandomAccessFile file = new RandomAccessFile(new File(readPath()), "r");
			while(decoder.PC <= file.length()) {
				file.seek(decoder.PC);
				decoder.decodeInstruction(Integer.reverseBytes(file.readInt()));
			}
			file.close();
			
	}
	
	private static Integer[] readIn()
	{
		ArrayList<Integer> list = new ArrayList<Integer>();
		Scanner reader = new Scanner(System.in);
		while(reader.hasNextInt()) {
			list.add(reader.nextInt());
		}
		reader.close();
		Integer[] result = new Integer[list.size()];
		list.toArray(result);
		return result;
	}
	private static String readPath()
	{
		Scanner reader = new Scanner(System.in);
		String n = reader.nextLine();
		reader.close();
		return n;
	}
}
