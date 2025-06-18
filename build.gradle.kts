plugins {
    id("java")
    id("application")
}

group = "com.servermanager.minecraft"
version = "1.0"

tasks.processResources {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

sourceSets {
    main {
        resources {
            srcDir("src/main/resources")
        }
    }
}

repositories {
    mavenCentral()
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17)) // Usa Java 17
}

application {
    mainClass.set("com.servermanager.minecraft.Main")
}

val javafxLib = file("javafx/lib")

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(files(javafxLib.listFiles()))
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.tomlj:tomlj:1.1.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.apache.commons:commons-compress:1.26.1")
// o la versión más reciente
}

tasks.test {
    useJUnitPlatform()
}

tasks.named<JavaExec>("run") {
    doFirst {
        jvmArgs = listOf(
            "--module-path=${javafxLib.absolutePath}",
            "--add-modules=javafx.controls,javafx.fxml,javafx.web",
            "--add-opens=javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED",
            "--add-opens=javafx.graphics/com.sun.javafx.sg.prism=ALL-UNNAMED"
        )
    }
}

tasks.withType<Javadoc> {
    options.encoding = "UTF-8"
}
