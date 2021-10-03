package com.github.hanfak.valid8or.implmentation;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.implmentation.ValidationRule.validationRule;

class ValidationRuleTest extends TestFixtures {

  @Test
  void equalsContract() {
    EqualsVerifier.forClass(ValidationRule.class)
        .verify();
  }

  @Test
  void toStringMethod() {
    var validationRule = validationRule(isEven, new RuntimeException(), input -> "some Message for input: " + input);
    assertThat(validationRule.toString())
        .matches("ValidationRule\\(rule=.*, exception=java.lang.RuntimeException, exceptionMessageFunction=.*\\)");
  }
}