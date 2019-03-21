/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;
import frc.robot.RobotMap;
import frc.robot.utils.DashboardVariable;

/**
 * Add your docs here.
 */
public class PIDArm extends PIDSubsystem
{
	/**
	 * Add your docs here.
	 */
	// PID values
	static final double kP = 0.005;
	static final double kI = 0.0001;
	static final double kD = 0.0005;

	public boolean pidEnabled = true;

	public PWMVictorSPX[] armMotors;

	// arm potentiometer
	public Potentiometer potentiometer;
	// channel, full rotation degrees, offset (starting angle)
	// Potentiometer does 10 spins so 3600 degrees

	public PIDArm()
	{
		super( "PID Arm", kP, kI, kD );
		potentiometer = new AnalogPotentiometer( RobotMap.ARM_POTENTIOMETER, 3600, 0 );

		// Insert a subsystem name and PID values here
		armMotors = new PWMVictorSPX[RobotMap.ARM_MOTORS.length];
		for ( int i = 0; i < armMotors.length; i++ )
		{
			armMotors[i] = new PWMVictorSPX( RobotMap.ARM_MOTORS[i] );
			armMotors[i].setName( this.getName(), "Motor" + i );
			armMotors[i].setInverted( true );
		}

		potentiometer.setPIDSourceType( PIDSourceType.kDisplacement );

		// setSetpoint() - Sets where the PID controller should move the system
		// to
		// enable() - Enables the PID controller.

		setAbsoluteTolerance( 0.2 );
		getPIDController().setContinuous( false );
		getPIDController().setInputRange( 1614.0, 1900.0 );

		enableSpeedLimit( true );
	}

	public void enableSpeedLimit( boolean enable )
	{
		if ( enable )
			setOutputRange( -0.5, 0.5 );
		else
			setOutputRange( -1.0, 1.0 );
	}

	@Override
	public void initDefaultCommand()
	{
		// Set the default command for a subsystem here.
		// setDefaultCommand(new MySpecialCommand());
	}

	@Override
	protected double returnPIDInput()
	{
		// Return your input value for the PID loop
		// e.g. a sensor, like a potentiometer:
		return potentiometer.pidGet(); // return the degrees according to the potentiometer
		// potentiometer output is calculated using the following formula:
		// (Analog Input Voltage/Analog Supply Voltage)*FullScale + Offset.
	}

	@Override
	protected void usePIDOutput( double output )
	{
		for ( int i = 0; i < armMotors.length; i++ )
		{
			armMotors[i].pidWrite( output );
		}
	}
}