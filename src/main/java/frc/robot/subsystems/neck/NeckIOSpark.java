package frc.robot.subsystems.neck;

import com.revrobotics.spark.SparkBase.ControlType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkClosedLoopController;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Constants.NeckConstants;
import frc.robot.subsystems.encoder.EncoderIO;
import frc.robot.subsystems.encoder.SparkEncoderIO;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class NeckIOSpark implements NeckIO {
    private final SparkMax m_neckMotor;
    private final EncoderIO m_neckEncoder;
    private ArmFeedforward armFeedforward;
    private PIDController neckPIDcontroller2;
    private SparkMaxConfig m_neckConfig;
    SparkClosedLoopController neckController;

    public NeckIOSpark() {
        m_neckMotor = new SparkMax(NeckConstants.kNeckMotorPort, MotorType.kBrushless);
        m_neckEncoder = new SparkEncoderIO(m_neckMotor.getAbsoluteEncoder());
        m_neckConfig = new SparkMaxConfig();
        armFeedforward = new ArmFeedforward(NeckConstants.kNeck_kS, NeckConstants.kNeck_kG, NeckConstants.kNeck_kV);
        neckController = m_neckMotor.getClosedLoopController();

        // TODO FIND END AND START ANGLES
        // See https://www.chiefdelphi.com/t/holding-up-a-wrist-with-a-neo/425787/14 to set these
        // double endAngle = 0;
        // double startAngle = 0;
        // double valueAtEndAngle = 0;
        // m_neckConfig.encoder.positionConversionFactor((endAngle - startAngle) / valueAtEndAngle);

        neckPIDcontroller2 =
                new PIDController(NeckConstants.kNeck_kP2, NeckConstants.kNeck_kI2, NeckConstants.kNeck_kD2);

        m_neckConfig.closedLoop.pid(NeckConstants.kNeck_kP, NeckConstants.kNeck_kI, NeckConstants.kNeck_kD);
        m_neckMotor.configure(m_neckConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    @Override
    public double getNeckAngle() {

        return m_neckEncoder.getPosition();
    }

    @Override
    public EncoderIO getNeckEncoder() {
        return m_neckEncoder;
    }

    @Override
    public void move(double speed) {
        m_neckMotor.set(speed);
    }

    @Override
    public void moveTo(double target) {
        move(neckPIDcontroller2.calculate(getNeckAngle(), target) * NeckConstants.kNeckForwardSpeed * 10);
    }

    @Override
    public void moveTo(Rotation2d target) {
        neckController.setReference(target.getRotations(), ControlType.kPosition);
    }

    @Override
    public void stop() {
        m_neckMotor.set(0);
    }
}
