package com.itechmobile.budget.ui.mvp

abstract class PresenterBase<T : MvpView> : MvpPresenter<T> {

    private var mView: T? = null
    val view: T? get() = mView
    val isViewAttached: Boolean get() = mView != null


    override fun attachView(mvpView: T) {
        mView = mvpView
    }

    override fun detachView() {
        mView = null
    }

    override fun destroy() {

    }

}