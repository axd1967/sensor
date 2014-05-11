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

        void mode(String m);

        void ref(double refaz);

//        void acc(String acc);
        void rotdata(double az, double pitch, double roll);
    }

    enum Mode { left, center, right, undefined };

    Mode m = Mode.undefined;

    private FB fb;

    public HeadingSensor(FB fb, Context ctx) {

        this.fb = fb;
    }

    // send feedback if true
    boolean running = false;

    public void initHdg(Context ctx) {
        mSensorManager = (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR), SensorManager.SENSOR_DELAY_NORMAL);

        refHdg();

    }

    public void onResume(Context ctx) {


        refHdg();
    }

    int ctr;
    double refAz;

    private void refHdg() {
        // get current heading as reference
        // ...

        m = Mode.center;
        running = false;

        refAz = hAz;
        ctr = 10;

    }

    public void onPause() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {


        if (sensorEvent.sensor.getType() != Sensor.TYPE_ROTATION_VECTOR)
            return;

        float[] rotationMatrix = new float[16];
        SensorManager.getRotationMatrixFromVector(rotationMatrix, sensorEvent.values);
        SensorManager.remapCoordinateSystem(rotationMatrix, SensorManager.AXIS_X, SensorManager.AXIS_Z, rotationMatrix);

        determineOrientation(rotationMatrix);

        // inclination

    }

    double hAz = 0;
    final static double k = 0.8;

    private void determineOrientation(float[] rotationMatrix) {

        float[] orientationValues = new float[3];

        SensorManager.getOrientation(rotationMatrix, orientationValues);

        double azimuth = Math.toDegrees(orientationValues[0]);
        double pitch = Math.toDegrees(orientationValues[1]);
        double roll = Math.toDegrees(orientationValues[2]);


        hAz = (1-k)*hAz + k *azimuth;

        if (ctr > 0) {
            --ctr;
            fb.mode("n: "+ ctr);
            return;
        }

        if (!running) {
            refAz = hAz;
            running = true;
            m = Mode.center;
            fb.mode("run");
        }

        //fb.ref(refAz);

        fb.rotdata(hAz, pitch, roll);

        double limit = 30; // !
        if (m == Mode.center && beyond(hAz, refAz, +limit)) {
            m = Mode.right;
            fb.right();
        }
        else if (m == Mode.center && before (hAz, refAz, -limit)) {
            m = Mode.left;
            fb.left();

        }
        else if (
                m == Mode.right && before (hAz, refAz, + 10)
                || m == Mode.left && beyond (hAz, refAz, - 10)

                ) {
            m = Mode.center;
            fb.center();
        }
    }

    boolean beyond(double current, double target, double delta) {

        if (current < 0) current += 360;
        if (target < 0) target += 360;

        return (target + delta < current );
    }

    boolean before(double current, double target, double delta) {

        if (current < 0) current += 360;
        if (target < 0) target += 360;

        return (current < target + delta );
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
