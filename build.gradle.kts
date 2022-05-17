plugins {
    id("java-library")
    id("maven-publish")
}

group = "dev.booky"
version = "2.0.0"

repositories {
    maven("https://repo.destroystokyo.com/repository/maven-public/")
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
}

dependencies {
    api("com.velocitypowered:velocity-api:1.1.8")
    annotationProcessor("com.velocitypowered:velocity-api:1.1.8")
}

java {
    withSourcesJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = project.name.toLowerCase()
        from(components["java"])
    }
}

tasks {
    val processSources = create<Sync>("processSources") {
        from(sourceSets.main.get().java.srcDirs)

        sourceSets.main.get().java.srcDirs.forEach(inputs::dir)
        inputs.property("version", project.version)

        filesMatching("**/*.java") {
            expand("version" to project.version)
        }
        into("$buildDir/src")
    }

    compileJava {
        dependsOn(processSources)
        source = fileTree("$buildDir/src")
    }
}
