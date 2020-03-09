package thread.sample.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import thread.sample.model.GenericTaskModel;
import thread.sample.service.ThreadManagementService;

@Component @Slf4j
public class ThreadSampleCommand extends AbstractCommand {

  @Autowired
  private ThreadManagementService<GenericTaskModel<String>> threadManager;

  @Override
  public int execute(String... args) throws Exception {
    log.info("=== START ===");
    threadManager.execute();
    log.info("=== END ===");
    return 0;
  }


}
