#include "Scheduler.h"
#include "ConcurrentTask.h"
#include "MsgService.h"
#include "Config.h"
#include "SharedSpace.h"
#include "DistanceControllerTask.h"
#include "DetectIrrigationModeTask.h"
#include "IrrigationTask.h"
#include <avr/sleep.h>

Scheduler sched;
SharedSpace* sp;

void setup() {
	  Serial.begin(9600);
   
    pinMode(RED_LED1_PIN, OUTPUT);
    pinMode(GREEN_LED2_PIN, OUTPUT);
    pinMode(GREEN_LED3_PIN, OUTPUT);

    pinMode(ECHO_SONAR_PIN, INPUT);
    pinMode(TRIGGER_SONAR_PIN, OUTPUT);
		
		sp = new SharedSpace();
    sp->init();

		// Set Scheduler tick each 50ms
	  sched.init(WAIT_SCHED);

		// Create tasks
		Task* distanceControllerTask = new DistanceControllerTask(TRIGGER_SONAR_PIN, ECHO_SONAR_PIN, sp);
		distanceControllerTask->init(WAIT_SCHED);

		Task* detectIrrigationModeTask = new DetectIrrigationModeTask(RED_LED1_PIN, GREEN_LED2_PIN, GREEN_LED3_PIN, ECHO_SONAR_PIN, TRIGGER_SONAR_PIN, sp);
		detectIrrigationModeTask->init(WAIT_SCHED);

    Task* irrigationTask = new IrrigationTask(SERVO_PIN, sp);
    irrigationTask->init(WAIT_SCHED);

    sched.addTask(distanceControllerTask);
    sched.addTask(detectIrrigationModeTask);
    sched.addTask(irrigationTask);


}

void loop() {
	// Start scheduler
  sched.schedule();
}
