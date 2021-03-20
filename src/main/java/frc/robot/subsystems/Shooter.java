// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  
  private WPI_TalonSRX leftSideMotor = new WPI_TalonSRX(Constants.shooterLeftMotorTalonID);
  private VictorSP rightSideMotor = new VictorSP(Constants.shooterRightMotorPWMID);

  public Shooter() {
    rightSideMotor.setInverted(true); //The right side goes the opposite to correct
  }

  /**
   * Sets the speed of the shooter mechanism and displays it on the WPILIB Smart Dashboard
   * @param percentSpeed Speed range for both motors from 0.0 - 1.0
   */
  public void setPercentAndUpdateSmartDashBoard(double percentSpeed) {
    leftSideMotor.set(percentSpeed);
    rightSideMotor.set(percentSpeed);
    SmartDashboard.putNumber("Shooter Percent", percentSpeed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
