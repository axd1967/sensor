package be.axd.test.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by alex on 11/05/2014.
 */
public class HeadingSensor implements SensorEventListener {

    private SensorManager mSensorManager;

    public interface FB {
        void left();
        void center();
        void right();

//        void acc(String acc);
        void rotdata(double az, double pitch, double roll);
    }

    enum Mode { left, center, right };

    Mode m;

    private FB fb;

    public HeadingSensor(FB fb, Context ctx) {

        this.fb = fb;
    }

    public void initHdg(Context ctx) {
        mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);

    }

    public void onResume(Context ctx) {

//        mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);

        refHdg();
    }

    private void refHdg() {
        // get current heading as reference
        // ...

        m = Mode.center;
    }

    public void onPause() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

//        // accuracy
//        switch ( sensorEvent.accuracy) {
//            case SensorManager.SENSOR_STATUS_ACCURACY_HIGH:
//                fb.acc("HIGH");
//                break;
//            case SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM:
//                fb.acc("MED");
//                break;
//            case SensorManager.SENSOR_STATUS_ACCURACY_LOW:
//                fb.acc("LOW");
//                break;
//            case SensorManager.SENSOR_STATUS_UNRELIABLE:
//                fb.acc("UNR");
//                break;
//            default:
//                fb.acc("XXXXXXXXXXX");
//                break;
//        }

        if (sensorEvent.sensor.getType() != Sensor.TYPE_ROTATION_VECTOR)
            return;

        float[] rotationMatrix = new float[16];
        SensorManager.getRotationMatrixFromVector(rotationMatrix,
                sensorEvent.values);
        determineOrientation(rotationMatrix);

        // inclination

    }

    private void determineOrientation(float[] rotationMatrix) {

        float[] orientationValues = new float[3];

        SensorManager.getOrientation(rotationMatrix, orientationValues);

        double azimuth = Math.toDegrees(orientationValues[0]);
        double pitch = Math.toDegrees(orientationValues[1]);
        double roll = Math.toDegrees(orientationValues[2]);

        fb.rotdata(azimuth, pitch, roll);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
