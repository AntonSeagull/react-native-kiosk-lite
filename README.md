# react-native-kiosk-lite

A lightweight React Native module that simulates kiosk mode on Android — no device owner or system privileges required.

---

## ✨ Features

- Enables immersive fullscreen mode (hides navigation & status bars)
- Blocks the Android hardware back button
- Detects when the app goes to background and brings it back to foreground
- Simple API: `lock()`, `unlock()`, `bringToFront()`
- Pure JavaScript wrapper included for easy integration

> ⚠️ iOS is **not supported** for kiosk behavior due to system limitations, but safe stub methods are included.

---

## 📦 Installation

```sh
npm install react-native-kiosk-lite
# or
yarn add react-native-kiosk-lite
```

Make sure to run:

```sh
npx pod-install
```

For Android, no extra setup is required if you're using autolinking.

---

## 🚀 Usage Example: `KioskLock` Component

You can use this drop-in component to activate kiosk mode automatically:

```tsx
import { useEffect } from 'react';
import { AppState, BackHandler } from 'react-native';
import * as KioskLite from 'react-native-kiosk-lite';

type Props = {
  autoUnlock?: boolean; // if false, stays locked after unmount
};

/**
 * Activates kiosk lock mode:
 * - Enables immersive UI
 * - Blocks back button
 * - Monitors backgrounding and brings app to front
 */
const KioskLock = ({ autoUnlock = true }: Props) => {
  useEffect(() => {
    // Enable immersive mode
    KioskLite.lock();

    // Block back button
    const backHandler = BackHandler.addEventListener(
      'hardwareBackPress',
      () => true
    );

    // Bring app to front if backgrounded
    const appStateListener = AppState.addEventListener('change', (state) => {
      if (state === 'background') {
        setTimeout(() => {
          KioskLite.bringToFront();
        }, 500);
      }
    });

    return () => {
      backHandler.remove();
      appStateListener.remove();
      if (autoUnlock) {
        KioskLite.unlock();
      }
    };
  }, [autoUnlock]);

  return null;
};

export default KioskLock;
```

Then in your `App.tsx`:

```tsx
import React from 'react';
import { View, Text } from 'react-native';
import KioskLock from './KioskLock'; // or from your utils

const App = () => (
  <View style={{ flex: 1, justifyContent: 'center', alignItems: 'center' }}>
    <KioskLock />
    <Text>Kiosk Mode Enabled</Text>
  </View>
);

export default App;
```

---

## 🧪 API

```ts
KioskLite.lock(): void
KioskLite.unlock(): void
KioskLite.bringToFront(): void
```

---

## 📱 Platform Support

| Platform | Support           |
| -------- | ----------------- |
| Android  | ✅ Yes            |
| iOS      | 🚫 No (stub only) |

---

## 🧩 Roadmap Ideas

- [ ] Foreground Service support
- [ ] PIN-protected unlock
- [ ] Overlay blocker for status bar
- [ ] Auto-return on timeout

---

## 📄 License

MIT © [Anton Seagull](https://github.com/AntonSeagull)

---

## 🙌 Contributions Welcome

Feel free to open issues or pull requests to improve this module.
