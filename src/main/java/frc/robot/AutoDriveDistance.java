package frc.robot;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDriveDistance {

  private static CANSparkMax m_leftLeader;
  private static CANSparkMax m_rightLeader;
  private SlewRateLimiter m_rateLimitForward;
//  private SlewRateLimiter m_rateLimitTurn;
  private final int STOP = 0;
  private final int START = 1;
  private final int WAIT = 2;
  private int state=START;
  private double m_speedCmd = .2;
  private int count=0;
  private static final double SECONDS_DELAY = 50.;
  private double speedCmd;
  private final int kDriveTime = 350;
  private final int kStopTime = 50;
    
  public AutoDriveDistance(CANSparkMax leftLeader, CANSparkMax rightLeader){
    m_leftLeader = leftLeader;
    m_rightLeader = rightLeader;
    m_rateLimitForward = new SlewRateLimiter(2.);
//  m_rateLimitTurn = new SlewRateLimiter(4.);
    state = WAIT;
  }
  public void init(){
    state=WAIT;
    count=0;
  }
  public int calc(int pauseTime){
    switch(state){
        case START:
          if (count++ > kDriveTime) {count=0;state=STOP;}
          speedCmd = m_rateLimitForward.calculate(m_speedCmd);
          m_leftLeader.set(-speedCmd);
          m_rightLeader.set(speedCmd);
        break;
        case STOP:
//          if (count++ > kStopTime) {count=0;state=WAIT;}
          speedCmd = m_rateLimitForward.calculate(0.);
          m_leftLeader.set(speedCmd);
          m_rightLeader.set(speedCmd);
        break;
        case WAIT:
          if (count++ > pauseTime) {count=0; state=START;}
        break;
    }        
    SmartDashboard.putNumber("Auto Drive Dist State", state);

    return(state);
  }
}
