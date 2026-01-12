// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class RobotContainer {
    // Pick the correct port from Driver Station USB tab (commonly 0).
    private final CommandPS4Controller controller = new CommandPS4Controller(0);

    public RobotContainer() {
        configureBindings();
    }

    private void configureBindings() {
        controller.cross().onTrue(new InstantCommand(() -> System.out.print("South")));
        controller.square().onTrue(new InstantCommand(() -> System.out.print("East")));
        controller.circle().onTrue(new InstantCommand(() -> System.out.print("West")));
        controller.triangle().onTrue(new InstantCommand(() -> System.out.print("North")));

        controller.pov(0).onTrue(new InstantCommand(() -> System.out.print("POV Up")));
        controller.pov(90).onTrue(new InstantCommand(() -> System.out.print("POV Right")));
        controller.pov(180).onTrue(new InstantCommand(() -> System.out.print("POV down")));
        controller.pov(270).onTrue(new InstantCommand(() -> System.out.print("POV Left")));

        new Trigger(() -> Math.abs(controller.getRightX()) > 0.09)
                .onTrue(new RunCommand(() -> System.out.println(controller.getRightX())));
        new Trigger(() -> Math.abs(controller.getRightY()) > 0.09)
                .onTrue(new RunCommand(() -> System.out.println(controller.getRightY())));

        new Trigger(() -> Math.abs(controller.getLeftX()) > 0.09)
                .onTrue(new RunCommand(() -> System.out.println(controller.getLeftX())));
        new Trigger(() -> Math.abs(controller.getLeftY()) > 0.09)
                .onTrue(new RunCommand(() -> System.out.println(controller.getLeftY())));
    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
