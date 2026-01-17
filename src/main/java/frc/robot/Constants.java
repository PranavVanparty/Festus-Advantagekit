// Copyright 2021-2025 FRC 6328
// http://github.com/Mechanical-Advantage
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License
// version 3 as published by the Free Software Foundation or
// available in the root directory of this project.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.

package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

/**
 * This class defines the runtime mode used by AdvantageKit. The mode is always "real" when running on a roboRIO. Change
 * the value of "simMode" to switch between "sim" (physics sim) and "replay" (log replay from a file).
 */
public final class Constants {
    public static final Mode simMode = Mode.SIM;
    public static final Mode currentMode = RobotBase.isReal() ? Mode.REAL : simMode;

    public static final class OIConstants {
        public static final int kDriverControllerPort = 0;
        public static final int kGunnerControllerPort = 1;
    }

    public static enum Mode {
        /** Running on a real robot. */
        REAL,

        /** Running a physics simulator. */
        SIM,

        /** Replaying from a log file. */
        REPLAY
    }

    public static final class NeckConstants {
        // These are port values (where it's located on the robot)
        public static final int kNeckMotorPort = 11;
        // NOT UPDATED TO 2024 NECK VALUES
        public static final double kNeckReverseSpeed = -0.4;
        public static final double kNeckForwardSpeed = 0.6; // Was 0.6
        public static final double kNeckForwardMaxSpeed = 0.15;
        public static final double kNeckReverseMaxSpeed = -0.15;
        public static final double kNeckStableSpeed = 0.058;

        // TODO tune
        public static final double kNeckSlowModifier = 0.57;

        // NOT UPDATED TO 2024 NECK VALUES
        public static final double kEncoderUpperThreshold = 0.30;
        public static final double kEncoderLowerThreshold = 0.01;
        public static final double KEncoderDeadbandThreshold = 0.01;
        public static final double kNeckStowAngle = 0.8;
        public static final double kNeckFloorAngle = 0;
        public static final double kNeckHighAngle = 0.315; // good
        public static final double kNeckMidAngle = 0.37; // good
        public static final double kNeckLowAngle = 0.70;
        public static final double kLoadingStation = 0.36;

        // Controller constants
        public static final double kNeck_kS = 1.7;
        public static final double kNeck_kG = 0.5;
        public static final double kNeck_kV = 0.0;

        public static final double kNeck_kP = 1.5;
        public static final double kNeck_kI = 0.0;
        public static final double kNeck_kD = 0.0;
        public static final double kNeck_kP2 = 1.5;
        public static final double kNeck_kI2 = 0;
        public static final double kNeck_kD2 = 0;

        public static final double kNeck_Mass = 12.06; // kg
        public static final double kNeck_Length = 0.66; // meters
    }
}
