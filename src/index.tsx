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