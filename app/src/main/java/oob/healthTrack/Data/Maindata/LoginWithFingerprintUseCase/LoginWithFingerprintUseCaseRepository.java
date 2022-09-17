package oob.healthTrack.Data.Maindata.LoginWithFingerprintUseCase;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import oob.healthTrack.Data.Maindata.FingerprintDependenciesWrapper;
import oob.healthTrack.Data.Maindata.FingerprintListener;
import oob.healthTrack.Domain.Maindomain.LoginWithFingerprintUseCase.LoginWithFingerprintUseCaseRepositoryInterface;
import oob.healthTrack.Framework.ApplicationContext;

public class LoginWithFingerprintUseCaseRepository implements LoginWithFingerprintUseCaseRepositoryInterface, FingerprintListener.ListenerCallback {

    private FingerprintListener fingerprintListener;
    private LoginWithFingerprintCallback callback;
    private SharedPreferences sharedPreferences;

    public LoginWithFingerprintUseCaseRepository(Context context) {
        this.fingerprintListener = new FingerprintListener(this);
        this.sharedPreferences = context.getSharedPreferences(ApplicationContext.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void login(LoginWithFingerprintCallback callback) {
        this.fingerprintListener.startAuth(FingerprintDependenciesWrapper.getFingerprintManager(), FingerprintDependenciesWrapper.getCryptoObjectForDecrypting());

        this.callback = callback;
        this.callback.onListenerSetUp();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void authenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        this.callback.onListenerResult();

        String username = this.sharedPreferences.getString(ApplicationContext.USERNAME_KEY, null);
        String encryptedPassword = this.sharedPreferences.getString(ApplicationContext.ENCRYPTED_PASSWORD_KEY, null);

        String password = FingerprintDependenciesWrapper.decryptString(result.getCryptoObject().getCipher(), encryptedPassword);

        if (username != null && !username.isEmpty() &&
                password != null && !password.isEmpty()) {
            this.callback.onLoginSuccess();
        } else {
            this.callback.onLoginError();
        }

        this.cleanDependenciesValues();
    }

    @Override
    public void authenticationFailed(String error) {
        if (this.callback == null) {
            return;
        }
        this.callback.onListenerResult();

        this.callback.onLoginError();

        this.cleanDependenciesValues();
    }

    private void cleanDependenciesValues() {
        this.fingerprintListener.cancel();
        this.callback = null;
    }
}
