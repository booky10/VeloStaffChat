plugins {
    id("java-library")
    id("maven-publish")
}

group = "dev.booky"
version = "2.0.0"

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
    annotationProcessor("com.velocitypowered:velocity-api:3.4.0-SNAPSHOT")
}

java {
    withSourcesJar()
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
        vendor = JvmVendorSpec.ADOPTIUM
    }
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = project.name.lowercase()
        from(components["java"])
    }
}

tasks {
    val processSources = register<Sync>("processSources") {
        from(sourceSets.main.get().java.srcDirs)

        sourceSets.main.get().java.srcDirs.forEach(inputs::dir)
        inputs.property("version", project.version)

        filesMatching("**/*.java") {
            expand("version" to project.version)
        }
        into(project.layout.buildDirectory.dir("src"))
    }

    compileJava {
        dependsOn(processSources)
        source = fileTree(project.layout.buildDirectory.dir("src"))

        options.encoding = Charsets.UTF_8.name()
        options.compilerArgs.add("-Xlint:unchecked")
        options.compilerArgs.add("-Xlint:deprecation")
        options.compilerArgs.add("-Xlint:removal")
    }
}
