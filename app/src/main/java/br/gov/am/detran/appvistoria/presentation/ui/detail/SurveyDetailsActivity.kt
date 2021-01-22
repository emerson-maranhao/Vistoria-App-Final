package br.gov.am.detran.appvistoria.presentation.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.get
import br.gov.am.detran.appvistoria.R
import br.gov.am.detran.appvistoria.databinding.ActivitySurveyDetailsBinding
import br.gov.am.detran.appvistoria.domain.Survey
import br.gov.am.detran.appvistoria.network.OAuthInterceptor
import br.gov.am.detran.appvistoria.session.UserPreferences
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient


const val TAG = "DetailActivity"

class SurveyDetailsActivity : AppCompatActivity() {
    lateinit var survey: Survey

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding=ActivitySurveyDetailsBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        survey = intent.getParcelableExtra("EXTRA_SURVEY")!!

        val licensePlate: String = survey.license_plate
        val renavam: String = survey.renavam
        val year: String = survey.year
        val brand: String = survey.brand
        val type: String = survey.type
        val color: String = survey.color
        val kind: String = survey.kind
        val uf: String = survey.uf
        val chassis: String = survey.chassis
        val engine: String = survey.engine
        val chassisPhoto: String? = survey.chassis_photo
        val chassisObs: String? = survey.chassis_obs
        val enginePhoto: String? = survey.engine_photo
        val engineObs: String? = survey.engine_obs
        val backPhoto: String? = survey.back_photo
        val backObs: String? = survey.back_obs
        val nLaudo:String?=survey.nLaudo

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
           title = nLaudo
            //title= "LV 12112-10/20"

            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        val statusElectric: String? =
            if (survey.electric_pendency!!.isNotEmpty()) survey.electric_pendency else "Nenhuma"
        val statusExternal: String? =
            if (survey.external_pendency!!.isNotEmpty()) survey.external_pendency else "Nenhuma"
        val statusInternal: String? =
            if (survey.internal_pendency!!.isNotEmpty()) survey.internal_pendency else "Nenhuma"
        val statusMandatory: String? =
            if (survey.mandatory_pendency!!.isNotEmpty()) survey.mandatory_pendency else "Nenhuma"

        val surveyPlace: String = survey.survey_place

        Log.i(TAG, "Radio: ${survey.survey_place}")

        binding.edtPlacaDetail.setText(licensePlate)
        binding.edtRenavamDetail.setText(renavam)
        binding.edtAnoDetail.setText(year)
        binding.edtMarcaDetail.setText(brand)
        binding.edtTipoDetail.setText(type)
        binding.edtCorDetail.setText(color)
        binding.edtEspecieDetail.setText(kind)
        binding.edtUfDetail.setText(uf)
        binding.edtChassiDetail.setText(chassis)
        binding.edtMotorDetail.setText(engine)

        if (chassisObs!!.isEmpty()) binding.edtObsChassiDetail.setText(getString(R.string.nenhuma_obs)) else binding.edtObsChassiDetail.setText(
            chassisObs
        )
        if (engineObs!!.isEmpty()) binding.edtObsMotorDetail.setText(getString(R.string.nenhuma_obs)) else binding.edtObsMotorDetail.setText(
            engineObs
        )
        if (backObs!!.isEmpty()) binding.edtObsTraseiraDetail.setText(getString(R.string.nenhuma_obs)) else binding.edtObsTraseiraDetail.setText(
            backObs
        )

//        edt_obs_motor_detail.setText(engineObs)
//        edt_obs_traseira_detail.setText(backObs)

        binding.statusElectricPendency.text = statusElectric
        binding.statusExternalPendency.text = statusExternal
        binding. statusInternalPendency.text = statusInternal
        binding.statusMandatoryPendency.text = statusMandatory

        // Adicionar header na request
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(OAuthInterceptor("Bearer", UserPreferences.token))
            .build()

        // Inserir o OkHttpClient no picasso
        val picasso = Picasso.Builder(this)
            .downloader(OkHttp3Downloader(okHttpClient))
            .build()

        // Carregar a imagem na imageView setada

        picasso
            .load("$chassisPhoto")
            .error(R.drawable.ic_image_not_found)
            .fit()
            .noFade()
            .placeholder(R.drawable.loading_image)
            .into(binding.imgChassiVeiculoDetail)

        picasso
            .load("$enginePhoto")
            //.resize(300, 250)
            //.centerCrop()
            .placeholder(R.drawable.loading_image)
            .error(R.drawable.ic_image_not_found)
            .into(binding.imgMotorVeiculoDetail)


        picasso
            .load("$backPhoto")
            //.resize(300, 250)
            //.centerCrop()
            .placeholder(R.drawable.loading_image)
            .error(R.drawable.ic_image_not_found)
            .into(binding.imgTraseiraVeiculoDetail)

        when (survey.survey_place) {
            getString(R.string.parqueamento_detail) -> binding.radioParqueamento.isChecked = true.apply {
                binding.radioGroupPlace[1].isEnabled = false
                binding.radioGroupPlace[2].isEnabled = false
                binding.edtOutroLocal.visibility = View.INVISIBLE
            }
            getString(R.string.detran_sede_detail) -> binding.radioDetran.isChecked = true.apply {
                binding.radioGroupPlace[0].isEnabled = false
               binding.radioGroupPlace[2].isEnabled = false
                binding.edtOutroLocal.visibility = View.INVISIBLE
            }
            else -> { // Note the block
               binding.radioOutro.isChecked = true
                binding.radioGroupPlace[0].isEnabled = false
               binding.radioGroupPlace[1].isEnabled = false
                binding.edtOutroLocal.setText(surveyPlace)
            }
        }
    }

    fun openAlbum(view: View) {
        val transaction = supportFragmentManager.beginTransaction()
        val fragment = ImageDialogFragment()
        val bundle = Bundle()

        /*
         * Obtendo o posicionamento da imagem acionada, pois é ela
         * que inicialmente será carregada no ImageDialogFragment.
         * */
        val imagePosition = when (view.id) {
            R.id.img_chassi_veiculo_detail -> 0
            R.id.img_motor_veiculo_detail -> 1
            R.id.img_traseira_veiculo_detail -> 2
            // R.id.img_odometro_veiculo_add -> 3
            else -> 3
        }

        bundle.putParcelable("survey", survey)
        Log.i("log----------", imagePosition.toString())
        bundle.putInt("position", imagePosition)

        fragment.arguments = bundle
        fragment.show(transaction, ImageDialogFragment.KEY)
    }

    companion object {
        fun getStartIntent(context: Context, survey: Survey): Intent {
            return Intent(context, SurveyDetailsActivity::class.java).apply {
                this.putExtra("EXTRA_SURVEY", survey)
            }
        }
    }
}
