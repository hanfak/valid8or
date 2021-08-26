package com.github.hanfak.valid8or.implmentation;


interface Valid8OrMustMustSatisfyAllRulesBuilderFlow<T> extends
    ForMustInput<T>,

    MustSatisfy<T>,

    MustThrowException<T>,
    MustMessage<T>,
    MustConnectorOrValidate<T>,

    Terminal<T> {
}
