package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;



public class HaloDriveCommand extends Command{

    public HaloDriveCommand(){
        // Use requires() here to declare subsystem dependencies
        requires(Robot.driveTrain);
        
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
        // possibly could add something here in future to stop the slight 
        // turning of the robot when it starts from rest
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        //Robot.m_oi.myController.getBumper(Hand.kLeft);
        Robot.driveTrain.curvDrive();
        //DriveTrain.curvDrive(Robot.m_oi.myController.getX(Hand.kRight), Robot.m_oi.myController.getY(Hand.kLeft));
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
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    }
    }