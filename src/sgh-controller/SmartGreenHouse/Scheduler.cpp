#include "Scheduler.h"

/**
 * Class used to manage and define the scheduler. It will be used to run all the task with a defined priority. In that case the priority is the order the tasks are inserted.
 */

//initialize the scheduler with the base period.The cheduler will try to execute all the tasks each time this time is elapsed, but the task will work depending of their internal period. The period must be the greatest common divisor between all the periods of the tasks.
void Scheduler::init(int basePeriod){
  this->basePeriod = basePeriod;
  timer.setupPeriod(basePeriod);
  nTasks = 0;
}

//function used to add a task to the scheduler onlly if it is not already full.
bool Scheduler::addTask(Task* task){
  if (nTasks < MAX_TASKS-1){
    taskList[nTasks] = task;
    nTasks++;
    return true;
  } else {
    return false;
  }
}

//start scheduling
void Scheduler::schedule(){
  timer.waitForNextTick();
  for (int i = 0; i < nTasks; i++){
    //With this condition, each task will be executed only if its internal period is ended.
    if (taskList[i]->updateAndCheckTime(basePeriod)){
      taskList[i]->tick();
    }
  }
}

