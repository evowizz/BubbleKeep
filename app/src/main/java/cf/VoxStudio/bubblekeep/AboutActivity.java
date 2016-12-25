package cf.VoxStudio.bubblekeep;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.concurrent.Callable;


public class AboutActivity extends AppCompatActivity {
    //TODO: We need to finish it

    boolean isEvoWizzHidden = true;
    boolean isVojtechhHidden = true;
    boolean isAPHidden = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);
        Toolbar Toolbar = (Toolbar) findViewById(R.id.toolbar);
        Toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        setSupportActionBar(Toolbar);

        //setting navigation bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        final RelativeLayout EvoWizzCard = (RelativeLayout) findViewById(R.id.EvoWizz_card);
        final RelativeLayout EvoWizzCardBellow = (RelativeLayout) findViewById(R.id.EvoWizz_card_bellow);
        final RelativeLayout VojtechhCard = (RelativeLayout) findViewById(R.id.vojtechh_card);
        final RelativeLayout VojtechhCardBellow = (RelativeLayout) findViewById(R.id.vojtechh_card_bellow);
        final RelativeLayout APCard = (RelativeLayout) findViewById(R.id.ap_card);
        final RelativeLayout APCardBellow = (RelativeLayout) findViewById(R.id.ap_card_bellow);


        final RelativeLayout EvoWizzTwitter = (RelativeLayout) findViewById(R.id.EvoWizz_twitter);
        final RelativeLayout EvoWizzGoogle = (RelativeLayout) findViewById(R.id.EvoWizz_google);
        final RelativeLayout VojtechhTwitter = (RelativeLayout) findViewById(R.id.vojtechh_twitter);
        final RelativeLayout VojtechhGoogle = (RelativeLayout) findViewById(R.id.vojtechh_google);
        final RelativeLayout APTwitter = (RelativeLayout) findViewById(R.id.ap_twitter);
        final RelativeLayout APGoogle = (RelativeLayout) findViewById(R.id.ap_google);

        final View seperator = findViewById(R.id.dev_seperator);

        final ImageView EvoWizzArrow = (ImageView) findViewById(R.id.EvoWizz_arrow);
        final ImageView VojtechhArrow = (ImageView) findViewById(R.id.vojtechh_arrow);
        final ImageView APArrow = (ImageView) findViewById(R.id.ap_arrow);

        final Animation animationFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        final Animation animationFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        final ImageView closeButton = (ImageView) findViewById(R.id.closeButton);

        EvoWizzCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEvoWizzHidden){
                    EvoWizzCardBellow.startAnimation(animationFadeIn);
                    EvoWizzCardBellow.setVisibility(View.VISIBLE);
                    EvoWizzArrow.animate().rotation(180).start();
                    seperator.setVisibility(View.GONE);
                    isEvoWizzHidden = false;
                }else {

                    EvoWizzCardBellow.startAnimation(animationFadeOut);
                    EvoWizzArrow.animate().rotation(0).start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            EvoWizzCardBellow.setVisibility(View.GONE);
                            seperator.setVisibility(View.VISIBLE);
                        }
                    }, 100);
                    isEvoWizzHidden = true;
                }
            }
        });

        VojtechhCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVojtechhHidden){
                    VojtechhCardBellow.startAnimation(animationFadeIn);
                    VojtechhCardBellow.setVisibility(View.VISIBLE);
                    VojtechhArrow.animate().rotation(180).start();
                    isVojtechhHidden = false;

                }else {

                    VojtechhCardBellow.startAnimation(animationFadeOut);
                    VojtechhArrow.animate().rotation(0).start();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            VojtechhCardBellow.setVisibility(View.GONE);

                        }
                    }, 100);
                    isVojtechhHidden = true;
                }
            }
        });

        APCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAPHidden){
                    APCardBellow.startAnimation(animationFadeIn);
                    APCardBellow.setVisibility(View.VISIBLE);
                    APArrow.animate().rotation(180).start();
                    APCard.setElevation(4);
                    isAPHidden = false;

                }else {

                    APCardBellow.startAnimation(animationFadeOut);
                    APArrow.animate().rotation(0).start();
                    APCard.setElevation(0);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            APCardBellow.setVisibility(View.GONE);

                        }
                    }, 100);
                    isAPHidden = true;
                }
            }
        });

        EvoWizzTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/EvoWizz"));
                startActivity(i);
            }
        });

        EvoWizzGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/+EvoWizz"));
                startActivity(i);
            }
        });

        VojtechhTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/Vojtuv_tweet"));
                startActivity(i);
            }
        });

        VojtechhGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/+VojtěchHořánek"));
                startActivity(i);
            }
        });

        APTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/c4di4l3x"));
                startActivity(i);
            }
        });

        APGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://plus.google.com/u/0/+AlexandrePiveteau"));
                startActivity(i);
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

}


