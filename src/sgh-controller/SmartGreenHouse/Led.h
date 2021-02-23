#ifndef __LED__
#define __LED__

#include "Light.h"

/**
 * Class used to manage a led
 */
class Led: public Light {
public:
  Led(int pin);
  void switchOn();
  void switchOff();
protected:
  int pin;
};

#endif
