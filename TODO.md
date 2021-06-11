TODO
https://dzone.com/articles/publish-your-artifacts-to-maven-central
https://shields.io/
- validator: for mustValidate, butWas() can have next step throwException(), to define a exception if not want to use default ValidationException. Step after throwException() will be thenConsume() or terminal()
    - Use arg for validate(Exception) to set all butWas to use custom exception
- IDEA:: 
    - separate could and must into separate builders
    - separate compound exceptions (ie orElseThrowValidationException) with separate exeption (ie validate) for could
     and must builders
     - Use either ifNotThrow() - where default message is input =...-  
     or butWas() - where default exception is
      ValidationExceptin(<message>). Thus one list, one rule type
- How to handle
    forInput(someAction())
        .couldSatisfy(x -> true).butWas(x -> "Some problem")
        .orSatisfies(x -> true).ifNotThrowAn(IllegalArgumentException::new)
        .orSatisfies(x -> true).ifNotThrow(IllegalArgumentException::new).withMessage(x -> "some other error")
    - How to force only one set to be used (flags, throw exception)
    - Remove ifNotThrowAn
    - Add more logic to handle
        - exceptionFunction being there or not, and/or
        - messageFunction being there or not
    - IDEA:: ifNotThrow(input -> ...) only no separate withMessage(). Can defin message in the exception using input
- IDEA::
    - For Validate/validateReturnOptional 
        - Could -> 
            - Check first pass, then all pass in the ifNotThrow() methods, then return input
            - Check in validate method if all fail, then throw exception
        - Must -> 
            - Check first fail, then all fail in the ifNotThrow() methods and return failure
            - check in validate method if all pass, then return input
- butWas have alternate which just takes string instead of function
    - maybe?
- Better language in all classes and interfaces
- refactor
    - too many similar methods with 1 in name, need abstraction for supplier and function used in throws method
    - domain classes, better names for fields/methods, remove lombok??
- Design
    - Split out must and could into two separate step builders, with static factories for both
    - withMessage/hasMessage is it necessary??
        - Remove ifThrowAn(Supplier...) and/or withMessage/hasMessage
    - If remove supplier, and dont want message in exception, can pass x ->"" or x -> {return null;} to withMessage
    - Use Queue to store rules (for mustSatisfy)??
- Modules (java 9)
    - only static Valid8or class can exported to user
    - domain classes cannot be accessed
- Extract out logic to delegates in Valid8orBuilder
- predicate helpers without commons lang or commons math
    - separate module
- delombok, and write tests
- Documentation
- Readme
- examples
- javadoc
- changelog
- jacoco
- findbugs
- pmd
- push library to github/maven
    - https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry
    - https://docs.github.com/en/actions/guides/publishing-java-packages-with-maven

- Tests
    - Testlogger, or capture system out to test thenconsume emthod
    - fluent api for mustSatisfy flow
    - Predicate helpers
