  // Copyright (c) FIRST and other WPILib contributors.
  // Open Source Software; you can modify and/or share it under the terms of
  // the WPILib BSD license file in the root directory of this project.

  package frc.robot.commands;

  import edu.wpi.first.wpilibj2.command.Command;
  import frc.robot.util.GCLimelight;
  import frc.robot.subsystems.drive.Drive;

  public class FaceAprilTag extends Command {

    private GCLimelight m_Vision;
    private double targetY = 0;
    private double targetX = 2.0;
    private Drive drive;

    public FaceAprilTag(GCLimelight m_Vision, Drive drive) {
      this.m_Vision = m_Vision;
      this.drive = drive;
      // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {}

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
      if(!m_Vision.validTarget()) {
        DriveCommands.joystickDrive(drive, () -> 0, () -> 0, () -> 0);
        return;
      }
      double yaw = m_Vision.getChosenTargetYawDegrees(4);
      
      if(yaw != 0) {
        DriveCommands.joystickDrive(drive, () -> 0, () -> 0, () -> (yaw*-1)/180);
      } 
      else {
        DriveCommands.joystickDrive(drive, () -> 0, () -> 0, () -> 0);
      }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {}

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
      return false;
    }

    
  }