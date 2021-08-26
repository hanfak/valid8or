package com.github.hanfak.valid8or.implmentation;

interface Valid8OrCouldSatisfyAllRulesBuilderFlow<T> extends
    ForCouldInput<T>,

    CouldSatisfy<T>,

    CouldThrowException<T>,
    CouldMessage<T>,
    CouldConnectorOrValidate<T>,

    Terminal<T> {}
