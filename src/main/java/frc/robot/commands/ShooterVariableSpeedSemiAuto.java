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

      new InstantCommand( () -> shooterInstance.setShooterPercentWheelSpeed(shooterSpeed), shooterInstance), //Run the shooter wheels

      new WaitCommand(shooterInstance.getShooterMotorPercentSpeed() == 0 ? Constants.shooterTriggerSpeedUpTime : 0), //Wait # seconds for the wheels to speed up before shooting out if the shooter's motors are not already moving

      new InstantCommand( () -> shooterInstance.setShooterTriggerPosition(Constants.shooterTriggerShootOutwardPosition), shooterInstance), //Activate the trigger pushing the puck out

      new WaitCommand(Constants.shooterTriggerSettlingTime), //Wait # seconds for the pucks to settle

      new InstantCommand( () -> shooterInstance.setShooterTriggerPosition(Constants.shooterTriggerLoadPosition), shooterInstance), //Retract the trigger & load the next available puck

      new WaitCommand(Constants.shooterTriggerSettlingTime)
    );
  }
}
