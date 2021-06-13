package com.github.hanfak.valid8or.implmentation.mustsatisfy.multiplerules;

import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.TestFixtures;

import java.util.Optional;

import static com.github.hanfak.valid8or.api.Valid8orMustSatisfyAllRules.forInput;
import static java.lang.String.format;

public class ValidateWithMessageInExceptionWithConsumerReturnsOptionalTest extends TestFixtures {
// TODO separate tests with in tests

  @Nested
  class ReturnsInputAsOptionalAndNoExceptionThrownOrConsumerUsedWhenInputIsValid {

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @Test
    void usingCustomExceptionWithCustomMessageNotUsingInputAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @Test
    void usingCustomMessageNotUsingInputAndNoCustomExceptionAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @Test
    void usingBothCustomMessageOnlyAndCustomExceptionForDifferentRulesAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isPresent().containsInstanceOf(Integer.class).contains(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isPresent().containsInstanceOf(Integer.class).contains(4);
    }
  }

  @Nested
  class ThrowsAnExceptionAndUsesConsumerWhenInputIsInvalid {

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumerThrowsCustomException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not even, for input: %s", value))
          .isInstanceOf(IllegalStateException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("Is not even, for input: " + value);
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo(format("For input '%s', was not valid because: 'Is not even, for input: %s'", value, value));

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Is not greater than 2, for input: 2");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'Is not greater than 2, for input: 2'");


      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not even, for input: %s", 3))
          .isInstanceOf(IllegalStateException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("Is not even, for input: 3");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '3', was not valid because: 'Is not even, for input: 3'");

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Is not greater than 2, for input: 2");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'Is not greater than 2, for input: 2'");

      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 1))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Is not greater than 2, for input: 1");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'Is not greater than 2, for input: 1'");
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingCustomExceptionWithCustomMessageNotUsingInputAndConsumerThrowsCustomException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not even, for input: " + value)
          .isInstanceOf(IllegalStateException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("Is not even, for input: " + value);
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo(format("For input '%s', was not valid because: 'Is not even, for input: %s'", value, value));

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not greater than 2, for input: 2")
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Is not greater than 2, for input: 2");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'Is not greater than 2, for input: 2'");


      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(IllegalStateException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("Is not even, for input: 3");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '3', was not valid because: 'Is not even, for input: 3'");

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not greater than 2, for input: 2")
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Is not greater than 2, for input: 2");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'Is not greater than 2, for input: 2'");

      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Is not greater than 2, for input: 1");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'Is not greater than 2, for input: 1'");
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingCustomMessageAndNoCustomExceptionAndConsumerWillThrowsValidationException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage(format("Is not even, for input: %s", value))
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage(format("Is not even, for input: %s", value));
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo(format("For input '%s', was not valid because: 'Is not even, for input: %s'", value, value));

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage(format("Is not greater than 2, for input: %s", 2));
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'Is not greater than 2, for input: 2'");


      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage(format("Is not even, for input: %s", 3))
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage(format("Is not even, for input: %s", 3));
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '3', was not valid because: 'Is not even, for input: 3'");

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage(format("Is not greater than 2, for input: %s", 2));
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'Is not greater than 2, for input: 2'");

      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 1))
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage(format("Is not greater than 2, for input: %s", 1));
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'Is not greater than 2, for input: 1'");
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingCustomMessageNotUsingInputAndNoCustomExceptionAndConsumerWillThrowsValidationException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage("Is not even, for input: " + value)
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("Is not even, for input: " + value);
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '%s', was not valid because: 'Is not even, for input: %s'", value, value);

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage("Is not greater than 2, for input: 2")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("Is not greater than 2, for input: 2");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'Is not greater than 2, for input: 2'");


      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("Is not even, for input: 3");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '3', was not valid because: 'Is not even, for input: 3'");

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage("Is not greater than 2, for input: 2")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("Is not greater than 2, for input: 2");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'Is not greater than 2, for input: 2'");

      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("Is not greater than 2, for input: 1");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'Is not greater than 2, for input: 1'");
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 1})
    void usingBothCustomMessageOnlyAndCustomExceptionForDifferentRulesAndConsumerWillThrowException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not even, for input: %s", value))
          .isInstanceOf(IllegalStateException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(IllegalStateException.class)
          .hasMessage("Is not even, for input: " + value);
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo(format("For input '%s', was not valid because: 'Is not even, for input: %s'", value, value));

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).orThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("Is not greater than 2, for input: 2");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'Is not greater than 2, for input: 2'");


      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not even, for input: %s", 3))
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("Is not even, for input: 3");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '3', was not valid because: 'Is not even, for input: 3'");

      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not greater than 2, for input: %s", 2))
          .isInstanceOf(IllegalArgumentException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessage("Is not greater than 2, for input: 2");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '2', was not valid because: 'Is not greater than 2, for input: 2'");

      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage(format("Is not even, for input: %s", 1))
          .isInstanceOf(ValidationException.class);
      assertThat(stubLogger.lastLogEventException())
          .isInstanceOf(ValidationException.class)
          .hasMessage("Is not even, for input: 1");
      assertThat(stubLogger.lastLogEventMessage())
          .isEqualTo("For input '1', was not valid because: 'Is not even, for input: 1'");
    }
  }
}
