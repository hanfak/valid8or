package com.github.hanfak.valid8or.helper;

import org.junit.jupiter.api.Test;

import static com.github.hanfak.valid8or.helper.PredicateBooleanRules.isFalse;
import static com.github.hanfak.valid8or.helper.PredicateBooleanRules.isTrue;
import static org.assertj.core.api.Assertions.assertThat;

class PredicateBooleanRulesTest {

  @Test
  void returnsBooleanWhenBooleanIsTrue() {
    assertThat(isTrue()).accepts(true);
    assertThat(isTrue()).rejects(false);
  }

  @Test
  void returnsBooleanWhenBooleanIsFalse() {
    assertThat(isFalse()).accepts(false);
    assertThat(isFalse()).rejects(true);

  }
}