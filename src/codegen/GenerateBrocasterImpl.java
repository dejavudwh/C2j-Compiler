package codegen;
import ast.AstNode;

import java.util.ArrayList;
import java.util.List;


public class GenerateBrocasterImpl implements GenerateBrocaster {
    private List<GenerateReceiver> beforeExecutionReceiver = new ArrayList<GenerateReceiver>();
    private List<GenerateReceiver> afterExecutionReceiver = new ArrayList<GenerateReceiver>();
    private static GenerateBrocasterImpl instance = null;
    public static GenerateBrocaster getInstance() {
    	if (instance == null) {
    		instance = new GenerateBrocasterImpl();
    	}
    	
    	return instance;
    }
    
    private GenerateBrocasterImpl() {
    	
    }
	@Override
	public void brocastBeforeExecution(AstNode node) {
		notifyReceivers(beforeExecutionReceiver, node);
	}

	@Override
	public void brocastAfterExecution(AstNode node) {
		notifyReceivers(afterExecutionReceiver, node);
	}

	@Override
	public void registerReceiverForBeforeExe(GenerateReceiver receiver) {
		if (beforeExecutionReceiver.contains(receiver) == false) {
			beforeExecutionReceiver.add(receiver);
		}
	}

	@Override
	public void registerReceiverForAfterExe(GenerateReceiver receiver) {
		if (afterExecutionReceiver.contains(receiver) == false) {
			afterExecutionReceiver.add(receiver);
		}
	}

	private void notifyReceivers(List<GenerateReceiver> receivers, AstNode node) {
		for (int i = 0; i < receivers.size(); i++) {
			receivers.get(i).handleExecutorMessage(node);
		}
	}

	@Override
	public void removeReceiver(GenerateReceiver receiver) {
		if (beforeExecutionReceiver.contains(receiver)) {
			beforeExecutionReceiver.remove(receiver);
		}
		
		if (afterExecutionReceiver.contains(receiver)) {
			afterExecutionReceiver.remove(receiver);
		}
		
	}
}
