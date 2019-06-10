# IMAP Email Poller
This is a sample application to poll IMAP emails from Gmail. 

If you encounter the following error message, 
```error
[AUTHENTICATION FAILED] Invalid credentials (Failure)
```
Perform the following steps:
- In Gmail Settings, Go To Accounts and Import
- Then Change Account Settings:  Other Google Account Settings
- Under Security tab and Account Permissions - Access for less secure Apps, Select Enable

-----

## Prerequisites

- Java 8
- Gradle 5.4.1

## Build

```bash
./gradlew build
```

## Run
```bash
./gradlew run
```

## Lint
```bash
./gradlew ktlintFormat
```