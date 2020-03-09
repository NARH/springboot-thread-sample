package thread.sample.command;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component @Slf4j
public class ThreadSampleCommand extends AbstractCommand {

  @Override
  public int execute(String... args) throws Exception {
    log.debug("foo");
    return 0;
  }


}
