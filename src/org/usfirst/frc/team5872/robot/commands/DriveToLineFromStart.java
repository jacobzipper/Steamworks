package org.usfirst.frc.team5872.robot.commands;

import org.usfirst.frc.team5872.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveToLineFromStart extends Command {
	private int TIMEOUT = 5;
    public DriveToLineFromStart() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.drive);
    	setTimeout(5);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drive.forwardTicks(.5, 5000, 5);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return isTimedOut();
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drive.stopMotors();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
