package isa;

import java.util.LinkedList;
import java.util.List;


public class Memory {

	List<Integer> ram = new LinkedList<Integer>();
			
	private int pointer;

	
	
	public void setPointer(int pointer)
	{
		this.pointer = pointer;
	}
	
	//Amount of memory to read should be given as an int, where each integer refers to one block.
	public int[] readMemory(int amount)
	{
		int[] returnvalue = new int[amount];
		int counter = 0;
		for(int x = pointer; x >= pointer + amount; x++)
			returnvalue[counter] = (int) ram.get(x);
		
		return returnvalue;
	}
	
	public void writeMemory(int amount, int[] content)
	{
		int x;
		int y = 0;
		for(x = pointer; x >= pointer + amount; x++)
		{
			ram.add(x,content[y]);
			y++;
		}
		pointer = x;
	}


}
