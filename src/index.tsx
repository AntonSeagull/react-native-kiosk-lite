import KioskLite from './NativeKioskLite';

export function lock(): void {
  KioskLite.lock();
}

export function unlock(): void {
  KioskLite.unlock();
}

export function bringToFront(): void {
  KioskLite.bringToFront();
}

export function createOverlay(): void {
  KioskLite.createOverlay();
}

export function removeOverlay(): void {
  KioskLite.removeOverlay();
}

export function requestOverlayPermission(): void {
  KioskLite.requestOverlayPermission();
}

export function hasOverlayPermission(): Promise<boolean> {
  return KioskLite.hasOverlayPermission();
}