package ca.mcgill.ecse211.wallfollowing;

import lejos.hardware.motor.*;

public class BangBangController implements UltrasonicController {

  private final int bandCenter; //constant offset from the wall
  private final int bandwidth; //width of dead band (allowed error (?))
  private final int motorLow;
  private final int motorHigh;
  private int distance; //Distance from the wall(?)
  private int distError; //
  private static final int DELTASPD = 150;

  public BangBangController(int bandCenter, int bandwidth, int motorLow, int motorHigh) {
    // Default Constructor
    this.bandCenter = bandCenter;
    this.bandwidth = bandwidth;
    this.motorLow = motorLow;
    this.motorHigh = motorHigh;
    WallFollowingLab.leftMotor.setSpeed(motorHigh); // Start robot moving forward
    WallFollowingLab.rightMotor.setSpeed(motorHigh);
    WallFollowingLab.leftMotor.backward();
    WallFollowingLab.rightMotor.backward();
  }

  @Override
  public void processUSData(int distance) {
    this.distance = distance;
    distError = bandCenter - this.distance;
    
    if (Math.abs(distError) <= bandwidth){
    	WallFollowingLab.leftMotor.setSpeed(motorHigh); // Start robot moving forward
    	WallFollowingLab.rightMotor.setSpeed(motorHigh);
    	WallFollowingLab.leftMotor.backward();
    	WallFollowingLab.rightMotor.backward();
    }
    
    else if(distError > 0){ //turn away from the wall
    	WallFollowingLab.leftMotor.setSpeed(motorHigh + DELTASPD);
    	WallFollowingLab.rightMotor.setSpeed(motorLow);
    	WallFollowingLab.leftMotor.backward();
    	WallFollowingLab.rightMotor.forward();
    }
    
    else if(distError < 0){ //turn towards the wall
    	WallFollowingLab.leftMotor.setSpeed(motorLow);
    	WallFollowingLab.rightMotor.setSpeed(motorHigh + DELTASPD);
    	WallFollowingLab.leftMotor.forward();
    	WallFollowingLab.rightMotor.backward();
    }
  }

  @Override
  public int readUSDistance() {
    return this.distance;
  }
}
