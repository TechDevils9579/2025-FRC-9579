package frc.robot;

import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Intake {
  private static CANSparkMax m_intake;

  public Intake(CANSparkMax intake){
    m_intake = intake;
  }

  public void calc(boolean inButton, boolean outButton, double speed){
        double intakeSpeed = 0;
        if (inButton){
          intakeSpeed = .1;
           if (speed > intakeSpeed){
            intakeSpeed = speed;
           }
        } else if (outButton) {
          intakeSpeed=(-.1);
           if (-speed < intakeSpeed){
            intakeSpeed = -speed;
           }
        }
        m_intake.set(intakeSpeed);
        SmartDashboard.putNumber("Intake Speed", intakeSpeed);
  }
}

    
