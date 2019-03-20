package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.subsystems.Arm;
import edu.wpi.first.wpilibj.command.Command;

public class MoveArmCommand extends Command
{

	boolean up, down;

	public MoveArmCommand( boolean up, boolean down )
	{
		requires( Robot.pidArm );
		// gets the up and down dpad state
		this.up = up;
		this.down = down;
	}

	// Called just before this Command runs the first time
	@Override
	protected void initialize()
	{
	}

	// Called repeatedly when this Command is scheduled to run
	@Override
	protected void execute()
	{
		if ( up )
		{
			Arm.moveArm( true );
		}

		if ( down )
		{
			Arm.moveArm( false );
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished()
	{
		return up || down;
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