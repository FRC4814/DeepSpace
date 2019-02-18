package frc.robot.utils;

public class Wait
{
	double time = 10;

	public static boolean timer( double time )
	{
		double deltaTime = 0;
		double prevTime = System.nanoTime();

		if ( deltaTime < time )
		{
			deltaTime += System.nanoTime() - prevTime;
		}

		if ( deltaTime >= time )
		{
			return true;
		}

		return false;
	}

	public static void reset( double deltaTime )
	{
		deltaTime = 0;
	}
}