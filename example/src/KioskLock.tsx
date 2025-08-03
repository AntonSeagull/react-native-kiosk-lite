import {
    useEffect,
    useRef,
} from 'react';

import type { AppStateStatus } from 'react-native';
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
 * - Reapplies overlay and lock() on app resume
 */
const KioskLock = ({ autoUnlock = true }: Props) => {
    const appStateRef = useRef<AppStateStatus>(AppState.currentState);

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

        // Блокируем "назад"
        const backHandler = BackHandler.addEventListener('hardwareBackPress', () => true);

        // Следим за переходами между фоном и активностью
        const appStateListener = AppState.addEventListener('change', (nextAppState) => {
            const prevState = appStateRef.current;
            appStateRef.current = nextAppState;

            if (prevState === 'background' && nextAppState === 'active') {
                initKiosk();
            }
        });

        return () => {
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