#include "SharingMethods.h"
#include "Arduino.h"

//  100:%=180:x
int SharingMethods::calculateOpeningServo(int newOpeningDAM){
  int currentOpening = (newOpeningDAM*180)/100;
  return currentOpening;
}
