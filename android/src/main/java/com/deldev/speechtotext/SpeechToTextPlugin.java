package com.deldev.speechtotext;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.content.Context;
import androidx.annotation.NonNull;
import android.content.pm.PackageManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.Permission;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.util.ArrayList;

@CapacitorPlugin(name = "SpeechToText", permissions = {
        @com.getcapacitor.annotation.Permission(alias = "record_audio", strings = { Manifest.permission.RECORD_AUDIO }),
        @com.getcapacitor.annotation.Permission(alias = "foreground_service", strings = {
                Manifest.permission.FOREGROUND_SERVICE })
})
public class SpeechToTextPlugin extends Plugin {

    private SpeechRecognizer speechRecognizer;
    private Intent recognizerIntent;
    private PluginCall savedCall;

    @Override
    public void load() {
        try {
            super.load();
            Context context = getContext();
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
            speechRecognizer.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                }

                @Override
                public void onBeginningOfSpeech() {
                }

                @Override
                public void onRmsChanged(float rmsdB) {
                }

                @Override
                public void onBufferReceived(byte[] buffer) {
                }

                @Override
                public void onEndOfSpeech() {
                }

                @Override
                public void onError(int error) {
                    notifyListeners("onError", new JSObject().put("error", error));
                }

                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                    if (matches != null && !matches.isEmpty()) {
                        JSObject ret = new JSObject();
                        ret.put("transcript", matches.get(0));
                        notifyListeners("onResults", ret);
                    }
                }

                @Override
                public void onPartialResults(Bundle partialResults) {
                }

                @Override
                public void onEvent(int eventType, Bundle params) {
                }
            });

            recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US");
            recognizerIntent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true);
        } catch (Exception e) {
            onError(e.getMessage());
        }

    }

    public void saveCall(PluginCall call) {
        this.savedCall = call;
    }

    public PluginCall getSavedCall() {
        return savedCall;
    }

    @PluginMethod
    public void startListening(PluginCall call) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    Context context = getContext();
                    if (context == null) {
                        call.reject("Context is null");
                        return;
                    }

                    Intent serviceIntent = new Intent(context, SpeechToTextService.class);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        context.startForegroundService(serviceIntent);
                    } else {
                        context.startService(serviceIntent);
                    }

                    if (speechRecognizer == null || recognizerIntent == null) {
                        call.reject("SpeechRecognizer not initialized");
                        return;
                    }

                    speechRecognizer.startListening(recognizerIntent);
                    call.resolve();
                } catch (Exception e) {
                    call.reject(
                            "Error in startListening: " + (e.getMessage() != null ? e.getMessage() : "Unknown error"));
                    onError("Error in startListening: " + (e.getMessage() != null ? e.getMessage() : "Unknown error"));
                }
            }
        });
    }

    @PluginMethod
    public void stopListening(PluginCall call) {
        if (speechRecognizer == null) {
            call.reject("SpeechRecognizer is null - cannot stop listening.");
            return;
        }

        try {
            speechRecognizer.stopListening();
            call.resolve();
        } catch (Exception e) {
            call.reject("Error stopping listening: " + e.getMessage());
        }
    }

    @PluginMethod
    public void checkPermissions(PluginCall call) {
        Context context = getContext();
        boolean hasRecordAudioPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
        boolean hasForegroundServicePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.FOREGROUND_SERVICE) == PackageManager.PERMISSION_GRANTED;

        JSObject result = new JSObject();
        result.put("recordAudio", hasRecordAudioPermission);
        result.put("foregroundService", hasForegroundServicePermission);
        call.resolve(result);
    }

    @PluginMethod
    public void requestPermissions(PluginCall call) {
        saveCall(call);
        ActivityCompat.requestPermissions(
                getActivity(),
                new String[] {
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.FOREGROUND_SERVICE
                },
                123 // Request code
        );
    }

    // @Override
    public void handleOnRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 123) {
            boolean recordAudioGranted = false;
            boolean foregroundServiceGranted = false;

            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.RECORD_AUDIO)) {
                    recordAudioGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
                if (permissions[i].equals(Manifest.permission.FOREGROUND_SERVICE)) {
                    foregroundServiceGranted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
                }
            }

            JSObject result = new JSObject();
            result.put("recordAudio", recordAudioGranted);
            result.put("foregroundService", foregroundServiceGranted);
            notifyListeners("onPermissionsResult", result);

            PluginCall savedCall = getSavedCall();
            if (savedCall != null) {
                savedCall.resolve(result);
                saveCall(null);
            }
        }
    }

    // @Override
    public void onError(String error) {
        notifyListeners("onError", new JSObject().put("error", error));
    }
}