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

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import java.awt.FileDialog
import java.io.File
import java.io.FilenameFilter

fun main() = singleWindowApplication(
    title = "Retrace",
    state = WindowState(width = 1280.dp, height = 768.dp),
    icon = BitmapPainter(useResource("ic_launcher.png", ::loadImageBitmap)),
) {
    Column(
        modifier = Modifier.padding(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {

        val deobfuscator = remember {
            Deobfuscator()
        }

        MappingBar(window, deobfuscator)

        TraceLayout(deobfuscator)
    }
}

@Composable
fun MappingBar(window: ComposeWindow, deobfuscator: Deobfuscator) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        MappingField(window, "R8 Mapping File", Modifier.weight(1.0f), deobfuscator.mapping) {
            deobfuscator.mapping = it
            deobfuscator.retrace()
        }
    }
}

@Composable
fun MappingField(
    window: ComposeWindow,
    label: String,
    modifier: Modifier,
    value: String,
    onFileSelected: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
        },
        enabled = false,
        label = { Text(label) },
        modifier = modifier.clickable {
            val dialog = FileDialog(window).apply {
                isVisible = true
                filenameFilter = FilenameFilter { dir, name -> name.endsWith(".txt") }
            }
            dialog.file?.let {
                onFileSelected(File(dialog.directory, dialog.file).absolutePath)
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            disabledTextColor = LocalContentColor.current.copy(LocalContentAlpha.current),
            disabledLabelColor = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high),
            disabledBorderColor = MaterialTheme.colors.primary.copy(alpha = ContentAlpha.high),
            disabledLeadingIconColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity),
            disabledTrailingIconColor = MaterialTheme.colors.onSurface.copy(alpha = TextFieldDefaults.IconOpacity),
            disabledPlaceholderColor = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium),
        ),
    )
}

@Composable
fun TraceLayout(deobfuscator: Deobfuscator) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        OutlinedTextField(
            value = deobfuscator.obfuscatedTrace,
            onValueChange = {
                deobfuscator.obfuscatedTrace = it
                deobfuscator.retrace()
            },
            label = { Text("Obfuscated Trace") },
            modifier = Modifier.weight(1.0f)
                .fillMaxHeight(1.0f),
        )
        OutlinedTextField(
            value = deobfuscator.deobfuscatedTrace,
            onValueChange = {},
            readOnly = true,
            label = { Text("Deobfuscated Trace") },
            modifier = Modifier.weight(1.0f)
                .fillMaxHeight(1.0f),
        )
    }
}
