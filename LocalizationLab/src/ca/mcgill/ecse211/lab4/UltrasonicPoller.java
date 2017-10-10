package ca.mcgill.ecse211.lab4;

import lejos.robotics.SampleProvider;

/**
 * Control of the wall follower is applied periodically by the UltrasonicPoller thread. The while
 * loop at the bottom executes in a loop. Assuming that the us.fetchSample, and cont.processUSData
 * methods operate in about 20mS, and that the thread sleeps for 50 mS at the end of each loop, then
 * one cycle through the loop is approximately 70 mS. This corresponds to a sampling rate of 1/70mS
 * or about 14 Hz.
 */
public class UltrasonicPoller extends Thread {
  private SampleProvider us;
  private Navigation navigation;
  private UltrasonicLocalizer ultrasonicLocalizer;
  private int distance;
  private float[] usData;
  
  private Object lock; /*lock object for mutual exclusion*/
  

  public UltrasonicPoller(SampleProvider us, float[] usData) {
    this.us = us;
    this.usData = usData;
    lock = new Object();
  }

  /*
   * Sensors now return floats using a uniform protocol. Need to convert US result to an integer
   * [0,255] (non-Javadoc)
   * 
   * @see java.lang.Thread#run()
   */
  public void run() {
    int distance;
    while (true) {
      us.fetchSample(usData, 0); // acquire data
      distance = (int) (usData[0] * 100.0); // extract from buffer, cast to int
      
      synchronized (lock) {
    	this.distance = distance;
      }
      //TODO:
//      //set the distance in the appropriate classes
//      navigation.setDistance(distance);
//      ultrasonicLocalizer.setDistance(distance);
      
      try {
        Thread.sleep(50);
      } catch (Exception e) {
      } // Poor man's timed sampling
    }
  }
  
  public int getDistance(){
	int result;

	  synchronized (lock) {
	    result = distance;
	  }

	return result;
  }
  
  
  //TODO:
//  public void addNavigation(Navigation aNavigation) {
//	  navigation = aNavigation;
//  }
//  
//  public void addUltrasonicLocalizer(UltrasonicLocalizer aUSLocalizer) {
//	  ultrasonicLocalizer = aUSLocalizer;
//  }

}
