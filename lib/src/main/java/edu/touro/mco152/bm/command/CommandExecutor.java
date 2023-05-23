package edu.touro.mco152.bm.command;

/**
 * Executor that takes BenchmarkCommands and executes them, in accordance with the command pattern
 */
public class CommandExecutor {
    private final BenchmarkCommand command;
    public CommandExecutor(BenchmarkCommand command){
        this.command = command;
    }

    public boolean execute()  {
        return command.execute();
    }
}
