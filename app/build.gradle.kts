plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("org.sonarqube")
    id("jacoco")
}

android {
    namespace = "com.misw.vinilos"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.misw.vinilos"
        minSdk = 21
        targetSdk = 36
        versionCode = 5
        versionName = "1.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val keystoreFile = System.getenv("KEYSTORE_PATH")
            if (keystoreFile != null) {
                storeFile = file(keystoreFile)
                storePassword = System.getenv("KEYSTORE_PASSWORD")
                keyAlias = System.getenv("KEY_ALIAS")
                keyPassword = System.getenv("KEY_PASSWORD")
            }
        }
    }

    buildTypes {
        debug {
            enableUnitTestCoverage = true
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("io.mockk:mockk:1.13.10")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.contrib)
    debugImplementation("androidx.fragment:fragment-testing:1.6.2")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Navigation Component
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

    // Fragment
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // Lifecycle ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // Glide — carga de imágenes desde URL
    implementation("com.github.bumptech.glide:glide:4.16.0")
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    reports {
        xml.required.set(true)
        xml.outputLocation.set(file("${layout.buildDirectory.get()}/reports/jacoco/jacocoTestReport.xml"))
    }
    val excludes = listOf(
        "**/ui/**/*Fragment*",
        "**/ui/**/*Adapter*",
        "**/MainActivity*",
        "**/databinding/**",
        "**/BuildConfig*",
        "**/R.class",
        "**/R$*.class"
    )
    sourceDirectories.setFrom(files("src/main/java"))
    classDirectories.setFrom(
        fileTree("${layout.buildDirectory.get()}/intermediates/javac/debug/compileDebugJavaWithJavac/classes") {
            exclude(excludes)
        } +
        fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/debug") {
            exclude(excludes)
        }
    )
    executionData.setFrom(
        fileTree(layout.buildDirectory.get()) { include("**/*.exec", "**/*.ec") }
    )
}

sonar {
    properties {
        property("sonar.projectKey", "organizacion-alternos_vinilos-android")
        property("sonar.organization", "organizacion-alternos")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.sources", "src/main")
        property("sonar.tests", "src/test,src/androidTest")
        property("sonar.java.source", "11")
        property("sonar.coverage.jacoco.xmlReportPaths",
            "build/reports/jacoco/jacocoTestReport.xml")
        property("sonar.coverage.exclusions",
            "**/ui/**/*Fragment*,**/ui/**/*Adapter*,**/MainActivity*,**/databinding/**")
    }
}