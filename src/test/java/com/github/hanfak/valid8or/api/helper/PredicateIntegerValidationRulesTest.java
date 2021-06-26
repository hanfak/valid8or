package com.github.hanfak.valid8or.api.helper;

import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.api.helper.PredicateIntegerRules.isGreaterThan;
import static com.github.hanfak.valid8or.api.helper.PredicateIntegerRules.isLessThan;
import static com.github.hanfak.valid8or.api.helper.PredicateIntegerRules.isNonZero;
import static com.github.hanfak.valid8or.api.helper.PredicateIntegerRules.isZero;

class PredicateIntegerValidationRulesTest extends TestFixtures {

  @Test
  void returnsBooleanWhenInputIsZero() {
    assertThat(isZero()).accepts(0);
    assertThat(isZero()).rejects(1);
  }

  @Test
  void returnsBooleanWhenInputIsNonZero() {
    assertThat(isNonZero()).rejects(0);
    assertThat(isNonZero()).accepts(1);
  }

  @Test
  void returnsBooleanWhenInputIsGreaterThanSomeInteger() {
    assertThat(isGreaterThan(5)).rejects(4, 5);
    assertThat(isGreaterThan(5)).accepts(8);
  }

  @Test
  void returnsBooleanWhenInputIsLessThanSomeInteger() {
    assertThat(isLessThan(5)).accepts(4);
    assertThat(isLessThan(5)).rejects(5, 8);
  }
}