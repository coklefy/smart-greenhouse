#ifndef _IRRIGATION_TASK_
#define _IRRIGATION_TASK_

#include "ConcurrentTask.h"
#include "ServoTimer2.h"
#include "Config.h"


/* Class which manages movement detection */
class IrrigationTask: public ConcurrentTask {

    public:
        IrrigationTask (int servoPin, SharedSpace* sp);
        void init(int period);
        void tick();

    protected:
      String humidity;
      MsgServiceBT* msgService;
      ServoTimer2 servo;
      int manual_range, automatic_range, irrigationTime;
      int servoPin;

      LightExt* red_led1;



    /*
        STATES INFO:
        WAITING: The ports are in a waiting state, wairing for an automatic or manual comand to be opened
        MANUAL_IRRIGATION: The ports should be opened in a manual mode through SmartGreenHouse ANDROID APP
        AUTOMATIC_IRRIGATION: The ports should be opened in a automatic mode.
        ERROR: The ports will be in an error state if there is detected any problem during the irrigation.
    */
    enum { WAITING, MANUAL_IRRIGATION, AUTOMATIC_IRRIGATION, IRRIGATING, ERROR} irrigation_state;

};

#endif
