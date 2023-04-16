package com.rtnturbogeocode;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.module.annotations.ReactModule;

import androidx.annotation.Nullable;

import android.location.Address;
import android.location.Geocoder;
import android.os.Build;

import java.util.List;

@ReactModule(name = RtnTurboGeocodeModule.NAME)
public class RtnTurboGeocodeModule extends NativeGeocodeSpec {
  public static final String NAME = "RtnTurboGeocode";
  private static final String ERROR_MESSAGE = "No Address Found";

  public RtnTurboGeocodeModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @Override
  @NonNull
  public String getName() {
    return NAME;
  }

  @Override
  public void getAddressFromGeopoint(ReadableMap geopoint, Promise promise) {
    Geocoder geocoder = new Geocoder(this.getReactApplicationContext());
    double lat = geopoint.getDouble("latitude");
    double lon = geopoint.getDouble("longitude");

    if (Build.VERSION.SDK_INT >= 33) {
      geocoder.getFromLocation(lat, lon, 1, new Geocoder.GeocodeListener() {
        @Override
        public void onGeocode(@NonNull List<Address> list) {
          promise.resolve(convertToAddress(list));
        }

        @Override
        public void onError(@Nullable String errorMessage) {
          promise.reject(ERROR_MESSAGE, errorMessage, (Throwable) null);
        }
      });
    } else {
      try {
        List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);

        promise.resolve(convertToAddress(addresses));
      } catch (Exception e) {
        promise.reject(ERROR_MESSAGE, e);
      }
    }
  }

  @Override
  public void getCoordinateFromAddressString(String address, Promise promise) {
    Geocoder geocoder = new Geocoder(this.getReactApplicationContext());

    if (Build.VERSION.SDK_INT >= 33) {
      geocoder.getFromLocationName(address, 1, new Geocoder.GeocodeListener() {
        @Override
        public void onGeocode(@NonNull List<Address> list) {
          promise.resolve(convertToGeopoint(list));
        }

        @Override
        public void onError(@Nullable String errorMessage) {
          promise.reject(ERROR_MESSAGE, errorMessage, (Throwable) null);
        }
      });
    } else {
      try {
        List<Address> addresses = geocoder.getFromLocationName(address, 1);

        promise.resolve(convertToGeopoint(addresses));
      } catch (Exception e) {
        promise.reject(ERROR_MESSAGE, e);
      }
    }
  }

  private WritableMap convertToGeopoint(List<Address> addresses) {
    if (addresses == null || addresses.size() < 1) {
      return null;
    }

    Address address = addresses.get(0);

    WritableMap map = Arguments.createMap();
    map.putDouble("latitude", address.getLatitude());
    map.putDouble("longitude", address.getLongitude());

    return map;
  }

  private WritableMap convertToAddress(List<Address> addresses) {
    if (addresses == null || addresses.size() < 1) {
      return null;
    }

    Address address = addresses.get(0);

    WritableMap map = Arguments.createMap();
    map.putString("street1", address.getFeatureName() + " " + address.getThoroughfare());
    map.putString("city", address.getLocality());
    map.putString("state", address.getAdminArea());
    map.putString("postalCode", address.getPostalCode());
    map.putString("country", address.getCountryName());

    return map;
  }
}
