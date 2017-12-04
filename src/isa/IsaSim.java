package isa;

public class IsaSim {
	Register reg = new Register();
	Memory mem;
	int PC = 0;
	InstructionArguments arguments;
	
	public IsaSim() {
		mem = new Memory();
	}
	
	public void decodeInstruction(int instruction) {
		arguments = new InstructionArguments();
		arguments.opcode = instruction & 0x7f;
		arguments.instruction = instruction;
		System.out.println("PC at " + PC);
		if(IsaConstants.i_list.contains(arguments.opcode))
			handle_itype();
		else if(IsaConstants.u_list.contains(arguments.opcode))
			handle_utype();
		else if(IsaConstants.s_list.contains(arguments.opcode))
			handle_stype();
		else if(IsaConstants.r_list.contains(arguments.opcode))
			handle_rtype();
		else if(IsaConstants.sb_list.contains(arguments.opcode))
			handle_sbtype();
		else if(IsaConstants.uj_list.contains(arguments.opcode))
			handle_ujtype();
		else {
			throw new IllegalArgumentException("Instruction contains unrecognized opcode");
		}
		return;
	}


    void handle_itype() {
    	
        arguments.rd = (arguments.instruction >>> 7) & 0x1f;
        arguments.funct3 = (arguments.instruction >>> 12) & 0x7;
        arguments.rs1 = (arguments.instruction >>> 15) & 0x1f;
        arguments.immediate = (arguments.instruction >> 20);
		System.out.println(arguments.toString());

        switch(arguments.opcode) {
        		case 0x03:
        			copyData(IsaExecute.load(this)); 
        			break;
        		case 0x13:
        			copyData(IsaExecute.handle_0x13(this));
        			break;
        		case 0x1b:
        			System.out.println("64-bit operations not supported");
        			break;
        		case 0x67:
        			copyData(IsaExecute.jalr(this));
        			break;
        		case 0x73:
        			copyData(IsaExecute.handle_0x73(this));
        			break;
        		default:
        			break;
        }
    }


    void handle_utype() {
        arguments.rd = (arguments.instruction >>> 7) & 0x1f;
        arguments.immediate = (arguments.instruction >>> 12) << 12;
		System.out.println(arguments.toString());

        switch(arguments.opcode) {
        		case 0x17:
        			copyData(IsaExecute.auipc(this));
        			break;
        		case 0x37:
        			copyData(IsaExecute.lui(this));
        			break;
        		default:
        			break;
        }
    }

    void handle_stype() {
        int imm4 = (arguments.instruction >>> 7) & 0x1f;
        arguments.funct3 = (arguments.instruction >>> 12) & 0x7;
        arguments.rs1 = (arguments.instruction >>> 15) & 0x1f;
        arguments.rs2 = (arguments.instruction >>> 20) & 0x1f;
        int imm5 = (arguments.instruction >> 25);
        arguments.immediate = imm4 | (imm5 << 5);
		System.out.println(arguments.toString());
        
        switch(arguments.opcode) {
        		case 0x23:
        			copyData(IsaExecute.store(this));
        			break;
        		default:
        			break;
        }
	}

    void handle_rtype() {
    		arguments.rd = (arguments.instruction >>> 7) & 0x1f;
        arguments.funct3 = (arguments.instruction >>> 12) & 0x7;
        arguments.rs1 = (arguments.instruction >>> 15) & 0x1f;
        arguments.rs2 = (arguments.instruction >>> 20) & 0x1f;
        arguments.funct7 = (arguments.instruction >>> 25);
		System.out.println(arguments.toString());

        switch(arguments.opcode) {
        		case 0x33:
        			copyData(IsaExecute.handle_0x33(this));
        			break;
        		case 0x3b:
        			System.out.println("64-bit operations not supported");
        			break;
        		default:
        			break;
        }
    }

    void handle_sbtype() {
        int imm4 = (arguments.instruction >>> 7) & 0x1f;
        arguments.funct3 = (arguments.instruction >>> 12) & 0x7;
        arguments.rs1 = (arguments.instruction >>> 15) & 0x1f;
        arguments.rs2 = (arguments.instruction >>> 20) & 0x1f;
        int imm5 = (arguments.instruction >>> 25);
        int bit11 = (imm4 & 0x1) << 11;
        int bit12 = (imm5 >>> 6);
        int bit1_4 = (imm4 >>> 1) << 1;
        int bit5_10 = (imm5 & 0x3f) << 5;
        arguments.immediate = bit1_4 | bit5_10 | bit11;
        //Handle sign bit
        if(bit12 == 1) {
        		arguments.immediate -= 4096; //2^12
        }
		System.out.println(arguments.toString());

        switch(arguments.opcode) {
		case 0x63:
			copyData(IsaExecute.branch(this));
			break;
		default:
			break;
        }
    }

    public void handle_ujtype() {
    		arguments.rd = (arguments.instruction >>> 7) & 0x1f;
        int imm = arguments.instruction >>> 12;
        int bit12_19 = (imm & 0xff) << 13;
        int bit11 = (imm & 0x100) << 3;
        int bit1_10 = (imm & 0x7fe00) >>> 8;
        int bit20 = (imm >>> 19);
        arguments.immediate = bit1_10 | bit11 | bit12_19;
        //Handle the sign bit
        if(bit20 == 1) {
        		arguments.immediate -= 1048576; //2^20
        }
		System.out.println(arguments.toString());

        switch(arguments.opcode) {
		case 0x6f:
			copyData(IsaExecute.jal(this));
			break;
		default:
			break;
        }
    }

	void copyData(IsaSim context) {
		this.mem = context.mem;
		this.arguments = context.arguments;
		this.reg = context.reg;
		this.PC = context.PC;
	}
}
