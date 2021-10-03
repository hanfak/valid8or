package com.github.hanfak.valid8or.implmentation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


class UtilsTest {

  @Test
  void shouldNotBeAbleToCreateInstance() throws Exception {
    Constructor<Utils> declaredConstructor = Utils.class.getDeclaredConstructor();
    assertTrue(Modifier.isPrivate(declaredConstructor.getModifiers()));
    declaredConstructor.setAccessible(true);
    try {
      declaredConstructor.newInstance();
      fail();
    } catch (InvocationTargetException e) {
      assertEquals(IllegalStateException.class, e.getCause().getClass());
      assertEquals("No instances!", e.getCause().getMessage());
    }
  }

  @SuppressWarnings("ConstantConditions")
  @Test
  void shouldThrowExceptionWithCustomMessageWhenInputIsNull() {
    Assertions.assertThatThrownBy(() -> Utils.check(true, "Boom"))
        .hasMessage("Boom")
        .isInstanceOf(IllegalArgumentException.class);
  }
}