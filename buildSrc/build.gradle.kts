plugins {
    kotlin("jvm") version("1.3.50")
    groovy
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(gradleApi())
    implementation(kotlin("stdlib-jdk8"))
    implementation(localGroovy())
}