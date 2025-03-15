import { PluginListenerHandle } from '@capacitor/core';

export interface SpeechToTextPlugin {
  // Start listening for speech
  startListening(options?: { language?: string }): Promise<void>;

  // Stop listening for speech
  stopListening(): Promise<void>;

  // Check if speech recognition is available
  isAvailable(): Promise<{ available: boolean }>;

  // Request necessary permissions
  requestPermissions(): Promise<{
    recordAudio: boolean;
    foregroundService: boolean;
  }>;

  // Check if permissions are granted
  checkPermissions(): Promise<{
    recordAudio: boolean;
    foregroundService: boolean;
  }>;

  // Add listeners for events
  addListener(
    eventName: 'onResults',
    listenerFunc: (result: { transcript: string }) => void,
  ): Promise<PluginListenerHandle>;
  removeAllListeners(): Promise<void>;

  addListener(eventName: 'onError', listenerFunc: (error: { message: string }) => void): Promise<PluginListenerHandle>;

  addListener(
    eventName: 'onPermissionsResult',
    listenerFunc: (result: { recordAudio: boolean; foregroundService: boolean }) => void,
  ): Promise<PluginListenerHandle>;

  // Remove all listeners
  removeAllListeners(): Promise<void>;
}
