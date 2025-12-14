package frc.robot.subsystems.neck;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkSim;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.system.plant.DCMotor;
import frc.robot.Constants.NeckConstants;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class NeckIOSim implements NeckIO {
    private final SparkMax m_neckMotor;
    SparkSim sim;

    public NeckIOSim() {
        m_neckMotor = new SparkMax(NeckConstants.kNeckMotorPort, MotorType.kBrushless);
        sim = new SparkSim(m_neckMotor, DCMotor.getNEO(1));
    }

    @Override
    public double getNeckAngle() {
        return sim.getPosition();
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
    public AbsoluteEncoder getNeckEncoder() {
        return null;
    }

    @Override
    public void stop() {
        m_neckMotor.set(0);
    }
}
