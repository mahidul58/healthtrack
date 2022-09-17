package oob.healthTrack.Domain.Maindomain.LoginWithFingerprintUseCase;

public interface LoginWithFingerprintUseCaseRepositoryInterface {
    void login(LoginWithFingerprintCallback callback);

    interface LoginWithFingerprintCallback {
        void onLoginSuccess();

        void onLoginError();

        void onListenerSetUp();

        void onListenerResult();
    }
}
