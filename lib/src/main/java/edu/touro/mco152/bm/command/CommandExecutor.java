package edu.touro.mco152.bm.command;

public class CommandExecutor {
    private final BenchmarkCommand command;
    public CommandExecutor(BenchmarkCommand command){
        this.command = command;
    }

    public boolean execute()  {
        return command.execute();
    }
}
