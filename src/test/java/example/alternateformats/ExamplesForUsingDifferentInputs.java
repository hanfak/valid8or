package example.alternateformats;

import com.github.hanfak.valid8or.implmentation.ConnectorOrValidateForCouldSatisfy;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;
import static java.util.stream.Collectors.joining;
// Different
public class ExamplesForUsingDifferentInputs {
  @Test
  void howToUseInput() {
    ConnectorOrValidateForCouldSatisfy<Integer> blah = forInput(2)
        .couldSatisfy(x -> false)
        .ifNotThrow(IllegalArgumentException::new)
        .withMessage(x -> "error, input was: " + x);
    System.out.println(blah.isInvalid());
    System.out.println(blah.allExceptionMessages());
    System.out.println("integer = " + blah.validate());

    int input = 2;
    Optional<Integer> validatedAction = forInput(input)
        .couldSatisfy(x -> true)
        .ifNotThrowAn(() -> new IllegalArgumentException("Some wrong with input: " + input))
        .validateThenReturnOptional();

    Integer validateAction1 = forInput(someAction())
        .couldSatisfy(x -> true).butWas(x -> "Some problem")
        .orSatisfies(x -> true).butWas(x -> "Some other problem")
        .orElseThrowValidationException(IllegalArgumentException::new,
            (i, message) -> String.format("All issues for '%s' are '%s'", i, message));

    Integer validateAction2 = forInput(someAction())
        .couldSatisfy(x -> true)
        .ifNotThrow(IllegalArgumentException::new)
        .withMessage(x -> "error, input was: " + x)
        .validate();

    Predicate<String> validationStrategy = someInput -> forInput(someInput)
        .couldSatisfy(x -> !x.contains("s"))
        .ifNotThrowAn(() -> new IllegalArgumentException("Some wrong with input: " + someInput))
        .isValid();

    String collect = Stream.of("Hello", "snakes", "pop", "boss")
        .filter(validationStrategy)
        .collect(joining(", "));
    System.out.println("collect = " + collect);

//    Function<String, String> validationStrategy1 = someInput -> forInput(someInput)
//        .couldSatisfy(x -> !x.contains("s"))
//        .ifNotThrowAn(() -> new IllegalArgumentException("Some wrong with input: " + someInput))
//        .validate();
//
//    String collect1 = Stream.of("Hello", "snakes", "pop", "boss")
//        .map(validationStrategy1)
//        .collect(joining(", "));
//    System.out.println("collect1 = " + collect1);

    Function<String, Set<String>> validationStrategy2 = someInput -> forInput(someInput)
        .mustSatisfy(x -> !x.contains("s"))
        .ifNotWillThrowAn(() -> new IllegalArgumentException("Contains an s in input: " + someInput))
        .andSatisfies(x -> !x.contains("a"))
        .ifNotWillThrowAn(() -> new IllegalArgumentException("Contains an a in input: " + someInput))
        .allExceptionMessages();

    String collect2 = Stream.of("Hello", "snakes", "pop", "bass")
        .map(validationStrategy2)
        .flatMap(Collection::stream)
        .collect(joining(", "));
    System.out.println("collect2 = " + collect2);
  }

  private int someAction() {
    return 2;
  }
}
