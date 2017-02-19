package org.usfirst.frc.team5872.robot.subsystems;

import org.usfirst.frc.team5872.robot.Robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Flywheel extends Subsystem {
	//Bang bang variables
    private static final double TOLERANCE = 0.5e-7;
    private static final double TARGET_VELOCITY = 1.1e-6;
    VelocityCalculator flywheelVelocity = new VelocityCalculator();
    BangBangCalculator velocityBangBang = new BangBangCalculator();
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	CANTalon flywheel = Robot.flywheelCIM;
	CANTalon ballStirrer = Robot.ballStirrer;

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void bangBang() {
    	ballStirrer.set(.4);
    	flywheelVelocity.setParameters(System.nanoTime(), flywheel.getEncPosition());
        double currentVelocity = flywheelVelocity.getVelocity();

        velocityBangBang.setParameters(currentVelocity, TARGET_VELOCITY, 0.84, 0.9, TOLERANCE);
        double motorOut = velocityBangBang.getBangBang();

        if(motorOut > 1) motorOut = 1;
        else if(motorOut < 0) motorOut = 0;
        flywheel.set(motorOut);
    }
    public void stopShooting() {
    	flywheel.set(0);
    	ballStirrer.set(0);
    }
}
class BangBangCalculator
{
    private double target;
    private double velocity;
    private double lowerPower, higherPower;
    private double tolerance;

    public void setParameters(double target, double velocity, double lowerPower, double higherPower, double tolerance)
    {
        this.target = target;
        this.velocity = velocity;
        this.lowerPower = lowerPower;
        this.higherPower = higherPower;
        this.tolerance = tolerance;
    }

    public double getBangBang()
    {
        if(velocity >= (target + tolerance))
        {
            return lowerPower;
        }

        else
        {
            return higherPower;
        }
    }
}

class VelocityCalculator
{
    private long time, encoder;

    private long lastEncoder, lastTime;

    public void setParameters(long time, long encoder)
    {
        this.time = time;
        this.encoder = encoder;
    }

    public double getVelocity()
    {
        double velocity = (double) (encoder - lastEncoder) / (time - lastTime);

        lastEncoder = encoder;
        lastTime = time;

        return velocity;
    }
}

