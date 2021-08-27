package com.github.hanfak.valid8or.implmentation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class UtilsTest {

  @SuppressWarnings("ConstantConditions")
  @Test
  void shouldThrowExceptionWithCustomMessageWhenInputIsNull() {
    Assertions.assertThatThrownBy(() -> Utils.check(true, "Boom"))
        .hasMessage("Boom")
        .isInstanceOf(IllegalArgumentException.class);
  }
}