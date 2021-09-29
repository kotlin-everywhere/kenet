# kenet

![CI](https://github.com/kotlin-everywhere/kenet/actions/workflows/gradle.yml/badge.svg) [![](https://jitpack.io/v/kotlin-everywhere/kenet.svg)](https://jitpack.io/#kotlin-everywhere/kenet)

![kenet](https://user-images.githubusercontent.com/682021/123745117-c260c200-d8ea-11eb-88fd-1e465cd47e00.png)

kenet is Kotlin Everywhere Network Framework.
kenet is lightweight network framework for full duplex communication and type safe. It is designed to make getting started quick and easy, with the ability to scale up to complex applications with kotlin. 

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

1. `fork` kenet project
   ![fork](https://user-images.githubusercontent.com/46353755/135192763-325b828a-8aa8-4397-b35c-5145ce897b84.png)
2. kenet의 소스코드 보완 및 수정 후 `commit`
3. 자신의 github kenet repository에 변경사항 `push`
4. 자신의 github kenet repository에서 `Pull request` 열고 변경사항 내용 작성
   ![pull_request](https://user-images.githubusercontent.com/46353755/135196660-9e4d7eb1-b1ca-4e8b-8283-765f84c939c2.png)
5. `merge` 요청 후 프로젝트 관리자의 응답 대기 (승인 또는 거절될 수 있음)

- `fetch upstream`: kenet project의 변경사항을 자신의 kenet repository에 업데이트, 프로젝트 `fork`한 뒤, `pull request` 하기 전에 kenet에 변경사항이 있을 수 있으므로 `fetch upstream` 진행한다.

## 커밋 메시지 컨벤션

Conventional Commits : https://www.conventionalcommits.org/en/v1.0.0/

커밋 메시지는 `<type>`을 제외하고 한글로 작성한다.

```markdown
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

### \<type>

`fix` : 코드상의 버그 관련 수정

`feat` : 새로운 기능의 코드 추가

`BREAKING CHANGE` : 주요 API 변화가 있을 때 footer 위치에 작성

예시
```markdown
docs(README): 맞춤법 수정
```
```markdown
feat(dsl): Sub Endpoint 추가
```

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

