## Current release process
The release process of the library and gradle-plugin is currently a manual process which means it is not fully automated 
and needs manual interaction. What needs to be done is mentioned in the following sections.

### Signing artefacts
First of all we need a published gnupg key. This can be easily achieved by downloading the gnupg cli tool and run the
following commands.
```shell
gpg --gen-key
```
Afterward you will be asked to enter the name and the email address used by the key as identification.
When you are done with these steps, you need to publish the public key to some keyservers
```shell
gpg --keyserver keyserver.ubuntu.com --send-keys <public key>
gpg --keyserver pgp.mit.edu --send-keys <public key>
```
You can verify the existence of the keys on the server by using the following commands.
```shell
gpg --keyserver hkp://keyserver.ubuntu.com --search-key 'lumpytales'
```
Make sure you create a backup of the files generated in (Windows)
```shell
C:\Users\username\AppData\Roaming\gnupg
```

### Publish artifacts (maven local)
The publishing of the artifacts is first of all done to your local repository running the task
```shell
./gradlew publishAllPublicationsTo2localRepository
```
In this process you will be asked to enter the password of you gpg key to create the signatures for the artifacts.
The artifacts will then be available in you local maven repository and can be taken from there for manual upload.
```shell
./poco-repository
```
### Release and Publish process
As the "core" and the "gradle-plugin" are always released and published with the same versions we must(!) publish both 
artifacts under that version! To do so read the following chapters.

#### CORE: Publish via sonatype (Maven central)
To publish an artifact to maven-central via sonatype you need to have a sonatype account. This can easily be achieved by
logging in with the github-credentials on 
```text
https://central.sonatype.com
```
The namespace (group id), necessary for publishing, for the project is already added automatically
```text
io.github.<github-owner>
io.github.lumpytales
```
As currently we are using the sonatype "Publishing (Early Access)" we have to follow the guide
```text
https://central.sonatype.org/publish-ea/publish-ea-guide/#publishing-your-components
```
This means that first of all we need to cleanup the old versions from the local maven repository, before we create 
the necessary zip file for the current version.
```text
rm ./poco-repository
```
Afterwards create the artifacts
```shell
./gradlew publishMavenPublicationTo2localRepository
```
Remove the "maven-metadata.xml" and related from the folder
```shell
./poco-repository/io/github/lumpytales/poco/core
```
After all zip (component.zip) the folder and upload it to the sonatype using the publish button.
```text
Deployment Name: 
io.github.lumpytales.poco:core:<version>

Description: 
Latest version of Pojo-Collector Framework
```

#### Gradle Plugin: Publish to gradle-plugin repository
TODO

#### Release
After the artifacts are published, we should release the artifacts to github. This can be done by running the following 
task
```shell
./gradlew release
git push
```