plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt) // adicionado por mim, GMINI
    kotlin("kapt") // necessário para o processamento de anotações. Adicionado por mim, GMINI
}

android {
    namespace = "com.bruno.applistadecomprascompose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.bruno.applistadecomprascompose"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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

//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_17 // mudei de "VERSION_1_8" para versão "VERSION_17" para não dar o erro que deu com a Anny
//        targetCompatibility = JavaVersion.VERSION_17 // mudei de "VERSION_1_8" para versão "VERSION_17" para não dar o erro que deu com a Anny
//    }
//    kotlinOptions {
//        jvmTarget = "17" // mudei de "1.8" para versão "17" para não dar o erro que deu com a Anny
//    }


    buildFeatures {
        compose = true
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Dagger Hilt
    implementation(libs.hilt.android)  // adicionado por mim, GMINI
    kapt(libs.hilt.compiler) // adicionado por mim, GMINI
    //kapt("com.google.dagger:hilt-compiler:2.48.1") // não deu certo, daí usei a linha de cima para adicionar

    // Dependências Auxiliares
    implementation ("androidx.activity:activity-ktx:1.10.0")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    // Dependência do viewModel Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")

    // dependências do ROOM que acessa o SQLite
    implementation("androidx.room:room-common:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
}