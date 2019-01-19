package frc.robot.utils;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class DashboardVariable<T>
{
	private static List<DashboardVariable<?>> defaultVariables = new ArrayList<DashboardVariable<?>>();

	private static boolean isInitialized = false;

	protected String name;
	protected T defaultValue;

	public DashboardVariable(String name, T defaultValue)
	{
		this.name = name;
		this.defaultValue = defaultValue;

		if (isInitialized)
			init();
		else
			defaultVariables.add(this);
	}

	/**
	 * Make sure that variables declared early (as statics) get put on the
	 * SmartDashboard (Attempting to put values before the Robot has initialized
	 * will throw exceptions)
	 */
	public static void initDefaultVariables()
	{
		for (int i = 0; i < defaultVariables.size(); i++)
		{
			defaultVariables.get(i).init();
		}

		isInitialized = true;
	}

	public void init()
	{
		System.out.println("Adding DashboardVariable '" + this.name + "' (Default: " + this.defaultValue.toString() + ")");
		set(this.defaultValue);
	}

	@SuppressWarnings("unchecked")
	public T get()
	{
		try
		{
			if (defaultValue instanceof Double)
			{
				return (T) (Object) SmartDashboard.getNumber(name, (Double) defaultValue);
			}
			else if (defaultValue instanceof Integer)
			{
				return (T) (Object) SmartDashboard.getNumber(name, (Integer) defaultValue);
			}
			else if (defaultValue instanceof Boolean)
			{
				return (T) (Object) SmartDashboard.getBoolean(name, (Boolean) defaultValue);
			}
			else if (defaultValue instanceof String)
			{
				return (T) (Object) SmartDashboard.getString(name, (String) defaultValue);
			}
		}
		catch (Exception e)
		{
			DriverStation.reportError(e.getMessage(), true);
		}

		return defaultValue;
	}

	public void set(T value)
	{
		try
		{
			if (defaultValue instanceof Double)
			{
				SmartDashboard.putNumber(name, (Double) value);
			}
			else if (defaultValue instanceof Integer)
			{
				SmartDashboard.putNumber(name, (Integer) value);
			}
			else if (defaultValue instanceof Boolean)
			{
				SmartDashboard.putBoolean(name, (Boolean) value);
			}
			else if (defaultValue instanceof String)
			{
				SmartDashboard.putString(name, (String) value);
			}
		}
		catch (Exception e)
		{
			DriverStation.reportError(e.getMessage(), true);
		}
	}
}
