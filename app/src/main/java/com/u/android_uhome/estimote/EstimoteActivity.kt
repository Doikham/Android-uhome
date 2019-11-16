package com.u.android_uhome.estimote
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.estimote.mustard.rx_goodness.rx_requirements_wizard.RequirementsWizardFactory
import com.u.android_uhome.R

class EstimoteActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estimote)

        val app = application as EstimoteApplication

        RequirementsWizardFactory
            .createEstimoteRequirementsWizard()
            .fulfillRequirements(this,
                onRequirementsFulfilled = {
                    Log.d("app", "requirements fulfilled")
                    app.enableBeaconNotifications()
                },
                onRequirementsMissing = { requirements ->
                    Log.e("app", "requirements missing: $requirements")
                },

                onError = { throwable ->
                    Log.e("app", "requirements error: $throwable")
                })
    }

}
