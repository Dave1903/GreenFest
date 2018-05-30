package com.thedevlopershome.greenfest;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ramotion.expandingcollection.ECBackgroundSwitcherView;
import com.ramotion.expandingcollection.ECCardData;
import com.ramotion.expandingcollection.ECPagerView;
import com.ramotion.expandingcollection.ECPagerViewAdapter;
import com.thefinestartist.finestwebview.FinestWebView;

import java.util.List;

import java.util.logging.LogRecord;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;


public class GreenMenuActivity extends AppCompatActivity {

    private ECPagerView ecPagerView;
    public int position=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


                setContentView(R.layout.activity_green_menu);


        ecPagerView = (ECPagerView) findViewById(R.id.ec_pager_element);

        // Generate example dataset
        List<ECCardData> dataset = CardDataImpl.generateExampleData();

        // Implement pager adapter and attach it to pager view
        ECPagerViewAdapter ecPagerViewAdapter = new ECPagerViewAdapter(getApplicationContext(), dataset) {
            @Override
            public void instantiateCard(LayoutInflater inflaterService, ViewGroup head, final ListView list, ECCardData data) {
                // Data object for current card
                CardDataImpl cardData = (CardDataImpl) data;

               // Set adapter and items to current card content list
                final List<String> listItems = cardData.getListItems();
                final CardListItemAdapter listItemAdapter = new CardListItemAdapter(getApplicationContext(), listItems);
                list.setAdapter(listItemAdapter);
                // Also some visual tuning can be done here
                list.setBackgroundColor(Color.WHITE);

                // Here we can create elements for head view or inflate layout from xml using inflater service
                TextView cardTitle = new TextView(getApplicationContext());
                cardTitle.setText(cardData.getCardTitle());
                cardTitle.setTextSize(COMPLEX_UNIT_DIP, 20);
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.gravity = Gravity.CENTER;
                head.addView(cardTitle, layoutParams);

                // Card toggling by click on head element
                head.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        switch(position) {
                            case 0:
                            Intent intent = new Intent(GreenMenuActivity.this, GreenFestActivity.class);
                            startActivity(intent);
                                break;
                            case 1: Intent regIntent=new Intent(GreenMenuActivity.this,PukaarRegistrationActivity.class);
                                     startActivity(regIntent);
                                    break;
                            case 2:Intent reIntent=new Intent(GreenMenuActivity.this,GreenInfoActivity.class);
                                   reIntent.putExtra("flag",1);
                                   startActivity(reIntent);
                                    break;

                            case 3:    Intent revIntent=new Intent(GreenMenuActivity.this,GreenInfoActivity.class);
                                       revIntent.putExtra("flag",2);
                                       startActivity(revIntent);
                                        break;
                            case 4:     String s="Debug-infos:";
                                s += "\n OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")";
                                s += "\n OS API Level: "+android.os.Build.VERSION.RELEASE + "("+android.os.Build.VERSION.SDK_INT+")";
                                s += "\n Device: " + android.os.Build.DEVICE;
                                s += "\n Model (and Product): " + android.os.Build.MODEL + " ("+ android.os.Build.PRODUCT + ")";
                                Intent intent1 = new Intent (Intent.ACTION_SEND);
                                intent1.setType("message/rfc822");
                                intent1.putExtra(Intent.EXTRA_EMAIL, new String[]{"thedevelopershome@gmail.com,gogreennitrr@gmail.com"});
                                intent1.putExtra(Intent.EXTRA_SUBJECT, "feedback for greenFestNitrr");
                                intent1.putExtra(Intent.EXTRA_TEXT,s);
                                intent1.setPackage("com.google.android.gm");
                                if (intent1.resolveActivity(getPackageManager())!=null)
                                    startActivity(intent1);
                                else
                                    Toast.makeText(GreenMenuActivity.this,"Gmail App is not installed",Toast.LENGTH_SHORT).show();
                                break;
                            case 5: Uri uriUrl = Uri.parse("http://gogreennitrr.org/");
                                new FinestWebView.Builder(GreenMenuActivity.this).show(String.valueOf(uriUrl));
                        }
                    }
                });

            }
        };
        ecPagerView.setPagerViewAdapter(ecPagerViewAdapter);
        ecPagerView.setOnCardSelectedListener(new ECPagerView.OnCardSelectedListener() {
            @Override
            public void cardSelected(int i, int i1, int i2) {
                position=i;
            }
        });

        // Add background switcher to pager view
        ecPagerView.setBackgroundSwitcherView((ECBackgroundSwitcherView) findViewById(R.id.ec_bg_switcher_element));

        // Directly modifying dataset
        dataset.remove(5);
        ecPagerViewAdapter.notifyDataSetChanged();

    }
}
