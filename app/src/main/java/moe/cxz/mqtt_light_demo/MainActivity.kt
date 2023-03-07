package moe.cxz.mqtt_light_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

class MainActivity : AppCompatActivity(), IMqttActionListener {
    private val brokerUrl = "tcp://bemfa.com:9501"
    private val clientId = "55156f8b615e4aa88e23bad2afbb3979"
    private val qos = 0
    private var mqttClient: MqttClient

    // Topics COLOR|BRIGHTNESS|TOGGLE
    private val colorTopic = "lightCtrlColor"
    private val brightnessTopic = "lightCtrlBrightness"
    private val toggleTopic = "lightCtrlToggle"

    init {
        mqttClient = MqttClient(brokerUrl, clientId, MemoryPersistence())
        try {
            mqttClient.connect()
        }catch (ex :Exception) {
            Toast.makeText(this, "连接MQTT服务器失败", Toast.LENGTH_SHORT).show()
        }

    }
    override fun onSuccess(asyncActionToken: IMqttToken?) {
        // MQTT 连接成功
        Log.d("mqtt", "mqtt connect success")
    }

    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
        // MQTT 连接失败
        Log.d("mqtt", "mqtt connect failed")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonSetToggle = findViewById<Button>(R.id.buttonSetToggle)
        val buttonSetRed = findViewById<Button>(R.id.buttonSetRed)
        val buttonSetGreen = findViewById<Button>(R.id.buttonSetGreen)
        val buttonSetYellow = findViewById<Button>(R.id.buttonSetYellow)

        var seekBar = findViewById<SeekBar>(R.id.seekBar)

        var statusOfToggle = "off"
        // Set OnClick Listeners

        // On/Off
        buttonSetToggle.setOnClickListener {
            statusOfToggle = if(statusOfToggle=="on") "off" else "on"
            try{
                mqttClient.publish(toggleTopic, statusOfToggle.toByteArray(), qos, false)
                when (statusOfToggle) {
                    "on" -> {
                        buttonSetToggle.text = "关灯"
                    }

                    "off" -> {
                        buttonSetToggle.text = "开灯"
                    }
                }
            }catch (ex :Exception){
                Log.d("mqtt", ex.message.toString())
                mqttClient.reconnect()
            }


        }

        // Red
        buttonSetRed.setOnClickListener {
            val message = "red"
            try {
                mqttClient.publish(colorTopic, message.toByteArray(), qos, false)
            }catch (ex :Exception){
                Log.d("mqtt", ex.message.toString())
                mqttClient.reconnect()
            }

        }
        // Green
        buttonSetGreen.setOnClickListener {
            val message = "green"
            try {
                mqttClient.publish(colorTopic, message.toByteArray(), qos, false)
            }catch (ex :Exception){
                Log.d("mqtt", ex.message.toString())
                mqttClient.reconnect()
            }

        }
        // Yellow
        buttonSetYellow.setOnClickListener {
            val message = "yellow"
            try {
                mqttClient.publish(colorTopic, message.toByteArray(), qos, false)
            }catch (ex :Exception){
                Log.d("mqtt", ex.message.toString())
                mqttClient.reconnect()
            }

        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                var message = "low"
                when (progress) {
                    0 -> {
                        message = "low"
                    }
                    1 -> {
                        message = "mid"
                    }
                    2 -> {
                        message = "high"
                    }
                }
                try {
                    mqttClient.publish(brightnessTopic, message.toByteArray(), qos, false)
                }catch (ex :Exception){
                    Log.d("mqtt", ex.message.toString())
                    mqttClient.reconnect()
                }

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // 这里实现开始拖动 SeekBar 时的相应操作
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                // 这里实现停止拖动 SeekBar 时的相应操作
            }
        })

        if (mqttClient.isConnected) {
            Toast.makeText(this, "连接成功", Toast.LENGTH_SHORT).show()
        }
    }
}