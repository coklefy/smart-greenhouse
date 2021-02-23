#include "Sonar.h"
#include "Arduino.h"


/**
 * Class used to manage a sonar and detect the distance between a user and the system.
 */
Sonar::Sonar(int echoP, int trigP){
  this->trigPin = trigP;
  this->echoPin = echoP;
}

//the sonar send 3 signlas and read the comeback to define the distance.
float Sonar::getDistance() {
  digitalWrite(trigPin, LOW);
  delayMicroseconds(3);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(5);
  digitalWrite(trigPin, LOW);

  float tUS = pulseIn(echoPin, HIGH);
  float t = tUS / 1000.0 / 1000.0 / 2;
  float d = t * vs;
  return d;
}

