package isa;

public class IsaExecute {

	public static IsaSim load(IsaSim context) {
		// TODO lb, lh, lw, ld, lbu, lhu, lwu
		switch(context.arguments.funct3)
		{
		case 000:	//lb
		case 001:	//lh
		case 010:	//lw
		case 011:	//ld
		case 100:	//lbu
		case 101:	//lhu
		case 110:	//lwu
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
			break;
		case 1:
			//slli
			n = context.reg.getRegister(context.arguments.rs1) << context.arguments.immediate;
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
			} else {
				//srai
				n = context.reg.getRegister(context.arguments.rs1) >> (context.arguments.immediate & 0xf);
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
		// TODO ecall
		switch(context.reg.getRegister(10)) {
		case 1:
			//Print an integer in a1
			System.out.print(context.reg.getRegister(11));
			break;
		case 4:
			//Print a string from address in a1
			int address = context.reg.getRegister(11);
			//TODO
			break;
		case 9:
			//TODO allocate a1 bytes on the heap, return to a0
			int bytes = context.reg.getRegister(11);
			
			break;
		case 10:
			//Exit with code 0
			System.out.println(context.reg);
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
		context.PC += context.arguments.immediate;
		return context;
	}

	public static IsaSim lui(IsaSim context) {
		//Load upper immediate
		int n = 0;
		n = context.arguments.immediate << 12;
		context.reg.setRegister(context.arguments.rd, n);
		context.PC += 4;
		return context;
	}

	public static IsaSim store(IsaSim context) {
		// TODO sb, sh, sw, sd
		switch(context.arguments.funct3)
		{
		
		case 000:	//sb
		
		case 001:	//sh
		
		case 010:	//sw
			
		case 011:	//sd
			
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
			if(context.arguments.funct7 == 0)
				n = context.reg.getRegister(context.arguments.rs1) + context.reg.getRegister(context.arguments.rs2);
			else
				n = context.reg.getRegister(context.arguments.rs1) - context.reg.getRegister(context.arguments.rs2);
			break;
		case 1:	//sll
			n = context.reg.getRegister(context.arguments.rs1) << context.reg.getRegister(context.arguments.rs2);
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
			} else {
				//sra
				n = context.reg.getRegister(context.arguments.rs1) >> context.reg.getRegister(context.arguments.rs2);
			}
			break;
		case 6:	//or
			n = context.reg.getRegister(context.arguments.rs1) | context.reg.getRegister(context.arguments.rs2);
			break;
		case 7:	//and
			n = context.reg.getRegister(context.arguments.rs1) & context.reg.getRegister(context.arguments.rs2);
			break;
		}
		
		context.reg.setRegister(context.arguments.rd, n);
		context.PC += 4;
		System.out.println(n);
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
