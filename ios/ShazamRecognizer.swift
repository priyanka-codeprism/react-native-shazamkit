//
//  ShazamRecognizer.swift
//  KablamApp
//
//  Created by Priyanka Pradhan on 7/11/24.
//

import Foundation
import AVKit
import ShazamKit
import React

typealias RCTPromiseResolveBlock = (_ result: Any?) -> Void
typealias RCTPromiseRejectBlock = (_ code: String, _ message: String, _ error: Error?) -> Void
@available(iOS 15.0, *)
@objc(ShazamRecognizer)
class ShazamRecognizer: NSObject {
    private var viewModel = ShazamKitModule()
    private var pendingPromise: (resolve: RCTPromiseResolveBlock, reject: RCTPromiseRejectBlock)?

    
    @objc
    func startRecognition(_ resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
     viewModel.startRecognition(resolve, reject: reject)
    }
    
    @objc
    func stopRecognition() {
        viewModel.stopRecognition()
    }

    @objc
    func isAvailable(_ resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        viewModel.isAvailable(resolve, reject: reject)
    }
    
    @objc
    func addToShazamLibrary(_ resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) {
        viewModel.addToShazamLibrary(resolve, reject: reject)
    }

    // Required method for emitting events to React Native
    @objc static func requiresMainQueueSetup() -> Bool {
        return true
    }
}
