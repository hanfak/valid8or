package example.alternateformats;

import com.github.hanfak.valid8or.api.Valid8or;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.function.Predicate;

import static java.util.function.Predicate.not;

public class HttpExample {
  HttpResponse hello = new HttpResponse(500, Optional.of("Hello"));
  HttpResponse hello1 = new HttpResponse(200, Optional.empty());
  Predicate<HttpResponse> greaterThan200 = x -> x.code >= 200;
  Predicate<HttpResponse> lessThan300 = x -> x.code < 300;
  @Test
  void name() {

    String s = Valid8or.forInput(hello1)
        .mustSatisfy(greaterThan200.and(lessThan300))
        .ifNotWillThrowAn(() -> new IllegalStateException("Is code " + hello.code))
        .andSatisfies(not(x -> x.code == 500))
        .ifNotWillThrowAn(() -> new IllegalArgumentException("Is 500 "))
        .validateThenReturnOptional()
        .map(x -> "The body is: " + x.body.orElse(""))
        .orElse("");
    System.out.println("s = " + s);
  }

  @Test
  @Disabled
  void name1() {
    String s = Valid8or.forInput(hello)
        .mustSatisfy(greaterThan200.and(lessThan300))
        .ifNotWillThrowAn(() -> new IllegalStateException("Is code " + hello.code))
        .andSatisfies(not(x -> x.code == 500))
        .ifNotWillThrowAn(() -> new IllegalArgumentException("Is 500 "))
        .validate()
        .getBody()
        .orElse("");
    System.out.println("s = " + s);
  }

  @Test
  void name2() {
    Optional<Failure> s = Valid8or.forInput(hello)
        .mustSatisfy(greaterThan200.and(lessThan300))
        .ifNotWillThrowAn(() -> new IllegalStateException("Is code " + hello.code))
        .andSatisfies(not(x -> x.code == 500))
        .ifNotWillThrowAn(() -> new IllegalArgumentException("Is 500 "))
        .allExceptionMessages()
        .stream()
        .filter(x -> x.contains("Is code"))
        .map(Failure::new)
        .findAny();
    System.out.println("s = " + s);
  }

  @Data
  @AllArgsConstructor
  public static class HttpResponse {
    int code;
    Optional<String> body;
  }

  @Data
  @AllArgsConstructor
  public static class Failure{
    String message;
  }
}
