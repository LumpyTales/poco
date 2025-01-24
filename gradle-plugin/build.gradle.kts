plugins {
    pmd
    jacoco
    id("com.gradle.plugin-publish") version "1.3.0"
    id("com.github.spotbugs") version "6.1.2"
    id("com.diffplug.spotless") version "7.0.2"
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
    compileOnly("jakarta.annotation:jakarta.annotation-api:3.0.0")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testImplementation("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.assertj:assertj-core:3.27.3")

    testImplementation("org.mockito:mockito-core:5.15.2")
    testImplementation("org.mockito:mockito-junit-jupiter:5.15.2")

    spotbugsPlugins("com.h3xstream.findsecbugs:findsecbugs-plugin:1.13.0")

    compileOnly("com.google.googlejavaformat:google-java-format:1.25.2")

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
