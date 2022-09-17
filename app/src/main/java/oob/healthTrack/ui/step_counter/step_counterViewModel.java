package oob.healthTrack.ui.step_counter;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import androidx.lifecycle.ViewModel;



import java.util.ArrayList;

import oob.healthTrack.AccelerationData;
import oob.healthTrack.StepDetector;

public class step_counterViewModel extends ViewModel {

    private boolean isCountingSteps;
    private SensorManager sensorManager;
    private Sensor accelerationSensor;

    private StepDetector stepDetector;
    private ArrayList<AccelerationData> accelerationDataArrayList = new ArrayList<>();

    private int amountOfSteps;
    private int walkingSteps, joggingSteps, runningSteps;


    public boolean isCountingSteps() {
        return isCountingSteps;
    }

    public void setCountingSteps(boolean countingSteps) {
        isCountingSteps = countingSteps;
    }

    public SensorManager getSensorManager() {
        return sensorManager;
    }

    public void setSensorManager(SensorManager sensorManager) {
        this.sensorManager = sensorManager;
    }

    public Sensor getAccelerationSensor() {
        return accelerationSensor;
    }

    public void setAccelerationSensor(Sensor accelerationSensor) {
        this.accelerationSensor = accelerationSensor;
    }

    public StepDetector getStepDetector() {
        return stepDetector;
    }

    public void setStepDetector(StepDetector stepDetector) {
        this.stepDetector = stepDetector;
    }

    public ArrayList<AccelerationData> getAccelerationDataArrayList() {
        return accelerationDataArrayList;
    }

    public void setAccelerationDataArrayList(ArrayList<AccelerationData> accelerationDataArrayList) {
        this.accelerationDataArrayList = accelerationDataArrayList;
    }

    public int getAmountOfSteps() {
        return amountOfSteps;
    }

    public void setAmountOfSteps(int amountOfSteps) {
        this.amountOfSteps = amountOfSteps;
    }

    public int getWalkingSteps() {
        return walkingSteps;
    }

    public void setWalkingSteps(int walkingSteps) {
        this.walkingSteps = walkingSteps;
    }

    public int getJoggingSteps() {
        return joggingSteps;
    }

    public void setJoggingSteps(int joggingSteps) {
        this.joggingSteps = joggingSteps;
    }

    public int getRunningSteps() {
        return runningSteps;
    }

    public void setRunningSteps(int runningSteps) {
        this.runningSteps = runningSteps;
    }

}
