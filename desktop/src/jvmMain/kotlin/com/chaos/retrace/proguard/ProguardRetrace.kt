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

package com.chaos.retrace.proguard

import com.chaos.retrace.Retracer
import proguard.retrace.ReTrace
import java.io.*
import java.nio.charset.StandardCharsets

class ProguardRetrace : Retracer {

    /**
     * 需搭配 net.sf.proguard:proguard-retrace:4.7 使用
     */
    /*override fun retrace(mapping: File, trace: File): String {
        val out = System.out
        val baos = ByteArrayOutputStream()
        val ps = PrintStream(baos)

        try {
            System.setOut(PrintStream(baos))
            ReTrace(ReTrace.STACK_TRACE_EXPRESSION, false, mapping).execute()
            System.setOut(out)
        } catch (e: Exception) {
            // Print just the stack trace message.
            return "Error: ${e.message}"
        } finally {
            baos.close()
        }
        return baos.toString(StandardCharsets.UTF_8.name())
    }*/

    override fun retrace(mapping: File, trace: File): String {
        val baos = ByteArrayOutputStream()
        val ps = PrintStream(baos)

        try {
            // Open the input stack trace. We're always using the UTF-8
            // character encoding, even for reading from the standard
            // input.
            val reader = LineNumberReader(BufferedReader(InputStreamReader(FileInputStream(trace), "UTF-8")))

            // Open the output stack trace, again using UTF-8 encoding.
            val writer = PrintWriter(ps)
             ReTrace(mapping).retrace(reader, writer)
        } catch (e: Exception) {
            // Print just the stack trace message.
            return "Error: ${e.message}"
        } finally {
            ps.close()
        }
        return baos.toString(StandardCharsets.UTF_8.name())
    }
}