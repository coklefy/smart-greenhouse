#ifndef _DISTANCE_CONTROLLER_TASK
#define _DISTANCE_CONTROLLER_TASK

#include "ProximitySensor.h"
#include "ConcurrentTask.h"

/* Class which manages movement detection */
class DistanceControllerTask: public ConcurrentTask {

    public:
        DistanceControllerTask(int trigPin, int echoPin, SharedSpace* sp);
        void init(int period);
        void tick();

    protected:
        int trigPin, echoPin;
        Sonar *sonar;
        int time_to_wait;

    /*
        STATES INFO:

        NEAR_MOVEMENT: motion detected in a close distance
        FAR_MOVEMENT: motion detected in a large distance
    */
    enum { NEAR_MOVEMENT, FAR_MOVEMENT} movement_state;

};

#endif
