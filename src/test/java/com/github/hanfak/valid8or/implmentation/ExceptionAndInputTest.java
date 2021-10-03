package com.github.hanfak.valid8or.implmentation;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static com.github.hanfak.valid8or.implmentation.ExceptionAndInput.exceptionAndInput;
import static org.assertj.core.api.Assertions.assertThat;

class ExceptionAndInputTest {

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(ExceptionAndInput.class)
        .verify();
  }

  @Test
  void toStringMethod() {
    var exceptionAndInput = exceptionAndInput(new RuntimeException(), "Some message", 5);
    assertThat(exceptionAndInput.toString()).isEqualTo("ExceptionAndInput(exception=java.lang.RuntimeException, message=Some message, input=5)");
  }
}