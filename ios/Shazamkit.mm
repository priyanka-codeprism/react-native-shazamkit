//
//  Shazamkit.mm
//  Created by Priyanka Pradhan on 06/11/24.
//

#import <React/RCTBridgeModule.h>

@interface RCT_EXTERN_MODULE(ShazamRecognizer, NSObject)

RCT_EXTERN_METHOD(startRecognition:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
//RCT_EXTERN_METHOD(startRecognition)
RCT_EXTERN_METHOD(stopRecognition)
RCT_EXTERN_METHOD(isAvailable:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)
RCT_EXTERN_METHOD(addToShazamLibrary:(RCTPromiseResolveBlock)resolve reject:(RCTPromiseRejectBlock)reject)

+ (BOOL)requiresMainQueueSetup
{
  return NO;
}

@end
