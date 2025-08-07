import KioskLite from './NativeKioskLite';

export function lock(): void {
  KioskLite?.lock();
}

export function unlock(): void {
  KioskLite?.unlock();
}

export function bringToFront(): void {
  KioskLite?.bringToFront();
}

export function createOverlay(): void {
  KioskLite?.createOverlay();
}

export function removeOverlay(): void {
  KioskLite?.removeOverlay();
}

export function requestOverlayPermission(): void {
  KioskLite?.requestOverlayPermission();
}

export function hasOverlayPermission(): Promise<boolean> {
  if (KioskLite?.hasOverlayPermission) {
    return KioskLite.hasOverlayPermission();
  } else {
    return Promise.resolve(false);
  }
}

export function startKioskMonitorService(): void {
  KioskLite?.startKioskMonitorService();
}
export function stopKioskMonitorService(): void {
  KioskLite?.stopKioskMonitorService();
}