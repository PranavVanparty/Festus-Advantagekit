// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants.NeckConstants;
import frc.robot.subsystems.Neck;
import frc.robot.subsystems.RangeFinder;
import frc.robot.util.GCLimelight;

public class MoveNeckToRange extends Command {
  // Get rid of later
  /** Creates a new SetNeckAngle. */
  private final Neck m_Neck;
  Rotation2d m_target;
  private double m_neckAngle;
  private GCLimelight m_Vision;
  private RangeFinder m_Range;

  public MoveNeckToRange(Neck neck, GCLimelight m_Vision, RangeFinder m_Range) {
    // Use addRequirements() here to declare subsystem dependencies.
    m_Neck = neck;
    this.m_Vision = m_Vision;
    this.m_Range = m_Range;
    addRequirements(m_Neck);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    double distance = 0;
    distance = m_Vision.getTargetDistanceX();
    m_neckAngle = m_Range.getNeckAngle(distance);
    //Insert the get distance from speaker than this subsystem should set neck angle
    //m_neckAngle = m_Neck.getFromDashboard();
    //SmartDashboard.putNumber("Target Neck Angle", m_neckAngle); 
    //SmartDashboard.putNumber("Speaker Distance", distance);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if((m_neckAngle > NeckConstants.KEncoderDeadbandThreshold)
    && (m_neckAngle < NeckConstants.kEncoderUpperThreshold))
    {
      if((m_Neck.getNeckAngle() > m_neckAngle)&& getError() > 0.01) {
        m_Neck.moveTo(m_neckAngle);
      }
      if((m_Neck.getNeckAngle() < m_neckAngle)&& getError() > 0.01) {
        m_Neck.moveTo(m_neckAngle);
      }
      SmartDashboard.putString("RunningNeck:", "MovingToAngle " + m_neckAngle);
    }
    else
    {
      m_Neck.stop();
    }
  } 

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_Neck.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return getError() < 0.002;
  }

  private double getError() {
    double error = Math.abs(m_Neck.getNeckAngle() - m_neckAngle) ;
    SmartDashboard.putNumber("Neck Error", error);
    return error;
  }


}
