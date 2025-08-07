import type { TurboModule } from 'react-native';
import {
  Platform,
  TurboModuleRegistry,
} from 'react-native';

export interface Spec extends TurboModule {
  lock(): void;
  unlock(): void;
  bringToFront(): void;

  createOverlay(): void;
  removeOverlay(): void;

  requestOverlayPermission(): void;
  hasOverlayPermission(): Promise<boolean>;

  startKioskMonitorService(): void;
  stopKioskMonitorService(): void;
}

const KioskLite =
  Platform.OS === 'android'
    ? TurboModuleRegistry.getEnforcing<Spec>('KioskLite')
    : null;

export default KioskLite;