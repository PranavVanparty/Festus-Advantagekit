// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Outtake extends SubsystemBase {

    SparkMax m_outtake = new SparkMax(10, MotorType.kBrushless);
    private final AbsoluteEncoder m_outEncoder;

    /** Creates a new Shooter. */
    public Outtake() {
        m_outEncoder = m_outtake.getAbsoluteEncoder();
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public void setSpeed(double speed) {
        m_outtake.set(speed);
    }
}
