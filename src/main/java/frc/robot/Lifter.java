package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

//import edu.wpi.first.math.controller.PIDController;

public class Lifter {

    private CANSparkMax m_motorLeft;
    private CANSparkMax m_motorRight;
    private RelativeEncoder m_encoderLeft;
    private RelativeEncoder m_encoderRight;
  
    private double leftCommand;
    private double rightCommand;

    private final double  topPosition = 0;
    private final double  bottomPosition = 470;

    public Lifter(CANSparkMax motorLeft, CANSparkMax motorRight,RelativeEncoder encoderLeft,RelativeEncoder encoderRight){
        m_motorLeft = motorLeft;
        m_motorRight = motorRight;
        m_encoderLeft = encoderLeft;
        m_encoderRight = encoderRight;

        m_motorRight.setInverted(true);

        encoderLeft.setPosition(0.0);
        encoderRight.setPosition(0.0);
    }

    public void calc(double leftMotorCommand, double rightMotorCommand){

        leftCommand = 0;
        if (leftMotorCommand < 0){
            if (m_encoderLeft.getPosition() > topPosition){
                leftCommand = leftMotorCommand;
            }
        } else {
            if (m_encoderLeft.getPosition() < bottomPosition){
                leftCommand = leftMotorCommand;
            }
        } 
        rightCommand = 0;
        if (rightMotorCommand < 0){
            if (m_encoderRight.getPosition() > topPosition){
                rightCommand = rightMotorCommand;
            }
        } else {
            if (m_encoderRight.getPosition() < bottomPosition){
                rightCommand = rightMotorCommand;
            }
        } 

        if (Math.abs(leftCommand) <0.1) {leftCommand = 0.0;}
        if (Math.abs(rightCommand) <0.1) {rightCommand = 0.0;}

        m_motorLeft.set(leftCommand);
        m_motorRight.set(rightCommand);

        //m_encoderLeft.setPosition(0.0);
        //m_encoderRight.setPosition(0.0);

        SmartDashboard.putNumber("left Lifter Pos", m_encoderLeft.getPosition());
        SmartDashboard.putNumber("right Lifter Pos", m_encoderRight.getPosition());
        SmartDashboard.putNumber("left Lifter CMd", leftCommand);
        SmartDashboard.putNumber("right Lifter Cmd", rightCommand);

    }
    
}
