// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.*;
import frc.robot.Constants;
import frc.robot.subsystems.Shooter;

public class ShooterVariableSpeedSemiAuto extends SequentialCommandGroup {

  public ShooterVariableSpeedSemiAuto(Shooter shooterInstance, double shooterSpeed) {
    addCommands(
      new InstantCommand( () -> shooterInstance.setShooterTriggerPosition(Constants.shooterTriggerLoadPosition), shooterInstance), //Retract the trigger (in case of something outside of this command affecting the position of the piston)

      new ConditionalCommand(
        new SequentialCommandGroup( //Run the shooter if not already running
          new InstantCommand( () -> shooterInstance.setShooterPercentWheelSpeed(shooterSpeed), shooterInstance), //Run the shooter wheels
          new WaitCommand(Constants.shooterTriggerSpeedUpTime) //Wait # seconds for the wheels to speed up before shooting out if the shooter's motors are not already moving
          ),
        new WaitCommand(0), //Skip
        () -> ( shooterInstance.getShooterMotorPercentSpeed() == 0 ? true : false ) //If the shooter is at 0% (-> true) run the shooter & wait for startup, else -> continue
      ),
      
      new InstantCommand( () -> shooterInstance.setShooterTriggerPosition(Constants.shooterTriggerShootOutwardPosition), shooterInstance), //Activate the trigger pushing the puck out

      new WaitCommand(Constants.shooterTriggerSettlingTime), //Wait # seconds for the pucks to settle

      new InstantCommand( () -> shooterInstance.setShooterTriggerPosition(Constants.shooterTriggerLoadPosition), shooterInstance), //Retract the trigger & load the next available puck

      new WaitCommand(Constants.shooterTriggerSettlingTime)
    );
  }
}
