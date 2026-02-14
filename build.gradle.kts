plugins {
    id("java")
}

group = "dev.danipraivet"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.formdev:flatlaf:3.6.2")
    implementation("com.mysql:mysql-connector-j:8.0.31")
    implementation("org.apache.pdfbox:pdfbox:2.0.30")
    implementation("com.itextpdf:itextpdf:5.5.13.3")
    implementation("org.apache.pdfbox:fontbox:2.0.30")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}