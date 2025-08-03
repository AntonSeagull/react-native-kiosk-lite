import {
  StyleSheet,
  View,
} from 'react-native';

import KioskLock from './KioskLock';

export default function App() {
  return (
    <View style={styles.container}>
      <KioskLock />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
});
