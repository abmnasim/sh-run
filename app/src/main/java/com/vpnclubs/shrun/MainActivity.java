package com.vpnclubs.shrun;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "example.sh";
    private String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button exe = findViewById(R.id.execute);
        TextView rst = findViewById(R.id.ping_result);

        exe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    save();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                rst.setText(shellExecute(path));
            }
        });
    }

    public void save() throws IOException {
        StringBuilder output = new StringBuilder();
        InputStream is = getResources().getAssets().open("test.sh");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null){
            output.append(line).append("\n");
        }
        String text = output.toString();
        FileOutputStream fos = null;

        fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
        fos.write(text.getBytes());
        path = getFilesDir()+"/"+FILE_NAME;
        Toast.makeText(this, "Save: "+getFilesDir()+"/"+FILE_NAME, Toast.LENGTH_LONG).show();
    }

    private String shellExecute(String path) {
        StringBuilder output = new StringBuilder();
        String cmd = "sh " + path;
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine())!= null) {
                output.append(line).append("\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return output.toString();
    }
}
