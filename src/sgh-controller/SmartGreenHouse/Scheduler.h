#ifndef __SCHEDULER__
#define __SCHEDULER__

#include "Timer.h"
#include "Task.h"

//define the max number of tasks.
#define MAX_TASKS 10

/**
 * Class used to manage and define the scheduler. It will be used to run all the task with a defined priority. In that case the priority is the order the tasks are inserted.
 */
class Scheduler {

  int basePeriod;
  int nTasks;
  Task* taskList[MAX_TASKS];
  Timer timer;

public:
  void init(int basePeriod);
  virtual bool addTask(Task* task);
  virtual void schedule();
};

#endif
