package com.company.sample.samplesnapkit;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.snapchat.kit.sdk.SnapLogin;
import com.snapchat.kit.sdk.core.controller.LoginStateController;
import com.snapchat.kit.sdk.core.models.MeData;
import com.snapchat.kit.sdk.core.models.UserDataResponse;
import com.snapchat.kit.sdk.login.networking.FetchUserDataCallback;

public class MainActivity extends AppCompatActivity {
    private Context usageContext;
    private ConstraintLayout layout;
    private View mLoginButton;

    private LoginStateController.OnLoginStateChangedListener mLoginStateChangedListener =
            new LoginStateController.OnLoginStateChangedListener() {
                @Override
                public void onLoginSucceeded() {

                }

                @Override
                public void onLoginFailed() {

                }

                @Override
                public void onLogout() {

                }
            };

    private LoginStateController.OnLoginStartListener mLoginStartListener =
            new LoginStateController.OnLoginStartListener() {
                @Override
                public void onLoginStart() {

                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usageContext = this; // or getApplicationContext();

        layout = findViewById(R.id.login_layout);

        LoginStateController controller = SnapLogin.getLoginStateController(usageContext);
        controller.addOnLoginStateChangedListener(mLoginStateChangedListener);
        controller.addOnLoginStartListener(mLoginStartListener);

        mLoginButton = SnapLogin.getButton(usageContext, layout);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        layout.removeView(mLoginButton);
        final TextView view = new TextView(usageContext);
        final Button button = new Button(usageContext);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SnapLogin.fetchUserData(usageContext, "me{displayName}", null, new FetchUserDataCallback() {
                    @Override
                    public void onSuccess(@Nullable UserDataResponse userDataResponse) {
                        if (userDataResponse == null || userDataResponse.getData() == null
                                || userDataResponse.getData().getMe() == null) {
                            return;
                        }

                        MeData data = userDataResponse.getData().getMe();
                        view.setText(data.getDisplayName());
                    }

                    @Override
                    public void onFailure(boolean isNetworkError, int statusCode) {
                        String setText = isNetworkError + ", " + statusCode;
                        view.setText(setText);
                    }
                });
            }
        });

        layout.addView(view);
        layout.addView(button);
    }
}