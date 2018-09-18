package com.itechmobile.budget.ui.mvp

interface MvpPresenter<V : MvpView> {

    /**
     * Передача View презентеру
     */
    fun attachView(mvpView: V)

    /**
     * Cигнал презентеру о том, что View готово к работе
     */
    fun viewIsReady()

    /**
     * Презентер должен отпустить View
     */
    fun detachView()

    /**
     * Cигнал презентеру о том, что View завершает свою работу и будет закрыто
     */
    fun destroy()
}