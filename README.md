
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
		implementation 'com.github.User:Repo:Tag'
	}
```
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

## How to use 

See [wiki](https://github.com/hanfak/valid8or/wiki)
