plugins {
    `android-library`
    `kotlin-android`
}

apply(from = "$rootDir/compose-module.gradle")

android {
    namespace = "com.example.onboarding_presentation"
}

dependencies {
    implementation(project(Modules.core))
    implementation(project(Modules.coreUI))
    implementation(project(Modules.onboardingDomain))
}