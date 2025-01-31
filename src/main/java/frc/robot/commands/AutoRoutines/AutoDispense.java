// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.AutoRoutines;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Constants.RakeConfig;
import frc.robot.commands.drivetrain.DriveDistance;
import frc.robot.commands.drivetrain.TurnDegrees;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Rake;

public class AutoDispense extends SequentialCommandGroup {
  /** Creates a new AutoDispense. */
  public AutoDispense(Drivetrain drivetrain, Rake rake) {
    addCommands(
        new DriveDistance(drivetrain, -0.5).withTimeout(2),
        new TurnDegrees(drivetrain, 180),
        new DriveDistance(drivetrain, -0.5).withTimeout(2),
        new InstantCommand(() -> rake.setSetpoint(RakeConfig.dispenseAngle)));
  }
}
