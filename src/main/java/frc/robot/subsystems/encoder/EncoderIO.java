package frc.robot.subsystems.encoder;

/**
 * IO layer for the neck encoder.
 *
 * <p>Concrete implementations should wrap either: - real hardware (SparkAbsoluteEncoder), or - simulation
 * (SparkAbsoluteEncoderSim / custom sim).
 */
public interface EncoderIO {

    // -------- Core measurement getters (used by the subsystem) --------

    /** Absolute position as Rotation2d (primary accessor for code). */
    public double getPosition();

    /** Absolute position in raw rotations (0–1 per revolution if configured that way). */
    public double getPositionRotations();

    /** Velocity in rotations per second. */
    public double getVelocityRotationsPerSec();

    // -------- Sim-related hooks (for SparkAbsoluteEncoderSim) --------

    /**
     * For simulation: directly set simulated position in rotations. Real implementations may ignore or throw if called.
     */
    public default void setSimPositionRotations(double positionRotations) {}

    /**
     * For simulation: directly set simulated velocity in rotations per second. Real implementations may ignore or throw
     * if called.
     */
    public default void setSimVelocityRotationsPerSec(double velocityRotationsPerSec) {}
}
