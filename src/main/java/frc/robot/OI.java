/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.buttons.Button;
import frc.robot.commands.HaloDriveCommand;
import frc.robot.commands.MovePIDArmCommand;
import frc.robot.commands.TogglePusherSolenoidCommand;
import frc.robot.commands.ToggleSliderSolenoidCommand;
import frc.robot.utils.CustomXboxController;
import frc.robot.utils.XboxButton;
import frc.robot.utils.XboxControllerButton;
import frc.robot.utils.DPadButton.Direction;
import frc.robot.utils.DPadButton;
import frc.robot.commands.MoveArmCommand;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI
{
	//// CREATING BUTTNS
	// One type of button is a joystick button which is any button on a
	//// joystick.
	// You create one by telling it which joystick it's on and which button
	// number it is.
	// Joystick stick = new Joystick(port);
	public static CustomXboxController myController;

	public Button slowPresetLB = new XboxControllerButton( myController, XboxButton.kBumperLeft );

	public OI()
	{
		myController = new CustomXboxController( RobotMap.controllerPort );
		myController.setDeadzone( 0.2 );

		Button pushPresetRB = new XboxControllerButton( myController, XboxButton.kBumperRight );
		// private Button slidePresetA = new XboxControllerButton( myController,
		// XboxButton.kButtonA );
		// private Button slidePresetX = new XboxControllerButton( myController,
		// XboxButton.kButtonX );

		Button armPreset_Floor = new XboxControllerButton( myController, XboxButton.kButtonA );
		Button armPreset_Cargo = new XboxControllerButton( myController, XboxButton.kButtonX );
		Button armPreset_Rocket = new XboxControllerButton( myController, XboxButton.kButtonB );
		Button armPreset_Default = new XboxControllerButton( myController, XboxButton.kButtonY );

		Button DPadUp = new DPadButton( myController, Direction.Up );
		Button DPadDown = new DPadButton( myController, Direction.Down );
		// slidePresetA.whileHeld( new ToggleSliderSolenoidCommand( false ) );
		// slidePresetX.whileHeld( new ToggleSliderSolenoidCommand( true ) );

		pushPresetRB.whenPressed( new TogglePusherSolenoidCommand( true ) );
		pushPresetRB.whenReleased( new TogglePusherSolenoidCommand( false ) );

		armPreset_Floor.whenPressed( new MovePIDArmCommand( Robot.armFloorPosition.get() ) );
		armPreset_Cargo.whenPressed( new MovePIDArmCommand( Robot.armCargoPosition.get() ) );
		armPreset_Rocket.whenPressed( new MovePIDArmCommand( Robot.armRocketPosition.get() ) );
		armPreset_Default.whenPressed( new MovePIDArmCommand( Robot.armDefaultPosition.get() ) );

		DPadUp.whileHeld( new MoveArmCommand( true, false ) );
		DPadUp.whileHeld( new MoveArmCommand( false, true ) );
	}
}
