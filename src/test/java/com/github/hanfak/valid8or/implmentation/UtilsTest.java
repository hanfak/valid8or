package com.github.hanfak.valid8or.implmentation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


class UtilsTest {

  @Test
  void shouldNotBeAbleToCreateInstance() {
    Assertions.assertThatThrownBy(Utils::new)
        .hasMessage("No instances!")
        .isInstanceOf(IllegalStateException.class);
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  void shouldThrowExceptionWithCustomMessageWhenInputIsNull() {
    Assertions.assertThatThrownBy(() -> Utils.check(true, "Boom"))
        .hasMessage("Boom")
        .isInstanceOf(IllegalArgumentException.class);
  }
}