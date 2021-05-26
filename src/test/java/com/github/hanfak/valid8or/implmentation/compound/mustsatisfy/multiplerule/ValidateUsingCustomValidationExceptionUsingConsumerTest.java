package com.github.hanfak.valid8or.implmentation.compound.mustsatisfy.multiplerule;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import testinfrastructure.CustomException;
import testinfrastructure.StubLogger;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;
import static java.lang.String.format;

public class ValidateUsingCustomValidationExceptionUsingConsumerTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownOrConsumerUsedWhenInputIsValid {

    @Test
    void usingCustomExceptionWithNoMessageAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
              .andSatisfies(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);
    }

    @Test
    void usingCustomExceptionhasMessageAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);

      assertThat(
          forInput(4)
              .mustSatisfy(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      ).isEqualTo(4);
    }
  }

  @Nested
  class ThrowsAnExceptionAndUsesConsumerWhenInputIsInvalid {

    @ParameterizedTest
    @ValueSource(ints = {3, 2, 1})
    void usingCustomExceptionWithNoMessageAndConsumerThrowsCustomException(int value) {
      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage(format("All problems are, for input: %s, with messages: 'null'", value))
          .isInstanceOf(CustomException.class);

      assertThatThrownBy(() ->
          forInput(value)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(IllegalArgumentException::new)
              .andSatisfies(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage(String.format("All problems are, for input: %s, with messages: 'null'", value))
          .isInstanceOf(CustomException.class);

      // TODO: test log message.
    }

    @Test
    void usingCustomExceptionhasMessageAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 3, with messages: 'Some Exception'")
          .isInstanceOf(CustomException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 2, with messages: 'Some Other Exception'")
          .isInstanceOf(CustomException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .andSatisfies(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'Some Exception, Some Other Exception'")
          .isInstanceOf(CustomException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 3, with messages: 'Some Exception'")
          .isInstanceOf(CustomException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 2, with messages: 'Some Other Exception'")
          .isInstanceOf(CustomException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).ifNotWillThrowAn(() -> new IllegalArgumentException("Some Other Exception"))
              .andSatisfies(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .thenConsume(stubLogger::log)
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
          forInput(3)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 3, with messages: 'Is not even, for input: 3'")
          .isInstanceOf(CustomException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(CustomException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'Is not even, for input: 1, Is not greater than 2, for input: 1'")
          .isInstanceOf(CustomException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 3, with messages: 'Is not even, for input: 3'")
          .isInstanceOf(CustomException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(CustomException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2).ifNotWillThrow(IllegalArgumentException::new)
              .hasMessage(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
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
          forInput(3)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 3, with messages: 'Is not even, for input: 3'")
          .isInstanceOf(CustomException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(CustomException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .andSatisfies(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'Is not even, for input: 1, Is not greater than 2, for input: 1'")
          .isInstanceOf(CustomException.class);

      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 3, with messages: 'Is not even, for input: 3'")
          .isInstanceOf(CustomException.class);
      assertThatThrownBy(() ->
          forInput(2)
              .mustSatisfy(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'")
          .isInstanceOf(CustomException.class);
      assertThatThrownBy(() ->
          forInput(1)
              .mustSatisfy(isGreaterThan2)
              .butIs(input -> "Is not greater than 2, for input: " + input)
              .andSatisfies(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .orElseThrowValidationException(CustomException::new,
                  (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
      )
          .hasMessage("All problems are, for input: 1, with messages: 'Is not greater than 2, for input: 1, Is not even, for input: 1'")
          .isInstanceOf(CustomException.class);
      // TODO: test log message.
    }
  }
}
