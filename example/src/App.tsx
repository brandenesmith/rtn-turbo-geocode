import * as React from 'react';

import { StyleSheet, View, Text, Button } from 'react-native';
import {
  getAddressFromGeopoint,
  getCoordinateFromAddressString,
} from 'rtn-turbo-geocode';

export default function App() {
  const [currentAddress, setAddress] = React.useState<string>('none');
  const [currentGeopoint, setGeopoint] = React.useState<string>('none');

  const getAddress = () => {
    getAddressFromGeopoint({
      latitude: 37.331656,
      longitude: -122.0301426,
    })
      .then((address) => {
        setAddress(
          `${address.street1}\n${address.city}, ${address.state} ${address.postalCode}\n${address.country}`
        );
      })
      .catch((error) => {
        console.log(error);
      });
  };

  const getGeopoint = () => {
    getCoordinateFromAddressString('1 infinite Loop, Cupertino, CA')
      .then((geopoint) => {
        setGeopoint(`Lat: ${geopoint.latitude}, Long: ${geopoint.longitude}`);
      })
      .catch((error) => {
        console.log(error);
      });
  };

  return (
    <View style={styles.container}>
      <Button onPress={getAddress} title="Get Address" color={'blue'} />
      <Text>Address: {currentAddress}</Text>
      <Button onPress={getGeopoint} title="Get Geopoint" color={'blue'} />
      <Text>Point: {currentGeopoint}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
