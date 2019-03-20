package frc.robot.utils;

import edu.wpi.first.wpilibj.buttons.Button;

/**
 *
 */
public class DPadButton extends Button
{
	private CustomXboxController driverController;
	private Direction direction;

	public DPadButton( CustomXboxController driverController, Direction direction )
	{
		this.driverController = driverController;
		this.direction = direction;
	}

	@Override
	public boolean get()
	{
		int degree = driverController.getPOV( 0 );

		return degree == direction.degree;
	}

	public enum Direction
	{
		Up( 0 ), Down( 180 ), Left( 270 ), Right( 90 );

		int degree;

		Direction( int degree )
		{
			this.degree = degree;
		}
	}
}
