package oob.healthTrack.Data.Maindata.CheckFingerprintSensorUseCase;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;


import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.hardware.fingerprint.FingerprintManagerCompat;

import oob.healthTrack.Data.Maindata.FingerprintDependenciesWrapper;
import oob.healthTrack.Domain.Maindomain.CheckFingerprintSensorUseCase.CheckFingerprintSensorUseCaseRepositoryInterface;

public class CheckFingerprintSensorUseCaseRepository implements CheckFingerprintSensorUseCaseRepositoryInterface {

    private Context context;

    public CheckFingerprintSensorUseCaseRepository(Context context) {
        this.context = context;
    }

    @Override
    public void check(FingerprintSensorCallback callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            callback.onAndroidVersionLowerThanMarshmallow();
            return;
        }

        if (ActivityCompat.checkSelfPermission(this.context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            callback.onPermissionNotGranted();
            return;
        }

        KeyguardManager keyguardManager = (KeyguardManager) this.context.getSystemService(Context.KEYGUARD_SERVICE);

        if (keyguardManager == null || !keyguardManager.isKeyguardSecure()) {
            callback.onLockScreenNotSecured();
            return;
        }

        FingerprintManagerCompat fingerprintManager = FingerprintManagerCompat.from(this.context);

        if (!fingerprintManager.isHardwareDetected()) {
            callback.onHardwareNotDetected();
            return;
        }

        if (!fingerprintManager.hasEnrolledFingerprints()) {
            callback.onNoFingerprintsEnrolled();
            return;
        }

        callback.onFingerprintSensorDetected();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void initFingerprintDependencies() {
        FingerprintDependenciesWrapper.init(this.context);
    }
}
