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
import com.android.tools.r8.retrace.Retrace
import java.io.File
import java.io.PrintStream

class Deobfuscator {
    var mapping: String by mutableStateOf("")

    var deobfuscatedTrace: String by mutableStateOf("")
    var obfuscatedTrace: String by mutableStateOf("")

    fun retrace() {
        if (obfuscatedTrace.isEmpty() || mapping.isEmpty()) {
            return
        }

        val workDir = File(System.getProperty("user.home"), "Library/Application Support/Retrace")
        if (!workDir.exists()) {
            workDir.mkdirs()
        }

        val obfuscated = File(workDir, "temp_trace")
        if (obfuscated.exists()) {
            obfuscated.delete()
        }
        obfuscated.createNewFile()
        obfuscated.writeText(obfuscatedTrace)

        val deobfuscated = File(workDir, "temp_retrace")
        if (deobfuscated.exists()) {
            deobfuscated.delete()
        }

        val out = System.out
        System.setOut(PrintStream(deobfuscated))
        Retrace.run(arrayOf<String>(mapping, obfuscated.absolutePath))
        System.setOut(out)

        deobfuscatedTrace = deobfuscated.readText()
    }
}