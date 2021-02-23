#include "SharedSpace.h"
#include "Config.h"
#include "MsgServiceBT.h"
#include "MsgService.h"

SharedSpace::SharedSpace(){}

void SharedSpace::init() {
   canIrrigate = false;

   userNear = false;
   manualModeON = false;
   umidita = "0";
   automatic_range = 0;
   manual_range = 255;
   lastTimeMsgBluetooth = 0;

   msgService = new MsgServiceBT(TX, RX);
   msgService->init();
   MsgService.init();
}

void SharedSpace::setIsUserNear(bool value){
  this->userNear = value;
}

bool SharedSpace::isUserNear(){
  return this->userNear;
}

void SharedSpace::setManualMode(bool value){
  this->manualModeON = value;
}

bool SharedSpace::isManualModeON(){
  return this->manualModeON;
}

void SharedSpace::setAutomaticRange(int value){
  this->automatic_range = value;
}

int SharedSpace::getAutomaticRange(){
  return this->automatic_range;
}


void SharedSpace::setManualRange(int value){
  this->manual_range = value;
}

int SharedSpace::getManualRange(){
  return this->manual_range;
}

void SharedSpace::setIrrigate(bool value){
  canIrrigate = value;
}

bool SharedSpace::shouldIrrigate(){
  return this->canIrrigate;
}

void SharedSpace::setUmidita(String value){
  this->umidita = value;
}

void SharedSpace::setLastTimeMsgBT(int value){
  this->lastTimeMsgBluetooth = value;
}

int SharedSpace::getLastTimeMsgBT(){
  return this->lastTimeMsgBluetooth;
}

String SharedSpace::getUmidita(){
  return this->umidita;
}

MsgServiceBT* SharedSpace::getMsgService(){
  return msgService;
}
