# PoCo - Pojo Collector 

## What is it?
It is a framework that makes it possible to create so-called collector classes. 
These collector classes collect all objects of nested types of a base object.

## Why could this be useful?
It can be useful if you have to work with complex objects, but only certain types are of interest for your own use case.

Let's assume we have an online store with a merchandise management system and we want to take stock. 
Then we may have orders (outgoing goods), stocks (inventory), returns (incoming goods), purchasing (incoming goods), etc.

All of these use cases have their own basic objects in which products are used, for example. This product data can in 
turn be used to carry out the inventory. However, you would first have to have the appropriate options for extracting 
the product data for each use case. And this is where PoCo comes into play, which can achieve exactly this by 
generating collector classes.

## How can I use it?
Currently, there is the possibility to integrate a gradle plugin into the build process which can generate the collector classes. 
Alternatively, you could also write your own CLI by including the project as dependency.

### Gradle plugin
To get started with the gradle plugin you should add it to your build.gradle in the plugins section.

### Plugin configuration
```kotlin
plugins {
    id("io.github.lumpytales.poco") version "0.1.0"
}
```
Beside that you also have to add the core-library and jakarta.annotation as dependency. Jakarta annotation is used to 
mark the classes as generated and use other informational annotations like "Nullable".
```kotlin
dependencies {
    implementation("io.github.lumpytales.poco:core:0.1.0")
    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0-M1")
}
```
After all add the generated classes to you source-sets.
```kotlin
java.sourceSets["main"].java {
    srcDirs(
        project.layout.buildDirectory.dir("generated-poco")
    )
}
```

### Task configuration
Then you are good to go and can add your own tasks to generate poco-classes.
```kotlin
tasks.register<io.github.lumpytales.poco.PocoGenerator>("genOrder") {
    baseClass = "de.fun.Order" // class which contains classes to collect
    outputPackageName = "de.funny.order"
}
tasks.register<io.github.lumpytales.poco.PocoGenerator>("genPet") {
    baseClass = "de.fun.Pet" // class which contains classes to collect
    outputPackageName = "de.funny.pet"
}
```
In case your poco-classes depend on your own classpath and not on a library you use,
you must add a corresponding dependsOn relation.
```kotlin
tasks.register<io.github.lumpytales.poco.PocoGenerator>("genOrder") {
    baseClass = "de.fun.Order" // class which contains classes to collect
    outputPackageName = "de.funny.order"

    // baseClass is on the classpath so depends on compilation
    dependsOn(tasks.compileJava)
}
```

### Generated sources
After you generated the poco-classes you should find, next to the poco-classes, 
a class which implements the context-interface. You can directly use the poco-classes or alternatively
the context-implementation if you want to use pre-initialized poco-classes in some kind of dependency-injection fashion.

Folder-structure:
```text
build
|- generated-poco
   |- de
      |- funny
         |- order
            |- ProductCollector.java
            |- TagCollector.java
            |- CollectorContextImpl.java
```