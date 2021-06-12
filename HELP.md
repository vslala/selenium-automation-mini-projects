# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.1/gradle-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.1/gradle-plugin/reference/html/#build-image)
* [Testcontainers](https://www.testcontainers.org/)
* [Spring Configuration Processor](https://docs.spring.io/spring-boot/docs/2.5.1/reference/htmlsingle/#configuration-metadata-annotation-processor)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

### Testing on Mac
Make sure that you have set the chromedriver under identified developers.
To do the same, follow the steps below,

Command 1

- Go to the path where chromedriver is stored and fire below command

`xattr -d com.apple.quarantine chromedriver`

Command 2

`spctl --add --label 'Approved' chromedriver`

