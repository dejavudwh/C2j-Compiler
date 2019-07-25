package interpreter;

import symboltable.Symbol;

import java.util.Map;

public class DirectMemValueSetter implements ValueSetter {
    private int memAddr = 0;

    public DirectMemValueSetter(int memAddr) {
        this.memAddr = memAddr;
    }

    @Override
    public void setValue(Object obj) {
        MemoryHeap memHeap = MemoryHeap.getInstance();
        Map.Entry<Integer, byte[]> entry = memHeap.getMem(memAddr);
        byte[] content = entry.getValue();
        int offset = memAddr - entry.getKey();
        Integer i = (Integer)obj;
        content[offset] = (byte)(i & 0xFF);
    }

    @Override
    public Symbol getSymbol() {
        return null;
    }
}
