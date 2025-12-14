package frc.robot.subsystems.encoder;

import com.revrobotics.spark.SparkAbsoluteEncoder;

/**
 * Real-hardware implementation of EncoderIO using a SparkMax + SparkAbsoluteEncoder.
 */
public class SparkEncoderIO implements EncoderIO {

    private final SparkAbsoluteEncoder encoder;

    /**
     * Construct with an existing CANSparkMax and a chosen absolute encoder type.
     * Example: new SparkMaxEncoderIO(swerveTurnMotor, SparkAbsoluteEncoder.Type.kDutyCycle);
     */
    public SparkEncoderIO(SparkAbsoluteEncoder sim) {
        this.encoder = sim;
    }
    
    @Override
    public double getPosition() {
        return encoder.getPosition();
    }

    @Override
    public double getPositionRotations() {
        return encoder.getPosition();
    }

    @Override
    public double getVelocityRotationsPerSec() {
        return encoder.getVelocity();
    }

}
