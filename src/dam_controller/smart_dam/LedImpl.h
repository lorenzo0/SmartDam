#ifndef __LEDIMPL_
#define __LEDIMPL_

class LedImpl {
public:
  virtual void switchOn() = 0;
  virtual void switchOff() = 0;    
  virtual void blinking() = 0;
};

#endif
