# Changelog

All notable changes to this project will be documented in this file. See [standard-version](https://github.com/conventional-changelog/standard-version) for commit guidelines.

### [0.2.1](https://github.com/kotlin-everywhere/kenet/compare/v0.2.0...v0.2.1) (2021-10-31)


### Features

* **gen-typescript:** TypeScript Variant 추가 ([f47cdc9](https://github.com/kotlin-everywhere/kenet/commit/f47cdc9e563aa5e590977da2524f7eead02ba1d6)), closes [#9](https://github.com/kotlin-everywhere/kenet/issues/9)
* **gen:** typescript 에서도 오류나지 않도록 일단 임포트 구문에 ts-ignore 추가 ([fabdadc](https://github.com/kotlin-everywhere/kenet/commit/fabdadc7b62ee7f97023cc5f52923c21a12b5e32))
* **server-engine-http:** Simple CORS 지원 추가 ([792258c](https://github.com/kotlin-everywhere/kenet/commit/792258caa1fed8d130e0062d8c8d88f017549fd1))
* **server:** 호출 지점 초기화 여부 확인 추가 ([8bbe673](https://github.com/kotlin-everywhere/kenet/commit/8bbe6735de5da9c5e66b6d83d116eee8b701052f)), closes [#8](https://github.com/kotlin-everywhere/kenet/issues/8)


### Bug Fixes

* **gen-typescript:** method 'POST' 로 변경, mode 'cors' 로 변경 ([d9fb6d4](https://github.com/kotlin-everywhere/kenet/commit/d9fb6d4652ad7bd84a27b67ea90a05c248671568)), closes [#10](https://github.com/kotlin-everywhere/kenet/issues/10)

## [0.2.0](https://github.com/kotlin-everywhere/kenet/compare/v0.1.0...v0.2.0) (2021-10-02)


### Features

* **dsl:** Sub Endpoint 추가 ([344bdfb](https://github.com/kotlin-everywhere/kenet/commit/344bdfb3a291e320fcfb1f621b9c8aad1f5a1d69))
* **gen, benchmark:** TypescriptGenerator Sub 지원 추가, 생성기 성능 테스트 시료 1만개 적용 및 Sub 사용으로 수정 ([82d8234](https://github.com/kotlin-everywhere/kenet/commit/82d82341966a7f71e7c5b6e51a2b9d801a4a361a))
* **server, client:** Sub Endpoint Path builder, finder 추가 ([f277104](https://github.com/kotlin-everywhere/kenet/commit/f277104f69233c9d0df7ad88705dc126f0070267))
* **server:** Sub Endpoint 지원 테스트 추가 ([6e6053a](https://github.com/kotlin-everywhere/kenet/commit/6e6053a156502f020ed6851947d29c3ea2a3c61d))
