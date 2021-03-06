// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.cameraserver.CameraServer;

import java.util.ArrayList;

import frc.robot.commands.*;
import frc.robot.subsystems.*;


/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();

  private final ExampleCommand m_autoCommand = new ExampleCommand(m_exampleSubsystem);

  //Subsystems
  private final Drive m_driveSubsystem = new Drive();
  private final Shooter m_shooterSubsystem = new Shooter();

  //HID
  public Joystick m_driveJoystick = new Joystick(Constants.defaultDriveJoystickPort); //Defaults to ports set in Constants if configureJoysticks() can't find it later.
  public Joystick m_auxJoystick = new Joystick(Constants.defaultAuxJoystickPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    CameraServer.getInstance().startAutomaticCapture();

    //Configure the Joysticks and button bindings.
    configureJoysticks();

    /* Set default commands for subsystems */

    //Arcade-type system for driving. Left joystick for FB. Right joystick for turning.
    m_driveSubsystem.setDefaultCommand(
        new RunCommand(() -> m_driveSubsystem
            .setRaw(m_driveJoystick.getY(GenericHID.Hand.kLeft),
                    m_driveJoystick.getRawAxis( Constants.driveJoystickRotationAxisNum )), m_driveSubsystem));

    JoystickButton manualToggleShooterWheelsButton = new JoystickButton(m_driveJoystick, Constants.driveBButton);
    manualToggleShooterWheelsButton.whenPressed( () -> m_shooterSubsystem.toggleShooterPercentWheelSpeed(Constants.shooterMotorsMaxSpeed) ); //Manually toggles the shooter's wheels.

    JoystickButton manualToggleTriggerButton = new JoystickButton(m_driveJoystick, Constants.driveAButton); 
    manualToggleTriggerButton.whenPressed( () -> m_shooterSubsystem.toggleShooterTrigger() ); //Manually toggles the shooter's trigger.
    
    JoystickButton runShooterFullSpeedDiscreteSemiAutoButton = new JoystickButton(m_driveJoystick, Constants.driveXButton);
    runShooterFullSpeedDiscreteSemiAutoButton.whenPressed( new ShooterVariableSpeedSemiAuto(m_shooterSubsystem, Constants.shooterMotorsMaxSpeed), false ); //Runs the semi-auto shooter command at full speed & can't be interupted.

    Trigger shooterEStopTrigger = new JoystickButton(m_driveJoystick, Constants.driveLeftShoulder)
                                    .or(new JoystickButton(m_driveJoystick, Constants.driveRightShoulder));
    shooterEStopTrigger.whenActive( () -> m_shooterSubsystem.setShooterPercentWheelSpeed(0) ); //Immediately stops the shooter wheels when the drive controller's shoulder buttons are pressed.
  }

  /**
   * Automatically finds the needed joysticks
   */
  private void configureJoysticks() {

    DriverStation ds = DriverStation.getInstance();

    //Automatically searches for the joysticks by name, if the names can not be found it notifies the user.
    ArrayList<String> foundJoysticksName = new ArrayList<String>();
    for(int port = 0; port <= 5; port++) {
      String jName = ds.getJoystickName(port);
      System.out.println("Registered DS Joystick: " + jName);

      if(jName.equals(Constants.driveJoystickName)) { //Found Driver Joystick.
        foundJoysticksName.add(jName);
        System.out.println("Found Drive Joystick: " + jName + " on port " + port);
      }
      if(jName.equals(Constants.auxJoystickName)) { //Found Aux Joystick.
        foundJoysticksName.add(jName);
        System.out.println("Found Aux Joystick: " + jName + " on port " + port);
      }
    }
    if(foundJoysticksName.size() < 2) {
      System.out.print("Only found " + foundJoysticksName.size() + " joystick(s):");
      for(String arrString : foundJoysticksName) { System.out.print(" " + arrString);}

      System.out.println("\n Make sure you can see both (" + Constants.driveJoystickName + ") and (" + Constants.auxJoystickName + ") in the driverstation!");
      System.out.println("Assigned the Drive joystick to PORT " + Constants.defaultDriveJoystickPort +" & Aux joystick to PORT " + Constants.defaultAuxJoystickPort +".");
    }
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An ExampleCommand will run in autonomous
    return m_autoCommand;
  }
}
