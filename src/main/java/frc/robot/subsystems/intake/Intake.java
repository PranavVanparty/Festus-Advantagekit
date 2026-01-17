// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems.intake;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Intake extends SubsystemBase {
    private final MotorController m_intakeMotor = new SparkMax(9, MotorType.kBrushless);
    private final DigitalInput m_noteSwitch = new DigitalInput(8);
    /** Creates a new Intake. */
    public Intake() {}

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public void setPickupSpeed(double speed) {
        m_intakeMotor.set(speed);
    }

    public void setPickupSpeedSlow() {
        m_intakeMotor.set(0.2);
    }

    // TODO: fix ringIsPresent and ringInPlace methods
    public boolean NoteIsPresent() {
        boolean ringIsPresent = !m_noteSwitch.get();
        // boolean ringIsPresent = false;
        // Uses switches on sides to determine if true
        return ringIsPresent;
    }

    public boolean NoteInPlace() {
        // boolean ringInPlace = !m_noteSensor.get();
        boolean ringInPlace = false;
        // Uses vision sensor to determine if true
        return ringInPlace;
    }

    public void stop() {
        m_intakeMotor.stopMotor();
    }

    public void setFeedSpeed() {
        m_intakeMotor.set(0.5);
    }

    public void setReverseSpeed() {
        m_intakeMotor.set(-0.5);
    }
}
