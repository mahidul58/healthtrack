package oob.healthTrack.Domain.Maindomain.CheckFingerprintSensorUseCase;

public interface CheckFingerprintSensorUseCaseRepositoryInterface {
    void check(FingerprintSensorCallback callback);

    void initFingerprintDependencies();

    interface FingerprintSensorCallback {
        void onAndroidVersionLowerThanMarshmallow();

        void onPermissionNotGranted();

        void onHardwareNotDetected();

        void onLockScreenNotSecured();

        void onFingerprintSensorDetected();

        void onNoFingerprintsEnrolled();
    }
}
