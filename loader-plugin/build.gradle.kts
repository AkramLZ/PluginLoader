plugins {
    id("java")
}

group = "${parent?.group}"
version = "${parent?.version}"

repositories {
    mavenCentral()
    maven("https://repo.codemc.org/repository/nms")
}

dependencies {
    compileOnly("org.spigotmc:spigot:1.8.8-R0.1-SNAPSHOT")
}