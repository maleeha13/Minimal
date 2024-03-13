plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.minimal"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.minimal"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    kotlinOptions{
        jvmTarget = "17"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.3")
    implementation("nl.dionsegijn:konfetti:1.3.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.0")
    implementation("com.google.android.material:material:1.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
    testImplementation ("junit:junit:4.13.2")
    androidTestImplementation ("org.robolectric:robolectric:4.3.1")
    testImplementation ("org.robolectric:robolectric:4.11.1")
    androidTestImplementation ("androidx.test:core:1.2.0")
    testImplementation ("androidx.test:core:1.2.0")
    testImplementation ("org.mockito:mockito-core:3.11.2")




}