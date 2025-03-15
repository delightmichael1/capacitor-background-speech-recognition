import Foundation
import Capacitor
import Speech

@objc(SpeechToText)
public class SpeechToText: CAPPlugin, SFSpeechRecognizerDelegate {
    private let speechRecognizer = SFSpeechRecognizer(locale: Locale(identifier: "en-US"))!
    private var recognitionRequest: SFSpeechAudioBufferRecognitionRequest?
    private var recognitionTask: SFSpeechRecognitionTask?
    private let audioEngine = AVAudioEngine()

    @objc func startListening(_ call: CAPPluginCall) {
        SFSpeechRecognizer.requestAuthorization { authStatus in
            if authStatus != .authorized {
                call.reject("Speech recognition not authorized")
                return
            }

            do {
                try self.startRecording()
                call.resolve()
            } catch {
                call.reject("Failed to start recording")
            }
        }
    }

    @objc func stopListening(_ call: CAPPluginCall) {
        self.audioEngine.stop()
        self.recognitionRequest?.endAudio()
        call.resolve()
    }

    private func startRecording() throws {
        let audioSession = AVAudioSession.sharedInstance()
        try audioSession.setCategory(.record, mode: .measurement, options: .duckOthers)
        try audioSession.setActive(true, options: .notifyOthersOnDeactivation)

        recognitionRequest = SFSpeechAudioBufferRecognitionRequest()

        let inputNode = audioEngine.inputNode
        guard let recognitionRequest = recognitionRequest else { return }

        recognitionTask = speechRecognizer.recognitionTask(with: recognitionRequest) { result, error in
            if let result = result {
                let transcript = result.bestTranscription.formattedString
                self.notifyListeners("onResults", data: ["transcript": transcript])
            } else if let error = error {
                self.notifyListeners("onError", data: ["error": error.localizedDescription])
            }
        }

        let recordingFormat = inputNode.outputFormat(forBus: 0)
        inputNode.installTap(onBus: 0, bufferSize: 1024, format: recordingFormat) { buffer, _ in
            recognitionRequest.append(buffer)
        }

        audioEngine.prepare()
        try audioEngine.start()
    }
}