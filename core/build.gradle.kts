plugins {
    pmd
    jacoco
    signing
    id("java-library")
    id("maven-publish")
    id("com.github.spotbugs") version "6.1.7"
    id("com.diffplug.spotless") version "7.0.2"
}

java {
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {

    implementation("com.squareup:javapoet:1.13.0")
    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")

    compileOnly("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    testCompileOnly("org.projectlombok:lombok:1.18.36")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.36")

    implementation("org.slf4j:slf4j-api:2.0.17")

    testImplementation(platform("org.junit:junit-bom:5.11.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    testImplementation("org.assertj:assertj-core:3.27.3")

    testImplementation("org.mockito:mockito-core:5.16.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.16.1")

    testImplementation("org.apache.commons:commons-lang3:3.17.0")

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
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = "core"

            from(components["java"])

            pom {
                name = "PoCo - Pojo Collector"
                description = "It is a framework that makes it possible to create so-called pojo-collector classes."
                url = "https://github.com/LumpyTales/poco"
                scm {
                    connection = "scm:git:git://github.com/LumpyTales/poco.git"
                    developerConnection = "scm:git:ssh://github.com/LumpyTales/poco.git"
                    url = "http://github.com/LumpyTales/poco"
                }
                licenses {
                    license {
                        name = "MIT License"
                        url = "https://opensource.org/license/MIT/"
                    }
                }
                developers {
                    developer {
                        id = "lumpytales"
                        name = "LumpyTales"
                        email = "64349444+LumpyTales@users.noreply.github.com"
                    }
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["maven"])
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
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
    jvmArgs("-Xshare:off")
}

tasks.jacocoTestReport {
    reports {
        xml.required = true
        html.required = true
        csv.required = true
    }
    dependsOn(tasks.test) // tests are required to run before generating the report
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
