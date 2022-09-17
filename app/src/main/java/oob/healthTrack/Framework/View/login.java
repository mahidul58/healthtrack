package oob.healthTrack.Framework.View;

import android.Manifest;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.os.Bundle;

import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;

import butterknife.OnClick;
import oob.healthTrack.Domain.Maindomain.CheckFingerprintSensorUseCase.CheckFingerprintSensorUseCase;
import oob.healthTrack.Domain.Maindomain.CheckFingerprintSensorUseCase.CheckFingerprintSensorUseCaseViewInterface;
import oob.healthTrack.Domain.Maindomain.CheckUsernameStoredUseCase.CheckUsernameStoredUseCase;
import oob.healthTrack.Domain.Maindomain.CheckUsernameStoredUseCase.CheckUsernameStoredUseCaseViewInterface;
import oob.healthTrack.Domain.Maindomain.LoginWithFingerprintUseCase.LoginWithFingerprintUseCase;
import oob.healthTrack.Domain.Maindomain.LoginWithFingerprintUseCase.LoginWithFingerprintUseCaseViewInterface;
import oob.healthTrack.Domain.Maindomain.RegisterUseCase.RegisterUseCase;
import oob.healthTrack.Domain.Maindomain.RegisterUseCase.RegisterUseCaseViewInterface;
import oob.healthTrack.Framework.ApplicationContext;
import oob.healthTrack.Framework.Util.DialogUtil;
import oob.healthTrack.MainActivity;
import oob.healthTrack.R;

public class login extends AppCompatActivity implements
        CheckFingerprintSensorUseCaseViewInterface,
        RegisterUseCaseViewInterface,
        LoginWithFingerprintUseCaseViewInterface,
        CheckUsernameStoredUseCaseViewInterface, DialogInterface.OnClickListener {

    @BindView(R.id.usernameET)
    EditText usernameET;
    @BindView(R.id.passwordET)
    EditText passwordET;
    @BindView(R.id.fingerprintIconIV)
    View fingerprintIconIV;

    private CheckFingerprintSensorUseCase checkFingerprintSensorUseCase;
    private CheckUsernameStoredUseCase checkUsernameStoredUseCase;
    private RegisterUseCase registerUseCase;
    private LoginWithFingerprintUseCase loginWithFingerprintUseCase;

    private boolean deviceHasFingerprintSensor = false;
    private boolean canAuthenticateViaFingerprintSensor = false;

    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.login);

        ButterKnife.bind(this);

        this.tintActionBarTextColor();
        this.injectUseCases();

        this.checkFingerprintSensorUseCase.check();
    }

    private void tintActionBarTextColor() {
        Toolbar actionBarToolbar = this.findViewById(R.id.action_bar);
        if (actionBarToolbar != null) {
            actionBarToolbar.setTitleTextColor(this.getResources().getColor(R.color.colorAccent));
        }
    }

    private void injectUseCases() {
        this.checkFingerprintSensorUseCase = new CheckFingerprintSensorUseCase(this);
        this.registerUseCase = new RegisterUseCase(this);
        this.loginWithFingerprintUseCase = new LoginWithFingerprintUseCase(this);
        this.checkUsernameStoredUseCase = new CheckUsernameStoredUseCase(this);
    }

    @OnClick(R.id.registerBtnLayoutLL)
    public void onRegisterBtnClicked() {
        if (!this.deviceHasFingerprintSensor) {
            DialogUtil.showAlertDialog(
                    this,
                    this.getString(R.string.main_warning_dialog_title),
                    this.getString(R.string.main_regular_register_dialog_message),
                    this.getString(R.string.main_close_dialog_positive_button)
            );
            return;
        }

        this.registerUseCase.register(this.usernameET.getText().toString(), this.passwordET.getText().toString());
    }

    @OnClick(R.id.loginBtnLayoutLL)
    public void onLoginBtnClicked() {
        if (!this.deviceHasFingerprintSensor || !this.canAuthenticateViaFingerprintSensor) {
            DialogUtil.showAlertDialog(
                    this,
                    this.getString(R.string.main_warning_dialog_title),
                    this.getString(R.string.main_regular_login_dialog_message),
                    this.getString(R.string.main_close_dialog_positive_button)
            );
            return;
        }

        this.loginWithFingerprintUseCase.login();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void goToAuthenticatedScreen() {
        this.startActivity(
                new Intent(this.getContext(), MainActivity.class)
        );
    }

    @Override
    public void showLoginErrorWarning() {
        DialogUtil.showAlertDialog(
                this,
                this.getString(R.string.main_error_dialog_title),
                this.getString(R.string.main_incorrect_username_password_dialog_message),
                this.getString(R.string.main_close_dialog_positive_button)
        );
    }

    @Override
    public void showFingerprintReadyDialog() {
        Drawable icon = this.getResources().getDrawable(R.drawable.fingerprint_icon);
        icon.setColorFilter(this.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);

        this.alertDialog = DialogUtil.showWaitingAlertDialog(
                this,
                this.getString(R.string.main_fingerprint_dialog_title),
                icon,
                this.getString(R.string.main_fingerprint_dialog_message)
        );
    }

    @Override
    public void hideFingerprintReadyDialog() {
        this.alertDialog.dismiss();
        Drawable icon = this.getResources().getDrawable(R.drawable.fingerprint_icon);
        icon.setColorFilter(this.getResources().getColor(android.R.color.white), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void showRegistrationSuccessDialog() {
        DialogUtil.showAlertDialog(
                this,
                this.getString(R.string.main_register_complete_dialog_title),
                this.getString(R.string.main_register_complete_dialog_message),
                this.getString(R.string.main_close_dialog_positive_button)
        );
        this.canAuthenticateViaFingerprintSensor = true;
        this.clearInputs();
    }

    @Override
    public void showRegistrationErrorsWarning() {
        DialogUtil.showAlertDialog(
                this,
                this.getString(R.string.main_error_dialog_title),
                this.getString(R.string.main_errors_registration_dialog_message),
                this.getString(R.string.main_close_dialog_positive_button)
        );
    }

    @Override
    public void showEmptyUsernamePasswordWarning() {
        DialogUtil.showAlertDialog(
                this,
                this.getString(R.string.main_error_dialog_title),
                this.getString(R.string.main_empty_username_password_dialog_message),
                this.getString(R.string.main_close_dialog_positive_button)
        );
    }

    @Override
    public void showFingerprintIcon() {
        this.fingerprintIconIV.setVisibility(View.VISIBLE);
    }

    @Override
    public void showAndroidVersionLowerThanMarshmallowWarning() {
        DialogUtil.showAlertDialog(
                this.getContext(),
                this.getString(R.string.main_error_dialog_title),
                this.getString(R.string.main_low_android_version_dialog_message),
                this.getString(android.R.string.ok)
        );
    }

    @Override
    public void showPermissionNotGrantedWarning() {
        DialogUtil.showAlertDialog(
                this.getContext(),
                this.getString(R.string.main_warning_dialog_title),
                this.getString(R.string.main_fingerprint_permission_missing_dialog_message),
                this.getString(R.string.main_fingerprint_grant_permission_btn_label),
                this,
                true
        );
    }

    @Override
    public void showHardwareNotDetectedWarning() {
        DialogUtil.showAlertDialog(
                this.getContext(),
                this.getString(R.string.main_error_dialog_title),
                this.getString(R.string.main_no_fingerprint_device_dialog_message),
                this.getString(android.R.string.ok)
        );
    }

    @Override
    public void showLockScreenNotSecuredWarning() {
        DialogUtil.showAlertDialog(
                this.getContext(),
                this.getString(R.string.main_error_dialog_title),
                this.getString(R.string.main_no_secure_lockScreen_set_dialog_message),
                this.getString(android.R.string.ok)
        );
    }

    @Override
    public void showFingerprintSensorIconReady() {
        this.deviceHasFingerprintSensor = true;

        this.fingerprintIconIV.setVisibility(View.GONE);
        if (this.checkUsernameStoredUseCase.check()) {
            this.canAuthenticateViaFingerprintSensor = true;
            this.fingerprintIconIV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showNoFingerprintsEnrolledWarning() {
        DialogUtil.showAlertDialog(
                this.getContext(),
                this.getString(R.string.main_warning_dialog_title),
                this.getString(R.string.main_no_fingerprints_enrolled_dialog_message),
                this.getString(android.R.string.ok)
        );
    }

    private void clearInputs() {
        this.usernameET.setText("");
        this.passwordET.setText("");
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void onClick(DialogInterface dialog, int which) {
        ActivityCompat.requestPermissions(login.this,
                new String[]{Manifest.permission.USE_FINGERPRINT},
                ApplicationContext.FINGERPRINT_PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == ApplicationContext.FINGERPRINT_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Recheck the rest of conditions
                this.checkFingerprintSensorUseCase.check();
            } else {
                this.showFingerprintPermissionNotGrantedWarning();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showFingerprintPermissionNotGrantedWarning() {
        DialogUtil.showAlertDialog(
                this.getContext(),
                this.getString(R.string.main_warning_dialog_title),
                this.getString(R.string.main_fingerprint_permission_missing_not_granted_dialog_message),
                this.getString(android.R.string.ok)
        );
    }
}
