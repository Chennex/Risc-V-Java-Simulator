package isa;

import java.util.Arrays;

public class Register{
	private int[] r = new int[32];
	
	public Register() {
		Arrays.fill(r, 0);
	}
	
	public void setRegister(int index, int value) throws IllegalArgumentException {
		if(index == 0)
			throw new IllegalArgumentException("Register r0 cannot be changed");
		if(index < 0 || index > 31)
			throw new IllegalArgumentException("Register index out of range");
		r[index] = value;
	}
	
	public int getRegister(int index) {
		if(index == 0)
			return 0;
		if(index < 0 || index > 31)
			throw new IllegalArgumentException("Register index out of range");
		return r[index];
	}
}