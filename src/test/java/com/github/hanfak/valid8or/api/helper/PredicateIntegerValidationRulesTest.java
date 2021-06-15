package com.github.hanfak.valid8or.api.helper;

import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.helper.PredicateIntegerRules.isZero;

class PredicateIntegerValidationRulesTest extends TestFixtures {

  @Test
  void returnsBooleanWhenInputIsZero() {
    assertThat(isZero().test(0)).isTrue();
    assertThat(isZero().test(1)).isFalse();
  }
}