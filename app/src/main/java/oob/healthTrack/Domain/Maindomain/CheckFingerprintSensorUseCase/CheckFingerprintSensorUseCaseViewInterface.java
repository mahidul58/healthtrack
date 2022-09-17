package oob.healthTrack.Domain.Maindomain.CheckFingerprintSensorUseCase;

import android.content.Context;

public interface CheckFingerprintSensorUseCaseViewInterface {
    Context getContext();

    void showAndroidVersionLowerThanMarshmallowWarning();

    void showPermissionNotGrantedWarning();

    void showHardwareNotDetectedWarning();

    void showLockScreenNotSecuredWarning();

    void showFingerprintSensorIconReady();

    void showNoFingerprintsEnrolledWarning();
}
