
#ifdef RCT_NEW_ARCH_ENABLED
#import "RTNGeocodeSpec.h"

@interface RtnTurboGeocode : NSObject <NativeGeocodeSpec>
#else
#import <React/RCTBridgeModule.h>

@interface RtnTurboGeocode : NSObject <RCTBridgeModule>
#endif

@end
