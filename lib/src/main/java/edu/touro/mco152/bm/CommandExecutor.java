package edu.touro.mco152.bm;

public class CommandExecutor {
    private final BenchmarkCommand command;
    public CommandExecutor(BenchmarkCommand command){
        this.command = command;
    }

    public boolean execute() throws Exception {
        return command.execute();
    }
}
