// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.SensorTerm;
import com.ctre.phoenix.motorcontrol.StatusFrame;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Drive extends SubsystemBase {

    private WPI_TalonSRX leftForwardTalon;
    private WPI_TalonSRX leftBackTalon;
  
    private WPI_TalonSRX rightForwardTalon;
    private WPI_TalonSRX rightBackTalon;
  
    private DifferentialDrive diffDrive;

    public Drive() {

        leftForwardTalon = new WPI_TalonSRX(Constants.leftTalonID[0]);
        leftForwardTalon.configFactoryDefault(); //Resets Talon to prevent unexpected behavor.
    
        leftBackTalon = new WPI_TalonSRX(Constants.leftTalonID[1]);
        leftBackTalon.configFactoryDefault();
    
        rightForwardTalon = new WPI_TalonSRX(Constants.rightTalonID[0]);
        rightForwardTalon.configFactoryDefault();
    
        rightBackTalon = new WPI_TalonSRX(Constants.rightTalonID[1]);
        rightBackTalon.configFactoryDefault();
    
        //Set's phase direction
        leftForwardTalon.setSensorPhase(false); //Set to true if sensor is naturally out of phase. Sets sensor phase for closed loop to function correctly.
        rightForwardTalon.setSensorPhase(true);
    
        //Inverts one side of the chasis
        leftForwardTalon.setInverted(true); //Inverts left master (and slave) to drive straight.
    
        //Set's brake mode
        leftForwardTalon.setNeutralMode(NeutralMode.Brake); //Setting the neutral throttle mode to 'brake' will prevent the motors from being able to continously turn freely.
        leftBackTalon.setNeutralMode(NeutralMode.Brake);
        rightForwardTalon.setNeutralMode(NeutralMode.Brake);
        rightBackTalon.setNeutralMode(NeutralMode.Brake);
    
        //Set followers & inversions
        leftBackTalon.set(ControlMode.Follower, leftForwardTalon.getDeviceID()); //Set the left back talon to be slave to leftForwardTalon.
        leftBackTalon.setInverted(InvertType.FollowMaster); //Follows invert of master.
    
        rightBackTalon.set(ControlMode.Follower, rightForwardTalon.getDeviceID()); //Sets the right back talon to be slave to rightForwardTalon.
        rightBackTalon.setInverted(InvertType.FollowMaster); //Follows invert of master.
    
        //Sets Feedback Device
        leftForwardTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Constants.PIDPrimary, Constants.talonEncoderTimeout); 
        
        //This allows for processing of signals from the other (remote) CAN bus
        rightForwardTalon.configRemoteFeedbackFilter(leftForwardTalon.getDeviceID(), RemoteSensorSource.TalonSRX_SelectedSensor, Constants.PIDRemote0, Constants.talonEncoderTimeout);
        
        //Setup Sum signal to be used for distance
        rightForwardTalon.configSensorTerm(SensorTerm.Sum0, FeedbackDevice.RemoteSensor0, Constants.talonEncoderTimeout); //Feedback Device of Remote Talon
        rightForwardTalon.configSensorTerm(SensorTerm.Sum1, FeedbackDevice.CTRE_MagEncoder_Relative, Constants.talonEncoderTimeout); //Quadrature Encoder of current Talon
    
        //Setup Difference signal to be used for turning
        rightForwardTalon.configSensorTerm(SensorTerm.Diff1, FeedbackDevice.RemoteSensor0, Constants.talonEncoderTimeout);
        rightForwardTalon.configSensorTerm(SensorTerm.Diff0, FeedbackDevice.CTRE_MagEncoder_Relative, Constants.talonEncoderTimeout);
    
        //Configure Sum (Sum of both QuadEncoders) to be used for the Primary PID Index
        rightForwardTalon.configSelectedFeedbackSensor(FeedbackDevice.SensorSum, Constants.PIDPrimary, Constants.talonEncoderTimeout);
    
        //Scale Feedback by 0.5 to half the sum of Distance (There are 2 encoders, so dividing the sum of both by 2 will get the average value)
        rightForwardTalon.configSelectedFeedbackCoefficient(0.5, Constants.PIDPrimary, Constants.talonEncoderTimeout);
    
        //Configure Difference (Difference between both Quad Encoders) to be used for Auxiliary PID Index
        rightForwardTalon.configSelectedFeedbackSensor(FeedbackDevice.SensorDifference, Constants.PIDTurn, Constants.talonEncoderTimeout);
    
        //Scale the Feedback Sensor using a coefficient
        rightForwardTalon.configSelectedFeedbackCoefficient(1, Constants.PIDTurn, Constants.talonEncoderTimeout);
    
        //Set status frame periods to ensure stale data isn't returned
        rightForwardTalon.setStatusFramePeriod(StatusFrame.Status_12_Feedback1, 20, Constants.talonEncoderTimeout);
        rightForwardTalon.setStatusFramePeriod(StatusFrame.Status_13_Base_PIDF0, 20, Constants.talonEncoderTimeout);
        rightForwardTalon.setStatusFramePeriod(StatusFrame.Status_14_Turn_PIDF1, 20, Constants.talonEncoderTimeout);
        rightForwardTalon.setStatusFramePeriod(StatusFrame.Status_10_Targets, 20, Constants.talonEncoderTimeout);
        leftForwardTalon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 5, Constants.talonEncoderTimeout);
    
        //Configure motor's neutral value deadband
        rightForwardTalon.configNeutralDeadband(Constants.PIDNeutralDeadband, Constants.talonEncoderTimeout);
        leftForwardTalon.configNeutralDeadband(Constants.PIDNeutralDeadband, Constants.talonEncoderTimeout);
            
        //Set's the minimium initial cruise acceleration and velocity measurements
        rightForwardTalon.configMotionAcceleration(Constants.initialCruiseAcceleration, Constants.talonEncoderTimeout);
        rightForwardTalon.configMotionCruiseVelocity(Constants.initialCruiseVelocity, Constants.talonEncoderTimeout);
    
        //Max out the motor's peak output (in percent, for all control modes)
        leftForwardTalon.configPeakOutputForward(+1.0, Constants.talonEncoderTimeout);
        leftForwardTalon.configPeakOutputReverse(-1.0, Constants.talonEncoderTimeout);
        rightForwardTalon.configPeakOutputForward(+1.0, Constants.talonEncoderTimeout);
        rightForwardTalon.configPeakOutputReverse(-1.0, Constants.talonEncoderTimeout);
    
        //Sets FPID Gains for distance servo
        rightForwardTalon.config_kP(Constants.PIDDistanceSlot, Constants.distanceGains.kP, Constants.talonEncoderTimeout);
        rightForwardTalon.config_kI(Constants.PIDDistanceSlot, Constants.distanceGains.kI, Constants.talonEncoderTimeout);
        rightForwardTalon.config_kD(Constants.PIDDistanceSlot, Constants.distanceGains.kD, Constants.talonEncoderTimeout);
        rightForwardTalon.config_kF(Constants.PIDDistanceSlot, Constants.distanceGains.kF, Constants.talonEncoderTimeout);
        rightForwardTalon.config_IntegralZone(Constants.PIDDistanceSlot, Constants.distanceGains.kIzone, Constants.talonEncoderTimeout);
        rightForwardTalon.configClosedLoopPeakOutput(Constants.PIDDistanceSlot, Constants.distanceGains.kPeakOutput, Constants.talonEncoderTimeout);
        rightForwardTalon.configAllowableClosedloopError(Constants.PIDDistanceSlot, 0, Constants.talonEncoderTimeout);
        
        /**
         * Sets the time taken for PID loop
         * - If sensor updates are too slow.
         * - Sensor deltas are very small per update, so derivative error never gets large enough to be useful.
         * - Sensor movement is very slow causing the derivative error to be near zero.
         */
        rightForwardTalon.configClosedLoopPeriod(0, Constants.PIDClosedLoopTimeMs, Constants.talonEncoderTimeout);
        rightForwardTalon.configClosedLoopPeriod(1, Constants.PIDClosedLoopTimeMs, Constants.talonEncoderTimeout);
    
        //False means the SRX's local output is PID0 + PID1, and the SRX on the other side is PID0 - PID1
        // True means the SRX's local output is PID0 - PID1, and the SRX on the other side is PID0 + PID1.
        rightForwardTalon.configAuxPIDPolarity(false, Constants.talonEncoderTimeout);

        zeroEncoders();
    
        diffDrive = new DifferentialDrive(leftForwardTalon, rightForwardTalon);
        diffDrive.setRightSideInverted(false); //WPI assumes that the left and right are opposite, this allows the motors to both move forward when applying positive output.
    }

    /**
     * Sets the raw speed of the drivetrain using an arcade style.
     * @param forw Value from -1.0 to 1.0 representing the forward (x-axis) percentage of the drivetrain's speed
     * @param rot  Value from -1.0 to 1.0 representing the rotation around the z-axis (CW is postive)
     */
    public void setRaw(double forw, double rot) {

        diffDrive.arcadeDrive(forw, -1 * rot, true); //Arcade drive expects a clockwise value to determine the different power to give to each side of the robot
                                                     // but our joystick gives us values in CCW so we negate the z rotation value (rot) to give us our expected rotation value.
    }

    /**
     * Zero the Talon SRX's quadrature encoders.
     */
    public void zeroEncoders() {

        leftForwardTalon.getSensorCollection().setQuadraturePosition(0, Constants.talonEncoderTimeout);
        rightForwardTalon.getSensorCollection().setQuadraturePosition(0, Constants.talonEncoderTimeout);
        System.out.println("All SRX drive encoders zeroed.");
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}