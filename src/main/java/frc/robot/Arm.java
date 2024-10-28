package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Arm {
  double m_armPos = 0;
  private CANSparkMax m_motor;
  private PIDController m_pid;
  private RelativeEncoder m_encoder;
  private SlewRateLimiter m_filter = new SlewRateLimiter(45.0);

  private double command = 0;
  private final double m_kp = .1;
  private final double m_kd = 0.0;
  private final double m_ki = 0.0;

  private final double UP_POSITION = 0.0;
  private final double DOWN_POSITION = -75;
  private final double AMP_POSITION = -35;

  public Arm(CANSparkMax motor, RelativeEncoder encoder){
        m_motor = motor;
        m_encoder = encoder;
        m_encoder.setPosition(0.0);
        m_pid = new PIDController(m_kp,m_ki, m_kd);
  }
  public void init(){

  }

  public void calc(boolean armUp, boolean armDown, boolean armAmp, boolean armUpReset){
    if (armUpReset && armUp){
      m_motor.set(.2);
      command = UP_POSITION;
      if (m_encoder.getPosition() > 0){
                m_encoder.setPosition(0.0);
      }      
    } else if (armUpReset && armDown){
      m_motor.set(-0.2);
    } else if (armUpReset){
      m_motor.set(0.0);
    } else {
      if(armUp){
        command = UP_POSITION;
      } else if (armDown){
        command = DOWN_POSITION;
      } else if (armAmp){
        command = AMP_POSITION;        
      }
      double command2 = m_filter.calculate(command);
      double pidCmd = m_pid.calculate(m_encoder.getPosition(),command2);
      m_motor.set(MathUtil.clamp(pidCmd,-.5,.5));

      SmartDashboard.putNumber("pidCmd", pidCmd);
      SmartDashboard.putNumber("Command", command);
      SmartDashboard.putNumber("Command2", command2);
      SmartDashboard.putNumber("Position", m_encoder.getPosition());
    }
  }
}
