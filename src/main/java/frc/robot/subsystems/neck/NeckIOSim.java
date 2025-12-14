package frc.robot.subsystems.neck;

import com.revrobotics.sim.SparkAbsoluteEncoderSim;
import com.revrobotics.sim.SparkMaxSim;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.Constants.NeckConstants;
import frc.robot.subsystems.encoder.EncoderIO;
import frc.robot.subsystems.encoder.SparkEncoderIOSim;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class NeckIOSim implements NeckIO {
    private final SparkMax m_neckMotor;
    SparkMaxSim sim;
    SparkEncoderIOSim encoderSim;

    public NeckIOSim() {
        m_neckMotor = new SparkMax(NeckConstants.kNeckMotorPort, MotorType.kBrushless);
        
        sim = new SparkMaxSim(m_neckMotor, DCMotor.getNEO(1));
        encoderSim = new SparkEncoderIOSim(sim.getAbsoluteEncoderSim());
    }

    @Override
    public double getNeckAngle() {
        return encoderSim.getPosition();
    }

    @Override
    public void move(double speed) {
        sim.setVelocity(speed);
    }

    @Override
    public void moveTo(Rotation2d pose) {}

    @Override
    public void moveTo(double target) {}

    @Override
    public EncoderIO getNeckEncoder() {
        return encoderSim;
    }

    @Override
    public void stop() {
        m_neckMotor.set(0);
    }
}
