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

package com.chaos.retrace

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.chaos.retrace.proguard.ProguardRetrace
import com.chaos.retrace.r8.R8Retrace
import java.io.File

class Deobfuscator {
    var proguardMapping: String by mutableStateOf("")
    var r8Mapping: String by mutableStateOf("")

    var deobfuscatedTrace: String by mutableStateOf("")
    var obfuscatedTrace: String by mutableStateOf("")

    private val r8Retrace: Retracer = R8Retrace()
    private val proguardRetrace: Retracer = ProguardRetrace()

    @Synchronized
    fun retrace() {
        if (obfuscatedTrace.isEmpty() || (r8Mapping.isEmpty() && proguardMapping.isEmpty())) {
            return
        }

        deobfuscatedTrace = "Retracing..."

        val obfuscated = File(Environment.workDir, "temp_trace")
        if (obfuscated.exists()) {
            obfuscated.delete()
        }
        obfuscated.createNewFile()
        obfuscated.writeText(obfuscatedTrace)

        var deobfuscated = obfuscatedTrace
        if (proguardMapping.isNotEmpty()) {
            deobfuscated = proguardRetrace.retrace(File(proguardMapping), obfuscated)

            obfuscated.writeText(deobfuscated)
        }

        if (r8Mapping.isNotEmpty()) {
            deobfuscated = r8Retrace.retrace(File(r8Mapping), obfuscated)
        }

        deobfuscatedTrace = deobfuscated.trim()
        obfuscated.delete()
    }
}