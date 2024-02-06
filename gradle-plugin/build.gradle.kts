plugins {
    pmd
    jacoco
    id("com.gradle.plugin-publish") version "1.2.1"
    id("com.github.spotbugs") version "6.0.6"
    id("com.diffplug.spotless") version "6.25.0"
}

java {
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

gradlePlugin {
    website.set("https://github.com/LumpyTales/poco")
    vcsUrl.set("https://github.com/LumpyTales/poco")
    plugins {
        create("pocoPlugin") {
            id = project.group.toString() + ".gradle-plugin"
            implementationClass = "io.github.lumpytales.poco.plugin.PocoPlugin"
            displayName = "PoCo - Pojo Collector Generator"
            description = "Gradle plugin used to generate so-called pojo-collector classes!"
            tags.set(listOf("poco", "pojo", "collector", "generator"))
        }
    }
}

dependencies {

    implementation(project(":core"))
    compileOnly("jakarta.annotation:jakarta.annotation-api:3.0.0-M1")

    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testImplementation("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.assertj:assertj-core:3.25.3")

    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.10.0")

    spotbugsPlugins("com.h3xstream.findsecbugs:findsecbugs-plugin:1.12.0")

    compileOnly("com.google.googlejavaformat:google-java-format:1.19.2")

}

// PUBLISHING ----------------------------------------------------------

publishing {
    repositories {
        maven {
            name = "2local"
            url = uri("../poco-repository")
        }
    }
}

// LINTING -------------------------------------------------------------

spotless {
    java {
        removeUnusedImports()
        formatAnnotations()
        googleJavaFormat("1.19.1").aosp().reflowLongStrings().skipJavadocFormatting()
    }
}

tasks.compileJava {
    dependsOn(tasks["spotlessApply"])
}

// TEST CONFIG ---------------------------------------------------------
tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
    jvmArgs("-Xshare:off")
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
        html.required = true
        csv.required = true
    }
    dependsOn(tasks.test)
}

// JAVADOC ------------------------------------------------------------
tasks.javadoc {
    options {
        this as CoreJavadocOptions
        addStringOption("Xdoclint:-missing", "-quiet")
    }
}

// QUALITY ASSURANCE CONFIG -------------------------------------------
tasks.withType<Checkstyle>().configureEach {
    reports {
        xml.required = true
        html.required = true
    }
}

pmd {
    ruleSetFiles = files("../config/pmd/rulesets.xml")
}

spotbugs {
    maxHeapSize = "5g"
    excludeFilter.set(
        file("../config/spotbugs/excludes.xml")
    )
}

tasks.spotbugsMain {
    reports {
        reports.create("html") {
            required = true
            outputLocation = layout.buildDirectory.file("reports/spotbugs/main.html")
            setStylesheet("fancy-hist.xsl")
        }
        reports.create("xml") {
            required = true
            outputLocation = layout.buildDirectory.file("reports/spotbugs/main.xml")
        }
    }
}

tasks.spotbugsTest {
    reports {
        reports.create("html") {
            required = true
            outputLocation = layout.buildDirectory.file("reports/spotbugs/test.html")
            setStylesheet("fancy-hist.xsl")
        }
        reports.create("xml") {
            required = true
            outputLocation = layout.buildDirectory.file("reports/spotbugs/test.xml")
        }
    }
}
