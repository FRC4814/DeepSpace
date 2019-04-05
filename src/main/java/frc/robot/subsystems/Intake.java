/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;
import frc.robot.commands.IntakeCommand;

public class Intake extends Subsystem
{
	Talon intakeMotor;

	public Intake()
	{
		intakeMotor = new Talon( RobotMap.INTAKE_MOTOR );
	}

	@Override
	public void initDefaultCommand()
	{
		setDefaultCommand( new IntakeCommand() );
	}

	public void setSpeed( double speed )
	{
		//multiplied to reduce speed
		intakeMotor.setSpeed( speed * 0.8 );
	}
}
