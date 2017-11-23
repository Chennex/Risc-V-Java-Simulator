package isa;

public class InstructionArguments{
	int opcode = -1;
	int rd = -1;
	int rs1 = -1;
	int rs2 = -1;
	int funct3 = -1;
	int funct7 = -1;
	int immediate = -1;
	
	//This is not a PC - this holds the entire instruction
	int instruction = -1;
	
	public InstructionArguments() {}

	@Override
	public String toString() {
		String result = "Current instruction: " + Integer.toBinaryString(instruction) + "\n";
		result += "Parsed elements: opcode = " + Integer.toBinaryString(opcode) + " (" + Integer.toHexString(opcode) + "), ";
		result += "rd = " + Integer.toBinaryString(rd) + " (" + rd + "), ";
		result += "rs1 = " + Integer.toBinaryString(rs1) + " (" + rs1 + "), ";
		result += "rs2 = " + Integer.toBinaryString(rs2) + " (" + rs2 + "), ";
		result += "funct3 = " + Integer.toBinaryString(funct3) + " (" + Integer.toHexString(funct3) + "), ";
		result += "funct7 = " + Integer.toBinaryString(funct7) + " (" + Integer.toHexString(funct7) + "), ";
		result += "immediate = " + Integer.toBinaryString(immediate) + " (" + Integer.toString(immediate) + "), ";
		return result;
	};
	
	
}