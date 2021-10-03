package com.github.hanfak.valid8or.helper;

import org.junit.jupiter.api.Test;

import java.util.Objects;

import static com.github.hanfak.valid8or.helper.PredicateObjectRules.isEqualsTo;
import static com.github.hanfak.valid8or.helper.PredicateObjectRules.isNotNull;
import static com.github.hanfak.valid8or.helper.PredicateObjectRules.isNull;
import static org.assertj.core.api.Assertions.assertThat;

class PredicateObjectRulesTest {

  @Test
  void returnsBooleanWhenObjectIsEqualsToAnotherObject() {
    assertThat(isEqualsTo(new TestObject("x"))).accepts(new TestObject("x"));
    assertThat(isEqualsTo(new TestObject1("x"))).rejects(new TestObject1("x"));
  }

  @Test
  void returnsBooleanWhenInputIsNull() {
    assertThat(isNull().test(null)).isTrue();
    assertThat(isNull()).rejects(new TestObject1("x"));
  }

  @Test
  void returnsBooleanWhenInputIsNotNull() {
    assertThat(isNotNull().test(null)).isFalse();
    assertThat(isNotNull()).accepts(new TestObject1("x"));
  }
}

class TestObject {

  private final String field;

  TestObject(String field) {
    this.field = field;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TestObject that = (TestObject) o;

    return Objects.equals(field, that.field);
  }

  @Override
  public int hashCode() {
    return field != null ? field.hashCode() : 0;
  }
}

class TestObject1 {

  private final String field;

  public TestObject1(String field) {
    this.field = field;
  }
}