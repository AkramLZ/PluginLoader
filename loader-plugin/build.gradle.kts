plugins {
    id("java")
}

group = "${parent?.group}"
version = "${parent?.version}"

repositories {
    mavenCentral()
}