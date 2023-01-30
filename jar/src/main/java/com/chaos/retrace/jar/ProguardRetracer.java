/*
 * Copyright 2023 Chaos Leung
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

package com.chaos.retrace.jar;

import proguard.retrace.ReTrace;

import java.io.*;
import java.nio.charset.StandardCharsets;


public class ProguardRetracer implements Retracer {
    @Override
    public String retrace(File mapping, File trace) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);

        try {
            // Open the input stack trace. We're always using the UTF-8
            // character encoding, even for reading from the standard
            // input.
            LineNumberReader reader = new LineNumberReader(new BufferedReader(new InputStreamReader(new FileInputStream(trace), "UTF-8")));

            // Open the output stack trace, again using UTF-8 encoding.
            PrintWriter writer = new PrintWriter(ps);
            new ReTrace(mapping).retrace(reader, writer);
            return baos.toString(StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            // Print just the stack trace message.
            return "Error: " + e.getMessage();
        } finally {
            Utils.close(ps);
        }
    }
}
