package ijev3as2;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.sensor.*;
import lejos.robotics.Color;
import lejos.robotics.MirrorMotor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.mapping.*;
import lejos.ev3.tools.LCDDisplay;
import lejos.hardware.BrickFinder;
import lejos.hardware.Button;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.TextLCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.robotics.SampleProvider;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.MovePilot;
import lejos.robotics.navigation.*;
import lejos.utility.Delay;

import java.lang.reflect.Array;

public class Car {
    static final RegulatedMotor LEFT_MOTOR = new EV3LargeRegulatedMotor(MotorPort.A); // left
    static final RegulatedMotor RIGHT_MOTOR = new EV3LargeRegulatedMotor(MotorPort.D); // right
    static final EV3MediumRegulatedMotor MEDIUM_MOTOR = new EV3MediumRegulatedMotor(MotorPort.B);//B
    static final double WHEELDIST = 12.0;           // measures in cm
    static final double WHEEL_DIAMETER = 5.5;     // Diameter in cm
    static final double WHEELCIRC = WHEEL_DIAMETER*Math.PI;  // Wheel circonference

    static final Wheel wheel1 = WheeledChassis.modelWheel(LEFT_MOTOR, 5.5).offset(-5.5);
    static final Wheel wheel2 = WheeledChassis.modelWheel(RIGHT_MOTOR, 5.5).offset(5.5);
    static  final Chassis chassis = new WheeledChassis(new Wheel[] {wheel1, wheel2},WheeledChassis.TYPE_DIFFERENTIAL);
    // Pilot (easier control of both motors)
    public MovePilot pilot = new MovePilot(chassis);
    public Navigator navi = new Navigator(pilot);
    public OdometryPoseProvider pose = new OdometryPoseProvider(pilot);
    // Buttons
    static final EV3 ev3brick = (EV3) BrickFinder.getLocal();
    Keys buttons = ev3brick.getKeys();

    public static GraphicsLCD graphics = ev3brick.getGraphicsLCD();
    
    //MappingOnRobot = new MappingOnRobot(GraphicsLCD, 13, 19);
    // Distance Sensor ultrasonic
    static final SampleProvider ultra= (new EV3IRSensor(LocalEV3.get().getPort("S3"))).getMode("Distance");  //S3
    static final float[] ultrasp = new float[ultra.sampleSize()];

//    // Distance Sensor ir
//    static final SampleProvider ir= (new EV3UltrasonicSensor(LocalEV3.get().getPort("S2"))).getMode("Distance");
//    static final float[] irsp = new float[ir.sampleSize()];

    // Brightness Sensor
//    static final SampleProvider color = (new EV3ColorSensor(LocalEV3.get().getPort("S2"))).getRGBMode(); //S4
//    static final float[] colorsp = new float[3];
    static final SampleProvider color2 = (new EV3ColorSensor(LocalEV3.get().getPort("S2"))).getColorIDMode(); //S4
    static final float[] colorsp2 = new float[1];
    
   
    // Touch Sensor
    //static final SampleProvider touch = (new EV3TouchSensor(LocalEV3.get().getPort("S3")));
    // Gyro Sensor
//    static final EV3GyroSensor gyroSensor = new EV3GyroSensor(LocalEV3.get().getPort("S3"));
//    static {
//        gyroSensor.reset();
//    }
//    static final SampleProvider angle = gyroSensor.getAngleMode(); //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
//    static final float[] angles = new float[angle.sampleSize()];
    // The robots LCD (text modus)
    static TextLCD textLCD = BrickFinder.getDefault().getTextLCD();

    public float getUltrasp() {
        //Delay.msDelay(200);
        ultra.fetchSample(ultrasp, 0);
        //LCD.drawString("distance: "+ ultrasp[0], 0, 0);
        //LCD.clear();
        return ultrasp[0];
    }

//    public float[] getColorsp() {
//        color.fetchSample(colorsp, 0);
//        //Delay.msDelay(2000);
//        LCD.drawString("color: "+ colorsp[0], 0, 0);
//        return colorsp;
//    }
//
//    public float getR() {
//        return colorsp[0];
//    }
//
//    public float getG() {
//        return colorsp[1];
//    }
//
//    public float getB() {
//        return colorsp[2];
//    }
//
//    //waiting for Dimple's calibration
//    public boolean isRed() {
//        getColorsp();
//        float leftR = colorsp[0];
//        float leftG = colorsp[1];
//        float leftB = colorsp[2];
//        return (leftR >= 0.15 && leftG <= 0.1 && leftB <= 0.1);
//    }
//
//    // waiting for Dimple's calibration
//    public boolean isGreen() {
//        getColorsp();
//        float leftR = colorsp[0];
//        float leftG = colorsp[1];
//        float leftB = colorsp[2];
//        ////     boolean isLeftR3 = (leftR <= 0.033  && leftR >= 0.032);
//        ////     boolean isLeftG3 = (leftG <= 0.113 && leftG >= 0.111);
//        ////     boolean isLeftB3 = (leftB <= 0.061 && leftB >= 0.059);
//        //  boolean isLeft3 = leftG > leftR && leftG > leftB;
//        return false;
//    }
//    
    public boolean isGreen2() {
        color2.fetchSample(colorsp2, 0);
        LCD.drawString("checking green"+ (colorsp2[0] == Color.GREEN), 0, 3);
        
        return (colorsp2[0] == 1);
    }
  
    public boolean isRed2() {
        color2.fetchSample(colorsp2, 0);
        LCD.drawString("checking red"+ (colorsp2[0] == 5), 0, 3);
        return (colorsp2[0] == 0);
    }
    
    // It's necessary to assert that -90 means turning left???????????????
    public void turnWest1( ){
        MEDIUM_MOTOR.rotate(-90);
    }
    public void turnWest2() {
    	MEDIUM_MOTOR.rotate(90);
    }
    public void turnEast1() {
        MEDIUM_MOTOR.rotate(90);
    }
    public void turnEast2() {
    	MEDIUM_MOTOR.rotate(-90);
    }

    public Waypoint getWayPoint() {
        return navi.getWaypoint();
    }

    public boolean isEscapeUp() {
        if (Button.ENTER.isUp())
            return true;
        else
            return false;
    }

    public static void main(String[] args)  {
        boolean loop = true;
        Car car = new Car();
        car.buttons.waitForAnyPress();
        //Maze maze = new Maze(13, 19);
        if (!car.isEscapeUp()) {
            loop = false;
            System.exit(0);
        }

        do {
        	LCD.drawString("hhh", 0, 0);
            car.LEFT_MOTOR.backward();;
            
        } while (loop);
    }

}
