# How to Release Holdmail

## Prerequisites

There are some prerequisites to releasing the application that need to happen.

1. Ensure that you have a bintray account with write access to https://bintray.com/spartasystems
2. Create environment variables, `BINTRAY_USER` and `BINTRAY_KEY` that have your credentials from the API Key option at https://bintray.com/profile/edit
3. Create a gpg key

## Executing a Release

Releases are executed via a release plugin in gradle, `./gradlew clean release`
