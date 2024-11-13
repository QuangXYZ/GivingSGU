plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.sgu.givingsgu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sgu.givingsgu"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true

    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(fileTree(mapOf(
        "dir" to "C:\\Users\\HP\\Downloads\\DemoZPDK_Android\\DemoZPDK_Android\\ZPDK-Android",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.ibrahimsn98:SmoothBottomBar:1.7.9")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.19")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation ("androidx.fragment:fragment-ktx:1.8.4")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-storage-ktx:21.0.1")
    implementation("io.github.bitvale:switcher:1.1.2")
    implementation("com.github.momo-wallet:mobile-sdk:1.0.7")
    implementation("com.auth0:java-jwt:3.18.2")
    implementation("com.github.stfalcon-studio:StfalconImageViewer:v1.0.1")
    implementation("androidx.activity:activity-ktx:1.2.3")
    implementation("commons-codec:commons-codec:1.17.1")
    implementation ("com.vipulasri:ticketview:1.1.2")




}