package thread.sample.model;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor
public class GenericTaskModel<E> {

  private final E value;

  private LocalDateTime time;
}
