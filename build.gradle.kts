import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id ("java-library")
    id ("maven-publish")
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

tasks {
    val processSources = create<Sync>("processSources") {
        from(sourceSets.main.get().java.srcDirs)
        inputs.property("version", project.version)
        filter<ReplaceTokens>("version" to project.version)
        into("$buildDir/src")
    }

    compileJava {
        source(processSources.outputs)
    }
}
