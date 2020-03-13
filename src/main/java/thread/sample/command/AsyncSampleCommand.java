package thread.sample.command;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import lombok.extern.slf4j.Slf4j;
import thread.sample.model.GenericTaskModel;

@Component @Slf4j
public class AsyncSampleCommand extends AbstractCommand {

  @Autowired
  private AsyncHelper asyncHelper;

  @Override
  public int execute(String... args) throws Exception {
    log.info("Start Async command");

    for(int i=0; i < 100; i++) {
      GenericTaskModel<String> model = new GenericTaskModel<>("TEST-" + i);
      ListenableFuture<GenericTaskModel<String>> future = asyncHelper.asyncProcessingForListenable(model, 100L);
      future.addCallback(m->{
        log.info("Success: {} [{}]", m.getValue(), m.getTime());
      }, error->{
        log.error("Failure: {}", error.getMessage());
      });
    }

    log.info("End Async command");
    return 0;
  }

  @Component @Slf4j
  public static class AsyncHelper {

    @Async("taskExecutor")
    public ListenableFuture<GenericTaskModel<String>> asyncProcessingForListenable(GenericTaskModel<String> model, long waitMsec) {
      log.info("Start Async process");

      try {
        Thread.sleep(waitMsec);
      }
      catch (InterruptedException e) {
        log.error(e.getMessage(), e);
      }
      model.setTime(LocalDateTime.now());

      log.info("End Async process");
      return new AsyncResult<GenericTaskModel<String>>(model);
    }
  }

  @Bean @Qualifier("taskExecutor")
  public TaskExecutor taskExecutor() {
    log.info("Start Thread Prool configuration");
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setCorePoolSize(1);
    executor.setMaxPoolSize(10);
    executor.setQueueCapacity(2);
    log.info("End Thread Prool configuration");
    return executor;
}
}
