import { useState } from 'react';
import {
  StyleSheet,
  View,
  Text,
  TouchableOpacity,
  Image,
  Platform,
} from 'react-native';
import ShazamRecognizer from 'react-native-shazamkit';

interface Music {
  title: string;
  subtitle: string;
  artist: string;
  shazamID: string;
  appleMusicID: string;
  appleMusicURL?: string;
  artworkURL?: string;
  genres?: any[];
  webURL?: string;
  videoURL?: string;
  explicitContent?: any;
  matchOffset?: any;
}

const placeholderArtworkUrl =
  'https://cdn.pixabay.com/photo/2023/02/16/03/43/music-player-7792956_1280.jpg';

export default function App() {
  const [isListening, setIsListening] = useState(false);
  const [matchedMusic, setMatchedMusic] = useState<Music | null>(null);

  //allow microphone permission before start listening
  const startListening = async () => {
    const developerToken =
      'eyJhbGciOiJFUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6Ilk3SjRMMzRGVVgifQ.eyJpc3MiOiJGVlNXSFozOTlRIiwiaWF0IjoxNzMwMTA5NTg5LCJleHAiOjE3Mzc4ODU1ODl9.C7fBiInanqSK07iLSGLNlk6Rj-Zwz8vW-_FO6o9nrA8wcmShHRdKQnukd4vYBMsMPJpZqbjkX6NvNEXxga_36A';
    setIsListening(true);
    try {
      // send developer token for android
      const result = await ShazamRecognizer.startRecognition(
        Platform.OS === 'android' ? developerToken : undefined
      );
      const re = Platform.OS === 'android' ? JSON.parse(result) : result;
      console.log('ShazamManager:', re);
      const data = re[0];
      setMatchedMusic(data);
      setIsListening(false);
    } catch (error) {
      console.error(error);
      setIsListening(false);
      setMatchedMusic(null);
    }
  };

  const stopListening = async () => {
    try {
      await ShazamRecognizer.stopRecognition();
    } catch (error) {
      console.error(error);
    }
  };

  const musicCard = () => {
    return (
      <View style={styles.card}>
        <View style={[styles.flexRow]}>
          <View style={styles.artworkContainer}>
            <Image
              source={{
                uri: matchedMusic?.artworkURL || placeholderArtworkUrl,
              }}
              style={styles.artworkImage}
            />
          </View>
          <View>
            <Text style={[styles.text, styles.title]}>
              {matchedMusic?.title}
            </Text>
            <Text style={[styles.text, styles.artist]}>
              {matchedMusic?.artist}
            </Text>
          </View>
        </View>
      </View>
    );
  };

  return (
    <View style={styles.container}>
      <TouchableOpacity onPress={startListening}>
        <Text>start Listening</Text>
      </TouchableOpacity>

      <TouchableOpacity onPress={stopListening}>
        <Text>cancel</Text>
      </TouchableOpacity>

      <View>
        {isListening ? (
          <Text>Listening...</Text>
        ) : (
          <View>
            {matchedMusic === null ? (
              <Text>No music matched</Text>
            ) : (
              <View>{musicCard()}</View>
            )}
          </View>
        )}
      </View>
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
  card: {
    backgroundColor: '#fef7fd',
    padding: 10,
    borderRadius: 10,
    marginVertical: 10,
  },

  flexRow: {
    flexDirection: 'row',
    alignItems: 'center',
  },
  artworkContainer: {
    width: 70,
    height: 70,
    marginRight: 10,
  },
  artworkImage: {
    width: '100%',
    height: '100%',
    borderRadius: 10,
  },
  text: {
    width: '60%',
  },
  title: {
    fontSize: 20,
    fontWeight: 'bold',
    color: 'black',
  },
  artist: {
    fontSize: 15,
    color: 'black',
    marginTop: 8,
  },
});
