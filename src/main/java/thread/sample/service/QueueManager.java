package thread.sample.service;

import java.util.concurrent.LinkedTransferQueue;

import org.springframework.stereotype.Service;

@Service
class QueueManager<E> {
  private LinkedTransferQueue<E> events = new LinkedTransferQueue<>();

  public void enqueue(E event) {
    events.add(event);
  }

  public E dequeue() throws InterruptedException {
    return events.take();
  }
}