import { useEffect } from 'react';

import {
    AppState,
    BackHandler,
    Platform,
} from 'react-native';
import * as KioskLite from 'react-native-kiosk-lite';

type Props = {
    autoUnlock?: boolean; // отключить lock и убрать overlay при размонтировании
};

/**
 * Activates kiosk lock mode:
 * - Enables immersive UI
 * - Blocks back button
 * - Requests & creates overlay to block status bar swipe
 * - Monitors backgrounding and brings app to front
 */
const KioskLock = ({ autoUnlock = true }: Props) => {
    useEffect(() => {
        if (Platform.OS === 'android') {
            // Включаем immersive режим
            KioskLite.lock();

            // Проверка разрешения и создание overlay
            KioskLite.hasOverlayPermission().then((granted) => {
                if (granted) {
                    KioskLite.createOverlay();
                } else {
                    KioskLite.requestOverlayPermission();
                }
            });

            // Блокируем "назад"
            const backHandler = BackHandler.addEventListener('hardwareBackPress', () => true);

            // Перевод в foreground при background'е
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
                    KioskLite.removeOverlay?.();
                }
            };
        }
    }, [autoUnlock]);

    return null;
};

export default KioskLock;