package com.u.android_uhome.home

class HomeConfigure {
    companion object Singleton {
        fun configure(activity: HomeActivity) {
            val router = HomeRouter()
            router.activity = activity

            val presenter = HomePresenter()
            presenter.activity = activity

            val interactor = HomeInteractor()
            interactor.presenter = presenter
            interactor.worker = HomeWorker()

            activity.interactor = interactor
            activity.router = router
        }
    }
}