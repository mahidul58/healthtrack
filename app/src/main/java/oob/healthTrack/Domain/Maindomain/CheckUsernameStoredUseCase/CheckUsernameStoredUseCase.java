package oob.healthTrack.Domain.Maindomain.CheckUsernameStoredUseCase;

import oob.healthTrack.Data.Maindata.CheckUsernameStoredUseCase.CheckUsernameStoredUseCaseRepository;

public class CheckUsernameStoredUseCase {
    private CheckUsernameStoredUseCaseViewInterface view;
    private CheckUsernameStoredUseCaseRepositoryInterface repository;

    public CheckUsernameStoredUseCase(CheckUsernameStoredUseCaseViewInterface view) {
        this.view = view;
        this.repository = new CheckUsernameStoredUseCaseRepository(this.view.getContext());
    }

    public boolean check() {
        return this.repository.check();
    }
}
