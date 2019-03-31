package com.example.postsense;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class history extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);



        ImageButton help = (ImageButton) findViewById(R.id.helpButton);

            help.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    TextView txtView = (TextView)findViewById(R.id.helpTextView);

                    //Toggle
                    if (txtView.getVisibility() == View.VISIBLE) {
                        txtView.setVisibility(View.INVISIBLE);
                    }
                    else
                        txtView.setVisibility(View.VISIBLE);

                    //If you want it only one time
                    //txtView.setVisibility(View.VISIBLE);
                }
            });

        }

    }


