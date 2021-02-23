# smart-greenhouse

> Project of course: Embedded systems and IoT ([unibo](https://www.unibo.it/it))


Building an integrated embedded system that represent a simplified version of a smart greenhouse. The task of the smart greenhouse is automated irrigation (of a certain soil or plant) by implementing a strategy that holds account of the perceived humidity, with the ability to control and intervene manually via the mobile app. The system is consisting of 5 parts (subsystems):

- **GreenHouse Server (PC)**: it contains the logic that defines and implements the irrigation strategy
- **GreenHouse Controller (Arduino)**: allows you to control the opening and closing of the sprinklers
- **GreenHouse Edge (ESP)**: allows you to perceive the humidity of the soil
- **GreenHouse Mobile App (Android)**: allows manual control of the greenhouse
- **GreenHouse Front End (PC)**: Front end for visualization / observation / data analysis
![*System diagram*](https://github.com/coklefy/smart-greenhouse/blob/main/doc/Schemi%20circuiti/system.jpg?raw=true)

## Design
Diagram showing the ARDUINO circuit implementation
![Arduino circuit](https://github.com/coklefy/smart-greenhouse/blob/main/doc/Schemi%20circuiti/SGH_ARDUINO.png?raw=true)

Diagram showing the ESPRUINO circuit implementation
![Espruino circuit](https://github.com/coklefy/smart-greenhouse/blob/main/doc/Schemi%20circuiti/ESP.png?raw=true)


