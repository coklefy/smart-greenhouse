#ifndef _SHARED_SPACE_
#define _SHARED_SPACE_

#include "Led.h"
#include "Sonar.h"
#include "SoftwareSerial.h"
#include "MsgService.h"


/* Class which contains all variables and objects with concurrent tasks */
class SharedSpace {

    public:
        SharedSpace();
        void init();

        void setIsUserNear(bool value);
        bool isUserNear();
        void setManualMode(bool value);
        bool isManualModeON();
        void setIrrigate(bool value);
        bool shouldIrrigate();
        int getAutomaticRange();
        void setAutomaticRange(int value);
        int getManualRange();
        void setManualRange(int value);
        String getUmidita();
        void setUmidita(String value);
        void setLastTimeMsgBT(int value);
        int getLastTimeMsgBT();
        MsgServiceBT* getMsgService();


    private:
      bool userNear, manualModeON, canIrrigate;
      int automatic_range, manual_range, lastTimeMsgBluetooth;
      String umidita;
      MsgServiceBT* msgService;


};

#endif
