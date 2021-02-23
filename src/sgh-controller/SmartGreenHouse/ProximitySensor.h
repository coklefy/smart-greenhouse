#ifndef __PROXIMITYSENSOR__
#define __PROXIMITYSENSOR__

/**
 * Class used to define a proximity sensor. WIll be used from the sonar class.
 */
class ProximitySensor {

  public:
    virtual float getDistance() = 0;

};


#endif
