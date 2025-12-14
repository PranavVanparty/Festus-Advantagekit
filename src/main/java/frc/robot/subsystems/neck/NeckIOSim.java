package frc.robot.subsystems.neck;

import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.simulation.SingleJointedArmSim;
import frc.robot.Constants.NeckConstants;
import frc.robot.subsystems.encoder.EncoderIO;
import frc.robot.subsystems.encoder.SparkEncoderIOSim;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class NeckIOSim implements NeckIO {
    private final SparkMax m_neckMotor;
    SparkMaxSim sim;
    SparkEncoderIOSim encoderSim;
    SingleJointedArmSim neckSim;

    private static final double LOOP_PERIOD_SECS = 0.02;

    public NeckIOSim() {
        neckSim = new SingleJointedArmSim(
                DCMotor.getNEO(1),
                /* gearing */ 100.0,
                /* moi (kg*m^2) */ SingleJointedArmSim.estimateMOI(
                        Units.inchesToMeters(NeckConstants.kNeck_Length),
                        Units.lbsToKilograms(NeckConstants.kNeck_Mass)),
                /* arm length (m) */ NeckConstants.kNeck_Length,
                /* min angle (rad) */ -0.42,
                /* max angle (rad) */ Math.PI / 2.0,
                /* simulate gravity */ true,
                /* starting angle (rad) */ -0.42);

        m_neckMotor = new SparkMax(NeckConstants.kNeckMotorPort, MotorType.kBrushless);

        sim = new SparkMaxSim(m_neckMotor, DCMotor.getNEO(1));
        encoderSim = new SparkEncoderIOSim(sim.getAbsoluteEncoderSim());
    }

    @Override
    public double getNeckAngle() {
        return neckSim.getAngleRads();
    }

    @Override
    public void move(double speed) {
        sim.setVelocity(speed);
    }

    @Override
    public void moveTo(Rotation2d target) {
        m_neckMotor.getClosedLoopController().setReference(target.getRotations(), ControlType.kPosition);
    }

    @Override
    public void moveTo(double target) {
        m_neckMotor.getClosedLoopController().setReference(target, ControlType.kPosition);
    }

    @Override
    public EncoderIO getNeckEncoder() {
        return encoderSim;
    }

    @Override
    public void stop() {
        m_neckMotor.set(0);
    }

    @Override
    public void updateInputs(NeckIOInputs inputs) {
        if (!DriverStation.isEnabled()) return;
        inputs.neckAngle = getNeckAngle();
        inputs.neckVelocity = neckSim.getVelocityRadPerSec();
        inputs.neckAppliedVolts = sim.getAppliedOutput() * sim.getBusVoltage();
        inputs.neckCurrentAmps = sim.getMotorCurrent();

        // Update the simulation based on the current applied voltage
        neckSim.setInputVoltage(inputs.neckAppliedVolts);
        neckSim.update(LOOP_PERIOD_SECS);
    }
}
