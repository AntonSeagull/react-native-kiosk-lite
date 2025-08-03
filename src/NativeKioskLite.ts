import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export interface Spec extends TurboModule {
  lock(): void;
  unlock(): void;
  bringToFront(): void;

  createOverlay(): void;
  removeOverlay(): void;

  requestOverlayPermission(): void;
  hasOverlayPermission(): Promise<boolean>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('KioskLite');