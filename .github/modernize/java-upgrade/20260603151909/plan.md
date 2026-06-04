# Upgrade Plan: testng-selenium-pom (20260603151909)

- **Generated**: 2026-06-03 15:19:09
- **HEAD Branch**: N/A
- **HEAD Commit ID**: N/A

## Available Tools

**JDKs**
- JDK 21: **<TO_BE_INSTALLED>** (required by Step 1 to verify the latest LTS runtime)
- JDK 26: /opt/homebrew/Cellar/openjdk/26.0.1/libexec/openjdk.jdk/Contents/Home (available for validation if needed)

**Build Tools**
- Maven 3.9.16: /opt/homebrew/Cellar/maven/3.9.16/libexec/bin/mvn

## Guidelines

- Target Java runtime: latest LTS (Java 21)

> Note: You can add any specific guidelines or constraints for the upgrade process here if needed, bullet points are preferred.

## Options

- Working branch: appmod/java-upgrade-20260603151909
- Run tests before and after the upgrade: true

## Upgrade Goals

- Java 21

## Technology Stack

| Technology/Dependency    | Current | Min Compatible | Why Incompatible                               |
| ------------------------ | ------- | -------------- | ---------------------------------------------- |
| Java                     | 21      | 21             | User requested latest LTS runtime              |
| Maven                    | 3.9.16  | 3.9.0          | Compatible with Java 21                        |
| maven-surefire-plugin    | 3.5.2   | 3.0.0          | Compatible with Java 21 and TestNG             |
| Selenium Java            | 4.27.0  | 4.0.0          | Compatible with Java 21                        |
| TestNG                   | 7.10.2  | 7.4.0          | Compatible with Java 21                        |

## Derived Upgrades

- No dependency or plugin upgrades are required for the Java runtime target. The project is already configured for Java 21 in `pom.xml`.

## Impact Analysis

### Dependency Changes

| File | Dependency | Current | Action | Target | Reason |
|------|------------|---------|--------|--------|--------|
| pom.xml | maven.compiler.source / maven.compiler.target | 21 / 21 | none | 21 / 21 | Project already targets Java 21; no upgrade change required |

### Source Code Changes

- No source code changes required. The existing source and test code are already configured for Java 21 compatibility.

### Configuration Changes

- No configuration file changes required beyond the current `pom.xml` settings.

### CI/CD Changes

- No CI/CD file changes are required in this repository for the Java target upgrade.

### Risks & Warnings

- **JDK 21 runtime not currently installed in the environment.** Mitigation: install JDK 21 in Step 1 and use it for verification.
- **No Git repository detected.** Mitigation: record plan and progress under `.github/modernize/java-upgrade/20260603151909` and proceed without version control.

## Upgrade Steps

- Step 1: Setup Environment and Install Java 21
  - **Rationale**: The project target is Java 21, and the environment must have a dedicated Java 21 runtime for accurate verification.
  - **Changes to Make**: Install JDK 21 and confirm the Java 21 toolchain is available.
  - **Verification**: `JAVA_HOME=<jdk21> mvn -version` or `java -version`; expected Java 21 available.

- Step 2: Baseline Compile and Test with Java 21
  - **Rationale**: Confirm the existing POM configuration already supports Java 21 and that the current project compiles and tests cleanly on the target runtime.
  - **Changes to Make**: None to source; run verification commands.
  - **Verification**: `JAVA_HOME=<jdk21> mvn clean test-compile -q && JAVA_HOME=<jdk21> mvn clean test -q`; expected all tests pass.

- Step 3: CVE Validation & Final Verification
  - **Rationale**: Verify direct dependencies for known CVEs and confirm the project remains stable after upgrade verification.
  - **Changes to Make**: None if no CVEs are reported; otherwise upgrade patched direct dependencies within the same minor line.
  - **Verification**: `mvn dependency:list -DexcludeTransitive=true` + `#appmod-validate-cves-for-java(...)`; then `JAVA_HOME=<jdk21> mvn clean test -q`.
