/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap{
  //used to map out the pins aka ports used by different parts of the robot
  //PWM
  public static final int[] LEFT_MOTORS = {1, 2, 3};
  public static final int[] RIGHT_MOTORS = {4, 5}; // note two motors are connected to one port
  public static final int ARM_POTENTIOMETER = 0;
  public static final int[] ARM_MOTORS = {8, 9};

  //DIO
  public static final int[] LEFT_ENCODERS = {0,1};
  public static final int[] RIGHT_ENCODERS = {2,3};

  //USB
  public static final int controllerPort = 0;
}
