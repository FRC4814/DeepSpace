package frc.robot.commands;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ToggleClimberSolenoidCommand extends Command
{
	private boolean isClimb;

	public ToggleClimberSolenoidCommand()
	{
		//This command pushes the hatch panel slider forward and/or back and locks it in place

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
		if ( Robot.isClimb.get() )
		{
			Robot.climberSolenoid1.set( DoubleSolenoid.Value.kForward );
			Robot.climberSolenoid2.set( DoubleSolenoid.Value.kForward );
			isClimb = false;

		}
		else
		{
			Robot.climberSolenoid1.set( DoubleSolenoid.Value.kReverse );
			Robot.climberSolenoid2.set( DoubleSolenoid.Value.kReverse );
			isClimb = true;

		}
	}

	// Make this return true when this Command no longer needs to run execute()
	@Override
	protected boolean isFinished()
	{
		if ( isClimb != Robot.isClimb.get() )
		{
			Robot.isClimb.set( isClimb );
			return true;
		}
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
