package frc.robot.commands;

import frc.robot.OI;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.DriveTrain;

public class HaloDriveCommand extends Command{
    public HaloDriveCommand(){
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveTrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        
        
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double forwardPower = Math.pow(-OI.myController.getY(Hand.kLeft), 3);
        double turnPower = Math.pow(OI.myController.getX(Hand.kRight), 5);
        double leftPower = forwardPower + turnPower;
        double rightPower = forwardPower - turnPower;

        Robot.driveTrain.setSpeed(leftPower, rightPower);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
        Robot.driveTrain.m_myRobot.tankDrive(0, 0);
        Robot.driveTrain.setSpeed(0.0, 0.0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
    }