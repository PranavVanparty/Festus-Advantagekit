package frc.robot.subsystems.neck;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.subsystems.encoder.EncoderIO;
import org.littletonrobotics.junction.AutoLog;

public interface NeckIO {
    @AutoLog
    public class NeckIOInputs {
        public double neckAngle = 0.0;
        public double neckVelocity = 0.0;
        public double neckAppliedVolts = 0.0;
        public double neckCurrentAmps = 0.0;
    }

    public default double getNeckAngle() {
        return 0.0;
    }

    default EncoderIO getNeckEncoder() {
        return null;
    }

    public default void move(double speed) {}

    public default void moveTo(Rotation2d target) {}

    public default void moveTo(double target) {}

    public default void stop() {}

    public default void updateInputs(NeckIOInputs inputs) {}
    ;
}
