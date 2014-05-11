package be.axd.test.sensor;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class SensorActivity extends Activity implements HeadingSensor.FB {

    HeadingSensor hdg = new HeadingSensor(this, this);

    TextView tv;
    TextView data;

    ////////////////////////////////////////////
    // HeadingSensor.FB
    ////////////////////////////////////////////

    @Override
    public void left() {

        tv.setText("left");

    }

    @Override
    public void center() {

        tv.setText("center");

    }

    @Override
    public void right() {

        tv.setText("right");

    }

    @Override
    public void rotdata(double az, double pitch, double roll) {
        data.setText("\n az=" + az + "\n pitch="+pitch+"\n roll="+roll);
    }

    ////////////////////////////////////////////
    // Mainac
    ////////////////////////////////////////////

    ////////////////////////////////////////////
    // Ac
    ////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        tv = (TextView) findViewById(R.id.text);
        data = (TextView) findViewById(R.id.data);

        hdg.initHdg(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        hdg.onResume(this);
    }

    @Override
    protected void onPause() {
        hdg.onPause();
        super.onPause();
    }
}
