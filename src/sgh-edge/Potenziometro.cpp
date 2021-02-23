#include "Potenziometro.h"
#include <Arduino.h>

/**
 * Class used to manage the potentiometer, used to detect the sugar level.
 */
Potenziometro::Potenziometro(String pin) {
  this->pin = pin;
}

float Potenziometro::getValue() {
  float val = analogRead(A0);    // read the value to pin A0
  return val;
}
