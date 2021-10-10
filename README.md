
[![github workflow](https://img.shields.io/github/workflow/status/hanfak/valid8or/main.yml?style=flat-square)](https://github.com/hanfak/valid8or/actions)
[![](https://jitpack.io/v/hanfak/valid8or.svg)](https://jitpack.io/#hanfak/valid8or)
# valid8or

A fluent validation library, to help build simple readable validations which can involve complex compound rules using predicates

## How to use 

See test for examples of how to use see [here](https://github.com/hanfak/valid8or/tree/main/src/test/java/com/github/hanfak/valid8or/implmentation/couldsatisfy) and [here](https://github.com/hanfak/valid8or/tree/main/src/test/java/com/github/hanfak/valid8or/implmentation/mustsatisfy)

See [wiki](https://github.com/hanfak/valid8or/wiki) for more information and examples

## Minimal Requirements 

Java 11+

## Usage

<details>
<summary>Gradle</summary>
	
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
	implementation 'com.github.hanfak:valid8or:1.0.1'
}
```
</details>

<details>
<summary>Maven</summary>
	
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
        <version>1.0.1</version>
    </dependency>
    ...
</dependencies>
```
</details>

## sonarcloud 

https://sonarcloud.io/dashboard?id=hanfak_valid8or
