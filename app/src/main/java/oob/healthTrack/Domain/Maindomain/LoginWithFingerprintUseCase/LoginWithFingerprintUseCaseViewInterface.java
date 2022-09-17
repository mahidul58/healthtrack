package oob.healthTrack.Domain.Maindomain.LoginWithFingerprintUseCase;

import android.content.Context;

public interface LoginWithFingerprintUseCaseViewInterface {
    void goToAuthenticatedScreen();

    void showLoginErrorWarning();

    void showFingerprintReadyDialog();

    void hideFingerprintReadyDialog();

    Context getContext();
}
