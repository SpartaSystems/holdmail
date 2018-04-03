# How to Release Holdmail

## Prerequisites

There are some prerequisites to releasing the application that need to happen.

1. Ensure that you have a bintray account with write access to https://bintray.com/spartasystems
2. Create environment variables, `BINTRAY_USER` and `BINTRAY_KEY` that have your credentials from the API Key option at https://bintray.com/profile/edit
3. Create a gpg key, you may need to install `gpg` on the command line.  [This is documented on github](https://help.github.com/articles/generating-a-new-gpg-key/)

## Executing a Release

Releases are executed via a release plugin in gradle, `./gradlew clean release`.  Once that's done, using your configured bintray account the release will be available there, and the repository tagged.

## Updating Documentation

- Update the README.md with the news of the new release!
- Use [generate-changelog.sh](docs/docutils/generate-changelog.sh) to generate a new CHANGELOG.md
    - Tip: Mark any junk issues you wish to exclude as 'invalid'
- Use [send-sample-emails.py](docs/docutils/send-sample-emails.py) to generate fake email data
    - Useful for making new screenshots

## Deploying Docker Image

Pre-requisites:
1. Ensure you have logged in to the docker registry, `docker login spartasystems-docker-containers.bintray.io`

Execution:
1. Checkout the tag created in the release
2. Run `./gradlew build docker dockerPush`  
