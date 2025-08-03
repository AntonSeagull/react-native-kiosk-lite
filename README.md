# react-native-kiosk-lite

Emulates kiosk mode in React Native without requiring device owner privileges or system lock task mode.

## Features

- Enables immersive full-screen UI (hides nav & status bar)
- Blocks back button
- Prevents app from being backgrounded (auto-brings to front)
- Optionally shows a transparent overlay to block swipe-down gestures
- Works without root or device admin

## Installation

```sh
yarn add react-native-kiosk-lite
```

Don’t forget to add required permissions and service in `AndroidManifest.xml`.

## Usage

```tsx
import { useEffect, useRef } from 'react';
import { AppState, BackHandler, Platform } from 'react-native';
import * as KioskLite from 'react-native-kiosk-lite';

const KioskLock = ({ autoUnlock = true }: { autoUnlock?: boolean }) => {
  const appStateRef = useRef(AppState.currentState);

  useEffect(() => {
    if (Platform.OS !== 'android') return;

    const initKiosk = () => {
      KioskLite.startKioskMonitorService();
      KioskLite.lock();

      KioskLite.hasOverlayPermission().then((granted) => {
        if (granted) {
          KioskLite.createOverlay();
        } else {
          KioskLite.requestOverlayPermission();
        }
      });
    };

    initKiosk();

    const backHandler = BackHandler.addEventListener('hardwareBackPress', () => true);

    const appStateListener = AppState.addEventListener('change', (nextAppState) => {
      const prev = appStateRef.current;
      appStateRef.current = nextAppState;

      if (prev === 'background' && nextAppState === 'active') {
        initKiosk();
      }
    });

    return () =>
      backHandler.remove();
      appStateListener.remove();

      if (autoUnlock) {
        KioskLite.stopKioskMonitorService();
        KioskLite.unlock();
        KioskLite.removeOverlay?.();
      }
    };
  }, [autoUnlock]);

  return null;
};

export default KioskLock;
```

<?xml version="1.0" encoding="utf-8"?>

<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.yourapp">

    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />

    <application
        android:name=".MainApplication"
        android:label="@string/app_name"
        android:icon="@mipmap/ic_launcher"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:allowBackup="false"
        android:theme="@style/AppTheme">

        <service
            android:name="com.kiosklite.KioskMonitorService"
            android:exported="false"
            android:foregroundServiceType="none" />

        <!-- Other components like activities -->

    </application>

</manifest>
