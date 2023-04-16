#import "RtnTurboGeocode.h"
#import <CoreLocation/CoreLocation.h>

@implementation RtnTurboGeocode

RCT_EXPORT_MODULE()

- (void)getAddressFromGeopoint:(JS::NativeGeocode::Geopoint &)geopoint resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    CLLocation *_location = [[CLLocation alloc] initWithLatitude:geopoint.latitude() longitude:geopoint.longitude()];
    CLGeocoder *geocoder = [[CLGeocoder alloc] init];
    
    [geocoder reverseGeocodeLocation:_location completionHandler:^(NSArray<CLPlacemark *> * _Nullable placemarks, NSError * _Nullable error) {
        if (error != nil) {
            NSString *code = [[NSString alloc] initWithFormat:@"%ld", (long)error.code];
            
            reject(code, error.description, error);
        } else if (placemarks != nil && placemarks.count > 0) {
            CLPlacemark *placemark = [placemarks objectAtIndex:0];

            NSMutableDictionary *dict = [[NSMutableDictionary alloc] initWithCapacity:5];
            [dict setObject:placemark.name forKey:@"street1"];
            [dict setObject:placemark.locality forKey:@"city"];
            [dict setObject:placemark.administrativeArea forKey:@"state"];
            [dict setObject:placemark.postalCode forKey:@"postalCode"];
            [dict setObject:placemark.country forKey:@"country"];
            
            resolve(dict);
        } else {
            resolve([NSNull null]);
        }
    }];
}

- (void)getCoordinateFromAddressString:(NSString *)address resolve:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject {
    CLGeocoder *geocoder = [[CLGeocoder alloc] init];
    
    [geocoder geocodeAddressString:address completionHandler:^(NSArray<CLPlacemark *> * _Nullable placemarks, NSError * _Nullable error) {
        if (error != nil) {
            NSString *code = [[NSString alloc] initWithFormat:@"%ld", (long)error.code];
            
            reject(code, error.description, error);
        } else if (placemarks != nil && placemarks.count > 0) {
            CLPlacemark *placemark = [placemarks objectAtIndex:0];
            
            NSDictionary *dict = @{
                @"latitude": [NSNumber numberWithDouble:placemark.location.coordinate.latitude],
                @"longitude": [NSNumber numberWithDouble:placemark.location.coordinate.longitude]
            };
            
            resolve(dict);
        } else {
            resolve([NSNull null]);
        }
    }];
}

// Don't compile this code when we build for the old architecture.
#ifdef RCT_NEW_ARCH_ENABLED
- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params
{
    return std::make_shared<facebook::react::NativeGeocodeSpecJSI>(params);
}
#endif

@end
