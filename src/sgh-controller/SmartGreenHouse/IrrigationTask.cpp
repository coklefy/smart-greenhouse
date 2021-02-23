#include "IrrigationTask.h"
#include "Arduino.h"
#include "Config.h"
#include "MsgServiceBT.h"
#include "MsgService.h"

IrrigationTask::IrrigationTask(int servoPin, SharedSpace* sp): servoPin(servoPin){
    this->sp = sp;
}

void IrrigationTask::init(int period) {
     Task::init(period);
     msgService = sp->getMsgService();
     msgService->init();
     manual_range = 255;
     automatic_range = 255;
     humidity = "0";
     irrigationTime = 0;
     irrigation_state = WAITING;
     /*Irrigation Led */
     red_led1 = new LedExt(red_led1_pin, 128);


}

void IrrigationTask::tick(){

    if(!sp->isManualModeON() && sp->shouldIrrigate()){
      irrigation_state = AUTOMATIC_IRRIGATION;
    } else if( sp->isManualModeON() && sp->shouldIrrigate()){
      irrigation_state = MANUAL_IRRIGATION;
    }

    switch (irrigation_state){

      case WAITING:
            sp->setLastTimeMsgBT(0);
            servo.detach();
          break;

      case AUTOMATIC_IRRIGATION:
            servo.attach(servoPin);
            servo.write(1500);
            irrigationTime += myPeriod;

            red_led1->setIntensity(sp->getAutomaticRange());

            // Check if the irrigation time expired
            if(irrigationTime >= IRRIGATION_TIME_EXPIRED){
              MsgService.sendMsg("StopT");
              irrigation_state = WAITING;
            }
            //check if explicit asking for stop
            if (MsgService.isMsgAvailable()) {
              Msg* msg = MsgService.receiveMsg();
              if (msg->getContent() == "Stop"){
                  MsgService.sendMsg("Stop");
                  msgService->sendMsg(Msg("Stop\n"));
                  irrigation_state = WAITING;
              }
              delete msg;
            }
          break;

      case MANUAL_IRRIGATION:
            servo.write(1500);

            red_led1->setIntensity(sp->getManualRange());

            //if the user is too far while irrigating, goes back to automatic.
            if (!sp->isUserNear()) {
                MsgService.sendMsg("Stop");
                msgService->sendMsg(Msg("Stop\n"));
                sp->setManualMode(false);
                irrigation_state = WAITING;
            }

            //if no message is available for more than 5s, we suppose bluetooth it's not connected anymore.
            if (msgService->isMsgAvailable() <= 0){
                sp->setLastTimeMsgBT(sp->getLastTimeMsgBT() + myPeriod);
                if(sp->getLastTimeMsgBT() >= BLUETOOTH_TIME_EXPIRED){
                    sp->setManualMode(false);
                    irrigation_state = WAITING;
                }
            }

            //if a message is available while irrigating, check if it's Stop.
            if (msgService->isMsgAvailable() > 0) {
                sp->setLastTimeMsgBT(0);
                Msg* msg = msgService->receiveMsg();
                if (msg->getContent() == "2"){
                  MsgService.sendMsg("Stop");
                  msgService->sendMsg(Msg("Stop\n"));
                  sp->setManualMode(false);
                  irrigation_state = WAITING;

                 delete msg;
                }
            }
          break;
    }

}
