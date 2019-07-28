package codegen;
import ast.AstNode;

import java.util.ArrayList;
import java.util.List;


public class ExecutorBrocasterImpl implements IExecutorBrocaster{
    private List<IExecutorReceiver> beforeExecutionReceiver = new ArrayList<IExecutorReceiver>();
    private List<IExecutorReceiver> afterExecutionReceiver = new ArrayList<IExecutorReceiver>();
    private static ExecutorBrocasterImpl instance = null;
    public static IExecutorBrocaster getInstance() {
    	if (instance == null) {
    		instance = new ExecutorBrocasterImpl();
    	}
    	
    	return instance;
    }
    
    private ExecutorBrocasterImpl() {
    	
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
	public void registerReceiverForBeforeExe(IExecutorReceiver receiver) {
		if (beforeExecutionReceiver.contains(receiver) == false) {
			beforeExecutionReceiver.add(receiver);
		}
	}

	@Override
	public void registerReceiverForAfterExe(IExecutorReceiver receiver) {
		if (afterExecutionReceiver.contains(receiver) == false) {
			afterExecutionReceiver.add(receiver);
		}
	}

	private void notifyReceivers(List<IExecutorReceiver> receivers, AstNode node) {
		for (int i = 0; i < receivers.size(); i++) {
			receivers.get(i).handleExecutorMessage(node);
		}
	}

	@Override
	public void removeReceiver(IExecutorReceiver receiver) {
		if (beforeExecutionReceiver.contains(receiver)) {
			beforeExecutionReceiver.remove(receiver);
		}
		
		if (afterExecutionReceiver.contains(receiver)) {
			afterExecutionReceiver.remove(receiver);
		}
		
	}
}
