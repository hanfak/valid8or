package com.github.hanfak.valid8or.implmentation.compound.couldsatisfy.singlerule;

import testinfrastructure.TestFixtures;
import testinfrastructure.StubLogger;
import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;
import static java.lang.String.format;

public class ValidateWithMessageInExceptionUsingConsumerTest extends TestFixtures {

  @Nested
  class ReturnsInputAndNoExceptionThrownOrConsumerUsedWhenInputIsValid {

    @Test
    void usingCustomExceptionWithNoMessageAndConsumer() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
              .thenConsume(stubLogger::log)
              .validate()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomExceptionWithMessageAndConsumer() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .thenConsume(stubLogger::log)
              .validate()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumer() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      ).isEqualTo(4);
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumer() {
      assertThat(
          forInput(4)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      ).isEqualTo(4);
    }
  }

  @Nested
  class ThrowsAnExceptionAndUsesConsumerWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithNoMessageAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven).ifNotThrowAn(IllegalStateException::new)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage(null)
          .isInstanceOf(IllegalStateException.class);

      // TODO: test log message.
    }

    @Test
    void usingCustomExceptionWithMessageAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven).ifNotThrowAn(() -> new IllegalStateException("Some Exception"))
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage("Some Exception")
          .isInstanceOf(IllegalStateException.class);
      // TODO: test log message.
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven).ifNotThrow(IllegalStateException::new)
              .withMessage(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(IllegalStateException.class);
      // TODO: test log message.
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumerWillThrowValidationException() {
      assertThatThrownBy(() ->
          forInput(3)
              .couldSatisfy(isEven)
              .butWas(input -> "Is not even, for input: " + input)
              .thenConsume(stubLogger::log)
              .validate()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(ValidationException.class);
      // TODO: test log message.
    }
  }
}
