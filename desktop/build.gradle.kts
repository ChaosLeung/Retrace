/*
 * Copyright 2021 Chaos Leung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform") // kotlin("jvm") doesn't work well in IDEA/AndroidStudio (https://github.com/JetBrains/compose-jb/issues/22)
    id("org.jetbrains.compose")
}

kotlin {
    jvm {}
    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.materialIconsExtended)
                implementation(compose.desktop.common)
                implementation(compose.desktop.currentOs)
                implementation("com.android.tools:r8:3.2.78")
                implementation("com.guardsquare:proguard-retrace:7.3.1")
//                implementation("net.sf.proguard:proguard-retrace:4.7")
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.chaos.retrace.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg)
            packageName = "Retrace"
            packageVersion = "1.0.2"
            vendor = "Chaos"
            description = "R8 Retrace"
            copyright = "Copyright © 2021 Chaos Leung"

            macOS {
                // Use -Pcompose.desktop.mac.sign=true to sign and notarize.
                bundleID = "com.chaos.retrace"
                dockName = "Retrace"
                iconFile.set(file("app_icon.icns"))
            }
        }
    }
}
