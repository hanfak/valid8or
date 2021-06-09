# valid8or

## Usage

### Maven
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>


<dependency>
  <groupId>com.github.hanfak</groupId>
  <artifactId>valid8or</artifactId>
  <version>0.0.1</version>
</dependency>
```

Must ->  use when at least one validity check fails, it all fails
  - For Validate/validateReturnOptional -> fails fast, passes at the end
  - For the other terminal methods that return input -> fails at the end (in terminal method)
  - For terminal methods that dont return input -> processed at end (in terminal method)
Could -> use when at all validity checks fails, it all fails
  - For Validate/validateReturnOptional -> fails at then end, passes fast
  - For the other terminal methods that return input -> fails at the end (in terminal method)
  - For terminal methods that dont return input -> processed at end (in terminal method)

## Ideas of which order to do validations

Secure by design P134
- Origin
  - Is the data from a legitimate sender?
  - Cheap and quick
- Size
  - Is it reasonably big?
  - Cheap and quick
- Lexical content
  - Does it contain the right characters and encoding?
  - requires a scan, which takes a bit more time and resources
- Syntax
  - Is the format right?
  - might require parsing the data, something that consumes CPU resources and occupies a thread for quite some time
-	Semantics
  -	Does the data make sense?
  -	probably involves a heavy round-trip to the database
