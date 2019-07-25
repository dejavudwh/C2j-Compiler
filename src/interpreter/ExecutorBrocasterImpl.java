package interpreter;

import ast.AstNode;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dejavudwh isHudw
 */

public class ExecutorBrocasterImpl implements ExecutorBrocaster{
    private List<ExecutorReceiver> beforeExecutionReceiver = new ArrayList<>();
    private List<ExecutorReceiver> afterExecutionReceiver = new ArrayList<>();
    private static ExecutorBrocasterImpl instance = null;
    public static ExecutorBrocaster getInstance() {
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
    public void registerReceiverForBeforeExe(ExecutorReceiver receiver) {
        if (!beforeExecutionReceiver.contains(receiver)) {
            beforeExecutionReceiver.add(receiver);
        }
    }

    @Override
    public void registerReceiverForAfterExe(ExecutorReceiver receiver) {
        if (!afterExecutionReceiver.contains(receiver)) {
            afterExecutionReceiver.add(receiver);
        }
    }

    private void notifyReceivers(List<ExecutorReceiver> receivers, AstNode node) {
        for (int i = 0; i < receivers.size(); i++) {
            receivers.get(i).handleExecutorMessage(node);
        }
    }

    @Override
    public void removeReceiver(ExecutorReceiver receiver) {
        if (beforeExecutionReceiver.contains(receiver)) {
            beforeExecutionReceiver.remove(receiver);
        }

        if (afterExecutionReceiver.contains(receiver)) {
            afterExecutionReceiver.remove(receiver);
        }

    }
}
