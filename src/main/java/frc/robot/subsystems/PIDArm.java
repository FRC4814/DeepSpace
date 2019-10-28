/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
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
	static DashboardVariable<Double> kP = new DashboardVariable<Double>( "kP", 0.0063 ); //OG 0.0025 new 0.004 //0.0031 after waterloo 0.005
	static DashboardVariable<Double> kI = new DashboardVariable<Double>( "kI", 0.000019 ); //OG 0.00003 new 0.00015 //0.00009 after waterloo 0.00005
	static DashboardVariable<Double> kD = new DashboardVariable<Double>( "kD", 0.0069 );//OG 0.0000 new 0.0013 //0.018 after waterloo 0.0006
	static DashboardVariable<Double> kF = new DashboardVariable<Double>( "kF", 0.15 ); //0.00015 after waterloo 0.004

	//used to adjust pot if not correc
	//ublic DigitalInput limitSwitch = new DigitalInput( RobotMap.limitSwitch );
	double offset = -937;
	double delta;

	public boolean pidEnabled = true;

	// arm potentiometer
	public final Potentiometer potentiometer;
	SpeedControllerGroup armMotors;
	// channel, full rotation degrees, offset (starting angle)
	// Potentiometer does 10 spins so 3600 degrees

	public PIDArm()
	{

		super( "PID Arm", kP.get(), kI.get(), kD.get() );
		potentiometer = new AnalogPotentiometer( RobotMap.ARM_POTENTIOMETER, 1800, offset );
		// if ( limitSwitch.get() )
		// {
		// 	delta = potentiometer.get() - 21;
		// 	offset -= delta;
		// }

		armMotors = new SpeedControllerGroup( new PWMVictorSPX( RobotMap.ARM_MOTORS[0] ) );
		armMotors.setInverted( true );

		// Insert a subsystem name and PID values here
		// armMotors = new PWMVictorSPX[RobotMap.ARM_MOTORS.length];
		// for ( int i = 0; i < armMotors.length; i++ )
		// {
		// 	armMotors[i] = new PWMVictorSPX( RobotMap.ARM_MOTORS[i] );
		// 	armMotors[i].setName( this.getName(), "Motor" + i );
		// 	armMotors[i].setInverted( true );
		// }

		potentiometer.setPIDSourceType( PIDSourceType.kDisplacement );

		// setSetpoint() - Sets where the PID controller should move the system
		// to
		// enable() - Enables the PID controller.

		setAbsoluteTolerance( 0.18 );
		getPIDController().setContinuous( false );
		getPIDController().setInputRange( -32.0, 250.0 );

		enableSpeedLimit( true );
	}

	public void enableSpeedLimit( boolean enable )
	{
		if ( enable )
			setOutputRange( -0.6, 0.6 );
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
	public double returnPIDInput()
	{
		// Return your input value for the PID loop
		// e.g. a sensor, like a potentiometer:
		//divide by ten to convert to degrees
		return potentiometer.pidGet(); // return the degrees according to the potentiometer
		// potentiometer output is calculated using the following formula:
		// (Analog Input Voltage/Analog Supply Voltage)*FullScale + Offset.
	}

	@Override
	protected void usePIDOutput( double output )
	{
		//kF used to account for gravity based on the angle of the arm
		armMotors.set( output + kF.get() * Math.cos( potentiometer.get() ) );
	}

	public void manualMove( double speed )
	{
		armMotors.set( speed );
	}
}