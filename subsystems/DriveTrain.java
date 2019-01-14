package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.RobotMap;
import frc.robot.commands.HaloDriveCommand;

public class DriveTrain extends Subsystem{

    public Encoder leftEnc;
    public int wheelSize = 6;
    public Encoder rightEnc;
    public double distancePerPulse;
    public double pulsesPerRev = 20.0;
    public DifferentialDrive m_myRobot;

    public DriveTrain(){
        //initialize the encoders
        leftEnc = new Encoder(RobotMap.leftEncoders[0], RobotMap.leftEncoders[1], false, Encoder.EncodingType.k4X);
        rightEnc = new Encoder(RobotMap.rightEncoders[0], RobotMap.rightEncoders[1], false, Encoder.EncodingType.k4X);

        SpeedControllerGroup leftGroup = new SpeedControllerGroup(new PWMVictorSPX(RobotMap.leftMotors[0]),new PWMVictorSPX(RobotMap.leftMotors[1]), new PWMVictorSPX(RobotMap.leftMotors[2]));
        SpeedControllerGroup rightGroup = new SpeedControllerGroup(new PWMVictorSPX(RobotMap.rightMotors[0]), new PWMVictorSPX(RobotMap.rightMotors[1]), new PWMVictorSPX(RobotMap.rightMotors[2]));
        leftGroup.setInverted(true);
        rightGroup.setInverted(true);
        //initialize the drive train
        m_myRobot = new DifferentialDrive(leftGroup, rightGroup);
                
        //calculate distance per pulse
        distancePerPulse = (wheelSize * Math.PI) / (pulsesPerRev);
        
        //set distance per pulse for encoders
        leftEnc.setDistancePerPulse(distancePerPulse);
        rightEnc.setDistancePerPulse(distancePerPulse);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new HaloDriveCommand());
    }

}