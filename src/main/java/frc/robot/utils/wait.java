package frc.robot.utils;

public class wait{

    public double time;

    public static boolean timer(double time){
        double deltaTime = 0;

        while(deltaTime < time ){
            double prevTime = System.nanoTime();
            deltaTime = System.nanoTime() - prevTime;
        }
        
        if(deltaTime == time){
            return true;
        }

        return false;

    }

}