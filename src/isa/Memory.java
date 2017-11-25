package isa;

import java.util.LinkedList;
import java.util.List;


public class Memory {

	List<Integer> ram = new LinkedList<Integer>();
	
	//Amount of memory to read should be given as an int, where each integer refers to one block.
	public int readMemory(int amount, int location)
	{
		int mask = 1;
		for(int i=0; i<amount; i++) {
			mask *= 2;
		}
		mask -= 1;
		int result = ram.get(location) & mask;
		result = (result << (32 - amount)) >> (32-amount); //Shift it to sign extend
		System.out.println("Read " + result + " (without a mask: " + ram.get(location)+ ") from memory location "+location);
		return result;
	}
	
	public void writeMemory(int amount, int content, int location)
	{
		//Expand memory to include place to write to.		
		int mask = 1;
		for(int i=0; i<amount; i++) {
			mask *= 2;
		}
		mask -= 1;
		while(location >= ram.size())
		{
			ram.add(0);
		}
		ram.set(location,content);
		System.out.println("Wrote " + (content & mask) + " (without a mask: " + content+ ") to memory location "+location);
		}

	public String toString() {
		String result = "Memory: \n";
		for(int i=0; i<ram.size(); i++) {
			if(ram.get(i) != 0) {
				result = result + "Memory location " + i + ": "+ ram.get(i)+"\n";
			}
		}
		return result;
	}
}


