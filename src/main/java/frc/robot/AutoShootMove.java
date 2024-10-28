package frc.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoShootMove {

  private ShootAuto m_shootAuto;
  private AutoDriveDistance m_autoDriveDistance;

  public AutoShootMove(ShootAuto shootAuto, AutoDriveDistance autoDriveDistance)
  {
    m_shootAuto = shootAuto;
    m_autoDriveDistance = autoDriveDistance;
  }

  private final int STOP=0;
  private final int WAIT=1;
  private final int SHOOT=2;
  private final int MOVE=3;

  private int state=WAIT;

  private int shootState=0;
  private int moveState=0;
  private int count=0;

  public void init(){
    state=WAIT;
    count=0;
  }
  public void calc(int waitTime){
    switch (state){
        case STOP:
        break;        
        case WAIT:
          if (count++ > waitTime) {
              count=0;        
              m_shootAuto.calc(true);
              state=SHOOT;
          }
        break;
        case SHOOT:
          shootState = m_shootAuto.calc(false);
          if (count > 300) {count=0;state=MOVE;}
//          if (count++ > 200) {count=0;state=MOVE;}
        break;
        case MOVE:
          moveState = m_autoDriveDistance.calc(0);
          if (count++>100) {count=0;state=STOP;}
        break;
    }
    SmartDashboard.putNumber("Auto Shoot Move State", state);
  }  
}
