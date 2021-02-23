#ifndef _DETECT_IRRIGATION_MODE_TASK_
#define _DETECT_IRRIGATION_MODE_TASK_

#include "ConcurrentTask.h"
#include "MsgServiceBT.h"
#include "MsgService.h"
#include "SoftwareSerial.h"
#include "LedExt.h"
#include "Led.h"
#include "SharedSpace.h"


/* Class which manages movement detection */
class DetectIrrigationModeTask: public ConcurrentTask {

    public:
        DetectIrrigationModeTask(int red_led1_pin, int green_led2_pin, int  green_led3_pin,int trigPin, int echoPin,  SharedSpace* sp);
        void init(int period);
        void tick();

    protected:
      int red_led1_pin, green_led2_pin, green_led3_pin, trigPin, echoPin;
      int lastTimeMsgBluetooth;
      Light* green_led2;
      Light* green_led3;
      MsgServiceBT* msgService;


    /*
        STATES INFO:
        START: if the state at the beggining. It passes to AUTOMATIC_MODE if MANUAL_MODE is false
        AUTOMATIC_MODE: change the humidity value in automitic mode
        MANUAL_MODE: change the humidity value in manual mode. This can be done though SmartGreenHouse ANDROID APP
    */
    enum { START, AUTOMATIC_MODE, MANUAL_MODE} irrigation_mode;

};

#endif
