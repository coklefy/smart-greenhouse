#ifndef __LIGHT__
#define __LIGHT__

/**
 * Class used to manage a light. Will be used by Led class.
 */
class Light {
public:
  virtual void switchOn() = 0;
  virtual void switchOff() = 0;
};

#endif
