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
  public final Supplier<IllegalStateException> illegalStateExceptionSupplier = () -> new IllegalStateException();
  public final Supplier<IllegalStateException> illegalStateExceptionAndMessageSupplier = () -> new IllegalStateException("is not even");
  public final Supplier<IllegalArgumentException> illegalArgumentExceptionSupplier = () -> new IllegalArgumentException();
  public final Supplier<IllegalArgumentException> illegalArgumentExceptionAndMessageSupplier = () -> new IllegalArgumentException("is not even");
  public final Function<String, String> isNotEvenExceptionMessage = input -> format("For input '%s', is not even", input);
  public final Function<String, String> isNotGreaterThan2ExceptionMessage = input -> format("For input '%s', is not greater than 2", input);
}
