#include "DistanceControllerTask.h"
#include "Sonar.h"
#include "Arduino.h"
#include "Config.h"
#include "SharedSpace.h"

DistanceControllerTask::DistanceControllerTask(int trigPin, int echoPin, SharedSpace* sp): trigPin(trigPin) , echoPin(echoPin) {
    this->sp = sp;

}

void DistanceControllerTask::init(int period) {
     Task::init(period);
     sonar = new Sonar(echoPin, trigPin);
     movement_state = FAR_MOVEMENT;
}

void DistanceControllerTask::tick(){

    float distance = sonar->getDistance();
/* After getting the distance from sonar, we check if this distance is in our interval. In this form we are going to change
   the state of the task. If distance < DIST, it means that there is a near movement. The same thing for other case too. */
    if(distance <= DIST){
      Serial.print("NEAR ");
      Serial.println(distance);
      movement_state = NEAR_MOVEMENT;
    } else if(distance > DIST){
        Serial.print("FAR ");
        Serial.println(distance);
      movement_state = FAR_MOVEMENT;
    }


    switch (movement_state) {
/* In the distance is in the interval we are going to write on the SharedSpace that the object is near. In this form other tasks can
   enter in this sharedSpace and get the information. */
      case NEAR_MOVEMENT:
          sp->setIsUserNear(true);
        break;

      case FAR_MOVEMENT:
          sp->setIsUserNear(false);
        break;
    }

}
