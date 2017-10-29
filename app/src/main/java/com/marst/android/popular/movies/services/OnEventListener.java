package com.marst.android.popular.movies.services;

public interface OnEventListener<T> {
    void onSuccess(T object);
    void onFailure(Exception e);
    void onSuccessNoMovies();
}
