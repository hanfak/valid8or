package com.github.hanfak.valid8or.api.helper;

import testinfrastructure.TestFixtures;
import org.junit.jupiter.api.Test;

import static com.github.hanfak.valid8or.api.helper.PredicateIntegerRules.isZero;
import static org.assertj.core.api.Assertions.assertThat;

class PredicateIntegerRulesTest extends TestFixtures {

  @Test
  void returnsBooleanWhenInputIsZero() {
    assertThat(isZero().test(0)).isTrue();
    assertThat(isZero().test(1)).isFalse();
  }
}