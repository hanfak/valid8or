package com.github.hanfak.valid8or;

import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import java.util.Optional;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;

class Valid8orTest extends TestFixtures {

  @Test
  void validateThenReturnOptionalCatersForDifferentInputsWhenValid() {
    Optional<Integer> integer = forInput(2)
        .couldSatisfy(x -> true).butWas(x -> "error")
        .validateThenReturnOptional();
    assertThat(integer).contains(2);

    Optional<Object> o = forInput(null)
        .couldSatisfy(x -> true).butWas(x -> "error")
        .validateThenReturnOptional();
    assertThat(o).isEmpty();
  }

  @Test
  void name() {
    int input = 2;
    Optional<Integer> validatedAction = forInput(input)
        .couldSatisfy(x -> true).butWas(x -> "error")
        .validateThenReturnOptional();

    Optional<Integer> validateAction1 = forInput(someAction())
        .couldSatisfy(x -> true).butWas(x -> "error")
        .validateThenReturnOptional();
  }

  private int someAction() {
    return 2;
  }
}