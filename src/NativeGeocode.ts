import type { TurboModule } from 'react-native';
import { TurboModuleRegistry } from 'react-native';

export type Geopoint = {
  latitude: number;
  longitude: number;
};

export type Location = {
  geopoint: Geopoint;
};

export type Address = {
  street1: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
};

export interface Spec extends TurboModule {
  getAddressFromGeopoint(geopoint: Geopoint): Promise<Address>;
  getCoordinateFromAddressString(address: string): Promise<Geopoint>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('RtnTurboGeocode');
