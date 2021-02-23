#ifndef __TASK__
#define __TASK__

/**
 * Class used define the structure of a task.
 */
class Task {
 protected:
  int myPeriod;
  int timeElapsed;

public:
  virtual void init(int period){
    myPeriod = period;
    timeElapsed = 0;
  }

  virtual void tick() = 0;

//this method will make the task work only if its internal period is elapsed.
  bool updateAndCheckTime(int basePeriod){
    timeElapsed += basePeriod;
    if (timeElapsed >= myPeriod){
      timeElapsed = 0;
      return true;
    } else {
      return false;
    }
  }

};

#endif
