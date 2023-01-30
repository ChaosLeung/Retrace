package com.chaos.retrace.jar;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {
    public TextField proguardMappingInput;
    public TextField r8MappingInput;
    public TextArea deobfuscatedTextArea;

    private File proguardMapping;
    private File r8Mapping;

    private final Retracer r8 = new R8Retracer();
    private final Retracer proguard = new ProguardRetracer();

    @FXML
    protected void onProguardMappingClicked() {
        Platform.runLater(() -> {
            FileChooser chooser = new FileChooser();
            File f = chooser.showOpenDialog(proguardMappingInput.getScene().getWindow());
            proguardMapping = f;
            if (f != null) {
                proguardMappingInput.setText(f.getAbsolutePath());
            }
        });
    }

    @FXML
    protected void onR8MappingClicked() {
        Platform.runLater(() -> {
            FileChooser chooser = new FileChooser();
            File f = chooser.showOpenDialog(r8MappingInput.getScene().getWindow());
            r8Mapping = f;
            if (f != null) {
                r8MappingInput.setText(f.getAbsolutePath());
            }
        });
    }

    protected void onObfuscatedTextChanged(String oldValue, final String newValue) {
        Utils.execute(() -> retrace(newValue));
    }

    private void retrace(String trace) {
        if (Utils.isEmpty(trace) || r8Mapping == null || proguardMapping == null) {
            return;
        }

        Platform.runLater(() -> deobfuscatedTextArea.setText("Retracing..."));

        File obfuscated = new File(Utils.getWorkDir(), "temp_trace");
        try {
            Utils.writeText(obfuscated, trace);

            String deobfuscated = trace;
            if (proguardMapping != null) {
                deobfuscated = proguard.retrace(proguardMapping, obfuscated);

                Utils.writeText(obfuscated, deobfuscated);
            }

            if (r8Mapping != null) {
                deobfuscated = r8.retrace(r8Mapping, obfuscated);
            }

            final String result = deobfuscated.trim();
            Platform.runLater(() -> deobfuscatedTextArea.setText(result));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            obfuscated.delete();
        }
    }
}