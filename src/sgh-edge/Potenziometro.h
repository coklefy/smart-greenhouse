#ifndef __POTENZIOMETRO__
#define __POTENZIOMETRO__
#include "Arduino.h"

/**
 * Class used to manage the potentiometer, used to detect the sugar level.
 */
class Potenziometro {
  public:
    Potenziometro(String pin);
    float getValue();
  protected:
    String pin;
};

#endif
