import { PluginListenerHandle, WebPlugin } from '@capacitor/core';
import type { SpeechToTextPlugin } from './definitions';

export class SpeechToTextWeb extends WebPlugin implements SpeechToTextPlugin {
  constructor() {
    super();
  }

  async startListening(): Promise<void> {
    console.warn('Speech to text is not supported on the web.');
  }

  async stopListening(): Promise<void> {
    console.warn('Speech to text is not supported on the web.');
  }

  async isAvailable(): Promise<{ available: boolean }> {
    return { available: false };
  }

  async requestPermissions(): Promise<{
    recordAudio: boolean;
    foregroundService: boolean;
    backgroundPermission?: boolean;
  }> {
    console.warn('Permissions are not applicable on the web.');
    return {
      recordAudio: false,
      foregroundService: false,
      backgroundPermission: false,
    };
  }

  async checkPermissions(): Promise<{
    recordAudio: boolean;
    foregroundService: boolean;
    backgroundPermission?: boolean;
  }> {
    console.warn('Permissions are not applicable on the web.');
    return {
      recordAudio: false,
      foregroundService: false,
      backgroundPermission: false,
    };
  }

  async addListener(eventName: string): Promise<PluginListenerHandle> {
    console.warn('Listeners are not available on the web.');

    const handle: PluginListenerHandle = {
      remove: async () => {
        console.warn('Listener removed.', eventName);
      },
    };

    return Promise.resolve(handle);
  }
}
