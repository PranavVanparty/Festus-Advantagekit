// Copyright 2021-2024 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot;

import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Meters;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static frc.robot.subsystems.vision.VisionConstants.camera0Name;
import static frc.robot.subsystems.vision.VisionConstants.camera1Name;
import static frc.robot.subsystems.vision.VisionConstants.robotToCamera0;
import static frc.robot.subsystems.vision.VisionConstants.robotToCamera1;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.button.CommandPS4Controller;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine;
import frc.robot.Constants.OIConstants;
import frc.robot.commands.DriveCommands;
import frc.robot.commands.FaceAprilTag;
import frc.robot.commands.neckCommands.MoveNeck;
import frc.robot.commands.neckCommands.NeckRaiseAndShoot;
import frc.robot.commands.neckCommands.NeckStable;
import frc.robot.subsystems.RangeFinder;
import frc.robot.subsystems.drive.Drive;
import frc.robot.subsystems.drive.DriveConstants;
import frc.robot.subsystems.drive.GyroIO;
import frc.robot.subsystems.drive.GyroIOPigeon2;
import frc.robot.subsystems.drive.GyroIOSim;
import frc.robot.subsystems.drive.ModuleIO;
import frc.robot.subsystems.drive.ModuleIOSim;
import frc.robot.subsystems.drive.ModuleIOSpark;
import frc.robot.subsystems.neck.Neck;
import frc.robot.subsystems.neck.NeckIO;
import frc.robot.subsystems.neck.NeckIOSim;
import frc.robot.subsystems.neck.NeckIOSpark;
import frc.robot.subsystems.vision.Vision;
import frc.robot.subsystems.vision.VisionConstants;
import frc.robot.subsystems.vision.VisionIO;
import frc.robot.subsystems.vision.VisionIOLimelight;
import frc.robot.subsystems.vision.VisionIOPhotonVisionSim;
import frc.robot.util.GCLimelight;
import org.ironmaple.simulation.SimulatedArena;
import org.ironmaple.simulation.drivesims.SwerveDriveSimulation;
import org.ironmaple.simulation.seasonspecific.crescendo2024.Arena2024Crescendo;
import org.ironmaple.simulation.seasonspecific.crescendo2024.CrescendoNoteOnField;
import org.ironmaple.simulation.seasonspecific.crescendo2024.NoteOnFly;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.LoggedDashboardChooser;

/**
 * This class is where the bulk of the robot should be declared. Since Command-based is a "declarative" paradigm, very
 * little robot logic should actually be handled in the {@link Robot} periodic methods (other than the scheduler calls).
 * Instead, the structure of the robot (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
    // Subsystems
    private final Drive drive;
    private final Vision vision;
    private SwerveDriveSimulation driveSimulation = null;

    // Controller
    private final CommandXboxController driver = new CommandXboxController(0);
    private final CommandXboxController gunner = new CommandXboxController(1);
    private final CommandPS4Controller pranav = new CommandPS4Controller(2);

    // Dashboard inputs
    private final LoggedDashboardChooser<Command> autoChooser;
    private final Neck m_Neck;
    private final GCLimelight m_Vision = new GCLimelight("limelight-gcc");
    private final RangeFinder m_Range = new RangeFinder();

    // The driver's controller
    XboxController m_driverController = new XboxController(OIConstants.kDriverControllerPort);
    XboxController m_gunnerController = new XboxController(OIConstants.kGunnerControllerPort);

    /** The container for the robot. Contains subsystems, OI devices, and commands. */
    public RobotContainer() {
        switch (Constants.currentMode) {
            case REAL:
                // Real robot, instantiate hardware IO implementations
                drive = new Drive(
                        new GyroIOPigeon2(),
                        new ModuleIOSpark(0),
                        new ModuleIOSpark(1),
                        new ModuleIOSpark(2),
                        new ModuleIOSpark(3),
                        (pose) -> {});

                this.vision = new Vision(
                        drive,
                        new VisionIOLimelight(VisionConstants.camera0Name, drive::getRotation),
                        new VisionIOLimelight(VisionConstants.camera1Name, drive::getRotation));

                m_Neck = new Neck(new NeckIOSpark());
                break;
            case SIM:
                // create a maple-sim swerve drive simulation instance

                SimulatedArena.overrideInstance(new Arena2024Crescendo()); // * set the field to Crescendo 2024
                this.driveSimulation =
                        new SwerveDriveSimulation(DriveConstants.mapleSimConfig, new Pose2d(3, 3, new Rotation2d()));
                // add the simulated drivetrain to the simulation field
                SimulatedArena.getInstance().addDriveTrainSimulation(driveSimulation);
                // Sim robot, instantiate physics sim IO implementations
                drive = new Drive(
                        new GyroIOSim(driveSimulation.getGyroSimulation()),
                        new ModuleIOSim(driveSimulation.getModules()[0]),
                        new ModuleIOSim(driveSimulation.getModules()[1]),
                        new ModuleIOSim(driveSimulation.getModules()[2]),
                        new ModuleIOSim(driveSimulation.getModules()[3]),
                        driveSimulation::setSimulationWorldPose);

                vision = new Vision(
                        drive,
                        new VisionIOPhotonVisionSim(
                                camera0Name, robotToCamera0, driveSimulation::getSimulatedDriveTrainPose),
                        new VisionIOPhotonVisionSim(
                                camera1Name, robotToCamera1, driveSimulation::getSimulatedDriveTrainPose));

                m_Neck = new Neck(new NeckIOSim());
                break;
            default:
                // Replayed robot, disable IO implementations
                drive = new Drive(
                        new GyroIO() {},
                        new ModuleIO() {},
                        new ModuleIO() {},
                        new ModuleIO() {},
                        new ModuleIO() {},
                        (pose) -> {});
                vision = new Vision(drive, new VisionIO() {}, new VisionIO() {});
                m_Neck = new Neck(new NeckIO() {});
                break;
        }

        // Set up auto routines
        autoChooser = new LoggedDashboardChooser<>("Auto Choices", AutoBuilder.buildAutoChooser());

        // Set up SysId routines
        autoChooser.addOption("Drive Wheel Radius Characterization", DriveCommands.wheelRadiusCharacterization(drive));
        autoChooser.addOption("Drive Simple FF Characterization", DriveCommands.feedforwardCharacterization(drive));
        autoChooser.addOption(
                "Drive SysId (Quasistatic Forward)", drive.sysIdQuasistatic(SysIdRoutine.Direction.kForward));
        autoChooser.addOption(
                "Drive SysId (Quasistatic Reverse)", drive.sysIdQuasistatic(SysIdRoutine.Direction.kReverse));
        autoChooser.addOption("Drive SysId (Dynamic Forward)", drive.sysIdDynamic(SysIdRoutine.Direction.kForward));
        autoChooser.addOption("Drive SysId (Dynamic Reverse)", drive.sysIdDynamic(SysIdRoutine.Direction.kReverse));

        // Configure the button bindings
        configureButtonBindings();
    }

    /**
     * Use this method to define your button->command mappings. Buttons can be created by instantiating a
     * {@link GenericHID} or one of its subclasses ({@link edu.wpi.first.wpilibj.Joystick} or {@link controller}), and
     * then passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
     */
    private void configureButtonBindings() {
        // Default command, normal field-relative drive
        // drive.setDefaultCommand(DriveCommands.joystickDrive(
        //         drive,
        //         (Math.abs(pranav.getLeftY()) > 0.03) ? () -> 0 : () -> pranav.getLeftY(),
        //         (Math.abs(pranav.getLeftX()) > 0.03) ? () -> 0 : () -> pranav.getLeftX(),
        //         (Math.abs(pranav.getRightX()) > 1) ? () -> 0 : () -> -pranav.getRightX()));

        drive.setDefaultCommand(DriveCommands.joystickDrive(
                drive,
                (Math.abs(driver.getLeftY()) > 0.03) ? () -> 0 : () -> driver.getLeftY(),
                (Math.abs(driver.getLeftX()) > 0.03) ? () -> 0 : () -> driver.getLeftX(),
                (Math.abs(driver.getRightX()) > 1) ? () -> 0 : () -> -driver.getRightX()));

        m_Neck.setDefaultCommand(new NeckStable(m_Neck));

        // Reset gyro / odometry
        final Runnable resetGyro = Constants.currentMode == Constants.Mode.SIM
                ? () -> drive.resetOdometry(
                        driveSimulation
                                .getSimulatedDriveTrainPose()) // reset odometry to actual robot pose during simulation
                : () -> drive.resetOdometry(
                        new Pose2d(drive.getPose().getTranslation(), new Rotation2d())); // zero gyro
        driver.start().onTrue(Commands.runOnce(resetGyro, drive).ignoringDisable(true));

        if (Constants.currentMode == Constants.Mode.SIM) {
            // * Shoots note from shooter
            pranav.L1().onTrue(Commands.runOnce((() -> {
                SimulatedArena.getInstance()
                        .addGamePieceProjectile(new NoteOnFly(
                                driveSimulation.getSimulatedDriveTrainPose().getTranslation(),
                                new Translation2d(0.12, 0),
                                driveSimulation.getDriveTrainSimulatedChassisSpeedsFieldRelative(),
                                driveSimulation.getSimulatedDriveTrainPose().getRotation(),
                                Meters.of(0.413),
                                MetersPerSecond.of(10),
                                Degrees.of(59.6)));
            })));
            // * create note on field
            pranav.R1().onTrue(Commands.runOnce(() -> SimulatedArena.getInstance()
                    .addGamePiece(new CrescendoNoteOnField(new Translation2d(3, 3)))));
        }
        // Change to whileTrue after re-maping for climer
        new Trigger(() -> m_gunnerController.getLeftY() != 0)
                .whileTrue(new MoveNeck(m_Neck, () -> -m_gunnerController.getLeftY()));

        // Test the neck raise to range
        new JoystickButton(m_gunnerController, Button.kX.value)
                .onTrue(new NeckRaiseAndShoot(m_Neck, m_Vision, m_Range));

        // Test the limelight face april tag code
        new JoystickButton(m_driverController, Button.kA.value).whileTrue(new FaceAprilTag(m_Vision, drive));

        // * PRANAV's Controller Bindings for Neck and Vision Testing
        new Trigger(() -> pranav.getLeftY() != 0).whileTrue(new MoveNeck(m_Neck, () -> -m_gunnerController.getLeftY()));

        // Test the neck raise to range
        pranav.square().onTrue(new NeckRaiseAndShoot(m_Neck, m_Vision, m_Range));

        // Test the limelight face april tag code
        pranav.cross().whileTrue(new FaceAprilTag(m_Vision, drive));

        //  .onTrue(new NeckRaiseAndShoot(m_Neck, 0.0887+0.004, m_robotShooter, m_robotIntake));
        // .onTrue(new NeckRaiseAndShoot(m_Neck, m_robotShooter, m_robotIntake, m_noteVision));

        //         new Trigger(() -> m_gunnerController.getLeftY() < -0.5).whileTrue(new MoveNeckUp(m_Neck));

        //         new Trigger(() -> m_gunnerController.getLeftY() > 0.5).whileTrue(new MoveNeckDown(m_Neck));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {
        return autoChooser.get();
    }

    public void resetSimulationField() {
        if (Constants.currentMode != Constants.Mode.SIM) return;

        drive.resetOdometry(new Pose2d(3, 3, new Rotation2d()));
        SimulatedArena.getInstance().resetFieldForAuto();
    }

    public void updateSimulation() {
        if (Constants.currentMode != Constants.Mode.SIM) return;
        SimulatedArena.getInstance().simulationPeriodic();
        Logger.recordOutput("FieldSimulation/RobotPosition", driveSimulation.getSimulatedDriveTrainPose());
        Logger.recordOutput("ZeroedComponentPoses", new Pose3d[] {new Pose3d()});
        Logger.recordOutput("finalComponentPoses", new Pose3d[] {
            new Pose3d(
                    -0.33, 0, 0.43, new Rotation3d(0, 0, m_Neck.getNeckAngle())) // Math.sin(Timer.getTimestamp()), 0))
        });
        // Logger.recordOutput(
        //         "FieldSimulation/Coral", SimulatedArena.getInstance().getGamePiecesArrayByType("Coral"));
        // Logger.recordOutput(
        //         "FieldSimulation/Algae", SimulatedArena.getInstance().getGamePiecesArrayByType("Algae"));
        Logger.recordOutput(
                "FieldSimulation/Notes", SimulatedArena.getInstance().getGamePiecesArrayByType("Note"));
    }
}
