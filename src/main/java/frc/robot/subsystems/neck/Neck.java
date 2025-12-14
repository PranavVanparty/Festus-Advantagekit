// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.
// EDIT PORTS; create code!
package frc.robot.subsystems.neck;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.subsystems.encoder.EncoderIO;
import frc.robot.subsystems.neck.NeckIO.NeckIOInputs;
import java.util.Map;

public class Neck extends SubsystemBase {
    /** Creates a new Neck. */
    private final NeckIO io;

    public boolean slowNeck;

    private final ShuffleboardTab m_neckTab = Shuffleboard.getTab("Neck");
    private final GenericEntry m_neckAngle;
    private NeckIOInputs m_neckInputs = new NeckIOInputs();

    public Neck(NeckIO io) {
        this.io = io;

        m_neckAngle = m_neckTab
                .add("Max Speed", 0.01)
                .withWidget(BuiltInWidgets.kNumberSlider) // specify the widget here
                .withProperties(Map.of(
                        "min", 0.0,
                        "max", 0.5)) // specify widget properties here
                .getEntry();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Neck Encoder:", io.getNeckEncoder().getPosition());
        io.updateInputs(m_neckInputs);
    }

    public boolean AtMaxHeight() {
        return io.getNeckAngle() < 0.65;
    }

    public boolean AtMinHeight() {
        return io.getNeckAngle() > 1.1;
    }

    public double getNeckAngle() {
        return io.getNeckAngle();
    }

    public EncoderIO getNeckEncoder() {
        return io.getNeckEncoder();
    }

    public void stop() {
        io.stop();
    }

    public void move(double kneckreversespeed) {
        io.move(kneckreversespeed);
    }

    public void moveTo(Rotation2d target) {
        io.moveTo(target);
    }

    public void moveTo(double target) {
        io.moveTo(target);
    }

    public double getFromDashboard() {
        return m_neckAngle.getDouble(0.0);
    }
}
