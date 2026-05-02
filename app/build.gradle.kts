plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.anibalcopitan.miappjc"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.anibalcopitan.miappjc"
        minSdk = 21
        targetSdk = 35
        versionCode = 5
        versionName = "1.1.5"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

//    flavorDimensions += listOf("brand")
//    productFlavors {
//        create("app") {
//            dimension = "brand"
//            resValue("string", "app_name", "App Monetizable")
//            buildConfigField("boolean", "ENABLE_INPUT", "true")
//        }
//        create("simpleapp") {
//            dimension = "brand"
//            applicationId = "com.anibalcopitan.simpleapp"
//            resValue("string", "app_name", "Simple App xyz")
//            buildConfigField("boolean", "ENABLE_INPUT", "false")
//        }
//    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "ENABLE_INPUT", "true")
        }
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            buildConfigField("boolean", "ENABLE_INPUT", "true")
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
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.window:window:1.4.0") // soporte a pantallas modernas con notch
    implementation("androidx.startup:startup-runtime:1.2.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
//    debugImplementation("androidx.compose.ui:ui-tooling-preview")

    // Kotlin + coroutines
    val work_version = "2.8.1" // "2.10.0"
    implementation("androidx.work:work-runtime-ktx:$work_version")
}