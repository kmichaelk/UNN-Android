plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "io.github.kmichaelk.unnandroid"
    compileSdk = 34

    defaultConfig {
        applicationId = "io.github.kmichaelk.unnandroid"
        minSdk = 24
        targetSdk = 34
        versionCode = 12
        versionName = "1.0.11"

        resourceConfigurations.clear()
        resourceConfigurations.add("ru")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.security:security-crypto:1.0.0")
    implementation("androidx.core:core-splashscreen:1.0.1")

    val verHilt = "2.44"
    implementation("com.google.dagger:hilt-android:$verHilt")
    kapt("com.google.dagger:hilt-android-compiler:$verHilt")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.retrofit2:converter-scalars:2.9.0")

    val verOkhttp = "4.12.0"
    implementation("com.squareup.okhttp3:okhttp:$verOkhttp")
    implementation("com.squareup.okhttp3:logging-interceptor:$verOkhttp")

    implementation("org.jsoup:jsoup:1.17.2")
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("androidx.compose.material:material-icons-extended:1.6.1")

    implementation("androidx.compose.material3:material3:1.2.0")

    implementation("io.coil-kt:coil-compose:2.5.0")

    implementation("me.onebone:toolbar-compose:2.3.5")

    /* ---------------------------------------------------------------------------- */

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}