// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {

    SparkMax m_intake = new SparkMax(9, MotorType.kBrushless);
    private final AbsoluteEncoder m_inEncoder;

    private final DigitalInput m_switch = new DigitalInput(8); // switch
    private final DigitalInput m_sensor = new DigitalInput(9); // sensor
    /** Creates a new Shooter. */
    public Intake() {
        m_inEncoder = m_intake.getAbsoluteEncoder();
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public void setSpeed(double speed) {
        m_intake.set(speed);
    }
}
