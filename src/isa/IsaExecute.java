package isa;

public class IsaExecute {

	public static IsaSim load(IsaSim context) {
		// lb, lh, lw, ld, lbu, lhu, lwu
		switch(context.arguments.funct3)
		{
		//First four cases get all bytes from the block, as they extend the sign
		case 0:	//lb
			context.reg.setRegister(context.arguments.rd, context.mem.readMemory(32, context.reg.getRegister(context.arguments.rs1) + context.arguments.immediate));
			break;
		case 1:	//lh
			context.reg.setRegister(context.arguments.rd, context.mem.readMemory(32, context.reg.getRegister(context.arguments.rs1) + context.arguments.immediate));
			break;
		case 2:	//lw (32 bit)
			context.reg.setRegister(context.arguments.rd, context.mem.readMemory(32, context.reg.getRegister(context.arguments.rs1) + context.arguments.immediate));
			break;
		case 3:	//ld - reverts to 32-bit implementation, as memory isn't 64-bit
			context.reg.setRegister(context.arguments.rd, context.mem.readMemory(32, context.reg.getRegister(context.arguments.rs1) + context.arguments.immediate));
			break;
		//These three cases get only a part of bytes from the block, as they don't extend the sign
		case 4:	//lbu
			context.reg.setRegister(context.arguments.rd, context.mem.readMemory(8, context.reg.getRegister(context.arguments.rs1) + context.arguments.immediate));
			break;
		case 5:	//lhu
			context.reg.setRegister(context.arguments.rd, context.mem.readMemory(16, context.reg.getRegister(context.arguments.rs1) + context.arguments.immediate));
			break;
		case 6:	//lwu - for 64-bit systems, here reverses to 32-bit implementation
			context.reg.setRegister(context.arguments.rd, context.mem.readMemory(16, context.reg.getRegister(context.arguments.rs1) + context.arguments.immediate));
			break;
		}
		context.PC += 4;
		return context;
	}

	public static IsaSim handle_0x13(IsaSim context) {
		int n = 0;
		switch(context.arguments.funct3) {
		case 0:
			//addi
			n = context.reg.getRegister(context.arguments.rs1) + context.arguments.immediate;
			System.out.println(context.reg.getRegister(context.arguments.rs1) + " + "+context.arguments.immediate + " = " + n);
			break;
		case 1:
			//slli
			n = context.reg.getRegister(context.arguments.rs1) << context.arguments.immediate;
			System.out.println(context.reg.getRegister(context.arguments.rs1) + " << "+context.arguments.immediate + " = " + n);
			break;
		case 2:
			//slti
			n = context.reg.getRegister(context.arguments.rs1) < context.arguments.immediate ? 1 : 0;
			break;
		case 3:
			//sltiu
			n = Integer.compareUnsigned(context.reg.getRegister(context.arguments.rs1), context.arguments.immediate) < 0 ? 1 : 0;
			break;
		case 4:
			//xori
			n = context.reg.getRegister(context.arguments.rs1) ^ context.arguments.immediate;
			break;
		case 5:
			if((context.arguments.immediate & 0x7f0) == 0) {
				//srli
				n = context.reg.getRegister(context.arguments.rs1) >>> context.arguments.immediate;
				System.out.println(context.reg.getRegister(context.arguments.rs1) + " >>> "+context.arguments.immediate + " = " + n);
			} else {
				//srai
				n = context.reg.getRegister(context.arguments.rs1) >> (context.arguments.immediate & 0xf);
				System.out.println(context.reg.getRegister(context.arguments.rs1) + " >> "+context.arguments.immediate + " = " + n);
			}
			break;
		case 6:
			//ori
			n = context.reg.getRegister(context.arguments.rs1) | context.arguments.immediate;
			break;
		case 7:
			//andi
			n = context.reg.getRegister(context.arguments.rs1) & context.arguments.immediate;
			break;
		}
		context.reg.setRegister(context.arguments.rd, n);
		context.PC += 4;
		return context;
	}

	public static IsaSim shift_immediate_word(IsaSim context) {
		int n = 0;
		switch(context.arguments.funct3) {
		case 1:
			//slliw
			n = context.reg.getRegister(context.arguments.rs1) << context.arguments.immediate;
		case 5:
			if((context.arguments.immediate & 0x7f0) == 0) {
				//srliw
				n = context.reg.getRegister(context.arguments.rs1) >>> context.arguments.immediate;
			} else {
				//sraiw
				n = context.reg.getRegister(context.arguments.rs1) >> ((context.arguments.immediate) & 0xf);
			}
		}
		context.reg.setRegister(context.arguments.rd, n);
		context.PC += 4;
		return context;
	}

	public static IsaSim jalr(IsaSim context) {
		// jalr
		context.reg.setRegister(context.arguments.rd, context.PC + 4);
		context.PC = context.reg.getRegister(context.arguments.rs1) + context.arguments.immediate;
		return context;
	}
	
	public static IsaSim handle_0x73(IsaSim context) {
		// ecall
		switch(context.reg.getRegister(10)) {
		case 1:
			//Print an integer in a1
			System.out.print(context.reg.getRegister(11));
			break;
		case 4:
			//Print a string from address in a1
			int address = context.reg.getRegister(11);
			//As characters are bits, we will need to get a character from following positions
			char current_character = '0';
			while(current_character != 0) {
				current_character = (char)context.mem.readMemory(8, address++);
				System.out.print(current_character);
			}
			break;
		case 9:
			//allocate a1 bytes on the heap, return to a0
			int bytes = context.reg.getRegister(11);
			int current_fp = context.reg.getRegister(8);
			context.reg.setRegister(9, current_fp + bytes);
			context.reg.setRegister(11, current_fp);
			break;
		case 10:
			//Exit with code 0
			System.out.println(context.reg);
			System.out.println(context.mem);
			System.exit(0);
			break;
		case 11:
			//Print a character in a1
			System.out.print((char)context.reg.getRegister(11));
			break;
		case 17:
			//Exit with code in a1
			System.out.println(context.reg);
			System.exit(context.reg.getRegister(11));
			break;
		}
		context.PC += 4;
		return context;
	}

	public static IsaSim auipc(IsaSim context) {
		// auipc - add upper immediate to PC
		context.reg.setRegister(context.arguments.rd, context.PC + context.arguments.immediate);
		context.PC += 4;
		return context;
	}

	public static IsaSim lui(IsaSim context) {
		//Load upper immediate
		context.reg.setRegister(context.arguments.rd, context.arguments.immediate);
		context.PC += 4;
		return context;
	}

	public static IsaSim store(IsaSim context) {
		switch(context.arguments.funct3)
		{
		case 0:	//sb
			context.mem.writeMemory(8, context.reg.getRegister(context.arguments.rs2), context.reg.getRegister(context.arguments.rs1) + context.arguments.immediate);
			break;
		case 1:	//sh
			context.mem.writeMemory(16, context.reg.getRegister(context.arguments.rs2), context.reg.getRegister(context.arguments.rs1) + context.arguments.immediate);
			break;
		case 2:	//sw
			context.mem.writeMemory(32, context.reg.getRegister(context.arguments.rs2), context.reg.getRegister(context.arguments.rs1) + context.arguments.immediate);
			break;
		case 3:	//sd - as memory isn't 64-bit, this only takes in 32 bits
			context.mem.writeMemory(32, context.reg.getRegister(context.arguments.rs2), context.reg.getRegister(context.arguments.rs1) + context.arguments.immediate);
			break;
		}
		context.PC += 4;
		return context;
	}

	public static IsaSim handle_0x33(IsaSim context) {
		// add, sub, sll, slt, sltu, xor, srl, sra, or, and
		
		int n = 0;	//Temp int to store result in. Should be written to registry at address rd.
		switch(context.arguments.funct3)
		{
		case 0:	//Add and sub. (Sub is add with negative number.)
			if(context.arguments.funct7 == 0) {
				n = context.reg.getRegister(context.arguments.rs1) + context.reg.getRegister(context.arguments.rs2);
				System.out.println(context.reg.getRegister(context.arguments.rs1) + " + "+context.reg.getRegister(context.arguments.rs2) + " = " + n);
			}
			else {
				n = context.reg.getRegister(context.arguments.rs1) - context.reg.getRegister(context.arguments.rs2);
				System.out.println(context.reg.getRegister(context.arguments.rs1) + " - "+context.reg.getRegister(context.arguments.rs2) + " = " + n);
			}
			break;
		case 1:	//sll
			n = context.reg.getRegister(context.arguments.rs1) << context.reg.getRegister(context.arguments.rs2);
			System.out.println(context.reg.getRegister(context.arguments.rs1) + " << "+context.reg.getRegister(context.arguments.rs2) + " = " + n);
			break;
		case 2:	//slt
			n = context.reg.getRegister(context.arguments.rs1) < context.reg.getRegister(context.arguments.rs2) ? 1 : 0;
			break;
		case 3:	//sltu
			n = Integer.compare(context.reg.getRegister(context.arguments.rs1), context.reg.getRegister(context.arguments.rs2)) < 0 ? 1 : 0;
			break;
		case 4:	//xor
			n = context.reg.getRegister(context.arguments.rs1) ^ context.reg.getRegister(context.arguments.rs2);
			break;
		case 5:	//srl and sra
			if(context.arguments.funct7 == 0) {
				//srl
				n = context.reg.getRegister(context.arguments.rs1) >>> context.reg.getRegister(context.arguments.rs2);
				System.out.println(context.reg.getRegister(context.arguments.rs1) + " >>> "+context.reg.getRegister(context.arguments.rs2) + " = " + n);
			} else {
				//sra
				n = context.reg.getRegister(context.arguments.rs1) >> context.reg.getRegister(context.arguments.rs2);
				System.out.println(context.reg.getRegister(context.arguments.rs1) + " >> "+context.reg.getRegister(context.arguments.rs2) + " = " + n);
			}
			break;
		case 6:	//or
			n = context.reg.getRegister(context.arguments.rs1) | context.reg.getRegister(context.arguments.rs2);
			System.out.println(context.reg.getRegister(context.arguments.rs1) + " | "+context.reg.getRegister(context.arguments.rs2) + " = " + n);
			break;
		case 7:	//and
			n = context.reg.getRegister(context.arguments.rs1) & context.reg.getRegister(context.arguments.rs2);
			System.out.println(context.reg.getRegister(context.arguments.rs1) + " & "+context.reg.getRegister(context.arguments.rs2) + " = " + n);
			break;
		}
		
		context.reg.setRegister(context.arguments.rd, n);
		context.PC += 4;
		return context;
	}

	public static IsaSim handle_0x3b(IsaSim context) {
		// addw, subw, sllw, srlw, sraw
		int n = 0;
		switch(context.arguments.funct3) {
		case 0:
			if(context.arguments.funct7 == 0) {
				//addw
				n = context.reg.getRegister(context.arguments.rs1) + context.reg.getRegister(context.arguments.rs2);
			} else {
				//subw
				n = context.reg.getRegister(context.arguments.rs1) - context.reg.getRegister(context.arguments.rs2);
			}
			break;
		case 1:
			//sllw
			n = context.reg.getRegister(context.arguments.rs1) << context.reg.getRegister(context.arguments.rs2);
			break;
		case 5:
			if(context.arguments.funct7 == 0) {
				//srlw
				n = context.reg.getRegister(context.arguments.rs1) >>> context.reg.getRegister(context.arguments.rs2);
			} else {
				//sraw
				n = context.reg.getRegister(context.arguments.rs1) >> context.reg.getRegister(context.arguments.rs2);
			}
			break;
		}
		context.reg.setRegister(context.arguments.rd, n);
		context.PC += 4;
		return context;
	}

	public static IsaSim branch(IsaSim context) {
		int comparator1 = context.reg.getRegister(context.arguments.rs1);
		int comparator2 = context.reg.getRegister(context.arguments.rs2);
		switch(context.arguments.funct3) {
		case 0:
			//beq
			if(comparator1 == comparator2)
				context.PC += context.arguments.immediate;
			else
				context.PC += 4;
			break;
		case 1:
			//bne
			if(comparator1 != comparator2)
				context.PC += context.arguments.immediate;
			else
				context.PC += 4;
			break;
		case 4:
			//blt
			if(comparator1 < comparator2)
				context.PC += context.arguments.immediate;
			else
				context.PC += 4;
			break;
		case 5:
			//bge
			if(comparator1 >= comparator2)
				context.PC += context.arguments.immediate;
			else
				context.PC += 4;
			break;
		case 6:
			//bltu
			if(Integer.compareUnsigned(comparator1, comparator2) < 0)
				context.PC += context.arguments.immediate;
			else
				context.PC += 4;
			break;
		case 7:
			//bgeu
			if(Integer.compareUnsigned(comparator1, comparator2) >= 0)
				context.PC += context.arguments.immediate;
			else
				context.PC += 4;
			break;
		}
		return context;
	}

	public static IsaSim jal(IsaSim context) {
		// jal
		context.reg.setRegister(context.arguments.rd, context.PC + 4);
		context.PC += context.arguments.immediate;
		return context;
	}

}
