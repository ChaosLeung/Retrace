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

package com.chaos.retrace.r8

import com.android.tools.r8.retrace.Retrace
import com.chaos.retrace.Retracer
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.nio.charset.StandardCharsets

class R8Retrace : Retracer {
    override fun retrace(mapping: File, trace: File): String {
        val out = System.out
        val baos = ByteArrayOutputStream()
        try {
            System.setOut(PrintStream(baos))
            Retrace.run(arrayOf<String>(mapping.absolutePath, trace.absolutePath))
            System.setOut(out)
        } catch (e: Exception) {
            return "Error: ${e.message}"
        } finally {
            baos.close()
        }
        return baos.toString(StandardCharsets.UTF_8.name())
    }
}