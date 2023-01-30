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

import com.android.tools.r8.retrace.Retrace;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class R8Retracer implements Retracer {
    @Override
    public String retrace(File mapping, File trace) {
        PrintStream out = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            System.setOut(new PrintStream(baos));
            Retrace.run(new String[]{mapping.getAbsolutePath(), trace.getAbsolutePath()});
            System.setOut(out);
            return baos.toString(StandardCharsets.UTF_8.name());
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        } finally {
            Utils.close(baos);
        }
    }
}
