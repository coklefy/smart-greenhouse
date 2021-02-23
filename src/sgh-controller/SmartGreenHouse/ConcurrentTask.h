#ifndef _CONCURRENT_TASK_
#define _CONCURRENT_TASK_

#include "Task.h"
#include "SharedSpace.h"

/* Class which refers to tasks with shared variables and objects */
class ConcurrentTask: public Task {
    protected:
        SharedSpace* sp;   // object containing all variables and objects shared with concurrent tasks
};

#endif
