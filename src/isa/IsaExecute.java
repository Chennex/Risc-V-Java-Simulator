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
		return context;
	}

	public static IsaSim handle_0x13(IsaSim context) {
		// TODO slli, slti, sltiu, xori, srli, srai, ori, andi
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
			//TODO: slti
			break;
		case 3:
			//TODO: sltiu
			break;
		case 4:
			//TODO: xori
			break;
		case 5:
			if((context.arguments.immediate & 0x7f0) == 0) {
				//TODO: srli
			} else {
				//srai
				context.arguments.immediate = (context.arguments.immediate) & 0xf;
				System.out.println(context.arguments.immediate);
				n = context.reg.getRegister(context.arguments.rs1) >> context.arguments.immediate;
			}
			break;
		case 6:
			//TODO: ori
			break;
		case 7:
			//TODO: andi
		}
		context.reg.setRegister(context.arguments.rd, n);
		context.PC += 4;
		return context;
	}

	public static IsaSim shift_immediate_word(IsaSim context) {
		// TODO slliw, srliw, sraiw
		return context;
	}

	public static IsaSim jalr(IsaSim context) {
		// TODO jalr
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
			System.exit(0);
			break;
		case 11:
			//Print a character in a1
			System.out.print((char)context.reg.getRegister(11));
			break;
		case 17:
			//Exit with code in a1
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
		return context;
	}

	public static IsaSim handle_0x33(IsaSim context) {
		// TODO add, sub, sll, slt, sltu, xor, srl, sra, or, and
		
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
			
		case 2:	//slt

		case 3:	//sltu

		case 4:	//xor
			n = context.arguments.rs1 ^ context.arguments.rs2;
			break;
		case 5:	//srl and sra
			
		case 6:	//or
			n = context.arguments.rs1 | context.arguments.rs2;
			break;
		case 7:	//and
			n = context.arguments.rs1 & context.arguments.rs2;
			break;
		}
		
		context.reg.setRegister(context.arguments.rd, n);
		context.PC += 4;
		System.out.println(n);
		return context;
	}

	public static IsaSim handle_0x3b(IsaSim context) {
		// TODO addw, subw, sllw, srlw, sraw
		
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
