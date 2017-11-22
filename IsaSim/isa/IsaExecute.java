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
			n = context.reg.getRegister(context.arguments.rs1) + context.arguments.immediate;
			break;
		case 1:
			n = context.reg.getRegister(context.arguments.rs1) << context.arguments.immediate;
			break;
		case 5:
			if((context.arguments.immediate & 0x7f0) == 0) {
				
			} else {
				context.arguments.immediate = (context.arguments.immediate) & 0xf;
				System.out.println(context.arguments.immediate);
				n = context.reg.getRegister(context.arguments.rs1) >> context.arguments.immediate;
			}
			break;
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
		System.out.println(context.reg.getRegister(10));
		System.out.println(context.reg.getRegister(11));
		System.out.println(context.reg.getRegister(12));
		System.exit(0);
		return context;
	}

	public static IsaSim auipc(IsaSim context) {
		// TODO auipc
		return context;
	}

	public static IsaSim lui(IsaSim context) {
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
				n = context.arguments.rs1 - context.arguments.rs2;
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
		// TODO beq, bne, blt, bge, bltu, bgeu
		return context;
	}

	public static IsaSim jal(IsaSim context) {
		// TODO jal
		return context;
	}


}
