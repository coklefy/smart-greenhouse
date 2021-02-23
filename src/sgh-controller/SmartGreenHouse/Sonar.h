#ifndef __SONAR__
#define __SONAR__

#include "ProximitySensor.h"

class Sonar: public ProximitySensor {

public:
  Sonar(int echoPin, int trigPin);
  float getDistance();

private:
    const float vs = 331.5 + 0.6*20;
    int echoPin, trigPin;
};

#endif
