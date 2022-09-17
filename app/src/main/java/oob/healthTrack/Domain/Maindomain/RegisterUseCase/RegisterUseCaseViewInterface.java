package oob.healthTrack.Domain.Maindomain.RegisterUseCase;

import android.content.Context;

public interface RegisterUseCaseViewInterface {
    Context getContext();

    void showFingerprintReadyDialog();

    void showRegistrationErrorsWarning();

    void hideFingerprintReadyDialog();

    void showRegistrationSuccessDialog();

    void showEmptyUsernamePasswordWarning();

    void showFingerprintIcon();
}
