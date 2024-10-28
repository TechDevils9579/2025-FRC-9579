package frc.robot;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drive {
  double m_forwardCommand;
  double m_turnCommand;
  double leftMotor;
  double rightMotor;
  private static CANSparkMax m_leftLeader;
  private static CANSparkMax m_rightLeader;
  private final double kForwardCommand=1;
  private final double kturnCommand=.5;
  private SlewRateLimiter m_rateLimitForward;
  private SlewRateLimiter m_rateLimitTurn;

  public Drive(CANSparkMax leftLeader, CANSparkMax rightLeader){
    m_leftLeader = leftLeader;
    m_rightLeader = rightLeader;
    m_rateLimitForward = new SlewRateLimiter(6);
    m_rateLimitTurn = new SlewRateLimiter(6);
  }

  public void calc(double forwardCommand1, double turnCommand1){
    double forwardCommand = m_rateLimitForward.calculate(forwardCommand1);
    double turnCommand = -m_rateLimitTurn.calculate(turnCommand1);

    //double forwardCommand = forwardCommand1;
    //double turnCommand = turnCommand1;

    double squareSign;
    if (forwardCommand >=0){
      squareSign=1;
    } else {
      squareSign=-1;
    }
    m_forwardCommand = forwardCommand * forwardCommand * squareSign * kForwardCommand;


    if (turnCommand >=0){
      squareSign=1;
    } else {
      squareSign=-1;
    }
    m_turnCommand = turnCommand * turnCommand * squareSign * kturnCommand;

    leftMotor = MathUtil.clamp(m_forwardCommand + m_turnCommand,-1,1);
    rightMotor = MathUtil.clamp(m_forwardCommand - m_turnCommand,-1,1);
    m_leftLeader.set(leftMotor);
    m_rightLeader.set(-rightMotor);
    SmartDashboard.putNumber("Js X", m_turnCommand);
    SmartDashboard.putNumber("JS Y", m_forwardCommand);
    SmartDashboard.putNumber("Left Drive", leftMotor);
    SmartDashboard.putNumber("Right Drive", rightMotor);
  }
}
