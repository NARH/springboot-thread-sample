package thread.sample.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import thread.sample.model.GenericTaskModel;

@Service @Slf4j
public class ThreadManagementService<E> {

  @Autowired
  private QueueManager<GenericTaskModel<String>> queueManager;

  private List<Future<List<GenericTaskModel<String>>>> results
    = Collections.synchronizedList(new ArrayList<>());

  private ExecutorService executorService;

  public void execute() {
    log.info("=== START ===");
    int threadSize = 3;
    int counter = 0;
    int loopSize = 10000;
    int fetchSize = 10000;
    int taskLoopSize = 1000;
    int queueSize = 0;

    executorService = Executors.newFixedThreadPool(threadSize);
    while(counter <= loopSize) {
      addTask(taskLoopSize * threadSize, counter);
      queueSize += taskLoopSize  * threadSize;

      for(int i = 0; i < fetchSize/taskLoopSize; i++) {
        Future<List<GenericTaskModel<String>>> future = executorService.submit(new Task(queueManager, taskLoopSize));
        results.add(future);
      }
      log.debug("===> {}", counter);

      List<Future<List<GenericTaskModel<String>>>> isDoneList = new ArrayList<>();
      results.stream().filter(p->p.isDone()).forEach(f->{
        isDoneList.add(f);
      });
      results.removeAll(isDoneList);

      counter++;
    }
    executorService.shutdown();
    log.info("=== END ===");
  }


  private void addTask(int fetchSize, int count) {
    IntStream.rangeClosed(0, fetchSize).forEach(i ->
      queueManager.enqueue(new GenericTaskModel<String>(Integer.toString((count * 10000) + i))));
  }

  @Slf4j
  public static class Task implements Callable<List<GenericTaskModel<String>>> {
    private final QueueManager<GenericTaskModel<String>> queueManager;
    private final int taskLoopSize;

    public Task(final QueueManager<GenericTaskModel<String>> queueManager, final int taskLoopSize) {
      this.queueManager = queueManager;
      this.taskLoopSize = taskLoopSize;
    }
    @Override
    public List<GenericTaskModel<String>> call() throws Exception {
      List<GenericTaskModel<String>> results = new ArrayList<GenericTaskModel<String>>();
      int count = 0;
      while(count < taskLoopSize) {
        GenericTaskModel<String> model = queueManager.dequeue();
        results.add(model);
        count++;
      }
      log.info("===> Task {}", results.size());
      return results;
    }

  }
}
