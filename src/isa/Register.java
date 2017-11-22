package isa;

import java.util.Arrays;

public class Register{
	private int[] r = new int[32];
	
	public Register() {
		Arrays.fill(r, 0);
	}
	
	public void setRegister(int index, int value) throws IllegalArgumentException {
		if(index == 0)
			System.out.println("NOTE: Register r0 cannot be changed");
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
	
	public String toString() {
		String result = "";
		for(int i=0; i<32;i++) {
			result = result + "Register "+i+": "+r[i]+"\n";
		}
		return result;
	}
}