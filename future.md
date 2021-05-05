
- Terminal op that returns optional values or empty when rule fails
    - return object with input and error message (nullable or default string)
        - getInputIfNoError
        - a predicate method that returns true if valid
- Input takes in list, acts on each element
    - rules for all elements must pass rules to be valid
    - rules for one element must pass rules to be valid
- Input handle other collections
- Input handle map 
- Input handle varags and arrays
- Input handle multiple different types of args
- Priority flags
    - order doesnot matter, flag does
    - if flagged rule fails  then invalid
- Negative langauge
    - "couldNotSatisfy", "mustNotSatisfy" etc

    