# DroidSpeech2.0
Android kotlin library for continuous speech recognition with localisations.

<b>Supports from Android SDK version 16 and above</b>.

<b><h1>About</h1></b>

Google's default speech recognition library doesn't allow to continuously listen to users voice and a manual stop and start   mechanism is involved to use the speech recognition again. This proved to be a downfall for third party developers to optimise the user experience of having continuous speech recognition after each speech result. Adding to this the speech recognition server throws up an error when called upon frequently thus preventing an error free experience to the end user. 

<b>Droid Speech 2.0</b> aims to close this gap and provide unparalleled optimisation of continuous speech recognition without any of the above said issues. It is developed keeping in mind all the loopholes which needs to be blocked to have the speech recognition run seamlessly in an android device.

<p align="center">
  <img src="https://user-images.githubusercontent.com/12429051/32413548-67f1805a-c239-11e7-8232-dd0a24e5c491.png" width="200"/>
    <img src="https://user-images.githubusercontent.com/12429051/32413549-68210c58-c239-11e7-96ec-62be7a944eba.png" width="200"/>
  <img src="https://user-images.githubusercontent.com/12429051/32413547-67beb4d6-c239-11e7-861f-a3808bd2c1e7.png" width="200"/>
  <img src="https://user-images.githubusercontent.com/12429051/32413550-684f8b46-c239-11e7-89d5-b4ab4905b369.png" width="200"/>
</p>

<b><h1>Usage</h1></b>
<b>Gradle dependency:</b>

Add the following to your project level build.gradle:

```java
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

Add this to your app build.gradle:

```java
dependencies {
    implementation 'com.github.vikramezhil:DroidSpeech2.0:v1.0'
}
```

<b>Maven:</b>

Add the following to the <repositories> section of your pom.xml:

```xml
<repositories>
  <repository>
      <id>jitpack.io</id>
      <url>https://jitpack.io</url>
  </repository>
</repositories>
```

Add the following to the <dependencies> section of your pom.xml:

```xml
<dependency>
    <groupId>com.github.vikramezhil</groupId>
    <artifactId>DroidSpeech2.0</artifactId>
    <version>v1.0</version>
</dependency>
```

<b><h1>Documentation</h1></b>

For a detailed documentation 📔, please have a look at the [Wiki](https://github.com/vikramezhil/DroidSpeech2.0/wiki).

```kotlin
var dks = Dks(application, supportFragmentManager, object: DksListener {
    override fun onDksLiveSpeechResult(liveSpeechResult: String) {
        Log.d("DKS", "Speech result - $liveSpeechResult")
    }

    override fun onDksFinalSpeechResult(speechResult: String) {
        Log.d("DKS", "Final speech result - $speechResult")
    }

    override fun onDksLiveSpeechFrequency(frequency: Float) {
        Log.d("DKS", "frequency - $frequency")
    }

    override fun onDksLanguagesAvailable(defaultLanguage: String?, supportedLanguages: ArrayList<String>?) {
        Log.d("DKS", "defaultLanguage - $defaultLanguage")
        Log.d("DKS", "supportedLanguages - $supportedLanguages")
    }

    override fun onDksSpeechError(errMsg: String) {
        Log.d("DKS", "errMsg - $errMsg")
    }
})
```
Listen to user speech

```kotlin
dks.startSpeechRecognition()
```

Close speech operations

```kotlin
dks.closeSpeechOperations()
```
