# kenet

![CI](https://github.com/kotlin-everywhere/kenet/actions/workflows/gradle.yml/badge.svg) [![](https://jitpack.io/v/kotlin-everywhere/kenet.svg)](https://jitpack.io/#kotlin-everywhere/kenet)

![kenet](https://user-images.githubusercontent.com/682021/123745117-c260c200-d8ea-11eb-88fd-1e465cd47e00.png)

kenet is Kotlin Everywhere Network Framework.
kenet is lightweitght network framework for full duplex comminucation and type safe. It is designed to make getting started quick and easy, with the ability to scale up to complex applications with kotlin. 

TODO ::  add links

# Installing with JitPack

## gradle
JitPack repository를 root `build.gradle` 파일의 repositories 맨아래에 추가
```groovy
    allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```
Dependency 추가
```groovy
    dependencies {
        implementation 'com.github.kotlin-everywhere:kenet:0.1.0'
    }
```

## maven
Build 파일에 JitPack repository 추가
```dtd
    <repositories>
        <repository>
            <id>jitpack.io</id>
            <url>https://jitpack.io</url>
        </repository>
    </repositories>
```
Dependency 추가
```dtd
    <dependency>
        <groupId>com.github.kotlin-everywhere</groupId>
        <artifactId>kenet</artifactId>
        <version>0.1.0</version>
    </dependency>
```

## sbt
`build.sbt` 파일의 resolvers 마지막에 JitPack repository 추가
```shell
  resolvers += "jitpack" at "https://jitpack.io"
```
dependency 추가
```shell
  libraryDependencies += "com.github.kotlin-everywhere" % "kenet" % "0.1.0"
```

## leiningen
`project.clj` 파일의 repositories 마지막에 JitPack repository 추가
```shell
  :repositories [["jitpack" "https://jitpack.io"]]
```
Dependency 추가
```shell
  :dependencies [[com.github.kotlin-everywhere/kenet "0.1.0"]]
```

# A Simple Example

```kotlin
// TODO :: echo example
```

# Contributing

TODO :: how to contribute

## 커밋 메시지

Conventional Commits : https://www.conventionalcommits.org/en/v1.0.0/

## 코딩 컨벤션

### 파일명

1. 파일명은 모두 소문자로 시작한다.
2. 패키지 파일은 패키지명으로 한다. org.kotlin.everywhere.net.dsl 패키지의 기본 파일은 org/kotlin/everywhere/net/dsl/dsl.kt 이다.

# Donate

TODO :: how to donate

페이팔 도네이션 (https://www.paypal.com/donate/buttons)

# Links

Mine-K : http://mine-k.co.kr/
Facebook Group : https://www.facebook.com/groups/kenetframework

