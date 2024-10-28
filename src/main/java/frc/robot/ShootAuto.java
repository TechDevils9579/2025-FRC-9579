package frc.robot;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ShootAuto {
  public static CANSparkMax m_shooterLeft;
  public static CANSparkMax m_shooterRight;
  public static CANSparkMax m_intake;
  private SlewRateLimiter m_rateLimitShoot;
  private SlewRateLimiter m_rateLimitIntake;
  private double m_intakeSpeed=0.;
  private double m_shooterSpeed=0.;

  public ShootAuto(CANSparkMax shooterLeft, CANSparkMax shooterRight, CANSparkMax intake)
  {
    m_shooterLeft = shooterLeft;
    m_shooterRight = shooterRight;
    m_intake = intake;
    m_rateLimitShoot= new SlewRateLimiter(2.);
    m_rateLimitIntake = new SlewRateLimiter(4.);
  }

  private final int WAIT=1;
  private final int STOP=0;
  private final int SPIN_UP=2;
  private final int SHOOT=3;
  private int count=0;
  private double shooterSpeed=1.0;
  private double intakeSpeed=-1.0;

  private int state=WAIT;
  public void init(){
    state=WAIT;
    count=0;
  }
  public int calc(boolean shootButtonRiseEdge){
    switch (state){
        case WAIT:
            if (shootButtonRiseEdge) {count=0;state=SPIN_UP;}
            m_rateLimitIntake.reset(0.0); 
            m_rateLimitShoot.reset(0.);
        break;
        case STOP:
        if (count++ > 10) {count=0;state=WAIT;}
        m_intakeSpeed = m_shooterSpeed = 0.0;
        m_shooterLeft.set(m_shooterSpeed); 
        m_shooterRight.set(m_shooterSpeed);
        m_intake.set(m_intakeSpeed);
        break;
        case SPIN_UP:
        if (shootButtonRiseEdge) {count=0;state=STOP;}
        if (count++ > 40) {count=0;state=SHOOT;}
        m_shooterSpeed = m_rateLimitShoot.calculate(shooterSpeed);
        m_shooterLeft.set(-m_shooterSpeed); 
        m_shooterRight.set(m_shooterSpeed);
        break;
        case SHOOT:
        if (shootButtonRiseEdge) {count=0;state=STOP;}
        if (count++ > 50) {count=0;state=STOP;}
        m_shooterLeft.set(-shooterSpeed); 
        m_shooterRight.set(shooterSpeed);
        m_intakeSpeed = m_rateLimitIntake.calculate(intakeSpeed);
        m_intake.set(m_intakeSpeed);
        break;
    }
    SmartDashboard.putNumber("Auto Shoot State", state);
    SmartDashboard.putNumber("m_shooterSpeed", m_shooterSpeed);
    SmartDashboard.putNumber("m_intakeSpeed", m_intakeSpeed);
    return(state);
  }  

}
