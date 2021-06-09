package com.github.hanfak.valid8or.implmentation.couldsatisfy;

public interface Valid8orBuilderInterface<T> extends
    ForInput<T>,

    Satisfy<T>,

    ThrowExceptionForCouldRule<T>,
    MessageForCouldRule<T>,
    ConnectorOrValidateForCouldSatisfy<T>,

    ThrowExceptionForMustRule<T>,
    MessageForMustRule<T>,
    ConnectorOrValidateForMustSatisfy<T>,

    Terminal<T> {
}