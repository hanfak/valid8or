package com.github.hanfak.valid8or.implmentation.couldsatisfy.notification;

public interface Valid8orCouldSatisfyAllRulesBuilderFlow<T> extends
    ForInput<T>,

    Satisfy<T>,

    ThrowException<T>,
    Message<T>,
    ConnectorOrValidate<T>,

    Terminal<T> {
}
