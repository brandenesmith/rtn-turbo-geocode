import type { Geopoint } from './NativeGeocode';
import type { Address } from './NativeGeocode';

const RTNGeocode = require('./NativeGeocode').default;

export function getAddressFromGeopoint(geopoint: Geopoint): Promise<Address> {
  return RTNGeocode.getAddressFromGeopoint(geopoint);
}

export function getCoordinateFromAddressString(
  address: string
): Promise<Geopoint> {
  return RTNGeocode.getCoordinateFromAddressString(address);
}
