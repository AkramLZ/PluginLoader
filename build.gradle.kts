plugins {
    id("java")
}

group = "me.akraml"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
}

allprojects {
    apply(plugin = "java")

    dependencies {
        compileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")
    }
}