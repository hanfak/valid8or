package com.github.hanfak.valid8or.implmentation.mustsatisfy.singlerule;

import com.github.hanfak.valid8or.implmentation.domain.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testinfrastructure.Helper;
import testinfrastructure.TestFixtures;

import java.util.Optional;

import static com.github.hanfak.valid8or.api.Valid8or.forInput;

public class ValidateWithMessageInExceptionWithConsumerReturnsOptionalTest extends TestFixtures {

  @Nested
  class ReturnsInputAsOptionalAndNoExceptionThrownOrConsumerUsedWhenInputIsValid {
    @Test
    void usingCustomExceptionWithNoMessageAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .thenConsume(Helper::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @Test
    void usingCustomExceptionhasMessageAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .thenConsume(Helper::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .thenConsume(Helper::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumer() {
      assertThat(
          forInput(4)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .thenConsume(Helper::log)
              .validateThenReturnOptional()
      ).isEqualTo(Optional.of(4));
    }
  }

  @Nested
  class ThrowsAnExceptionAndUsesConsumerWhenInputIsInvalid {

    @Test
    void usingCustomExceptionWithNoMessageAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven).ifNotWillThrowAn(IllegalStateException::new)
              .thenConsume(Helper::log)
              .validateThenReturnOptional()
      )
          .hasMessage(null)
          .isInstanceOf(IllegalStateException.class);
      // TODO: test log message.
    }

    @Test
    void usingCustomExceptionhasMessageAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven).ifNotWillThrowAn(() -> new IllegalStateException("Some Exception"))
              .thenConsume(Helper::log)
              .validateThenReturnOptional()
      )
          .hasMessage("Some Exception")
          .isInstanceOf(IllegalStateException.class);
      // TODO: test log message.
    }

    @Test
    void usingCustomExceptionWithCustomMessageUsingInputAndConsumerThrowsCustomException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven).ifNotWillThrow(IllegalStateException::new)
              .hasMessage(input -> "Is not even, for input: " + input)
              .thenConsume(Helper::log)
              .validateThenReturnOptional()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(IllegalStateException.class);
      // TODO: test log message.
    }

    @Test
    void usingCustomMessageAndNoCustomExceptionAndConsumerWillThrowsValidationException() {
      assertThatThrownBy(() ->
          forInput(3)
              .mustSatisfy(isEven)
              .butIs(input -> "Is not even, for input: " + input)
              .thenConsume(Helper::log)
              .validate()
      )
          .hasMessage("Is not even, for input: 3")
          .isInstanceOf(ValidationException.class);
      // TODO: test log message.
    }
  }
}
