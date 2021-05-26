package com.github.hanfak.valid8or.implmentation.compound.couldsatisfy.multiplerule;

import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.StubLogger;
import testinfrastructure.TestFixtures;

import java.util.Optional;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;

public class ValidateWithMessageInExceptionWithConsumerReturnsOptionalTest extends TestFixtures {

  @Nested
  class ReturnsInputAsOptionalAndNoExceptionThrownOrConsumerUsedWhenInputIsValid {

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomExceptionWithNoMessageAndConsumer(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
              .orSatisfies(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(value));

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
              .orSatisfies(isEven).ifNotThrowAn(IllegalStateException::new)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomExceptionWithMessageAndConsumer(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
              .orSatisfies(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(value));

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .orSatisfies(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumer(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .orSatisfies(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(value));

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .orSatisfies(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(value));
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomMessageAndNoCustomExceptionAndConsumer(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(value));

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(value));
    }
  }

  @Nested
  class ThrowsAnExceptionAndUsesConsumerWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithNoMessageAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
              .orSatisfies(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage(null)
          .isInstanceOf(IllegalStateException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
              .orSatisfies(isEven).ifNotThrowAn(IllegalStateException::new)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage(null)
          .isInstanceOf(IllegalArgumentException.class);
      // TODO: test log message.
    }

    @Test
    void usingCustomExceptionWithMessageAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .orSatisfies(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage("Some Exception")
          .isInstanceOf(IllegalStateException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .orSatisfies(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage("Some Other Exception")
          .isInstanceOf(IllegalArgumentException.class);
      // TODO: test log message.
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not even, for input: 1")
          .isInstanceOf(IllegalStateException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven).ifNotThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(IllegalArgumentException.class);
      // TODO: test log message.
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumerWillThrowsValidationException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage("Is not even, for input: 1")
          .isInstanceOf(ValidationException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage("Is not greater than 2, for input: 1")
          .isInstanceOf(ValidationException.class);
      // TODO: test log message.
    }
  }
}
