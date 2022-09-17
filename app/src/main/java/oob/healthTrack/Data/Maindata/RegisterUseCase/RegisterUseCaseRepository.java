package oob.healthTrack.Data.Maindata.RegisterUseCase;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import oob.healthTrack.Data.Maindata.FingerprintDependenciesWrapper;
import oob.healthTrack.Data.Maindata.FingerprintListener;
import oob.healthTrack.Domain.Maindomain.RegisterUseCase.RegisterUseCaseRepositoryInterface;
import oob.healthTrack.Framework.ApplicationContext;

public class RegisterUseCaseRepository implements RegisterUseCaseRepositoryInterface, FingerprintListener.ListenerCallback {

    private SharedPreferences sharedPreferences;
    private FingerprintListener fingerprintListener;
    private Callback callback;

    private String username;
    private String password;

    public RegisterUseCaseRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(ApplicationContext.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        this.fingerprintListener = new FingerprintListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void register(String username, String password, Callback callback) {
        this.fingerprintListener.startAuth(FingerprintDependenciesWrapper.getFingerprintManager(), FingerprintDependenciesWrapper.getCryptoObjectForEncrypting());

        this.username = username;
        this.password = password;
        this.callback = callback;
        this.callback.onListenerSetUp();
    }

    @Override
    @TargetApi(Build.VERSION_CODES.M)
    public void authenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
        this.callback.onListenerResult();

        String encryptedPassword = FingerprintDependenciesWrapper.encryptString(result.getCryptoObject().getCipher(), this.password);

        if (encryptedPassword == null) {
            this.callback.onRegisterError();
        } else {
            this.sharedPreferences.edit()
                    .putString(ApplicationContext.USERNAME_KEY, this.username)
                    .putString(ApplicationContext.ENCRYPTED_PASSWORD_KEY, encryptedPassword)
                    .apply();
            this.callback.onRegisterSuccess();
        }


        this.cleanDependenciesValues();
    }

    @Override
    public void authenticationFailed(String error) {
        if (this.callback == null) {
            return;
        }

        this.callback.onListenerResult();
        this.callback.onRegisterError();

        this.cleanDependenciesValues();
    }

    private void cleanDependenciesValues() {
        this.username = null;
        this.password = null;
        this.fingerprintListener.cancel();
        this.callback = null;
    }
}
