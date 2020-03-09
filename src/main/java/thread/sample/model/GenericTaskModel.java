package thread.sample.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data @RequiredArgsConstructor
public class GenericTaskModel<E> {

  private final E value;
}
