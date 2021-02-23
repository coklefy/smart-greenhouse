#include "DetectIrrigationModeTask.h"
#include "Arduino.h"
#include "Config.h"
#include "MsgServiceBT.h"
#include "MsgService.h"

DetectIrrigationModeTask::DetectIrrigationModeTask(int red_led1_pin, int green_led2_pin, int green_led3_pin, int trigPin, int echoPin, SharedSpace* sp): red_led1_pin(red_led1_pin), green_led2_pin(green_led2_pin), green_led3_pin(green_led3_pin), trigPin(trigPin), echoPin(echoPin) {
    this->sp = sp;
}

void DetectIrrigationModeTask::init(int period) {
     Task::init(period);
     msgService = sp->getMsgService();
     msgService->init();
     //MsgService.init();
     green_led2 = new Led(green_led2_pin);
     green_led3 = new Led(green_led3_pin);
     irrigation_mode = AUTOMATIC_MODE; // at the beggining the irrigation mode will be AUTOMATIC
}

void DetectIrrigationModeTask::tick(){

/* Controll if the user is NEAR and the connection with Bluetooth has been established. If the conditions are fullfilled enter in MANUAL_MODE. Otherwise if
   if user is FAR or the time to wait bluetooth connection expired than pass to AUTOMATIC_MODE*/
    if( sp->isUserNear()  && !sp->shouldIrrigate() && msgService->isMsgAvailable()){
      // Check if the last mode wasn't MANUAL, in thos way we are sure we entered in MANUAL_MODE
      if(!sp->isManualModeON()){
        MsgService.sendMsg("ManIn");
        msgService->sendMsg(Msg("ManIn\n"));
      }
       irrigation_mode = MANUAL_MODE;
       sp->setManualMode(true);
     }else if( (!sp->isUserNear() || sp->getLastTimeMsgBT() >= BLUETOOTH_TIME_EXPIRED) && !sp->shouldIrrigate()){
       // Check if the last mode wasn't AUTOMATIC, in thos way we are sure we entered in AUTOMATIC_MODE
       if(sp->isManualModeON()){
         MsgService.sendMsg("ManOut");
         msgService->sendMsg(Msg("ManOut\n"));
       }
       irrigation_mode = AUTOMATIC_MODE;
       sp->setManualMode(false);
     }
     // IF you are in another state and receive a message via serial or bluetooth, it will delete the message
     if(!sp->isManualModeON() && !sp->shouldIrrigate()){
       if (msgService->isMsgAvailable()){
          Msg* msg = msgService->receiveMsg();
          delete msg;
       }
     }

     //if you are not in AUTO, you delete the message, but if it's humidity update you update the value
     if (irrigation_mode != AUTOMATIC_MODE && !sp->shouldIrrigate()) {
       if (MsgService.isMsgAvailable()){
         Msg* msg = MsgService.receiveMsg();
       if (msg->getContent().substring(0,7).equals("Umidita")){
        sp->setUmidita(msg->getContent().substring(8));
       }
      delete msg;
    }
}


    switch (irrigation_mode) {

      case AUTOMATIC_MODE:
            Serial.print("Automatic mode on");
            red_led1->switchOff();
            green_led2->switchOff();
            green_led3->switchOn();

            if(MsgService.isMsgAvailable()){
              Msg* msg = MsgService.receiveMsg();
              if(msg->getContent() == "Start0"){
                MsgService.sendMsg("Start");
                msgService->sendMsg(Msg("Start\n"));
                sp->setAutomaticRange(10);
                sp->setIrrigate(true);
              } else if(msg->getContent() == "Start1"){
                MsgService.sendMsg("Start");
                msgService->sendMsg(Msg("Start\n"));
                sp->setAutomaticRange(80);
                sp->setIrrigate(true);
              } else if(msg->getContent() == "Start2"){
                MsgService.sendMsg("Start");
                msgService->sendMsg(Msg("Start\n"));
                sp->setAutomaticRange(255);
                sp->setIrrigate(true);
              }  else if (msg->getContent().substring(0,7).equals("Umidita")){
                sp->setUmidita(msg->getContent().substring(8));
              }
              delete msg;
           }

          break;

      case MANUAL_MODE:
            Serial.print("Manual mode on");

            red_led1->switchOff();
            green_led2->switchOn();
            green_led3->switchOff();
            // If the message is available, we execute different actions depending on the message we receive.
            if(msgService->isMsgAvailable() > 0){
              // Each time a message is available ( bluetooth is connected), we send the humidity to the SmartGreenHouse ANDROID APP.
              Msg* humid_msg = new Msg(sp->getUmidita());
              msgService->sendMsg(*humid_msg);
              sp->setLastTimeMsgBT(0);
              Msg* msg = msgService->receiveMsg();
              if(msg->getContent() == "1"){
                MsgService.sendMsg("Start");
                msgService->sendMsg(Msg("Start\n"));
                sp->setIrrigate(true);
              } else if(msg->getContent() == "3"){
                sp->setManualRange(10);
                red_led1->setIntensity(10);
              } else if(msg->getContent() == "4"){
                sp->setManualRange(80);
                red_led1->setIntensity(80);
              } else if(msg->getContent() == "5"){
                sp->setManualRange(255);
                red_led1->setIntensity(255);
            }
            delete msg;
            }
          break;
    }
}
