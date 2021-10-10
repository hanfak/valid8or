package com.github.hanfak.valid8or.implmentation.couldsatisfy.singlerule;

import com.github.hanfak.valid8or.implmentation.ValidationException;
import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import java.util.Set;

import static com.github.hanfak.valid8or.implmentation.Valid8orCouldSatisfyAllRules.forInput;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AllExceptionMessagesReturnedAfterValidatingWithMustCouldMessageInExceptionTest extends TestFixtures {

  @Test
  void checksInputIsValidUsingCustomMessageOutsideException() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
            .withMessage(input -> "Is not even, for input: " + input)
            .useConsumer(stubLogger::log)
            .allExceptionMessages()
    ).isEmpty();
    assertTrue(stubLogger.isEmpty());

    Set<String> actual = forInput(3)
        .couldSatisfy(isEven).orElseThrow(IllegalStateException::new)
        .withMessage(input -> "Is not even, for input: " + input)
        .useConsumer(stubLogger::log)
        .allExceptionMessages();
    assertThat(
        actual
    ).containsOnly("Is not even, for input: 3");
    assertThat(stubLogger.lastLogEventException())
        .isInstanceOf(ValidationException.class)
        .hasMessage("For input: '3', the following problems occurred: 'Is not even, for input: 3'");
    assertThat(stubLogger.lastLogEventMessage())
        .isEqualTo("For input '3', was not valid because: 'For input: '3', the following problems occurred: 'Is not even, for input: 3''");
  }

  @Test
  void checksInputIsValidUsingCustomMessageOnly() {
    assertThat(
        forInput(4)
            .couldSatisfy(isEven)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .useConsumer(stubLogger::log)
            .allExceptionMessages()
    ).isEmpty();

    assertThat(
        forInput(3)
            .couldSatisfy(isEven)
            .orThrowExceptionWith(input -> "Is not even, for input: " + input)
            .useConsumer(stubLogger::log)
            .allExceptionMessages()
    ).containsOnly("Is not even, for input: 3");
  }
}
