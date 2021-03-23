// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DoubleSolenoid;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Shooter extends SubsystemBase {
  
  private WPI_TalonSRX leftSideMotor = new WPI_TalonSRX(Constants.shooterLeftMotorTalonID);
  private VictorSP rightSideMotor = new VictorSP(Constants.shooterRightMotorPWMID);
  private DoubleSolenoid triggerSolenoid = new DoubleSolenoid(Constants.shooterPCMTriggerFowardPortID, Constants.shooterPCMTriggerReversePortID);

  public Shooter() {
    rightSideMotor.setInverted(true); //Invert right side for unified direction
    triggerSolenoid.set(Constants.shooterTriggerLoadPosition);
  }

  /**
   * Sets the speed of the shooter mechanism
   * @param percentSpeed Speed range for both motors from 0.0 - 1.0
   */
  public void setShooterPercentWheelSpeed(double percentSpeed) {
    leftSideMotor.set(percentSpeed);
    rightSideMotor.set(percentSpeed);
  }

  /**
   * Toggles the shooter's pneumatic trigger
   */
  public void toggleShooterTrigger() {
    DoubleSolenoid.Value currentPosition = triggerSolenoid.get();
    if(currentPosition == DoubleSolenoid.Value.kReverse)
      triggerSolenoid.set(DoubleSolenoid.Value.kForward);
    else
      triggerSolenoid.set(DoubleSolenoid.Value.kReverse);
  }

  /**
   * Toggles the speed of the shooter's mechanism based on the value provided and 0 
   * @param percentSpeed Percent of speed range of the motors 1-0.0 
   */
  public void toggleShooterPercentWheelSpeed(double percentSpeed) {
    if(getShooterMotorPercentSpeed() > 0)
      setShooterPercentWheelSpeed(0);
    else
      setShooterPercentWheelSpeed(percentSpeed);
  }

  /**
   * Sets the shooter trigger's position
   * @param position The solenoid position, either kForward or kReverse. 
   *    The piston for the trigger is facing the opposite of the direction the puck is supposted to face, 
   *    so to shoot the puck out you need to set the position to kReverse
   */
  public void setShooterTriggerPosition(DoubleSolenoid.Value position) {
    triggerSolenoid.set(position);
  }

  /**
   * Gets the shooter's motors set percent output
   */
  public double getShooterMotorPercentSpeed() {
    return ( leftSideMotor.get() + rightSideMotor.get() ) / 2;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
