package isa;

public class Memory {

	byte[] ram = new byte[2001];
	
	public void writeMemory(int amount, int content, int location) {
		byte[] result = intToByteArray(content);
		for(int i=0;i < amount / 8; i++) {
			ram[location++]= result[3-i];
		}
		System.out.println("Wrote to " + location + " the value " + Integer.toHexString(content));
		System.out.println(result[0] + ", " + result[1] + ", " + result[2] + ", " + result[3] + ", "  );
	}
	
	public int readMemory(int amount, int location) {
		byte[] result = intToByteArray(0);
		for(int i=0;i<amount/8;i++) {
			result[3-i]=ram[location++];
		}
		int resulti = fromByteArray(result);
		resulti = resulti <<(32 - amount) >> (32 - amount);
		System.out.println("Read from " + location + " the value " + Integer.toHexString(resulti));
		System.out.println(result[0] + ", " + result[1] + ", " + result[2] + ", " + result[3] + ", "  );
		return resulti;
	}
	
	public static final byte[] intToByteArray(int value) {
	    return new byte[] {
	            (byte)(value >> 24),
	            (byte)(value >> 16),
	            (byte)(value >> 8),
	            (byte)value};
	}
	int fromByteArray(byte[] bytes) {
	     return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
	}
	
	public String toString() {
		String result = "Memory: \n";
		for(int i=0; i<2001; i++) {
			if(ram[i] != 0) {
				result = result + "Memory location " + i + ": "+ Integer.toHexString(ram[i])+"\n";
			}
		}
		return result;
	}
}


