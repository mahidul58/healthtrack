package oob.healthTrack.Data.Maindata.CheckUsernameStoredUseCase;

import android.content.Context;
import android.content.SharedPreferences;

import oob.healthTrack.Domain.Maindomain.CheckUsernameStoredUseCase.CheckUsernameStoredUseCaseRepositoryInterface;
import oob.healthTrack.Framework.ApplicationContext;

public class CheckUsernameStoredUseCaseRepository implements CheckUsernameStoredUseCaseRepositoryInterface {
    private SharedPreferences sharedPreferences;

    public CheckUsernameStoredUseCaseRepository(Context context) {
        this.sharedPreferences = context.getSharedPreferences(ApplicationContext.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public boolean check() {
        return !this.sharedPreferences.getString(ApplicationContext.USERNAME_KEY, "").isEmpty();
    }
}
