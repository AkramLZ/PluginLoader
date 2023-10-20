plugins {
    id("java")
    id("application")
}

group = "${parent?.group}"
version = "${parent?.version}"

application {
    mainClass = "me.akraml.loader.LoaderBackend"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":loader-common"))
}