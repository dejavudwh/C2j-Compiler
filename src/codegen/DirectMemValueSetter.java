package codegen;

import java.util.Map;

import interpreter.ValueSetter;
import symboltable.*;

public class DirectMemValueSetter implements ValueSetter {
    private int memAddr = 0;
    
    public DirectMemValueSetter(int memAddr) {
    	this.memAddr = memAddr;
    }
	
	@Override
	public void setValue(Object obj) throws Exception {
		MemoryHeap memHeap = MemoryHeap.getInstance();
		Map.Entry<Integer, byte[]> entry = memHeap.getMem(memAddr);
		byte[] content = entry.getValue();
		int offset = memAddr - entry.getKey();
		Integer i = (Integer)obj;
		content[offset] = (byte)(i & 0xFF);
	}

	@Override
	public Symbol getSymbol() {
		// TODO Auto-generated method stub
		return null;
	}

}
