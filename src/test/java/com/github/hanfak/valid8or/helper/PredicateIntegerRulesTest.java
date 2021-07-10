package com.github.hanfak.valid8or.helper;

import org.junit.jupiter.api.Test;
import testinfrastructure.TestFixtures;

import static com.github.hanfak.valid8or.helper.PredicateIntegerRules.*;

class PredicateIntegerRulesTest extends TestFixtures {

  @Test
  void returnsBooleanWhenIsEqualToIsUsed() {
    assertThat(isEqualsTo(5)).accepts(5);
    assertThat(isEqualsTo(5)).rejects(4, 6);
  }

  @Test
  void returnsBooleanWhenIsNotEqualToIsUsed() {
    assertThat(isNotEqualsTo(5)).accepts(4, 6);
    assertThat(isNotEqualsTo(5)).rejects(5);
  }

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
  void returnsBooleanWhenInputIsPositve() {
    assertThat(isPositive()).accepts(1);
    assertThat(isPositive()).rejects(-1, 0);
  }

  @Test
  void returnsBooleanWhenInputIsNegative() {
    assertThat(isNegative()).accepts(-1);
    assertThat(isNegative()).rejects(0, 1);
  }

  @Test
  void returnsBooleanWhenInputIsEven() {
    assertThat(isEven()).accepts(-2, 0, 2, 10);
    assertThat(isEven()).rejects(1);
  }

  @Test
  void returnsBooleanWhenInputIsOdd() {
    assertThat(isOdd()).accepts(-1, 1, 11);
    assertThat(isOdd()).rejects(0, 2);
  }

  @Test
  void returnsBooleanWhenInputIsSquare() {
    assertThat(isSquare()).accepts(1, 4, 25);
    assertThat(isSquare()).rejects(-9, 0, 8);
  }

  @Test
  void returnsBooleanWhenInputIsPrime() {
    assertThat(isPrime()).accepts(2, 29);
    assertThat(isPrime()).rejects(-2, 0, 1, 8);
  }

  @Test
  void returnsBooleanWhenInputIsGreaterThanSomeInteger() {
    assertThat(isGreaterThan(5)).accepts(8);
    assertThat(isGreaterThan(5)).rejects(4, 5);
  }

  @Test
  void returnsBooleanWhenInputIsGreaterThanOrEqualToSomeInteger() {
    assertThat(isGreaterThanOrEqual(5)).accepts(5, 8);
    assertThat(isGreaterThanOrEqual(5)).rejects(4);
  }

  @Test
  void returnsBooleanWhenInputIsLessThanSomeInteger() {
    assertThat(isLessThan(5)).accepts(4);
    assertThat(isLessThan(5)).rejects(5, 8);
  }

  @Test
  void returnsBooleanWhenInputIsLessThanOrEqualToSomeInteger() {
    assertThat(isLessThanOrEqual(5)).accepts(4, 5);
    assertThat(isLessThanOrEqual(5)).rejects(6);
  }
}