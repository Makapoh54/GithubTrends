package com.test.anton.githubtrends.github.users;

import com.test.anton.githubtrends.data.GithubService;
import com.test.anton.githubtrends.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public class UserDetailsPresenter {
    private final UserDetailsContract.View mDetailsView;
    private final GithubService mGithubService;
    private Call<User> mCall;

    public UserDetailsPresenter(UserDetailsContract.View detailsView, GithubService githubService) {
        mDetailsView = detailsView;
        mGithubService = githubService;
    }

    public void retrieveDetails(String url) {
        mCall = mGithubService.getUser(url);
        mCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    mDetailsView.showDetails(response.body());
                    Timber.i("User details were loaded from api.");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (!mCall.isCanceled()) {
                    mDetailsView.showErrorMessage();
                    Timber.e(t, "Unable to load user details data.");
                } else {
                    Timber.e(t, "User data request was cancelled.");
                }
            }
        });
    }

    public void cancelretrieveDetails() {
        if (mCall.isExecuted()) {
            mCall.cancel();
        }
    }
}
