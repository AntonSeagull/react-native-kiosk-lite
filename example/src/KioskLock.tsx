import { useEffect } from 'react';

import {
    AppState,
    BackHandler,
} from 'react-native';
import * as KioskLite from 'react-native-kiosk-lite';

type Props = {
    autoUnlock?: boolean; // отключить lock при размонтировании
};

/**
 * Activates kiosk lock mode:
 * - Enables immersive UI
 * - Blocks back button
 * - Monitors backgrounding and brings app to front
 */
const KioskLock = ({ autoUnlock = true }: Props) => {
    useEffect(() => {
        // Активируем immersive mode
        KioskLite.lock();

        // Блокируем "назад"
        const backHandler = BackHandler.addEventListener('hardwareBackPress', () => true);

        // Отслеживаем background → возвращаем на передний план
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