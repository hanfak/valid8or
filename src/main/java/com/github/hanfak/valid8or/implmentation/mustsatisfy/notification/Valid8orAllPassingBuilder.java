package com.github.hanfak.valid8or.implmentation.mustsatisfy.notification;

public interface Valid8orAllPassingBuilder<T> extends
    ForInput<T>,

    Satisfy<T>,

    ThrowExceptionForMustRule<T>,
    MessageForMustRule<T>,
    ConnectorOrValidateForMustSatisfy<T>,

    Terminal<T> {
}
