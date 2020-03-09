package thread.sample.command;

public interface Command {

  public int execute(String ...args) throws Exception;
}
