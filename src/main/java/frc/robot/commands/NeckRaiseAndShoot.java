// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.RangeFinder;
import frc.robot.subsystems.neck.Neck;
import frc.robot.util.GCLimelight;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class NeckRaiseAndShoot extends SequentialCommandGroup {
    /** Creates a new NeckRaiseAndShoot. */

    // TODO: Add shooter + Intake code
    public NeckRaiseAndShoot(
            Neck neck, GCLimelight m_Vision, RangeFinder m_Range) { // Shooter shooter, Intake intake, Vision vision) {
        // Add your commands in the addCommands() call, e.g.
        // addCommands(new FooCommand(), new BarCommand());
        addCommands(
                new MoveNeckToRange(neck, m_Vision, m_Range)
                        // .alongWith(new InstantCommand(() -> shooter.setDistanceShooterSpeedFast(), shooter))
                        .withTimeout(2.0),
                // new ShootDistanceStable(neck, shooter, intake),
                new MoveNeck(neck, () -> 1).withTimeout(2));
    }
}
