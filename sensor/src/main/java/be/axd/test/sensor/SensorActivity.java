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

    int leftcounter=0;
    @Override
    public void left() {

        tv.setText("left " + ++ leftcounter);

    }

    int centercounter=0;
    @Override
    public void center() {

        tv.setText("center " + ++centercounter);

    }

    int rightcounter=0;
    @Override
    public void right() {

        tv.setText("right " + ++rightcounter);

    }

    @Override
    public void rotdata(double az, double pitch, double roll) {
        data.setText("\n az=" + az + "\n pitch="+pitch+"\n roll="+roll);
    }

    @Override
    public void ref(double refaz) {
        tv.setText("ref: "+refaz);
    }

    @Override
    public void mode(String m) {
        tv.setText(m);
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

        leftcounter = 0;
        centercounter = 0;
        rightcounter = 0;

        hdg.onResume(this);
        hdg.initHdg(this);
    }

    @Override
    protected void onPause() {
        hdg.onPause();
        super.onPause();
    }
}
