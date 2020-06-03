package com.example.dessert_order_app.uiFragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dessert_order_app.Activity.MainActivity;
import com.example.dessert_order_app.R;

import java.util.Locale;

public class SettingFragment extends Fragment {

    Button eng_language, chi_language;
    String lan;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loadLocale();
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        final Intent intent = new Intent(view.getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        eng_language = view.findViewById(R.id.btn_eng);
        chi_language = view.findViewById(R.id.btn_chi);


        eng_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lan = "en";
                setLocale(lan);
                startActivity(intent);
//                Fragment newFragment = new HomeFragment();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container, newFragment);
//                fragmentTransaction.commit();
            }
        });
        chi_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lan = "zh";
                setLocale(lan);
                startActivity(intent);
//
//                Fragment newFragment = new HomeFragment();
//                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.fragment_container, newFragment);
//                fragmentTransaction.commit();
            }
        });
        return view;
    }

    private void setLanguage(String localeCode) {
        Resources res = getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        Configuration configuration = res.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            configuration.locale = new Locale(localeCode.toLowerCase());
        }
        res.updateConfiguration(configuration, displayMetrics);
    }


    public void loadLocale() {
        SharedPreferences preferences = getActivity().getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = preferences.getString("My_Lang", "");
        setLocale(language);
    }


    private void setLocale(String lan) {
        Locale locale = new Locale(lan);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());

        // Save Date
        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("My_Lang", lan);
        editor.apply();

    }

}
