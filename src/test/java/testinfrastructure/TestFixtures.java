package testinfrastructure;

import org.assertj.core.api.WithAssertions;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.lang.String.format;

public class TestFixtures implements WithAssertions {

  public final StubLogger stubLogger = new StubLogger();
  public final Predicate<Integer> isEven = input -> input % 2 == 0;
  public final Predicate<Integer> isGreaterThan2 = input1 -> input1 > 2;
  public final Function<String, IllegalStateException> illegalStateException = IllegalStateException::new;
  public final Function<String, IllegalArgumentException> illegalArgumentException = IllegalArgumentException::new;
}
