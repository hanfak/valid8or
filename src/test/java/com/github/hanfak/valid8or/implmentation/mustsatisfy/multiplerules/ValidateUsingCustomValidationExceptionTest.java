package com.github.hanfak.valid8or.implmentation.mustsatisfy.multiplerules;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import testinfrastructure.CustomException;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.Valid8orMustSatisfyAllRules.forInput;
import static java.lang.String.format;

class ValidateUsingCustomValidationExceptionTest extends TestFixtures {

 @Nested
 class ReturnsInputAndNoExceptionThrownWhenInputIsValid {

  @Test
  void usingCustomExceptionWithCustomMessageUsingInput() {
   assertThat(
       forInput(4)
           .mustSatisfy(isEven).orThrow(IllegalStateException::new)
           .withMessage(input -> "Is not even, for input: " + input)
           .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
           .withMessage(input -> "Is not greater than 2, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   ).isEqualTo(4);

   assertThat(
       forInput(4)
           .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
           .withMessage(input -> "Is not greater than 2, for input: " + input)
           .andSatisfies(isEven).orThrow(IllegalStateException::new)
           .withMessage(input -> "Is not even, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   ).isEqualTo(4);
  }

  @Test
  void usingCustomMessageAndNoCustomException() {
   assertThat(
       forInput(4)
           .mustSatisfy(isEven)
           .butWas(input -> "Is not even, for input: " + input)
           .andSatisfies(isGreaterThan2)
           .butWas(input -> "Is not greater than 2, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   ).isEqualTo(4);

   assertThat(
       forInput(4)
           .mustSatisfy(isGreaterThan2)
           .butWas(input -> "Is not greater than 2, for input: " + input)
           .andSatisfies(isEven)
           .butWas(input -> "Is not even, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   ).isEqualTo(4);
  }
 }

 @Nested
 class ThrowsAnExceptionWhenInputIsInvalid {

  @Test
  void usingCustomExceptionWithCustomMessageUsingInputThrowsCustomException() {
   assertThatThrownBy(() ->
       forInput(3)
           .mustSatisfy(isEven).orThrow(IllegalStateException::new)
           .withMessage(input -> "Is not even, for input: " + input)
           .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
           .withMessage(input -> "Is not greater than 2, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   )
       .hasMessage("All problems are, for input: 3, with messages: 'Is not even, for input: 3'")
       .isInstanceOf(CustomException.class);
   assertThatThrownBy(() ->
       forInput(2)
           .mustSatisfy(isEven).orThrow(IllegalStateException::new)
           .withMessage(input -> "Is not even, for input: " + input)
           .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
           .withMessage(input -> "Is not greater than 2, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   )
       .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'")
       .isInstanceOf(CustomException.class);
   assertThatThrownBy(() ->
       forInput(1)
           .mustSatisfy(isEven).orThrow(IllegalStateException::new)
           .withMessage(input -> "Is not even, for input: " + input)
           .andSatisfies(isGreaterThan2).orThrow(IllegalArgumentException::new)
           .withMessage(input -> "Is not greater than 2, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   )
       .hasMessage("All problems are, for input: 1, with messages: 'Is not even, for input: 1, Is not greater than 2, for input: 1'")
       .isInstanceOf(CustomException.class);

   assertThatThrownBy(() ->
       forInput(3)
           .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
           .withMessage(input -> "Is not greater than 2, for input: " + input)
           .andSatisfies(isEven).orThrow(IllegalStateException::new)
           .withMessage(input -> "Is not even, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   )
       .hasMessage("All problems are, for input: 3, with messages: 'Is not even, for input: 3'")
       .isInstanceOf(CustomException.class);
   assertThatThrownBy(() ->
       forInput(2)
           .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
           .withMessage(input -> "Is not greater than 2, for input: " + input)
           .andSatisfies(isEven).orThrow(IllegalStateException::new)
           .withMessage(input -> "Is not even, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   )
       .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'")
       .isInstanceOf(CustomException.class);
   assertThatThrownBy(() ->
       forInput(1)
           .mustSatisfy(isGreaterThan2).orThrow(IllegalArgumentException::new)
           .withMessage(input -> "Is not greater than 2, for input: " + input)
           .andSatisfies(isEven).orThrow(IllegalStateException::new)
           .withMessage(input -> "Is not even, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   )
       .hasMessage("All problems are, for input: 1, with messages: 'Is not greater than 2, for input: 1, Is not even, for input: 1'")
       .isInstanceOf(CustomException.class);
  }

  @Test
  void usingCustomMessageAndNoCustomExceptionWillThrowValidationException() {
   assertThatThrownBy(() ->
       forInput(3)
           .mustSatisfy(isEven)
           .butWas(input -> "Is not even, for input: " + input)
           .andSatisfies(isGreaterThan2)
           .butWas(input -> "Is not greater than 2, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   )
       .hasMessage("All problems are, for input: 3, with messages: 'Is not even, for input: 3'")
       .isInstanceOf(CustomException.class);
   assertThatThrownBy(() ->
       forInput(2)
           .mustSatisfy(isEven)
           .butWas(input -> "Is not even, for input: " + input)
           .andSatisfies(isGreaterThan2)
           .butWas(input -> "Is not greater than 2, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   )
       .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'")
       .isInstanceOf(CustomException.class);
   assertThatThrownBy(() ->
       forInput(1)
           .mustSatisfy(isEven)
           .butWas(input -> "Is not even, for input: " + input)
           .andSatisfies(isGreaterThan2)
           .butWas(input -> "Is not greater than 2, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   )
       .hasMessage("All problems are, for input: 1, with messages: 'Is not even, for input: 1, Is not greater than 2, for input: 1'")
       .isInstanceOf(CustomException.class);

   assertThatThrownBy(() ->
       forInput(3)
           .mustSatisfy(isGreaterThan2)
           .butWas(input -> "Is not greater than 2, for input: " + input)
           .andSatisfies(isEven)
           .butWas(input -> "Is not even, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   )
       .hasMessage("All problems are, for input: 3, with messages: 'Is not even, for input: 3'")
       .isInstanceOf(CustomException.class);
   assertThatThrownBy(() ->
       forInput(2)
           .mustSatisfy(isGreaterThan2)
           .butWas(input -> "Is not greater than 2, for input: " + input)
           .andSatisfies(isEven)
           .butWas(input -> "Is not even, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   )
       .hasMessage("All problems are, for input: 2, with messages: 'Is not greater than 2, for input: 2'")
       .isInstanceOf(CustomException.class);
   assertThatThrownBy(() ->
       forInput(1)
           .mustSatisfy(isGreaterThan2)
           .butWas(input -> "Is not greater than 2, for input: " + input)
           .andSatisfies(isEven)
           .butWas(input -> "Is not even, for input: " + input)
           .validateOrThrowNotify(CustomException::new,
               (input, errors) -> format("All problems are, for input: %s, with messages: '%s'", input, errors))
   )
       .hasMessage("All problems are, for input: 1, with messages: 'Is not greater than 2, for input: 1, Is not even, for input: 1'")
       .isInstanceOf(CustomException.class);
  }
 }
}