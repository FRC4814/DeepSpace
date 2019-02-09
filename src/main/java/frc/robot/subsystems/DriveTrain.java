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

public class DriveTrain extends Subsystem{

    public static Encoder leftEnc;
    public int wheelSize = 6;
    public static Encoder rightEnc;
    public double distancePerPulse;
    public double pulsesPerRev = 20.0;
    public DifferentialDrive m_myRobot;
    public static PIDController drivePIDLeft, drivePIDRight;
    
    //curve drive variables
    public final static Double wheelNonLinearity = 0.65;
    public final static int wheelDistance = 23;
    public static double error = 0;
    public static final double quickTurnSpeed = 5.0;

    //drive straight variables
    public double speedL, speedR, speedMod = 1.0;
    public boolean onTarget;
    public static final DashboardVariable<Boolean> driveStraightOn = new DashboardVariable<Boolean>("drive straight", true);
    
    public static final DashboardVariable<Double> driveP = new DashboardVariable<Double>("DriveP", 0.02);
	public static final DashboardVariable<Double> driveI = new DashboardVariable<Double>("DriveI", 0.02);
	public static final DashboardVariable<Double> driveD = new DashboardVariable<Double>("DriveD", 0.02);

    public DriveTrain(){
        //calculate distance per pulse
        distancePerPulse = (wheelSize * Math.PI) / (pulsesPerRev);

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
        
        SpeedControllerGroup leftGroup = new SpeedControllerGroup(new PWMVictorSPX(RobotMap.leftMotors[0]),new PWMVictorSPX(RobotMap.leftMotors[1]), new PWMVictorSPX(RobotMap.leftMotors[2]));
        SpeedControllerGroup rightGroup = new SpeedControllerGroup(new PWMVictorSPX(RobotMap.rightMotors[0]), new PWMVictorSPX(RobotMap.rightMotors[1]));
        
        drivePIDLeft = new PIDController(0.02, 0.0, 0.02, leftEnc, leftGroup);
        drivePIDRight = new PIDController(0.02, 0.0, 0.02, rightEnc, rightGroup);

        leftGroup.setInverted(true);
        rightGroup.setInverted(true);
        //initialize the drive train
        m_myRobot = new DifferentialDrive(leftGroup, rightGroup);
                
        
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

    public static double getLeftError(){
        double leftError = leftEnc.getDistance() - rightEnc.getDistance();
        return leftError;
    }

    public static double getRightError(){
        double rightError = rightEnc.getDistance() - leftEnc.getDistance();
        return rightError;
    }

    public static double driveStraightPercentLeft(double leftSpeed){
        double rightDistance = rightEnc.getDistance();
        double leftDistance = leftEnc.getDistance();

        //check if the right is further than left
        if(leftDistance < rightDistance){
            //calculate the percent needed to adjust
            double percent = leftDistance / rightDistance;
            return percent;
        }
        return 1;
    }

    public static double driveStraightPercentright(double rightSpeed){
        double rightDistance = rightEnc.getDistance();
        double leftDistance = leftEnc.getDistance();

        //check if left is further than right
        if(rightDistance < leftDistance){
            //calculate the percent needed to adjust
            double percent = rightDistance/leftDistance;
            return percent;
        }
        return 1;
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new HaloDriveCommand());
    }

    public static void curvDrive(double wheel, double throttle){
        boolean quickTurn = false;
        //calculate radius
        final Double denominator = Math.sin(Math.PI /2.0 * wheelNonLinearity);
        wheel = Math.sin(Math.PI /2.0 *wheelNonLinearity * wheel ) / denominator;
        
        double angularPower;
        double linearPower = throttle;
        
        //make quick turn true when the speed is slow
        if(Math.abs(linearPower)<quickTurnSpeed){
            quickTurn = true;
        }

        angularPower = wheel;
        /*if(throttle<0){
            wheel = -wheel;
        }
        */

        //check if driver needs to turn quickly
        if(!quickTurn){
           angularPower = throttle * wheel * 0.65;
        }

        //drive the left/right motors
        double pwmLeft;
        double pwmRight;

        pwmLeft = pwmRight = linearPower;
        pwmLeft += angularPower;  //add drive straight to hopefully make the robot straight
        pwmRight -= angularPower; //subtract drive straight to hopefully make the robot straight

        if(driveStraightOn.get()){
            Robot.driveTrain.m_myRobot.tankDrive(pwmLeft * driveStraightPercentLeft(pwmLeft), pwmRight * driveStraightPercentright(pwmRight));
        }
        else{
            Robot.driveTrain.m_myRobot.tankDrive(pwmLeft, pwmRight, quickTurn);
        }
    }

    //finds the velocity of the robot
    public double getLinearVelocity(){
        return (leftEnc.getDistance() + rightEnc.getDistance())/2;
    }

    public double getAngularVelocity(){
        return (leftEnc.getDistance() - rightEnc.getDistance()) / wheelDistance;
    }

    public static void ArcadeDrive(){
        Robot.driveTrain.m_myRobot.arcadeDrive(Robot.m_oi.myController.getY(Hand.kLeft), -Robot.m_oi.myController.getX(Hand.kRight));
    }

}