package com.github.hanfak.valid8or.implmentation;


interface Valid8OrMustMustSatisfyAllRulesBuilderFlowMust<T> extends
    ForMustInput<T>,

    MustSatisfy<T>,

    MustThrowException<T>,
    MustMessage<T>,
    MustConnectorOrValidate<T>,

    Terminal<T> {
}
