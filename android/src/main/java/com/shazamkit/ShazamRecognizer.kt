package com.shazamkit

import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.util.Log
import com.facebook.react.bridge.*
// import com.shazam.shazamkit.*
// import kotlinx.coroutines.*
// import java.util.*
// import com.shazam.shazamkit.ShazamKitResult
// import com.shazam.shazamkit.ShazamKitResult.Success
// import com.shazam.shazamkit.ShazamKitResult.Failure

import androidx.annotation.RequiresPermission

import org.json.JSONArray
import org.json.JSONObject

class ShazamRecognizer(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    // companion object {
    //     private const val TAG = "ShazamRecognizer"
    //     private const val CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO
    //     private const val AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT

    // }
    // private var audioRecord: AudioRecord? = null
    // private var isRecording = false
    // private var recordingThread: Thread? = null
    // private var currentSession: StreamingSession? = null
    // private var catalog: Catalog? = null
    // private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
    // private var matchPromise: Promise? = null


      override fun getName(): String = "ShazamRecognizer"

    

    @ReactMethod
    fun isAvailable(promise: Promise) {
        promise.resolve(true);
    }

    // @ReactMethod
    // fun startRecognition(developerToken:String, promise: Promise) {
    //             initializeShazamKit(developerToken) // Initialize session with token

    //     // println("startRecognition")
    //      matchPromise = promise
    //   try {   
    //         val audioSource = MediaRecorder.AudioSource.MIC
    //         val audioFormat = AudioFormat.Builder().setChannelMask(AudioFormat.CHANNEL_IN_MONO)
    //             .setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(44100).build()

    //         audioRecord =
    //             AudioRecord.Builder().setAudioSource(audioSource).setAudioFormat(audioFormat)
    //                 .build()
    //         val bufferSize = AudioRecord.getMinBufferSize(
    //             41_000,
    //             AudioFormat.CHANNEL_IN_MONO,
    //             AudioFormat.ENCODING_PCM_16BIT
    //         )
    //         audioRecord?.startRecording()
    //         isRecording = true
    //         recordingThread = Thread({
    //             val readBuffer = ByteArray(bufferSize);
    //             //println("readBuffer: $readBuffer");
    //             println("readBuffer byte array: ${Arrays.toString(readBuffer)}");
    //             while (isRecording) {
    //                 val actualRead = audioRecord!!.read(readBuffer, 0, bufferSize)
    //                 currentSession?.matchStream(readBuffer, actualRead, System.currentTimeMillis())
    //             }
    //         }, "AudioRecorder Thread")
    //         recordingThread!!.start()
    //     } catch (e: Exception) {
    //          e.message?.let {
    //              onError(it)
    //              promise.reject("HANDLE_ERROR", it)
    //         }
    //     }
                       
    // }

    // @ReactMethod
    // fun stopRecognition() {
    //     if (!isRecording) {
    //         //promise.reject("NOT_RECORDING", "No recording in progress")
    //         return
    //     }
    //     try {
    //         isRecording = false
    //         audioRecord?.apply {
    //             stop()
    //             release()
    //         }
    //         audioRecord = null
    //         recordingThread?.join()
    //         recordingThread = null
    //         //promise.resolve(null)

    //     } catch (e: Exception) {
    //         //promise.reject("STOP_ERROR", "Error stopping recording: ${e.message}")
    //     }
    // }

    //  private fun initializeShazamKit(developerToken:String) {
    //      //Log.d(TAG, "Initializing ShazamKit")
    //     try{  
    //         coroutineScope.launch {
    //              val tokenProvider = object : DeveloperTokenProvider {
    //                 override fun provideDeveloperToken(): DeveloperToken {
    //                     return DeveloperToken(developerToken)                   
    //                 }
    //         }
    //         catalog = ShazamKit.createShazamCatalog(tokenProvider)
    //             catalog?.let {
    //               when (val result = ShazamKit.createStreamingSession(
    //                 it,
    //                 AudioSampleRateInHz.SAMPLE_RATE_44100,
    //                 8192
    //             )) {
    //                 is ShazamKitResult.Success  -> {
    //                     currentSession = result.data
    //                     currentSession?.recognitionResults()?.collect { matchResult: MatchResult ->
    //                         matchPromise?.let {
    //                             handleMatchResult(matchResult, it)
    //                         } ?: Log.e(TAG, "matchPromise is null")      
    //                     } ?: Log.e(TAG, "currentSession is null")
                        
    //                 }
    //                 is ShazamKitResult.Failure -> {
    //                     result.reason.message?.let {
    //                          onError(it)         
    //                          }
    //                 }
    //             }             
    //              } ?: Log.e(TAG, "Catalog is null")           
    //         }

    //     }catch (e: Exception) {
    //         Log.e(TAG, "Error initializing ShazamKit: ${e.message}")
    //     }
    // }

    // private fun handleMatchResult(result: MatchResult,promise: Promise) {
    //   //println("handleMatchResult: $result")
    //     try {
    //         when (result) {
    //             is MatchResult.Match -> {
    //                 //println("Match found: ${result}")
    //                 val matchData = result.toJsonString()
    //                 println("Match found: $matchData")
    //                 promise.resolve(matchData)
                    
    //             }
    //             is MatchResult.NoMatch -> {
    //                 println("No match found")
    //                  promise.reject("No match found")
    //             }
    //             is MatchResult.Error -> {
    //                 println("Error: ${result.exception.message}")
    //                 promise.reject("MATCH_ERROR", result.exception.message)
    //             }
    //         }
    //     } catch (e: Exception) {
    //         e.message?.let {
    //              onError(it)
    //              promise.reject("HANDLE_ERROR", it)
    //         }
            
    //     }finally {
    //         stopRecognition()  // Stop recognition after handling the result
    //     }
    // }

    // private fun onError(message: String) {
    //     println("onError: $message")
    //    //callbackChannel.invokeMethod("didHasError", message)
    //     Log.e(TAG, message)
    // }
  
}


    // fun MatchResult.Match.toJsonString(): String {
    // val itemJsonArray = JSONArray()
    // this.matchedMediaItems.forEach { item ->
    //     val itemJsonObject = JSONObject()
    //     itemJsonObject.put("title", item.title)
    //     itemJsonObject.put("subtitle", item.subtitle)
    //     itemJsonObject.put("shazamID", item.shazamID)
    //     itemJsonObject.put("appleMusicID", item.appleMusicID)
    //     item.appleMusicURL?.let {
    //         itemJsonObject.put("appleMusicURL", it.toURI().toString())
    //     }
    //     item.artworkURL?.let {
    //         itemJsonObject.put("artworkURL", it.toURI().toString())
    //     }
    //     itemJsonObject.put("artist", item.artist)
    //     itemJsonObject.put("matchOffset", item.matchOffsetInMs)
    //     itemJsonObject.put("explicitContent", item.explicitContent)
    //     item.videoURL?.let {
    //         itemJsonObject.put("videoURL", it.toURI().toString())
    //     }
    //     item.webURL?.let {
    //         itemJsonObject.put("webURL", it.toURI().toString())
    //     }
    //     itemJsonObject.put("genres", JSONArray(item.genres))
    //     //itemJsonObject.put("isrc", item.isrc)
    //     itemJsonArray.put(itemJsonObject)
    // }
    // return itemJsonArray.toString()
    //}