package frc.robot.subsystems.neck;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.encoder.EncoderIO;

public interface NeckIO {
    public default double getNeckAngle() { return 0.0; }

    default EncoderIO getNeckEncoder() { return null; }

    public default void move(double speed) {}

    public default void moveTo(Rotation2d target) {}

    public default void moveTo(double target) {}

    public default void stop() {}
}
