package thread.sample.command;

import org.springframework.boot.CommandLineRunner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractCommand implements CommandLineRunner, Command {
  @Override
  public void run(String... args) throws Exception {
    log.debug("init");
  }
}
