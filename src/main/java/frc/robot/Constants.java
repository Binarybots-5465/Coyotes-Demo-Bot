// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;
import frc.robot.PIDGains;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    
    /* HID Constants */
    
    //Set's the name for configureJoysticks() in RobotContainer.java to search for.
    public static String driveJoystickName = "Logitech Dual Action";
    public static String auxJoystickName = "Controller (HORIPAD S)";

    //Default Joystick ports
    public static int defaultDriveJoystickPort = 0;
    public static int defaultAuxJoystickPort = 1;

    //Joystick Axis Values
    public static int driveJoystickRotationAxisNum = 2; //The Joystick class doesn't correctly read the right hand side's input (the driverstation reports it under a different axis)
                                                        // reading the value from the axis number will correct this.
    public static double driveJoystickTurnCoefficient = 0.25;

    //Joystick Button Values
    public static int auxAButton = 1; //Represents the A button on the Horipad Gamecube controller.
    public static int auxXButton = 3; //Represents the X button on the Horipad Gamecube controller.
    public static int auxBButton = 2; //Represents the B button on the Horipad Gamecube controller.

    /* Drive Constants */

    //Talon SRX ID's
    public static final double driveDeadband = 0.01; //1% Deadband.
    public static int[] rightTalonID = new int[]{2, 3}; //Encoder on 1.
    public static int[] leftTalonID = new int[]{4, 5}; //Encoder on 3.

    //Encoder Positions
    public static int leftTalonEncoderID = 2; //The Talon SRX ID that the encoder is mounted to.
    public static int rightTalonEncoderID = 4;
    
    public static double encoderUnitsPerRotation = 4096; //4096 units per rotation given by encoder (per 100ms).
    public static int talonEncoderTimeout = 30; //100 ms timeout for encoder to respond.
    
    public static double driveTrainWheelDiameter = 6; //6 inch wheel diameter.

    //Motion Magic Constants & Gains
    public static double motorPeakVelocity = 5655; //(kMaxRPM  / 600) * (kSensorUnitsPerRotation / kGearRatio)
    public static int initialCruiseVelocity = (int)( motorPeakVelocity * 3/4 ); // 75% top speed.
    public static int initialCruiseAcceleration = (int)( motorPeakVelocity * 3/4 ); //If initialCruiseAcceleration == initialCruiseVelocity, it will take 1 second to get up to speed.
    
    /**
      * PID Gains may have to be adjusted based on the responsiveness of control loop.
      * kF: 1023 represents output value to Talon at 100%
      * Note: Gains calculated from a tool found on CD, available from personal Google Drive (https://docs.google.com/spreadsheets/d/1c2VJxmdmWaVfT8U2ppdRMaNbdX99FHWba4rvoxasips/edit?usp=sharing)
      * 	                                    			           kP   kI   kD    kF             Iz   PeakOut 
      */
    public final static PIDGains distanceGains = new PIDGains( 0.0, 0.0,  0.0, 0.1809018568,  100,  0.50 );
    public final static PIDGains turningGains =  new PIDGains( 0.0, 0.0,  0.0, 0.1809018568,  200,  1.00 );

    //Sets constants for PID Index
    // We allow either a 0 or 1 when selecting a PID Index, where 0 is primary and 1 is auxiliary.
    public final static int PIDPrimary = 0;
    public final static int PIDTurn = 1;
    
    //Sets constants for PID remote sensors
    // We allow either a 0 or 1 when selecting an ordinal for remote devices (You can have up to 2 devices assigned remotely to a SRX/Victor).
    public final static int PIDRemote0 = 0;
    public final static int PIDRemote1 = 1;

    //Sets constants for PID slots used for distance and turning
    // Firmware supports slots [0, 3] and can be used for either PID Set.
    public final static int PIDDistanceSlot = 0;
    public final static int PIDTurningSlot = 1;

    //Sets constant for the motor deadband for PID.
    public final static double PIDNeutralDeadband = 0.04; // 4% deadband.

    //Sets constant for the length of time the PID period runs.
    public final static int PIDClosedLoopTimeMs = 1; //1 ms per loop cycle.

    /* Shooter Constants */

    //Motor ID's
    public final static int shooterLeftMotorTalonID = 6; //If we're using a Talon this will be the CAN ID for the left side.
    public final static int shooterRightMotorPWMID = 0; //The right side is using a VictorSP on PWM channel 0
    
    //Button ID
    public final static int shooterManualRunShooterButton = 3; //This would be the Triangle button on the PS4 controller
}
