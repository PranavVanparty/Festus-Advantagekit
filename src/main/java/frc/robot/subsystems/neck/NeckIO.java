package frc.robot.subsystems.neck;

import com.revrobotics.AbsoluteEncoder;

import edu.wpi.first.math.geometry.Rotation2d;

public interface NeckIO {
    public default double getNeckAngle() { return 0.0; }

    public default AbsoluteEncoder getNeckEncoder() { return null; }

    public default void move(double speed) {}

    public default void moveTo(Rotation2d target) {}

    public default void moveTo(double target) {}

    public default void stop() {}
}
