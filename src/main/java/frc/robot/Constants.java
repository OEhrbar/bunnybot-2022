// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.util.Units;

public final class Constants {
  public final static class DrivetrainConfig {
    public static final int leftDrivePort = 2;
    public static final int rightDrivePort = 4;
    public static final int leftFollowPort = 0;
    public static final int rightFollowPort = 0;

    public static final double driveSpeed = .3;
    public static final double turnSpeed = .2;

    // Multiplied by drive speed when in slow mode
    public static final double slowMultiplier = 0.25;

    public static final double gearRatio = 1 / 7.5833; // Times motor has to rotate for wheel to rotate once
    public static final double wheelDiameterMeters = Units.inchesToMeters(4);
    public static final double rotationToDistanceConversion = (Math.PI * wheelDiameterMeters) * gearRatio; // Encoder rotations to distance moved
    public static final double drivetrainWidthMeters = Units.inchesToMeters(28); // Distance between left and right wheels in meters

    // Path following PID
    //public static final double kP = 4.3789; <- value from SysId
    public static final double pathP = -0.0002;
    public static final double pathI = 0;
    public static final double pathD = 0;

    // Drive distance PID
    public static final double driveDistP = 0.3;
    public static final double driveDistI = 0;
    public static final double driveDistD = 0;

    // Auto turn PID
    public static final double turnP = 0.006;
    public static final double turnI = 0;
    public static final double turnD = 0.0001;

    // Drivetrain characterization (Do not change, found using SysId)
    public static final double kS = 0.17247;
    public static final double kV = 2.8886;
    public static final double kA = 2.1367;

    // Ramsete config (Do not change, defualt values from documentation)
    public static final double kB = 2.0;
    public static final double kZeta = 0.7;
  }

  public final static class RakeConfig {
    public static enum RakeMode {
      Automatic,
      Manual
    }

    public static final double gearRatio = 1 / 1;
    public static final int leftMotorID = 0;
    public static final int rightMotorID = 60;
    public static final double motorSpeed = 1;

    // Preset rake angles (degrees)
    public static final double startAngle = 60;
    public static final double raiseAngle = 15;
    public static final double dispenseAngle = 125;
    public static final double intakeAngle = 0;

    public static final double kP = 0.0005;
    public static final double kI = 0.0;
    public static final double kD = 0.0;
  }

  public final static class ShuffleboardConfig {
    public static final boolean drivetrainPrintsEnabled = true;
    public static final boolean rakePrintsEnabled = true;
  }

  public final static double axisThreshold = 0.1;
}
