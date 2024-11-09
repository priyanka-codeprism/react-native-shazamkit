// //
// //  ShazamKitModule.swift
// //
// //  Created by Priyanka Pradhan on 14/10/24.
// //


import Foundation
import ShazamKit
import AVFoundation
import React
import AVKit
import Combine

@available(iOS 15.0, *)
class ShazamKitModule: NSObject, SHSessionDelegate {

    private let session = SHSession()
    private var audioEngine = AVAudioEngine()
    private var pendingPromise: (resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock)?
    private var isListening = false

    private var latestResults = [SHMediaItem]()

    override init() {
        super.init()
        session.delegate = self
    }

    func configureAudioSession() throws {
        let audioSession = AVAudioSession.sharedInstance()
        try audioSession.setCategory(.playAndRecord, mode: .default, options: [])
        try audioSession.setActive(true)
     }


    private func generateSignature() {
      
        let inputNode = audioEngine.inputNode
        //print("Input Node: ", inputNode)
        let recordingFormat = inputNode.outputFormat(forBus: .zero)
        inputNode.installTap(onBus: .zero, bufferSize: 4096, format: recordingFormat) { [weak session] buffer, _ in
         // print("Buffer received, processing...")
          let channelDataArray = buffer.floatChannelData
                  if let channelData = channelDataArray {
                    let channelDataValue = channelData.pointee
                      let frameLength = Int(buffer.frameLength)

                    var isSilent = true
                      for frame in 0..<frameLength {
                          let value = channelDataValue[frame]
                          print("Value: ", value)
                          if value != 0.0 {
                              isSilent = false
                              break
                          }
                      }
                    
                      if isSilent {
                          //print("Microphone is silent!!!")
                      } else {
                          print("Microphone is working!!!",buffer.frameLength)
                      }
                  }
           
            if buffer.frameLength > 0 {
            //print("Frame length: \(buffer.frameLength)")
            
            self.session.matchStreamingBuffer(buffer, at: nil)
        } else {
            print("Empty buffer, nothing to match")
        }
        }
    }
    
    // MARK: - Start Recognition
    func startRecognition(_ resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        //print("Starting recognition")
         pendingPromise = (resolve, reject)
    do {
        try configureAudioSession()
    } catch {
        print("Failed to configure audio session: \(error.localizedDescription)")
        return
    }

        if audioEngine != nil, audioEngine.isRunning {
           // print("AudioEngine already running, stopping recognition")
            stopRecognition()
            return
        }
        generateSignature()
        audioEngine.prepare()
        do {
            try audioEngine.start()
        } catch {
            NSLog("Error starting audio engine: \(error.localizedDescription)")
            return
        }
    }
    
    // MARK: - Stop Recognition
    func stopRecognition() {
        audioEngine.stop()
        audioEngine.inputNode.removeTap(onBus: .zero)
        print("Stopped recognition")
        //audioEngine = nil
    }

    // MARK: - Check Availability
    func isAvailable(_ resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        let available = SHSession.self != nil // Check if ShazamKit is available
         //let available = SHSession.self  // Check if ShazamKit is available
         print("ShazamKit is available: \(available)")
         resolve(available)
       
    }

    // MARK: - Add to Shazam Library
    func addToShazamLibrary(_ resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        print("Adding to Shazam library")
        if latestResults.isEmpty {
            resolve(["success": false])
            return
        }
        
        SHMediaLibrary.default.add(latestResults) { [weak self] error in
            if let error = error {
                NSLog("Error adding to Shazam library: \(error.localizedDescription)")
                resolve(["success": false])
                return
            }

            self?.latestResults.removeAll()
            resolve(["success": true])
        }
    }

    // MARK: - SHSessionDelegate Methods
    func session(_ session: SHSession, didFind match: SHMatch) {
        print("Match found: \(match.mediaItems)")
        if let promise = pendingPromise {
            let items = match.mediaItems.map { item in
                [
                    "title": item.title ?? "Unknown Title",
                    "artist": item.artist ?? "Unknown Artist",
                    "shazamID": item.shazamID ?? "",
                    "appleMusicID": item.appleMusicID ?? "",
                    "appleMusicURL": item.appleMusicURL?.absoluteString ?? "",
                    "artworkURL": item.artworkURL?.absoluteString ?? "",
                    "genres": item.genres,
                    "webURL": item.webURL?.absoluteString ?? "",
                    "subtitle": item.subtitle ?? "",
                    "videoURL": item.videoURL?.absoluteString ?? "",
                    "explicitContent": item.explicitContent,
                    "matchOffset": item.matchOffset
                ] as [String: Any]
            }
            
            pendingPromise = nil
            latestResults = match.mediaItems
            promise.resolve(items)

        // Stop listening after the match is found
        stopRecognition()
        }
    }

    func session(_ session: SHSession, didNotFind match: SHSignature) {
        print("No match found")
        if let promise = pendingPromise {
            pendingPromise = nil
            promise.reject("NO_MATCH", "No match found", nil)
        }
    }
}
