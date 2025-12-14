package frc.robot.subsystems.encoder;

import com.revrobotics.sim.SparkAbsoluteEncoderSim;

public class SparkEncoderIOSim implements EncoderIO {
    private final SparkAbsoluteEncoderSim encoderSim;

    public SparkEncoderIOSim(SparkAbsoluteEncoderSim sim) {
        encoderSim = sim;
    }

    @Override
    public double getPosition() {
        return encoderSim.getPosition();
    }

    @Override
    public double getPositionRotations() {
        return encoderSim.getPosition();
    }

    @Override
    public double getVelocityRotationsPerSec() {
        return encoderSim.getVelocity();
    }

    @Override
    public void setSimPositionRotations(double positionRotations) {
        encoderSim.setPosition(positionRotations);
    }

    @Override
    public void setSimVelocityRotationsPerSec(double velocityRotationsPerSec) {
        encoderSim.setVelocity(velocityRotationsPerSec);
    }
}
