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
    //encoder and drive stuff
    public static Encoder leftEnc, rightEnc;
    public int wheelSize = 6;
    public double distancePerPulse;
    public double pulsesPerRev = 20.0;
    public DifferentialDrive m_myRobot;
    public static PIDController drivePIDLeft, drivePIDRight;
    
    //curve drive variables
    public final static Double wheelNonLinearity = 0.65;
    public final static int wheelDistance = 23;
    public static double error = 0;
    public static final double quickTurnSpeed = 0.3;

    //drive straight variables
    public double speedL, speedR, speedMod = 1.0;
    public boolean onTarget;
    public static final DashboardVariable<Boolean> driveStraightOn = new DashboardVariable<Boolean>("drive straight", true);
    
    //entering the P, I, and D variables to dashboard
    public static final DashboardVariable<Double> driveP = new DashboardVariable<Double>("DriveP", 0.02);
	public static final DashboardVariable<Double> driveI = new DashboardVariable<Double>("DriveI", 0.02);
	public static final DashboardVariable<Double> driveD = new DashboardVariable<Double>("DriveD", 0.02);

    public DriveTrain(){
        //calculate distance per pulse
        distancePerPulse = (wheelSize * Math.PI) / (pulsesPerRev);

        //initialize the encoders
        leftEnc = new Encoder(RobotMap.LEFT_ENCODERS[0], RobotMap.LEFT_ENCODERS[1], false, Encoder.EncodingType.k4X);
        leftEnc.setName(this.getName(), "EncoderL");
		leftEnc.setPIDSourceType(PIDSourceType.kDisplacement);
		leftEnc.setMaxPeriod(0.2);
		//leftEnc.setMinRate(10);
		leftEnc.setDistancePerPulse(distancePerPulse);
		leftEnc.setSamplesToAverage(7);
        leftEnc.reset();
    
        rightEnc = new Encoder(RobotMap.RIGHT_ENCODERS[0], RobotMap.RIGHT_ENCODERS[1], false, Encoder.EncodingType.k4X);
        rightEnc.setName(this.getName(), "EncoderR");
		rightEnc.setPIDSourceType(PIDSourceType.kDisplacement);
		rightEnc.setMaxPeriod(0.2);
		//rightEncoder.setMinRate(10);
		rightEnc.setDistancePerPulse(distancePerPulse);
		rightEnc.setSamplesToAverage(7);
        rightEnc.reset();
        
        // links the 3 left motor controllers together, and 2 right motor controllers together
        SpeedControllerGroup leftGroup = new SpeedControllerGroup(new PWMVictorSPX(RobotMap.LEFT_MOTORS[0]),new PWMVictorSPX(RobotMap.LEFT_MOTORS[1]), new PWMVictorSPX(RobotMap.LEFT_MOTORS[2]));
        SpeedControllerGroup rightGroup = new SpeedControllerGroup(new PWMVictorSPX(RobotMap.RIGHT_MOTORS[0]), new PWMVictorSPX(RobotMap.RIGHT_MOTORS[1]));
        drivePIDLeft = new PIDController(0.02, 0.0, 0.02, leftEnc, leftGroup);
        drivePIDRight = new PIDController(0.02, 0.0, 0.02, rightEnc, rightGroup);

        //inverts the motors otherwise they go backwards
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
    //resets encoders (duh)
    public void resetEncoders(){
        leftEnc.reset();
        rightEnc.reset();
    }

    public static double getLeftError(){
        double leftError = leftEnc.getDistance() - rightEnc.getDistance();
        return leftError;
    }

    public static double getRightError(){
        double rightError = rightEnc.getDistance() - leftEnc.getDistance();
        return rightError;
    }
    //drive straight for left side
    public static double driveStraightPercentLeft(){
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
    //main drive straight method
    public static double drivestraightPercent(){
        double percent;
        percent = driveStraightPercentLeft() - driveStraightPercentright();
        return percent;
    }
    //drive straight for right side
    public static double driveStraightPercentright(){
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

    public static void curvDrive(){
        //creates and inits the throttle and quickturn variables for quick turn logic
        double throttle = Robot.m_oi.myController.getY(Hand.kLeft);
        boolean isQuickTurn = false;
        boolean slow = Robot.m_oi.myController.getBumper(Hand.kLeft);
        //quick turn logic
        if(Math.abs(throttle)<0.3){
            isQuickTurn = true;
        }
        //slows the max speed for more accuracy
        if(slow){
             //checks if drive straight is on or off
            if(driveStraightOn.get()){
                Robot.driveTrain.m_myRobot.curvatureDrive(throttle/3,-Robot.m_oi.myController.getX(Hand.kRight) * drivestraightPercent(), isQuickTurn);
            }
            else{
                Robot.driveTrain.m_myRobot.curvatureDrive(throttle/3,-Robot.m_oi.myController.getX(Hand.kRight),isQuickTurn);
            }
        }else{
            //checks if drive straight is on or off
            if(driveStraightOn.get()){
                Robot.driveTrain.m_myRobot.curvatureDrive(throttle,-Robot.m_oi.myController.getX(Hand.kRight) * drivestraightPercent(), isQuickTurn);
            }
            else{
                Robot.driveTrain.m_myRobot.curvatureDrive(throttle,-Robot.m_oi.myController.getX(Hand.kRight),isQuickTurn);
        }
    }
    }

}