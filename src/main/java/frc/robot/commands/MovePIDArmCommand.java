/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.subsystems.PIDArm;

public class MovePIDArmCommand extends Command
{
	double startAngle;
	double targetAngle;
	double currentAngle;
	double speed = 50.0;
	boolean onTarget;
	boolean climb;
	boolean usePID;

	public MovePIDArmCommand( double targetAngle, boolean climb, boolean usePID )
	{
		this.targetAngle = targetAngle;
		this.usePID = usePID;
		this.climb = climb;

		// Use requires() here to declare subsystem dependencies
		requires( Robot.pidArm );
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize()
	{
		startAngle = Robot.pidArm.potentiometer.get();
		currentAngle = startAngle;
		onTarget = false;

	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute()
	{

		//if you are not climbing use PID
		if ( !climb )
		{

			currentAngle = Robot.pidArm.potentiometer.get();

			// calculates the difference between the startSetpoint and targetSetpoint.
			// If startAngle < targetAngle, set speed as positive (keep going in this
			// direction)
			// else, set speed as negative (reverse direction)
			double delta = ( startAngle < targetAngle ) ? speed : -speed;

			// Tells the program when the setpoint has been achieved.
			// if startAngle < targetAngle, set onTarget to be true (end program) when the
			// angle
			// is greater than or equal to the target
			// else (if startAngle > targetAngle) set onTarget to be true when the angle is
			// smaller than or equal to target
			onTarget = ( startAngle < targetAngle ) ? currentAngle >= targetAngle : currentAngle <= targetAngle;

			// if not yet onTarget, set the next setpoint/angle to be the currentAngle +
			// delta
			// else, set the next setpoint to be the targetAngle (in case it overshoots)
			if ( !onTarget )
				currentAngle += delta;
			else
				currentAngle = targetAngle;

			// set the next location to move the arm motor such that the motion is
			// continuous
			Robot.pidArm.setSetpoint( currentAngle );
		}
		else
		{
			Robot.pidArm.manualMove( -1 );

		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished()
	{
		return Robot.pidArm.onTarget() && onTarget;
	}

	// Called once after isFinished returns true
	@Override
	protected void end()
	{
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	@Override
	protected void interrupted()
	{
	}
}
