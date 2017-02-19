
package org.usfirst.frc.team5872.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.RobotDrive;
import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import org.usfirst.frc.team5872.robot.commands.ExampleCommand;
import org.usfirst.frc.team5872.robot.subsystems.ExampleSubsystem;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {

	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();

    Command autonomousCommand;
    SendableChooser chooser;
    
    //Motor Controller Declarations
    CANTalon fl;
	CANTalon bl;
	CANTalon fr;
	CANTalon br;
    
    //Essential Declarations
    RobotDrive myRobot;
    Joystick stick;
    Timer timer;
    
    //Sensor Declarations and Variables
    AnalogGyro gyro;
    AHRS ahrs;
    PIDController turnController;
    double rotateToAngleRate;
    double Kp = 0.03;
    

    
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", new ExampleCommand());
        //chooser.addObject("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", chooser);
        
        //Motor Controller Assignments
        fl = new CANTalon(3);
        bl = new CANTalon(2);
        fr = new CANTalon(1);
        br = new CANTalon(0);
        
        //Initialize encoders
        fl.configEncoderCodesPerRev(1000);
        bl.configEncoderCodesPerRev(1000);
        fr.configEncoderCodesPerRev(1000);
        br.configEncoderCodesPerRev(1000);
        
        //Essential Assignments
        myRobot = new RobotDrive(3, 1, 2, 0);
        stick = new Joystick(0);
        timer = new Timer();
        
        //Sensor Assignments
        gyro = new AnalogGyro(1);
        ahrs = new AHRS(I2C.Port.kMXP); 
        
        
    }
	
	/**
     * This function is called once each time the robot enters Disabled mode.
     * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
     */
    public void disabledInit(){

    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different autonomous modes
	 * using the dashboard. The sendable chooser code works with the Java SmartDashboard. If you prefer the LabVIEW
	 * Dashboard, remove all of the chooser code and uncomment the getString code to get the auto name from the text box
	 * below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the chooser code above (like the commented example)
	 * or additional comparisons to the switch structure below with additional strings & commands.
	 */
    public void autonomousInit() {
        autonomousCommand = (Command) chooser.getSelected();
        
		/*String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
		switch(autoSelected) {
		case "My Auto":
			autonomousCommand = new MyAutoCommand();
			break;
		case "Default Auto":
		default:
			autonomousCommand = new ExampleCommand();
			break;
		}*/
    	
    	//schedule the autonomous command (example)
        if (autonomousCommand != null) 
        	autonomousCommand.start();
        
        timer.reset(); //Resets the timer to 0
        timer.start(); //Start counting
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomous() {
    	
        Scheduler.getInstance().run();
        gyro.reset();
        
        while (isAutonomous()) {
        	
        	double angle = gyro.getAngle(); //get current heading
        	myRobot.drive(-1.0, -angle*Kp); //drive towards heading 0
        	Timer.delay(0.004);        	
        }
        
        myRobot.drive(0.0, 0.0); 
        
        /*Drive for 2 seconds
        if (timer.get() < 2.0)
             myRobot.drive(-0.5,  0); //drive forwards half speed
        else 
             myRobot.drive(0.0, 0.0); //stop robot*/
    }

    public void teleopInit() {
    	
		//This makes sure that the autonomous stops running when
        //teleop starts running. If you want the autonomous to 
        //continue until interrupted by another command, remove
        //this line or comment it out.
        if (autonomousCommand != null) 
        	autonomousCommand.cancel();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	while(true) {
    		
    		Scheduler.getInstance().run();
    		myRobot.arcadeDrive(stick); //This line drives the robot using the values of the joystick and the motor controllers selected above
        
    		//Tank Drive Logitech Controller Joystick Declarations and Assignments
    		double l = -stick.getRawAxis(1);
    		double r = stick.getRawAxis(5);
        
    		//Arcade Drive Joystick Declarations and Assignments
    		double y = -stick.getRawAxis(1);
    		double z = stick.getRawAxis(2);
    	
    		//Tank Drive
    		if (stick.getRawAxis(1) != 0) { //if left joystick is active
    			fl.set(l);
    			bl.set(l);
    		}
    		else {
    			fl.set(0);
    			bl.set(0);
    		}
    		if (stick.getRawAxis(5) != 0) { //if right joystick is active
    			fr.set(r);
        		br.set(r);
    		}
    		else {
    			fr.set(0);
    			br.set(0);
    		}
    		
    		//Arcade Drive
    		if (stick.getRawAxis(1) != 0) { //if y-axis is active
    			fl.set(y);
            	bl.set(y);
           		fr.set(y);
           		br.set(y);
    		}
    		else {
    			fl.set(0);
    			bl.set(0);
    			fr.set(0);
    			br.set(0);
    		}
        	if (stick.getRawAxis(2) != 0) { //if z-axis is active
        		fl.set(-z);
        		bl.set(-z);
        		fr.set(z);
        		br.set(z);
        	}
        	else {
        		fl.set(0);
        		bl.set(0);
        		fr.set(0);
        		br.set(0);
        	}
        	
        	if (stick.getRawButton(1) && ahrs.getAngle() < 5){
        		gyroRight(90, 0.5);
        	}
        	else{
        		fl.set(0);
        		fr.set(0);
        		bl.set(0);
        		br.set(0);
        	}
    	}
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
        LiveWindow.run();
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
    public void gyrotLeft(int degrees, double speed){
    	
    	if(ahrs.getAngle() < degrees && ahrs.getAngle() == 0){
    		setSpeed(-speed,speed);
    	}
    	else{
    		stopMotors();
    		ahrs.reset();
    	}
    	
    }
    public void setSpeed(double left,double right) {
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
    public void resetEncoders() {
    	fl.setEncPosition(0);
    	bl.setEncPosition(0);
    	fr.setEncPosition(0);
    	br.setEncPosition(0);
    }
    public void encoderDrive(int rightTicks, int leftTicks, double leftPower, double rightPower, double timeout) {
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
    	while(isAutonomous() && !done) {
    		curFrontRight = fr.getEncPosition();
        	curBackRight = br.getEncPosition();
        	curFrontLeft = fl.getEncPosition();
        	curBackLeft = bl.getEncPosition();
        	done = (Math.abs(targetFrontRight - curFrontRight) < 5 || Math.abs(targetBackRight - curBackRight) < 5 || Math.abs(targetFrontLeft - curFrontLeft) < 5 || Math.abs(targetBackLeft - curBackLeft) < 5);
    	}
    	stopMotors();
    }
}

