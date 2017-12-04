package isa;

import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.util.Arrays;

public class Register{
	private int[] r = new int[32];
	
	public Register() {
		Arrays.fill(r, 0);
		setRegister(2,2000);	//Defaults stack pointer to 200.
	}
	
	public void setRegister(int index, int value) throws IllegalArgumentException {
		if(index == 0)
			//Do nothing. x0 is immutable.
			return;
		if(index < 0 || index > 31)
			throw new IllegalArgumentException("Register index out of range");
		else
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
		String result = "Registers: \n";
		for(int i=0; i<32;i++) {
			result = result + "Register "+i+": "+r[i]+"\n";
		}
		return result;
	}
	
	public void writeToFile()  {
		String path = Main.readPath("Input path to output file: ");
		DataOutputStream output;
		try {
			output = new DataOutputStream(new FileOutputStream(path));
			for(int a: r) {
				output.writeInt(a);
			}
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}