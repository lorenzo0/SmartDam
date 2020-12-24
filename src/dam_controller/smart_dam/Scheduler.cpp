#include "Scheduler.h"
#include "MsgService.h"
#include <TimerOne.h>

volatile boolean timerFlagScheduler;

void timerHandler(void){
  timerFlagScheduler = true;
}

void Scheduler::init(int basePeriod){
  this->basePeriod = basePeriod;
  timerFlagScheduler = false;
 
  period = 1000l*basePeriod;
  Timer1.initialize(period);
  Timer1.attachInterrupt(timerHandler);
  nTasks = 0;
  i = 0;
  indexCurrentTaskActive = 0;
}

bool Scheduler::addTask(Task* task){
  if (nTasks < MAX_TASKS-1){
    taskList[nTasks] = task;
    nTasks++;
    return true;
  } else {
    return false; 
  }
}

/* 
 *  Nella politica di scheduling vengono gestite le interruzioni.
 *  Queste, sono sempre attive ma non sempre prese in considerazione. 
 *  Facendo un esempio forviante, non si può interrompere l'esecuzione del task 
 *  (interazione con bottone BUTTON_STOP) se nel task di IDLE.
 *  
 *  L'interruzione del bottone STOP viene gestita solo nel caso in cui in esecuzione 
 *  è la task numero 3 - running.
 *  L'interruzione del bottone START viene gestita solo nel caso in cui in esecuzione 
 *  è la task numero 0 - idle.
 *  L'interruzione del sensore PIR viene gestita solo nel caso in cui in esecuzione 
 *  è la task numero 2 - sleep.
 *  
 *  Infine, viene presa in considerazione la casistica in cui il task completi la sua
 *  totale esecuzione (data dalle costanti definite in design time), quindi entri in uno stato di completed.
 *  
 *  La variabile 'i' è stata introdotta per rendere il codice più leggibile, sostituendo 'indexCurrentTaskActive'
*/
void Scheduler::schedule(){
  while (!timerFlagScheduler){}
  timerFlagScheduler = false;
  i = indexCurrentTaskActive;
   if (taskList[i] -> updateAndCheckTime(basePeriod))
      taskList[i] -> tick();
      
   if(taskList[i]->isCompleted()){
      Scheduler::redirectTask(taskList[i] -> getNextTask());
      taskList[i]->setCompleted(false);
  }

  
}

/*
 * In questa procedura viene gestita la corrispondenza del prossimo task che dovrà
 * essere mandato in esecuzione dallo scheduler. Ogni task imposta il suo nextTask in base a
 * come è stato terminato il task (completed/interrupted). 
 * Le corrispondenze prima menzionate sono:
 *    - 0 è idle
 *    - 1 è error
 *    - 2 sleep
 *    - 3 running
 *    
 * Queste, vengono definite in base all'inserimento dei task nell'oggetto task
 * nel file smart_exp.ino
 */

void Scheduler::redirectTask(int nextState){
  switch(nextState){
    case 0:
      setIndexCurrentTaskActive(0);
      MsgService.sendMsg("IDLE");
      break;
    case 1:
      setIndexCurrentTaskActive(1);
      MsgService.sendMsg("ERROR");
      break;
     case 2:
      setIndexCurrentTaskActive(2);
      MsgService.sendMsg("SLEEP");
      break;
  }
}

void Scheduler::setIndexCurrentTaskActive(int index){
  this->indexCurrentTaskActive = index;
}
