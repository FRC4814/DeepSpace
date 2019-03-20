package frc.robot.subsystems;

import edu.wpi.first.wpilibj.PWMVictorSPX;
import frc.robot.RobotMap;

public class Arm
{

	public static PWMVictorSPX[] armMotors;

	public static void moveArm( boolean up )
	{
		armMotors = new PWMVictorSPX[RobotMap.ARM_MOTORS.length];
		for ( int i = 0; i < armMotors.length; i++ )
		{
			armMotors[i] = new PWMVictorSPX( RobotMap.ARM_MOTORS[i] );
		}
		if ( up )
		{
			for ( int i = 0; i < armMotors.length; i++ )
			{
				armMotors[i].set( 0.5 ); // set the speed of the motors
			}
		}
		else if ( !up )
		{
			for ( int i = 0; i < armMotors.length; i++ )
			{
				armMotors[i].set( -0.5 ); // set the speed of the motors
			}
		}
	}

}