package org.usfirst.frc.team5872.robot.commands;

import org.usfirst.frc.team5872.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DoGearMiddleFromStart extends Command {

    public DoGearMiddleFromStart() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drive.forwardTicks(.5, 100000, 5);
    	try {
			wait(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
