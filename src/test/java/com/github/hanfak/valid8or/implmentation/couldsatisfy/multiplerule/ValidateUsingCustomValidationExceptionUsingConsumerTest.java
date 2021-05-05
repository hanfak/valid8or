package com.github.hanfak.valid8or.implmentation.couldsatisfy.multiplerule;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.CustomException;
import testinfrastructure.Helper;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;
import static java.lang.String.format;

public class ValidateUsingCustomValidationExceptionUsingConsumerTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownOrConsumerUsedWhenInputIsValid {

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomExceptionWithNoMessageAndConsumer(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
              .orSatisfies(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
              .thenConsume(Helper::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(value);

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
              .orSatisfies(isEven).ifNotThrowAn(IllegalStateException::new)
              .thenConsume(Helper::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomExceptionWithMessageAndConsumer(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .orSatisfies(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .thenConsume(Helper::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(value);

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .orSatisfies(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .thenConsume(Helper::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(value);
    }

    @ParameterizedTest
    @ValueSource(ints = {4, 3, 2})
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumer(int value) {
      assertThat(
          forInput(value)
              .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(Helper::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(value);

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven).ifNotThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(Helper::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(value);
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
              .thenConsume(Helper::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(value);

      assertThat(
          forInput(value)
              .couldSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(Helper::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(value);
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
              .thenConsume(Helper::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'null'")
          .isInstanceOf(CustomException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2).ifNotThrowAn(IllegalArgumentException::new)
              .orSatisfies(isEven).ifNotThrowAn(IllegalStateException::new)
              .thenConsume(Helper::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'null'")
          .isInstanceOf(CustomException.class);

      // TODO: test log message.
    }

    @Test
    void usingCustomExceptionWithMessageAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .orSatisfies(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .thenConsume(Helper::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'Some Exception, Some Other Exception'")
          .isInstanceOf(CustomException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2).ifNotThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .orSatisfies(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .thenConsume(Helper::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'Some Other Exception, Some Exception'")
          .isInstanceOf(CustomException.class);
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
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'Is not even, for input: 1, Is not greater than 2, for input: 1'")
          .isInstanceOf(CustomException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2).ifNotThrow(IllegalArgumentException::new)
              .withMessage(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven).ifNotThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'Is not greater than 2, for input: 1, Is not even, for input: 1'")
          .isInstanceOf(CustomException.class);
      // TODO: test log message.
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumerWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .orSatisfies(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(Helper::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'Is not even, for input: 1, Is not greater than 2, for input: 1'")
          .isInstanceOf(CustomException.class);

      assertThatThrownBy(() ->
          forInput(1)
              .couldSatisfy(isGreaterThan2)
              .butWas(input -> "Is not greater than 2, for input: " + input)
              .orSatisfies(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(Helper::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'Is not greater than 2, for input: 1, Is not even, for input: 1'")
          .isInstanceOf(CustomException.class);
      // TODO: test log message.
    }
  }
}
