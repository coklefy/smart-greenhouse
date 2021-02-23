#ifndef _CONFING_
#define _CONFING_

//define the pin of each sensor
#define RED_LED1_PIN 13
#define GREEN_LED2_PIN 12
#define GREEN_LED3_PIN 11
#define ECHO_SONAR_PIN 8
#define TRIGGER_SONAR_PIN 7
#define TX 0
#define RX 1
#define SERVO_PIN 3

//Define period of each task. in this case all the tasks have the same period
#define WAIT_SCHED 50

#define DIST  0.30   // Engagement distance
#define Umin   30    // min engagement time
#define DeltaU  5    // max time with no engament
#define Tmax  5      // max time of erogation


//define the max irrigation time in auto mode, and the max time after which the bluetooth will be disconnected
#define IRRIGATION_TIME_EXPIRED 5000
#define BLUETOOTH_TIME_EXPIRED 2000

#endif
