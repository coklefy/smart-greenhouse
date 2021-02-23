#include "Arduino.h"
#include "MsgServiceBT.h"


MsgServiceBT::MsgServiceBT(int rxPin, int txPin){
  channel = new SoftwareSerial(rxPin, txPin);
}

void MsgServiceBT::init(){
  content.reserve(256);
  channel->begin(9600);
}

bool MsgServiceBT::sendMsg(Msg msg){
  channel->println(msg.getContent());
}

bool MsgServiceBT::isMsgAvailable(){
  return channel->available();
}

Msg* MsgServiceBT::receiveMsg(){
  while (channel->available()) {
    char ch = (char) channel->read();
    if (ch == '\n'){
      Msg* msg = new Msg(content);
      content = "";
      return msg;
    } else {
      content += ch;
    }
  }

   if(!channel->available())
  {
      Msg* msg = new Msg(content);
      content = "";
      return msg;
  }

  return NULL;
}

