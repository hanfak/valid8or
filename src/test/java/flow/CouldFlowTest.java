package flow;
import com.github.hanfak.valid8or.implmentation.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
@Disabled("TODO later")
public class CouldFlowTest {
  @Test
  void forInputMethodHasTwoMethodsNext() {
    thenMethodReturnsType("CouldSatisfy", forMethod("forInput"));

    thenTheMethodForInputHasNextIntermediateMethod("couldSatisfy");
  }

  @Test
  void forCouldSatisfyMethodHasThreeMethodsNext() {
    thenCouldSatisfyHasReturnType("CouldThrowException");

    thenTheMethodCouldSatisfyHasNextIntermediateMethods("orThrow", "butWas");
  }

  @Test
  void forIfNotThrowMethodHasOneMethodNext() {
    thenIfNotThrowMethodHasReturnType("MessageForCouldRule");

    thenifNotThrowMethodHasNextIntermediateMethod("withMessage");
  }

  @Test
  void forOrSatisfiesMethodHasNextThreeIntermediateMethods() {
    thenOrSatisfiesMethodHasReturnType("ThrowExceptionForCouldRule");

    thenTheMethodOrSatisfiesHasNextIntermediateMethods("ifNotThrow", "ifNotThrowAn", "butWas");
  }

  @Test
  void forIfNotThrowAnMethodHasTwoIntermediateAndSixTerminalMethodsNext() {
    thenIfNotThrowAnHasReturnType("ConnectorOrValidateForCouldSatisfy");

    //forInput only has two methods it can call next
    thenIfNotThrowAnHasNextIntermediateMethods("orSatisfies", "thenConsume");
    thenHasTerminalMethodsThatDoThrowExceptionAre(
        "orElseThrowValidationException", "validate", "validateThenReturnOptional");
    thenHasTerminalMethodsThatDoNotThrowExceptionAre(
        "isInvalid", "isValid", "allExceptionMessages");
  }

  @Test
  void forButWasMethodHasTwoIntermediateAndSixTerminalMethodsNext() {
    thenButWasMethodHasReturnType("ConnectorOrValidateForCouldSatisfy");

    //forInput only has two methods it can call next
    thenButWasHasNextIntermediateMethods("orSatisfies", "thenConsume");
    thenHasTerminalMethodsThatDoThrowExceptionAre(
        "orElseThrowValidationException", "validate", "validateThenReturnOptional");
    thenHasTerminalMethodsThatDoNotThrowExceptionAre(
        "isInvalid", "isValid", "allExceptionMessages");
  }

  @Test
  void forThenConsumeMethodHasThreeTerminalMethodsNext() {
    thenTheThenConsumeMethodHasReturnType("ConsumerTerminal");

    thenHasTerminalMethodsThatDoNotThrowExceptionAre("isInvalid", "isValid", "allExceptionMessages");
  }

  @Test
  void forWithMessageMethodHasTwoIntermediateAndSixTerminalMethods() {
    thenTheWithMessageMethodHasReturnType("ConnectorOrValidateForCouldSatisfy");

    //forInput only has two methods it can call next
    thenTheMethodWithMessageHasNextIntermediateMethods("orSatisfies", "thenConsume");
    thenHasTerminalMethodsThatDoThrowExceptionAre(
        "orElseThrowValidationException", "validate", "validateThenReturnOptional");
    thenHasTerminalMethodsThatDoNotThrowExceptionAre(
        "isInvalid", "isValid", "allExceptionMessages");
  }

  private void thenTheMethodWithMessageHasNextIntermediateMethods(String... nextMethods) {
    thenConnectorOrValidateForCouldSatisfyHasIntermediateMethods(nextMethods);
  }

  private void thenTheWithMessageMethodHasReturnType(String returnType) {
    Method[] methods = CouldMessage.class.getDeclaredMethods();
    assertThat(methods.length).isEqualTo(1);
    assertThat(methods[0].getName()).isEqualTo("withMessage");
    assertThat(methods[0].getReturnType().toString())
        .describedAs("withMessage should return ConnectorOrValidateForCouldSatisfy type")
        .contains(returnType);
  }

  private void thenButWasMethodHasReturnType(String returnType) {
    Method[] methods = CouldThrowException.class.getDeclaredMethods();
    assertThat(methods.length).isEqualTo(3);
    assertThat(methods[2].getName()).isEqualTo("butWas");
    assertThat(methods[2].getReturnType().toString())
        .describedAs("butWas should return ConnectorOrValidateForCouldSatisfy type")
        .contains(returnType);
  }

  private void thenIfNotThrowAnHasReturnType(String returnType) {
    Method[] methods = CouldThrowException.class.getDeclaredMethods();
    assertThat(methods.length).isEqualTo(3);
    assertThat(methods[1].getName()).isEqualTo("ifNotThrowAn");
    assertThat(methods[1].getReturnType().toString())
        .describedAs("ifNotThrowAn should return ConnectorOrValidateForCouldSatisfy type")
        .contains(returnType);
  }

  private void thenIfNotThrowMethodHasReturnType(String returnType) {
    Method[] methods = CouldThrowException.class.getDeclaredMethods();
    assertThat(methods.length).isEqualTo(3);
    assertThat(methods[0].getName()).isEqualTo("orThrow");
    assertThat(methods[0].getReturnType().toString())
        .describedAs("ifNotThrow should return MessageForCouldRule type")
        .contains(returnType);
  }

  private void thenCouldSatisfyHasReturnType(String returnType) {
    Method[] methods = CouldSatisfy.class.getDeclaredMethods();
    assertThat(methods.length).isEqualTo(1);
    assertThat(methods[0].getName()).isEqualTo("couldSatisfy");
    assertThat(methods[0].getReturnType().toString())
        .describedAs("couldSatisfy should return CouldThrowException type")
        .contains(returnType);
  }

  private void thenifNotThrowMethodHasNextIntermediateMethod(String methodName) {
    Method[] methodsOfMessageForCouldRule = CouldMessage.class.getDeclaredMethods();
    assertThat(methodsOfMessageForCouldRule.length).isEqualTo(1);
    assertThat(methodsOfMessageForCouldRule[0].getName()).isEqualTo(methodName);
  }

  private void thenTheMethodForInputHasNextIntermediateMethod(String... nextMethods) {
    Method[] methodsOfSatisfy = CouldSatisfy.class.getDeclaredMethods();
    assertThat(methodsOfSatisfy.length).isEqualTo(1);
    assertThat(methodsOfSatisfy[0].getName()).isEqualTo("couldSatisfy");
    assertThat(Arrays.stream(methodsOfSatisfy).map(Method::getName))
        .containsOnly(nextMethods[0]);
  }

  private String forMethod(String value) {
    return value;
  }

  private void thenMethodReturnsType(String returnType, String method) {
    Method[] methods = ForCouldInput.class.getDeclaredMethods();
    assertThat(methods.length).isEqualTo(1);
    assertThat(methods[0].getName()).isEqualTo(method);
    assertThat(methods[0].getReturnType().toString())
        .describedAs(String.format("%s should return %s type", method, returnType))
        .contains(returnType);
  }

  private void thenButWasHasNextIntermediateMethods(String... nextMethods) {
    thenConnectorOrValidateForCouldSatisfyHasIntermediateMethods(nextMethods);
  }

  private void thenIfNotThrowAnHasNextIntermediateMethods(String... nextMethods) {
    thenConnectorOrValidateForCouldSatisfyHasIntermediateMethods(nextMethods);
  }

  private void thenThrowExceptionForCouldRuleHasIntermediateMethods(String... nextMethods) {
    Method[] methodsOfThrowExceptionForCouldRule = CouldThrowException.class.getDeclaredMethods();
    assertThat(methodsOfThrowExceptionForCouldRule.length).isEqualTo(2);
    assertThat(Arrays.stream(methodsOfThrowExceptionForCouldRule).map(Method::getName))
        .containsOnly(nextMethods[0], nextMethods[1]);
  }

  private void thenOrSatisfiesMethodHasReturnType(String returnType) {
    Method[] methods = CouldConnectorOrValidate.class.getDeclaredMethods();
    assertThat(methods.length).isEqualTo(2);
    assertThat(methods[0].getName()).isEqualTo("orSatisfies");
    assertThat(methods[0].getReturnType().toString())
        .describedAs("orSatisfies should return ThrowExceptionForCouldRule type")
        .contains(returnType);
  }

  private void thenTheThenConsumeMethodHasReturnType(String returnType) {
    Method[] methods = CouldConnectorOrValidate.class.getDeclaredMethods();
    assertThat(methods.length).isEqualTo(2);
    assertThat(methods[1].getName()).isEqualTo("thenConsume");
    assertThat(methods[1].getReturnType().toString())
        .describedAs("thenConsume should return ConsumerTerminal type")
        .contains(returnType);
  }

  private void thenTheMethodOrSatisfiesHasNextIntermediateMethods(String... nextMethods) {
    thenThrowExceptionForCouldRuleHasIntermediateMethods(nextMethods);
  }

  private void thenConnectorOrValidateForCouldSatisfyHasIntermediateMethods(String... nextMethods) {
    Method[] methodsOfConnectorOrValidateForCouldSatisfy = CouldConnectorOrValidate.class.getDeclaredMethods();
    assertThat(methodsOfConnectorOrValidateForCouldSatisfy.length).isEqualTo(2);
    assertThat(Arrays.stream(methodsOfConnectorOrValidateForCouldSatisfy).map(Method::getName))
        .containsOnly(nextMethods[0], nextMethods[1]);
  }

  private void thenHasTerminalMethodsThatDoThrowExceptionAre(String... nextMethods) {
    assertThat(Terminal.class.getInterfaces().length).isEqualTo(1);
    assertThat(Terminal.class.getInterfaces()[0].getSimpleName()).isEqualTo("ConsumerTerminal");
    Method[] methodsOfConsumerTerminal = ConsumerTerminal.class.getDeclaredMethods();
    assertThat(methodsOfConsumerTerminal.length).isEqualTo(4);
    assertThat(Arrays.stream(methodsOfConsumerTerminal).map(Method::getName))
        .containsOnly(nextMethods[0], nextMethods[1], nextMethods[2]);
  }

  private void thenHasTerminalMethodsThatDoNotThrowExceptionAre(String... nextMethods) {
    assertThat(CouldConnectorOrValidate.class.getInterfaces().length).isEqualTo(1);
    assertThat(CouldConnectorOrValidate.class.getInterfaces()[0].getSimpleName()).isEqualTo("Terminal");
    assertThat(Terminal.class.getDeclaredMethods().length).isEqualTo(3);
    assertThat(Arrays.stream(Terminal.class.getDeclaredMethods()).map(Method::getName))
        .containsOnly(nextMethods[0], nextMethods[1], nextMethods[2]);
  }


  private void thenTheMethodCouldSatisfyHasNextIntermediateMethods(String... nextMethods) {
    thenThrowExceptionForCouldRuleHasIntermediateMethods(nextMethods);
  }
  @Test
  void forMustSatisfyMethodHasNextTwoMethods() {
    Method[] methods = CouldSatisfy.class.getDeclaredMethods();
    assertThat(methods.length).isEqualTo(2);
    assertThat(methods[1].getName()).isEqualTo("mustSatisfy");
    assertThat(methods[1].getReturnType().toString())
        .describedAs("mustSatisfy should return ThrowExceptionForMustRule type")
        .contains("ThrowExceptionForMustRule");

    //forInput only has two methods it can call next
    Method[] methodsOfThrowExceptionForMustRule = CouldThrowException.class.getDeclaredMethods();
    assertThat(methodsOfThrowExceptionForMustRule.length).isEqualTo(3);
    assertThat(methodsOfThrowExceptionForMustRule[0].getName()).isEqualTo("ifNotWillThrow");
    assertThat(methodsOfThrowExceptionForMustRule[1].getName()).isEqualTo("ifNotWillThrowAn");
    assertThat(methodsOfThrowExceptionForMustRule[2].getName()).isEqualTo("butIs");
  }
}