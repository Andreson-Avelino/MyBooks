import java.util.Properties


plugins {
    alias(libs.plugins.android.application)
}
val localProperties = Properties().apply {
    load(rootProject.file("local.properties").inputStream())
}

android {
    namespace = "com.example.mybooks"
    compileSdk {
        version = release(37)
    }
    // Habilita a criação de recursos personalizados definidos pelo Gradle.
    buildFeatures {
        resValues = true
    }

    defaultConfig {
        applicationId = "com.example.mybooks"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        // Cria um recurso String com a chave armazenada no local.properties.
        resValue(
            "string",
            "google_books_api_key",
            localProperties.getProperty("GOOGLE_BOOKS_API_KEY") ?: ""
        )
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    // Permite fazer requisições HTTP e converter o JSON recebido pela API.
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
}