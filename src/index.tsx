import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-shazamkit' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n';

const Shazamkit = NativeModules.ShazamRecognizer
  ? NativeModules.ShazamRecognizer
  : new Proxy(
      {
        isAvailable(): boolean {
          return false;
        },

        startRecognition(developerToken?: string) {
          if (Platform.OS === 'android' && !developerToken) {
            throw new Error('Developer token is required on Android');
          }
        },

        stopRecognition() {},

        addToShazamLibrary() {
          return { success: false };
        },

        addListener() {
          // Nothing to do; unsupported platform.
          return Promise.resolve();
        },

        removeListeners() {
          // Nothing to do; unsupported platform.
          return Promise.resolve();
        },
      },
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );
// Define the startRecognition method for the correct platform
Shazamkit.startRecognition = Platform.select({
  ios: () => {
    return NativeModules.Shazamkit.startRecognition(); // No token required for iOS
  },
  android: (developerToken?: string) => {
    if (!developerToken) {
      throw new Error('Developer token is required on Android');
    }
    return NativeModules.Shazamkit.startRecognition(developerToken); // Token required for Android
  },
});
export default Shazamkit;
