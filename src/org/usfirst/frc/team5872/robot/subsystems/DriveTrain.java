package org.usfirst.frc.team5872.robot.subsystems;

import org.usfirst.frc.team5872.robot.Robot;

import com.ctre.CANTalon;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class DriveTrain extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	CANTalon fr = Robot.fr;
	CANTalon fl = Robot.fl;
	CANTalon br = Robot.br;
	CANTalon bl = Robot.bl;
	AHRS ahrs = Robot.ahrs;
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    private void setSpeed(double left,double right) {
    	fl.set(left);
		fr.set(right);
		bl.set(left);
		br.set(right);
    }
    public void stopMotors() {
    	fl.set(0);
		fr.set(0);
		bl.set(0);
		br.set(0);
    }
    private void resetEncoders() {
    	fl.setEncPosition(0);
    	bl.setEncPosition(0);
    	fr.setEncPosition(0);
    	br.setEncPosition(0);
    }
    private void encoderDrive(int rightTicks, int leftTicks, double leftPower, double rightPower, double timeout) {
    	resetEncoders();
    	int targetFrontRight = fr.getEncPosition()+rightTicks;
    	int targetBackRight = br.getEncPosition()+rightTicks;
    	int targetFrontLeft = fl.getEncPosition()+leftTicks;
    	int targetBackLeft = bl.getEncPosition()+leftTicks;
    	int curFrontRight = fr.getEncPosition();
    	int curBackRight = br.getEncPosition();
    	int curFrontLeft = fl.getEncPosition();
    	int curBackLeft = bl.getEncPosition();
    	boolean done = (Math.abs(targetFrontRight - curFrontRight) < 5 || Math.abs(targetBackRight - curBackRight) < 5 || Math.abs(targetFrontLeft - curFrontLeft) < 5 || Math.abs(targetBackLeft - curBackLeft) < 5);
    	setSpeed(leftPower,rightPower);
    	while(!done) {
    		curFrontRight = fr.getEncPosition();
        	curBackRight = br.getEncPosition();
        	curFrontLeft = fl.getEncPosition();
        	curBackLeft = bl.getEncPosition();
        	done = (Math.abs(targetFrontRight - curFrontRight) < 5 || Math.abs(targetBackRight - curBackRight) < 5 || Math.abs(targetFrontLeft - curFrontLeft) < 5 || Math.abs(targetBackLeft - curBackLeft) < 5);
    	}
    	stopMotors();
    }
    public void forwardTicks(double power, int ticks, int timeout) {
    	encoderDrive(ticks,ticks,power,power,timeout);
    }
    public void backwardTicks(double power, int ticks, int timeout) {
    	encoderDrive(ticks,ticks,-power,-power,timeout);
    }
    public void rightTicks(double power, int ticks, int timeout) {
    	encoderDrive(ticks,ticks,power,-power,timeout);
    }
    public void leftTicks(double power, int ticks, int timeout) {
    	encoderDrive(ticks,ticks,-power,power,timeout);
    }
public void gyroRight(int degrees, double speed){
    	
    	if(ahrs.getAngle() < degrees && ahrs.getAngle() == 0){
    		
    		setSpeed(speed,-speed);
    		
    	}
    	else{
    		
    		stopMotors();
    		ahrs.reset();
    		
    	}
    	
    }
    public void gyroLeft(int degrees, double speed){
    	
    	if(ahrs.getAngle() < degrees && ahrs.getAngle() == 0){
    		setSpeed(-speed,speed);
    	}
    	else{
    		stopMotors();
    		ahrs.reset();
    	}
    	
    }
}

