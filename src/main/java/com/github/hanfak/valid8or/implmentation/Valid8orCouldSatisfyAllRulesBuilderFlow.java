package com.github.hanfak.valid8or.implmentation;

interface Valid8orCouldSatisfyAllRulesBuilderFlow<T> extends
    ForCouldInput<T>,

    CouldSatisfy<T>,

    CouldThrowException<T>,
    CouldMessage<T>,
    CouldConnectorOrValidate<T>,

    Terminal<T> {}
