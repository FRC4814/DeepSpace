package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Robot;
import frc.robot.RobotMap;
import frc.robot.commands.HaloDriveCommand;
import frc.robot.utils.DashboardVariable;
import frc.robot.Robot;

public class DriveTrain extends Subsystem{

    public static Encoder leftEnc, rightEnc;
    public int wheelSize = 6;
    public double pulsesPerRev = 20.0;
    public double distancePerPulse = (wheelSize * Math.PI) / (pulsesPerRev);
    public DifferentialDrive m_myRobot;
    public PIDController drivePIDLeft, drivePIDRight;

    // make the controllers grouped together so their speeds can be changed together
    public SpeedControllerGroup leftGroup = new SpeedControllerGroup(new PWMVictorSPX(RobotMap.leftMotors[0]),new PWMVictorSPX(RobotMap.leftMotors[1]), new PWMVictorSPX(RobotMap.leftMotors[2]));
    public SpeedControllerGroup rightGroup = new SpeedControllerGroup(new PWMVictorSPX(RobotMap.rightMotors[0]), new PWMVictorSPX(RobotMap.rightMotors[1]));
    
    //change to dashboard variable!
    public static final DashboardVariable<Double> driveP = new DashboardVariable("DriveP", 0.02);
	public static final DashboardVariable<Double> driveI = new DashboardVariable("DriveI", 0.02);
    public static final DashboardVariable<Double> driveD = new DashboardVariable("DriveD", 0.02);

    public DriveTrain(){
        //initialize the encoders
        leftEnc = new Encoder(RobotMap.leftEncoders[0], RobotMap.leftEncoders[1], false, Encoder.EncodingType.k4X);
        leftEnc.setName(this.getName(), "EncoderL");
		leftEnc.setPIDSourceType(PIDSourceType.kDisplacement);
		leftEnc.setMaxPeriod(0.2);
		//leftEnc.setMinRate(10);
		leftEnc.setDistancePerPulse(distancePerPulse);
		leftEnc.setSamplesToAverage(7);
        leftEnc.reset();
        
        rightEnc = new Encoder(RobotMap.rightEncoders[0], RobotMap.rightEncoders[1], false, Encoder.EncodingType.k4X);
        rightEnc.setName(this.getName(), "EncoderR");
		rightEnc.setPIDSourceType(PIDSourceType.kDisplacement);
		rightEnc.setMaxPeriod(0.2);
		//rightEncoder.setMinRate(10);
		rightEnc.setDistancePerPulse(distancePerPulse);
		rightEnc.setSamplesToAverage(7);
        rightEnc.reset();
        
        // links the 3 left motor controllers together, and 2 right motor controllers together
        SpeedControllerGroup leftGroup = new SpeedControllerGroup(new PWMVictorSPX(RobotMap.leftMotors[0]),new PWMVictorSPX(RobotMap.leftMotors[1]), new PWMVictorSPX(RobotMap.leftMotors[2]));
        SpeedControllerGroup rightGroup = new SpeedControllerGroup(new PWMVictorSPX(RobotMap.rightMotors[0]), new PWMVictorSPX(RobotMap.rightMotors[1]));

        drivePIDLeft = new PIDController(0.02, 0.0, 0.02, leftEnc, leftGroup);
        drivePIDRight = new PIDController(0.02, 0.0, 0.02, rightEnc, rightGroup);

        leftGroup.setInverted(true);
        rightGroup.setInverted(true);
        //initialize the drive train
        m_myRobot = new DifferentialDrive(leftGroup, rightGroup);
    }

    public static double driveStraight() {
        double error = leftEnc.getDistance() - rightEnc.getDistance();
        double turnPower = driveP.get() * error;
        return turnPower;
    }

    public void startPID(){
        drivePIDLeft.enable();
        drivePIDRight.enable();
    }

    public void updatePID(){
        drivePIDLeft.setPID(driveP.get(), driveI.get(), driveD.get());   
        drivePIDRight.setPID(driveP.get(), driveI.get(), driveD.get());
    }

    public void disablePID(){
        drivePIDLeft.disable();
        drivePIDRight.disable();
    }

    public void setSpeed(double speedLeft, double speedRight) {
        leftGroup.set(speedLeft);
        rightGroup.set(speedRight);
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new HaloDriveCommand());
    }

    public static void ArcadeDrive(){
        Robot.driveTrain.m_myRobot.arcadeDrive(Robot.m_oi.myController.getY(Hand.kLeft), Robot.m_oi.myController.getX(Hand.kRight));
    }
/*
    public static void curvDrive(boolean quickTurn, double wheel, double throttle) {
        // calculate radius
        final Double denominator = Math.sin(Math.PI/2.0 * wheelNonLinearity);
        // calculate needed motor speed
        Robot.driveTrain.m_myRobot.tankDrive(pwmLeft, pwmRight, rightSpeed);
    }
*/
}