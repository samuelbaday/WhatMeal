package sbsolutions.whatmeal;

import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WhatMeanMain extends AppCompatActivity {

    @Bind(R.id.add_card)
    CardView addCardView;

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_what_mean_main);
        ButterKnife.bind(this);

        addCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(WhatMeanMain.this,AddIngredient.class));
            }
        });
    }
}
