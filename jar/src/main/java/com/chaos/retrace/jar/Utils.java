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

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Utils {

    private static final ExecutorService SINGLE = Executors.newSingleThreadExecutor();

    public static void close(Closeable c) {
        if (c == null) return;
        try {
            c.close();
        } catch (IOException ignored) {
        }
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }

    public static File getWorkDir() {
        return new File(System.getProperty("user.dir"));
    }

    public static void writeText(File f, String text) throws IOException {
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(text.getBytes(StandardCharsets.UTF_8));
        fos.close();
    }

    public static void execute(Runnable r) {
        SINGLE.execute(r);
    }
}
