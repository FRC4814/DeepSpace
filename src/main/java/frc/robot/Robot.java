/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.command.*;
import edu.wpi.first.wpilibj.smartdashboard.*;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.PIDArm;
import frc.robot.utils.*;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot
{
	public static OI m_oi;

	public static PowerDistributionPanel panel;

	// drivetrain
	public static DriveTrain driveTrain = new DriveTrain();
	// entering the P, I, and D variables to dashboard
	public static final DashboardVariable<Double> driveP = new DashboardVariable<Double>( "DriveP", 0.02 );
	public static final DashboardVariable<Double> driveI = new DashboardVariable<Double>( "DriveI", 0.02 );
	public static final DashboardVariable<Double> driveD = new DashboardVariable<Double>( "DriveD", 0.02 );

	//presets for the arm
	public static final DashboardVariable<Double> armFloorPosition = new DashboardVariable<Double>( "PIDArm (Floor)", -21.0 );
	public static final DashboardVariable<Double> armCargoPosition = new DashboardVariable<Double>( "PIDArm (Cargo)", 60.0 );
	public static final DashboardVariable<Double> armRocketPosition = new DashboardVariable<Double>( "PIDArm (Rocket)", 20.0 );
	public static final DashboardVariable<Double> armDefaultPosition = new DashboardVariable<Double>( "PIDArm (Default)", 110.0 );

	public static final DashboardVariable<Double> armClimbPosition = new DashboardVariable<Double>( "PIDArm (Climb)", -21.0 );

	public static DashboardVariable<Boolean> isSlid = new DashboardVariable<Boolean>( "Slid", false );
	public static DashboardVariable<Boolean> isClimb = new DashboardVariable<Boolean>( "Climb", false );

	// potentiometer values
	// public static Potentiometer potentiometer = new AnalogPotentiometer(0, 3600,
	// 0);
	public static PIDArm pidArm = new PIDArm();

	public static Intake intake = new Intake();

	// pneumatics
	public static final Compressor compressor = new Compressor( 0 );
	public static final DoubleSolenoid pusherSolenoid = new DoubleSolenoid( 4, 5 );
	public static final DoubleSolenoid sliderSolenoid = new DoubleSolenoid( 6, 7 );
	public static final DoubleSolenoid climberSolenoid1 = new DoubleSolenoid( 0, 1 );
	public static final DoubleSolenoid climberSolenoid2 = new DoubleSolenoid( 2, 3 );

	// Auto variables
	Command m_autonomousCommand;
	SendableChooser<Command> m_chooser = new SendableChooser<>();

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	@Override
	public void robotInit()
	{
		// Receiving input from controller
		m_oi = new OI();

		panel = new PowerDistributionPanel();

		// SmartDashboard things go here
		DashboardVariable.initDefaultVariables();
		CameraServer.getInstance().startAutomaticCapture();
		// m_chooser.setDefaultOption("Default Auto", new ExampleCommand());
		// chooser.addOption("My Auto", new MyAutoCommand());
		SmartDashboard.putData( "Auto mode", m_chooser );
		SmartDashboard.putNumber( "Potentiometer Value", Math.round( pidArm.potentiometer.get() ) );

		// Initialize pneumatics
		compressor.setClosedLoopControl( true );
		pusherSolenoid.set( DoubleSolenoid.Value.kOff );
		sliderSolenoid.set( DoubleSolenoid.Value.kOff );
		climberSolenoid1.set( DoubleSolenoid.Value.kOff );
		climberSolenoid2.set( DoubleSolenoid.Value.kOff );
	}

	/**
	 * This function is called every robot packet, no matter the mode. Use this for
	 * items like diagnostics that you want ran during disabled, autonomous,
	 * teleoperated and test.
	 *
	 * <p>
	 * This runs after the mode specific periodic functions, but before LiveWindow
	 * and SmartDashboard integrated updating.
	 */
	@Override
	public void robotPeriodic()
	{
		// degrees of potentiometer
		SmartDashboard.putNumber( "Potentiometer Value", Math.round( pidArm.potentiometer.get() ) );
	}

	/**
	 * This function is called once each time the robot enters Disabled mode. You
	 * can use it to reset any subsystem information you want to clear when the
	 * robot is disabled.
	 */
	@Override
	public void disabledInit()
	{
		pidArm.disable();
	}

	@Override
	public void disabledPeriodic()
	{
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable chooser
	 * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
	 * remove all of the chooser code and uncomment the getString code to get the
	 * auto name from the text box below the Gyro
	 *
	 * <p>
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons to
	 * the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit()
	{
		m_autonomousCommand = m_chooser.getSelected();
		driveTrain.resetEncoders();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
		 * switch(autoSelected) { case "My Auto": autonomousCommand = new
		 * MyAutoCommand(); break; case "Default Auto": default: autonomousCommand = new
		 * ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if ( m_autonomousCommand != null )
		{
			m_autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic()
	{
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit()
	{
		driveTrain.resetEncoders();
		pusherSolenoid.set( DoubleSolenoid.Value.kOff );
		sliderSolenoid.set( DoubleSolenoid.Value.kOff );
		climberSolenoid1.set( DoubleSolenoid.Value.kOff );
		climberSolenoid2.set( DoubleSolenoid.Value.kOff );

		pidArm.setSetpoint( armFloorPosition.get() );
		pidArm.enable();

		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if ( m_autonomousCommand != null )
		{
			m_autonomousCommand.cancel();
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic()
	{
		Scheduler.getInstance().run();

	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic()
	{
	}
}
