/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.buttons.Button;
import frc.robot.commands.MoveArmCommand;
import frc.robot.commands.MovePIDArmCommand;
import frc.robot.commands.ToggleClimberSolenoidCommand;
import frc.robot.commands.TogglePusherSolenoidCommand;
import frc.robot.commands.ToggleSliderSolenoidCommand;
import frc.robot.utils.CustomXboxController;
import frc.robot.utils.XboxButton;
import frc.robot.utils.XboxControllerButton;
import frc.robot.utils.DPadButton.Direction;
import frc.robot.utils.DPadButton;

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
	public static CustomXboxController myController = new CustomXboxController( RobotMap.controllerPort );;

	public Button slowPresetLB = new XboxControllerButton( myController, XboxButton.kBumperLeft );

	private Button pushPresetRB = new XboxControllerButton( myController, XboxButton.kBumperRight );
	private Button slidePresetIn = new DPadButton( myController, Direction.Left );
	private Button slidePresetOut = new DPadButton( myController, Direction.Right );

	private Button armPreset_Floor = new XboxControllerButton( myController, XboxButton.kButtonA );
	private Button armPreset_Cargo = new XboxControllerButton( myController, XboxButton.kButtonX );
	private Button armPreset_Rocket = new XboxControllerButton( myController, XboxButton.kButtonB );
	private Button armPreset_Default = new XboxControllerButton( myController, XboxButton.kButtonY );

	private Button armPreset_Climb = new XboxControllerButton( myController, XboxButton.kButtonStart );
	private Button climb_Piston = new XboxControllerButton( myController, XboxButton.kButtonBack );

	private Button DPadUp = new DPadButton( myController, Direction.Up );
	private Button DPadDown = new DPadButton( myController, Direction.Down );

	public OI()
	{
		myController.setDeadzone( 0.2 );

		slidePresetIn.whenPressed( new ToggleSliderSolenoidCommand( false ) );
		slidePresetOut.whenReleased( new ToggleSliderSolenoidCommand( true ) );

		pushPresetRB.whenPressed( new TogglePusherSolenoidCommand( true ) );
		pushPresetRB.whenReleased( new TogglePusherSolenoidCommand( false ) );

		DPadUp.whileHeld( new MoveArmCommand( true, false ) );
		DPadDown.whileHeld( new MoveArmCommand( false, true ) );

		armPreset_Floor.whenPressed( new MovePIDArmCommand( Robot.armFloorPosition.get(), false, true ) );
		armPreset_Cargo.whenPressed( new MovePIDArmCommand( Robot.armCargoPosition.get(), false, true ) );
		armPreset_Rocket.whenPressed( new MovePIDArmCommand( Robot.armRocketPosition.get(), false, true ) );
		armPreset_Default.whenPressed( new MovePIDArmCommand( Robot.armDefaultPosition.get(), false, true ) );

		armPreset_Climb.whileHeld( new MovePIDArmCommand( Robot.armClimbPosition.get(), true, true ) );
		climb_Piston.whenPressed( new ToggleClimberSolenoidCommand() );

	}
}
