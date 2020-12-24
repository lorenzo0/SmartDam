#ifndef __SONAR__
#define __SONAR__

class Sonar{
  
public: 

  Sonar(int pinSonarEcho, int pinSonarTrig);
  float tick();
  float getDistance();

private:

  int pinSonarEcho;
  int pinSonarTrig;
  
  //si suppone che 20 sia la temperatura
  const double vs = 331.45 + 0.62*20;
  
};

#endif
