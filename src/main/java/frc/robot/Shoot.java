package frc.robot;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Shoot {
  public static CANSparkMax m_shooterLeft;
  public static CANSparkMax m_shooterRight;

  public Shoot(CANSparkMax shooterLeft, CANSparkMax shooterRight)
  {
    m_shooterLeft = shooterLeft;
    m_shooterRight = shooterRight;
  }
  public void init(){

  }

  public void calc(boolean shootButton, boolean inButton, double speed){
    // Shooter Control
    double shooterSpeed=0;
    //double speed = armJoystick.getLeftTriggerAxis();
    SmartDashboard.putNumber("Shooter Trigger Speed", speed);
    if (shootButton){
      shooterSpeed=.05;
        if (speed > shooterSpeed){
        shooterSpeed = speed;
        }
    }
    if (inButton){
      shooterSpeed=-.05;
        if (-speed < shooterSpeed){
        shooterSpeed = -speed;
        }
    }
    m_shooterLeft.set(-shooterSpeed); 
    m_shooterRight.set(shooterSpeed);
    SmartDashboard.putNumber("Shooter Speed", shooterSpeed);
  }  
}
