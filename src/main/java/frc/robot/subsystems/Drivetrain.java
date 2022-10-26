// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants.DrivetrainConfig;
import frc.robot.Constants.ShuffleboardConfig;

import com.kauailabs.navx.frc.AHRS;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.math.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;

public class Drivetrain extends SubsystemBase {
  private final NetworkTableEntry xPosEntry, yPosEntry, headingEntry, leftOutputEntry, rightOutputEntry;

  private final CANSparkMax leftDrive = new CANSparkMax(DrivetrainConfig.leftDrivePort, MotorType.kBrushless);
  private final CANSparkMax rightDrive = new CANSparkMax(DrivetrainConfig.rightDrivePort, MotorType.kBrushless);

  private final CANSparkMax leftFollow = new CANSparkMax(DrivetrainConfig.leftFollowPort, MotorType.kBrushless);
  private final CANSparkMax rightFollow = new CANSparkMax(DrivetrainConfig.rightFollowPort, MotorType.kBrushless);

  private final AHRS gyro = new AHRS();

  private final DifferentialDriveKinematics kinematics = new DifferentialDriveKinematics(
      DrivetrainConfig.drivetrainWidthMeters);
  private final DifferentialDriveOdometry odometry = new DifferentialDriveOdometry(new Rotation2d());

  private SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(DrivetrainConfig.kS, DrivetrainConfig.kV,
      DrivetrainConfig.kA);

  private Pose2d pose;

  public Drivetrain() {
    //leftFollow.follow(leftDrive);
    //rightFollow.follow(rightDrive);

    leftDrive.setInverted(false);
    rightDrive.setInverted(true);

    leftDrive.getEncoder().setPositionConversionFactor(DrivetrainConfig.rotationToDistanceConversion);
    rightDrive.getEncoder().setPositionConversionFactor(DrivetrainConfig.rotationToDistanceConversion);

    ShuffleboardTab driveTab = Shuffleboard.getTab("Drivetrain");
    ShuffleboardLayout odometryLayout = driveTab.getLayout("Odometry", BuiltInLayouts.kList).withSize(2, 5);

    xPosEntry = odometryLayout.add("X", 0.0).getEntry();
    yPosEntry = odometryLayout.add("Y", 0.0).getEntry();
    headingEntry = odometryLayout.add("Heading", 0.0).getEntry();

    leftOutputEntry = driveTab.add("Left Output", 0).getEntry();
    rightOutputEntry = driveTab.add("Right Output", 0).getEntry();

    resetOdometry();
  }

  public void periodic() {
    updateOdometry();
  }

  /** Updates the odometry pose with the heading and position measurements. */
  private void updateOdometry() {
    // DifferentialDriveWheelSpeeds speeds = getMotorSpeeds();
    pose = odometry.update(getHeading(), getLeftDistance(), getRightDistance());

    if (ShuffleboardConfig.drivetrainPrintsEnabled) {
      xPosEntry.setDouble(pose.getX());
      yPosEntry.setDouble(pose.getY());
      headingEntry.setDouble(pose.getRotation().getDegrees());
    }
  }

  public Rotation2d getHeading() {
    return new Rotation2d(-gyro.getAngle() * (Math.PI / 180));
  }

  /** Returns a DifferentialDriveWheelSpeeds object from the encoder velocities. */
  public DifferentialDriveWheelSpeeds getMotorSpeeds() {
    double left = leftDrive.getEncoder().getVelocity();
    double right = rightDrive.getEncoder().getVelocity();

    return new DifferentialDriveWheelSpeeds(left, right);
  }

  public double getLeftDistance() {
    return leftDrive.getEncoder().getPosition();
  }

  public double getRightDistance() {
    return rightDrive.getEncoder().getPosition();
  }

  public SimpleMotorFeedforward getFeedForward() {
    return feedforward;
  }

  public DifferentialDriveKinematics getKinematics() {
    return kinematics;
  }

  public Pose2d getPose() {
    return pose;
  }

  /** Drives the robot with given speeds for left and right wheels.
   * 
   * @param left speed of left wheels
   * @param right speed of right wheels
   */
  public void tankDrive(double left, double right) {
    updateOutputs(left, right);
  }

  /** Drives the robot with given voltages for left and right wheels.
   * 
   * @param left voltage for left wheels
   * @param right voltage for right wheels
   */
  public void tankDriveVolts(double left, double right) {
    updateOutputs(left / 12, right / 12); // Divides by 12 to scale possible inputs between 0 and 1 (12 in max volts)
  }

  /** Drives the robot with given directional and rotational speeds.
   * 
   * @param forward speed in robot's current direction
   * @param turn turn speed (clockwise is positive)
   */
  public void arcadeDrive(double forward, double turn) {
    double left = forward + turn;
    double right = forward - turn;

    updateOutputs(left, right);
  }

  public void updateOutputs(double left, double right) {
    left = Math.max(Math.min(0.2, left), -0.2);
    right = Math.max(Math.min(0.2, left), -0.2);
    ;

    if (ShuffleboardConfig.drivetrainPrintsEnabled) {
      leftOutputEntry.setDouble(left);
      rightOutputEntry.setDouble(right);
    }

    leftDrive.set(left);
    rightDrive.set(right);
  }

  public void stop() {
    updateOutputs(0, 0);
  }

  public void resetOdometry() {
    odometry.resetPosition(new Pose2d(), new Rotation2d());
    gyro.reset();

    leftDrive.getEncoder().setPosition(0);
    rightDrive.getEncoder().setPosition(0);
  }
}
