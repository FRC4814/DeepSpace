package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.PIDArm;

public class MoveArmCommand extends Command
{
	private boolean up, down;

	public MoveArmCommand( boolean up, boolean down )
	{
		// Use requires() here to declare subsystem dependencies
		requires( Robot.pidArm );
		this.up = up;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize()
	{
		// possibly could add something here in future to stop the slight 
		// turning of the robot when it starts from rest
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute()
	{
		if ( up )
		{
			Robot.pidArm.manualMove( 0.3 );
		}
		if ( down )
		{
			Robot.pidArm.manualMove( -0.3 );
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished()
	{
		return false;
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