// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkLowLevel.MotorType;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoSink;
import edu.wpi.first.cscore.VideoSource.ConnectionStrategy;
//import edu.wpi.first.networktables.NetworkTableEntry;
//import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  private final XboxController driveJoystick = new XboxController(0);

  private final XboxController armJoystick = new XboxController(1);

  private static CANSparkMax shooterLeft = new CANSparkMax(8,  MotorType.kBrushless);
  private static CANSparkMax shooterRight = new CANSparkMax(11,  MotorType.kBrushless);
  private Shoot m_shoot = new Shoot(shooterLeft, shooterRight);
  private static CANSparkMax intake = new CANSparkMax(14,  MotorType.kBrushless);
  private static CANSparkMax m_armMotor = new CANSparkMax(13,  MotorType.kBrushless);
  private RelativeEncoder m_armEncoder;
  private Arm m_arm;

  private static CANSparkMax leftLeader = new CANSparkMax(5,  MotorType.kBrushed);
  private static CANSparkMax leftFollower = new CANSparkMax(6,  MotorType.kBrushed);
  private static CANSparkMax rightLeader = new CANSparkMax(9,  MotorType.kBrushed);
  private static CANSparkMax rightFollower = new CANSparkMax(7,  MotorType.kBrushed);
  private Drive m_drive = new Drive(leftLeader, rightLeader);
  private Intake m_intake = new Intake(intake);

// Lifter
  private static CANSparkMax lifterMotorLeft = new CANSparkMax(15,  MotorType.kBrushless);
  private static CANSparkMax lifterMotorRight = new CANSparkMax(16,  MotorType.kBrushless);
  private RelativeEncoder lifterEncoderLeft;
  private RelativeEncoder lifterEncoderRight;
  private Lifter m_lifter;

  private static final double SECONDS_DELAY = 50.;
  private static final String kDrive = "Drive";
  private static final String kPauseDrive = "Pause Drive";
  private static final String kShootDrive = "Shoot Drive";
  private static final String kPauseShootDrive = "Pause Shoot Drive";
  private static final String kDefaultAuto = kShootDrive;

  private AutoDriveDistance m_autoDriveDistance = new AutoDriveDistance(leftLeader, rightLeader);
  private ShootAuto m_shootAuto = new ShootAuto(shooterLeft, shooterRight, intake);
  private AutoShootMove m_autoShootMove = new AutoShootMove(m_shootAuto, m_autoDriveDistance);

  UsbCamera camera1;
  UsbCamera camera2;
  VideoSink server;
//  private RelativeEncoder leftLeaderEncoder;
//  private RelativeEncoder rightLeaderEncoder;
//  private static CANSparkMax m_testMotor = new CANSparkMax(8,  MotorType.kBrushless);
//  private RelativeEncoder m_testEncoder;
//  private Test2MDistSensor m_testDist = new Test2MDistSensor();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("Drive", kDrive);
    m_chooser.addOption("Pause Drive", kPauseDrive);
    m_chooser.addOption("Shoot Drive", kShootDrive);
    m_chooser.addOption("Pause Shoot Drive", kPauseShootDrive);
    SmartDashboard.putData("Auto choices", m_chooser);

    m_armEncoder = m_armMotor.getEncoder();
    m_arm = new Arm(m_armMotor, m_armEncoder);

    lifterEncoderLeft = lifterMotorLeft.getEncoder();
    lifterEncoderRight = lifterMotorRight.getEncoder();
    m_lifter = new Lifter(lifterMotorLeft, lifterMotorRight, lifterEncoderLeft, lifterEncoderRight);

//    m_testEncoder = m_testMotor.getEncoder();
//    m_testDist.init();


    leftFollower.follow(leftLeader, false);
    rightFollower.follow(rightLeader, false);

    camera1 = CameraServer.startAutomaticCapture(0);
   camera2 = CameraServer.startAutomaticCapture(1);
    server = CameraServer.getServer();

    camera1.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    camera2.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    CvSink cvSink  = CameraServer.getVideo();
    CvSource outputStream = CameraServer.putVideo("Blur",640,480);
  }

  /**
   * This function is called every 20 ms, no matter the mode. Use this for items like diagnostics
   * that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before LiveWindow and
   * SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {}

  private int count=0;
  /**
   * This autonomous (along with the chooser code above) shows how to select between different
   * autonomous modes using the dashboard. The sendable chooser code works with the Java
   * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
   * uncomment the getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to the switch structure
   * below with additional strings. If using the SendableChooser make sure to add them to the
   * chooser code above as well.
   */
  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    m_autoDriveDistance.init();
    m_autoShootMove.init();
    m_shootAuto.init();
    count=0;
  }

  /** This function is called periodically during autonomous. */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kDrive:
        //m_autoDriveDistance.calc((int)(0 *SECONDS_DELAY));
        break;
      case kPauseDrive:
        //m_autoDriveDistance.calc((int)(5 *SECONDS_DELAY));
        break;
      default:
      case kShootDrive:
        //m_autoShootMove.calc((int)(0 *SECONDS_DELAY));
        break;
      case kPauseShootDrive:
        //m_autoShootMove.calc((int)(5 *SECONDS_DELAY));
        break;
    }
    //m_autoDriveDistance.calc((int)(0 *SECONDS_DELAY));
    //m_autoShootMove.calc((int)(0 *SECONDS_DELAY));
    if (count==0) {
      m_shootAuto.calc(true);
      count=1;
    } else if (count < 200) {
      m_shootAuto.calc(false);
      count++;
    } else {
      m_autoDriveDistance.calc((int)(0 *SECONDS_DELAY));
    }
  }

  /** This function is called once when teleop is enabled. */
  @Override
  public void teleopInit() {}

  /** This function is called periodically during operator control. */
  @Override
  public void teleopPeriodic() {
    m_drive.calc(driveJoystick.getLeftY(), driveJoystick.getRightX());
    m_intake.calc(armJoystick.getYButton(),armJoystick.getAButton(), armJoystick.getLeftTriggerAxis());
    m_shoot.calc(armJoystick.getXButton(),armJoystick.getBButton(),armJoystick.getLeftTriggerAxis() );
    m_arm.calc(armJoystick.getPOV() == 0,armJoystick.getPOV() == 180, armJoystick.getPOV() == 90, armJoystick.getRightBumperPressed());
    m_lifter.calc(armJoystick.getLeftY(), armJoystick.getRightY());
    m_shootAuto.calc(armJoystick.getLeftBumperPressed());

    if(driveJoystick.getLeftBumper()){
     server.setSource(camera1);
    } else if (driveJoystick.getRightBumper()){
      server.setSource(camera2);
    }
  }

  /** This function is called once when the robot is disabled. */
  @Override
  public void disabledInit() {
    //m_testDist.disabledInit();
  }

  /** This function is called periodically when disabled. */
  @Override
  public void disabledPeriodic() {}

  //TestMotor testMotor;
  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {
    //testMotor = new TestMotor(m_testMotor, m_testEncoder);
    //SmartDashboard.putNumber("Test",0.0);
  }

  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {  
    //testMotor.calc(driveJoystick.getAButtonPressed(), driveJoystick.getBButtonPressed());
    //SmartDashboard.putNumber("Test.1",1.0);
  }

  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}

  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
