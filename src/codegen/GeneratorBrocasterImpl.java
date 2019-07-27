package codegen;

import ast.AstNode;
import interpreter.ExecutorBrocasterImpl;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dejavdwh isHudw
 */

public class GeneratorBrocasterImpl implements GeneratorBrocaster {
    private List<GeneratorReceiver> beforeExecutionReceiver = new ArrayList<GeneratorReceiver>();
    private List<GeneratorReceiver> afterExecutionReceiver = new ArrayList<GeneratorReceiver>();
    private static GeneratorBrocasterImpl instance = null;
    public static GeneratorBrocaster getInstance() {
        if (instance == null) {
            instance = new GeneratorBrocasterImpl();
        }

        return instance;
    }

    private GeneratorBrocasterImpl() {

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
    public void registerReceiverForBeforeExe(GeneratorReceiver receiver) {
        if (!beforeExecutionReceiver.contains(receiver)) {
            beforeExecutionReceiver.add(receiver);
        }
    }

    @Override
    public void registerReceiverForAfterExe(GeneratorReceiver receiver) {
        if (!afterExecutionReceiver.contains(receiver)) {
            afterExecutionReceiver.add(receiver);
        }
    }

    private void notifyReceivers(List<GeneratorReceiver> receivers, AstNode node) {
        for (int i = 0; i < receivers.size(); i++) {
            receivers.get(i).handleExecutorMessage(node);
        }
    }

    @Override
    public void removeReceiver(GeneratorReceiver receiver) {
        if (beforeExecutionReceiver.contains(receiver)) {
            beforeExecutionReceiver.remove(receiver);
        }

        if (afterExecutionReceiver.contains(receiver)) {
            afterExecutionReceiver.remove(receiver);
        }

    }
}
