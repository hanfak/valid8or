package com.github.hanfak.valid8or.implmentation.mustsatisfy;

public interface Valid8orMustSatisfyAllRulesBuilderFlow<T> extends
    ForInput<T>,

    Satisfy<T>,

    ThrowException<T>,
    Message<T>,
    ConnectorOrValidate<T>,

    Terminal<T> {
}
