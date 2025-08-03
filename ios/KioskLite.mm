#import "KioskLite.h"
#import <React/RCTLog.h>

@implementation KioskLite
RCT_EXPORT_MODULE()

- (void)lock {
  RCTLogWarn(@"KioskLite.lock() called on iOS — not supported.");
}

- (void)unlock {
  RCTLogWarn(@"KioskLite.unlock() called on iOS — not supported.");
}

- (void)bringToFront {
  RCTLogWarn(@"KioskLite.bringToFront() called on iOS — not supported.");
}

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
(const facebook::react::ObjCTurboModule::InitParams &)params
{
  return std::make_shared<facebook::react::NativeKioskLiteSpecJSI>(params);
}

@end