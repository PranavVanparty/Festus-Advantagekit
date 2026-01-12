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

package frc.robot.subsystems.vision;

import static edu.wpi.first.math.util.Units.degreesToRadians;
import static frc.robot.subsystems.vision.VisionConstants.aprilTagLayout;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.RobotBase;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import org.photonvision.simulation.PhotonCameraSim;
import org.photonvision.simulation.SimCameraProperties;
import org.photonvision.simulation.VisionSystemSim;

/** IO implementation for physics sim using PhotonVision simulator. */
public class VisionIOPhotonVisionSim extends VisionIOPhotonVision {
    private static VisionSystemSim visionSim;

    private final Supplier<Pose2d> poseSupplier;
    private final PhotonCameraSim cameraSim;

    // Guard against repeated native crashes (OpenCV TLS container issue).
    private static final AtomicBoolean visionSimHealthy = new AtomicBoolean(true);

    /**
     * Creates a new VisionIOPhotonVisionSim.
     *
     * @param name The name of the camera.
     * @param poseSupplier Supplier for the robot pose to use in simulation.
     */
    public VisionIOPhotonVisionSim(String name, Transform3d robotToCamera, Supplier<Pose2d> poseSupplier) {
        super(name, robotToCamera);
        this.poseSupplier = poseSupplier;

        // Initialize vision sim
        if (visionSim == null) {
            visionSim = new VisionSystemSim("main");
            visionSim.addAprilTags(aprilTagLayout);
        }

        // Add sim camera
        var cameraProperties = new SimCameraProperties();
        // Keep sim workload reasonable + consistent on macOS.
        cameraProperties.setFPS(30);
        cameraProperties.setCalibration(640, 480, new Rotation2d(degreesToRadians(70)));
        cameraSim = new PhotonCameraSim(camera, cameraProperties);
        visionSim.addCamera(cameraSim, robotToCamera);
    }

    @Override
    public void updateInputs(VisionIOInputs inputs) {
        // PhotonVision sim is only valid in sim; also avoid crashing robot code if OpenCV explodes.
        if (RobotBase.isSimulation() && visionSimHealthy.get()) {
            try {
                visionSim.update(poseSupplier.get());
            } catch (Throwable t) {
                visionSimHealthy.set(false);
                DriverStation.reportError(
                        "PhotonVision VisionSystemSim crashed (disabling vision sim to prevent robot crash).",
                        t.getStackTrace());
            }
        }

        // Still populate whatever PhotonVision can provide (or empty if sim disabled)
        super.updateInputs(inputs);
    }
}
