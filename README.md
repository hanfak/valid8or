
[![github workflow](https://img.shields.io/github/workflow/status/hanfak/valid8or/main.yml?style=flat-square)](https://github.com/hanfak/valid8or/actions)
[![](https://jitpack.io/v/hanfak/valid8or.svg)](https://jitpack.io/#hanfak/valid8or)
# valid8or

## Minimal Requirements 

Java 11+

## Usage
### Gradle
Add it in your root build.gradle at the end of repositories:
```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Add the dependency
```groovy
	dependencies {
		implementation 'com.github.hanfak:valid8or:0.0.5'
	}
```
### Maven
```xml
<repositories>
    ...
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
    ...
</repositories>

<dependencies>
    ...
    <dependency>
        <groupId>com.github.hanfak</groupId>
        <artifactId>valid8or</artifactId>
        <version>0.0.5</version>
    </dependency>
    ...
</dependencies>
```

## How to use 

See test for examples of how to use

See [wiki](https://github.com/hanfak/valid8or/wiki) for more information and examples

## sonarcloud 

https://sonarcloud.io/dashboard?id=hanfak_valid8or
