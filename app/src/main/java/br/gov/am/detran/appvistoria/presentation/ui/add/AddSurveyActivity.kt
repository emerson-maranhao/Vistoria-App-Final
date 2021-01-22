package br.gov.am.detran.appvistoria.presentation.ui.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import br.gov.am.detran.appvistoria.R
import br.gov.am.detran.appvistoria.databinding.ActivityAddSurveyBinding
import br.gov.am.detran.appvistoria.domain.RequestBodySurvey
import br.gov.am.detran.appvistoria.domain.Survey
import br.gov.am.detran.appvistoria.domain.Vehicle
import br.gov.am.detran.appvistoria.presentation.MainActivity
import br.gov.am.detran.appvistoria.presentation.ui.home.Status
import br.gov.am.detran.appvistoria.until.CustomProgressDialog
import br.gov.am.detran.appvistoria.until.ImageResizer
import br.gov.am.detran.appvistoria.until.Utils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

private var imageview: ImageView? = null
private const val TAG = "AddSurveyActivity"

lateinit var fileProvider: Uri
lateinit var photoFile1: File
lateinit var photoFile2: File
lateinit var photoFile3: File
var latitude: String? = null
var longitude: String? = null
lateinit var listDataHeader: List<FilterHeader>
lateinit var listDataChild: HashMap<String, List<String>>
lateinit var listAdapter: FIltersExpandableAdapter
lateinit var activity: AddSurveyActivity
private lateinit var fusedLocationClient: FusedLocationProviderClient

private lateinit var binding: ActivityAddSurveyBinding

private const val FILE_NAME = "image.jpg"
var chassis_photo_uri: String? = null
var engine_photo_uri: String? = null

var back_photo_uri: String? = null


class AddSurveyActivity : AppCompatActivity() {

    private val addSurveyViewModel: AddSurveyViewModel by viewModel()
    private val progressDialog = CustomProgressDialog()
    private var radioSelectedPlace: String = "Parqueamento"
    private var radioSelectedStatus: String = ""
    private var electricPendency: String? = null
    private var externalPendency: String? = null
    private var internalPendency: String? = null
    private var mandatoryPendency: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAddSurveyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setNavigationOnClickListener {
            val alertDialog: AlertDialog = activity.let {
                val builder = AlertDialog.Builder(it, R.style.AlertDialogTheme)
                builder.apply {
                    setPositiveButton(getString(R.string.sim)
                    ) { _, _ ->
                        finish()
                        // User clicked OK button
                    }
                    setNegativeButton(getString(R.string.nao)
                    ) { dialog, _ ->
                        // User cancelled the dialog
                        dialog.cancel()
                    }
                }
                // Set other dialog properties
                builder.setTitle(getString(R.string.atencao))
                builder.setMessage(getString(R.string.todos_dados_serao_perdidos))

                // Create the AlertDialog
                builder.create()
            }
            alertDialog.show()
        }
        supportActionBar?.apply {
            title = "Nova Vistoria"

            // show back button on toolbar
            // on back button press, it will navigate to parent activity
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowHomeEnabled(true)
        }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkPermissionLocation()


//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
//
//        if (checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
//        ) {
//
//            return
//        }
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location: Location? ->
//                // Got last known location. In some rare situations this can be null.
//                //Toast.makeText(this, "Latitude>" + location?.latitude, Toast.LENGTH_SHORT).show()
//                Log.i("Latitude:", location?.latitude?.toFloat().toString())
//                Log.i("Longitude:", location?.longitude?.toFloat().toString())
//                latitude = location?.latitude?.toFloat().toString()
//                longitude = location?.longitude?.toFloat().toString()
//            }


        activity = this
        val vehicle: Vehicle? = intent.getParcelableExtra("request")

        Log.i(TAG, "Dados recebido da API ${vehicle.toString()}")
//        expandablelistviewFilter = findViewById(R.id.expandablelistview_filter)

        (binding.edtPlacaAdd as TextView).text = vehicle?.placa
        (binding.edtRenavamAdd as TextView).text = vehicle?.renavam
        (binding.edtAnoAdd as TextView).text = vehicle?.anoFabricacao
        (binding.edtMarcaAdd as TextView).text = vehicle?.descricaoMarca
        (binding.edtTipoAdd as TextView).text = vehicle?.descricaoTipo
        (binding.edtCorAdd as TextView).text = vehicle?.descricaoCor
        (binding.edtEspecieAdd as TextView).text = vehicle?.descricaoCategoria
        (binding.edtUfAdd as TextView).text = vehicle?.ufEmplacamento

        //Verfificaçao de valores nulos retornados da API
        if (vehicle?.renavam.isNullOrBlank()) {
            (binding.edtRenavamAdd as TextView).text = getString(R.string.nao_possui)
        } else {
            (binding.edtRenavamAdd as TextView).text = vehicle?.renavam
        }
        if (vehicle?.ufEmplacamento.isNullOrBlank()) {
            (binding.edtUfAdd as TextView).text = getString(R.string.nao_possui)
        } else {
            (binding.edtUfAdd as TextView).text = vehicle?.ufEmplacamento
        }
        if (vehicle?.descricaoMarca.isNullOrBlank()) {
            (binding.edtMarcaAdd as TextView).text = getString(R.string.nao_possui)
        } else {
            (binding.edtMarcaAdd as TextView).text = vehicle?.descricaoMarca
        }
        if (vehicle?.descricaoTipo.isNullOrBlank()) {
            (binding.edtTipoAdd as TextView).text = getString(R.string.nao_possui)
        } else {
            (binding.edtTipoAdd as TextView).text = vehicle?.descricaoTipo
        }
        if (vehicle?.descricaoCor.isNullOrBlank()) {
            (binding.edtCorAdd as TextView).text = getString(R.string.nao_possui)
        } else {
            (binding.edtCorAdd as TextView).text = vehicle?.descricaoCor
        }

        if (vehicle?.descricaoCategoria.isNullOrBlank()) {
            (binding.edtEspecieAdd as TextView).text = getString(R.string.nao_possui)
        } else {
            (binding.edtEspecieAdd as TextView).text = vehicle?.descricaoCategoria
        }

        if (vehicle?.anoFabricacao.isNullOrBlank()) {
            (binding.edtAnoAdd as TextView).text = getString(R.string.nao_possui)
        } else {
            (binding.edtAnoAdd as TextView).text = vehicle?.anoFabricacao
        }


        if (vehicle?.numeroMotor.isNullOrBlank()) {
            (binding.edtMotorAdd as TextView).text = getString(R.string.nao_possui)
        } else {
            (binding.edtMotorAdd as TextView).text = vehicle?.numeroMotor
        }
        if (vehicle?.chassi.isNullOrBlank()) {
            (binding.edtChassiAdd as TextView).text = getString(R.string.nao_possui)
        } else {
            (binding.edtChassiAdd as TextView).text = vehicle?.chassi
        }

        prepareItensToListExpanded()
        // Get radio group selected item using on checked change listener
        binding.radioGroupPlace.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            when (checkedId) {
                R.id.radioParqueamento -> {
                    if (binding.edtOutroLocalAdd.toString().trim().isNotEmpty()) {
                        binding.edtOutroLocalAdd.text = null
                    }
                    Utils().hideSoftKeyboard(this@AddSurveyActivity, binding.edtOutroLocalAdd)
                    Log.i(TAG, radio.text.toString())
                    binding.edtOutroLocalAdd.isVisible = false
                    radioSelectedPlace = radio.text.toString()
                    Log.i(TAG, "local1: $radioSelectedPlace")
                }
                R.id.radioDetranSede -> {
                    Utils().hideSoftKeyboard(this@AddSurveyActivity, binding.edtOutroLocalAdd)
                    if (binding.edtOutroLocalAdd.toString().trim().isNotEmpty()) {
                        binding.edtOutroLocalAdd.text = null
                    }
                    Log.i(TAG, radio.text.toString())
                    binding.edtOutroLocalAdd.isVisible = false
                    radioSelectedPlace = radio.text.toString()
                    Log.i(TAG, "local2: $radioSelectedPlace")
                }
                R.id.radioOutro -> {
                    Log.i(TAG, radio.text.toString())
                    radioSelectedPlace = radio.text.toString()
                    Log.i(TAG, "local3: $radioSelectedPlace")
                    binding.edtOutroLocalAdd.isVisible = true
                    binding.edtOutroLocalAdd.requestFocus()
                    Utils().showSoftKeyboard(this@AddSurveyActivity, binding.edtOutroLocalAdd)
                }
            }
        }

        addSurveyViewModel.photoUrl.observe(this, { bitmap ->
            // check for null and set your imageBitmap accordingly
            Log.i("Model:", "ViewModel Recriada")

            bitmap?.let {
                imageview!!.setImageBitmap(it)
            }

        })

        // clicked image
        binding.cardChassiVeiculoAdd.setOnClickListener {
            imageview = binding.imgChassiVeiculoAdd2
            takePhotoFromCamera(REQUEST_CODE_LICENSE)
        }
        binding.imgMotorVeiculoAdd.setOnClickListener {
            imageview = binding.imgMotorVeiculoAdd2
            takePhotoFromCamera(REQUEST_CODE_ENGINE)
        }
        binding.imgTraseiraVeiculoAdd.setOnClickListener {
            imageview = binding.imgTraseiraVeiculoAdd2
            takePhotoFromCamera(REQUEST_CODE_BACK)
        }

        binding.edtOutroLocalAdd.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                radioSelectedPlace = binding.edtOutroLocalAdd.text.toString()
            }
        })

        binding.btnSalvarVistoria.setOnClickListener {

            electricPendency = listAdapter.getValuesStatusEletric()
            externalPendency = listAdapter.getValuesStatusExternal()
            internalPendency = listAdapter.getValuesStatusInternal()
            mandatoryPendency = listAdapter.getValuesStatusMandatory()
            when {

                chassis_photo_uri.isNullOrEmpty() || engine_photo_uri
                    .isNullOrEmpty() || back_photo_uri.isNullOrEmpty()
                -> {
                    Toast.makeText(
                        this,
                        getString(R.string.imagens_obrigatorias),
                        Toast.LENGTH_LONG
                    )
                        .show()
                }

                radioSelectedPlace == getString(R.string.outro) && binding.edtOutroLocalAdd.text.isEmpty() -> {
                    Toast.makeText(this, getString(R.string.outro_local_vazio), Toast.LENGTH_LONG)
                        .show()
                }
                radioSelectedPlace == getString(R.string.outro) && binding.edtOutroLocalAdd.text.isNotEmpty()
                -> {
                    radioSelectedPlace = binding.edtOutroLocalAdd.text.toString()
                }
                radioSelectedPlace != getString(R.string.outro) && radioSelectedPlace != getString(R.string.detran_sede) && radioSelectedPlace != getString(
                    R.string.parqueamento) && binding.edtOutroLocalAdd.text.isNullOrEmpty() -> {
                    Toast.makeText(this, getString(R.string.outro_local_vazio), Toast.LENGTH_LONG)
                        .show()
                }
                else -> {

                    val listItems = arrayOf(getString(R.string.aprovada),
                        getString(R.string.pendente),
                        getString(
                            R.string.reprovada
                        ))

                    val mBuilder =
                        AlertDialog.Builder(this@AddSurveyActivity, R.style.AlertDialogTheme)

                    mBuilder.setTitle(getString(R.string.status_vistoria))
                    mBuilder.setCancelable(false)
                    mBuilder.setSingleChoiceItems(listItems, -1) { _, i ->
                        radioSelectedStatus = listItems[i]
                    }

                    mBuilder.setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        val position = (dialog as AlertDialog).listView.checkedItemPosition
                        // if selected, then get item text
                        if (position != -1) {
                            val selectedItem = listItems[position]
                            radioSelectedStatus = selectedItem
                            Log.i(TAG, radioSelectedPlace)
                            Log.i(TAG, radioSelectedStatus)

                            val survey = Survey(
                                _id = "",
                                user = "",
                                license_plate = binding.edtPlacaAdd.text.toString(),
                                renavam = binding.edtRenavamAdd.text.toString(),
                                year = binding.edtAnoAdd.text.toString(),
                                brand = binding.edtMarcaAdd.text.toString(),
                                type = binding.edtTipoAdd.text.toString(),
                                color = binding.edtCorAdd.text.toString(),
                                kind = binding.edtEspecieAdd.text.toString(),
                                uf = binding.edtUfAdd.text.toString(),
                                chassis = binding.edtChassiAdd.text.toString(),
                                engine = binding.edtMotorAdd.text.toString(),
                                chassis_photo = chassis_photo_uri,
                                chassis_obs = binding.edtObsChassiAdd.text.toString(),
                                engine_photo = engine_photo_uri,
                                engine_obs = binding.edtObsMotorAdd.text.toString(),
                                back_photo = back_photo_uri,
                                back_obs = binding.edtObsTraseiraAdd.text.toString(),
                                electric_pendency = electricPendency,
                                external_pendency = externalPendency,
                                internal_pendency = internalPendency,
                                mandatory_pendency = mandatoryPendency,
                                survey_place = radioSelectedPlace,
                                status = radioSelectedStatus,
                                data_insert = "",
                                date_now = "",
                                nLaudo = "",
                                latitude = latitude,
                                longitude = longitude
                            )
                            dialog.cancel()
                            uploadImage(survey)
                        }

                    }

                    // Set the cancel button click listener
                    mBuilder.setNegativeButton(getString(R.string.cancelar)) { dialog, _ ->
                        // Do something when click the neutral button
//                        Log.i("cancel:", "opa")
//                        edt_outro_local_add.setText("")
                        dialog.cancel()
                    }

                    val dialog = mBuilder.create()
                    dialog.show()
                    // initially disable the positive button
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
                    // dialog list item click listener
                    dialog.listView.onItemClickListener =
                        AdapterView.OnItemClickListener { _, _, position, _ ->
                            // enable positive button when user select an item
                            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                                .isEnabled = position != -1
                        }
                }
            }
        }
    }

    override fun onBackPressed() {
        val alertDialog: AlertDialog = activity.let {
            val builder = AlertDialog.Builder(it, R.style.AlertDialogTheme)
            builder.apply {
                setPositiveButton(getString(R.string.sim)
                ) { _, _ ->
                    finish()
                    // User clicked OK button
                }
                setNegativeButton(getString(R.string.nao)
                ) { dialog, _ ->
                    // User cancelled the dialog
                    dialog.cancel()
                }
            }
            // Set other dialog properties
            builder.setTitle(getString(R.string.atencao))
            builder.setMessage(getString(R.string.todos_dados_serao_perdidos))

            // Create the AlertDialog
            builder.create()
        }
        alertDialog.show()

    }

    private fun takePhotoFromCamera(code: Int) {
        if (checkPermissionCamera()) {
            when (code) {
                REQUEST_CODE_LICENSE -> {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    photoFile1 = getPhotoFile(FILE_NAME)
                    chassis_photo_uri = photoFile1.absolutePath.toString()
                    //Doesn't work for API >= 24
                    //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile1)

                    fileProvider =
                        FileProvider.getUriForFile(
                            this,
                            getString(R.string.file_provider),
                            photoFile1
                        )

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

                    if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                        startActivityForResult(takePictureIntent, code)
                    } else {
                        Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
                    }
                }
                REQUEST_CODE_ENGINE -> {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    photoFile2 = getPhotoFile(FILE_NAME)
                    engine_photo_uri = photoFile2.absolutePath.toString()

                    //Doesn't work for API >= 24
                    //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile1)

                    fileProvider =
                        FileProvider.getUriForFile(
                            this,
                            getString(R.string.file_provider),
                            photoFile2
                        )

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

                    if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                        startActivityForResult(takePictureIntent, code)
                    } else {
                        Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
                    }
                }
                REQUEST_CODE_BACK -> {
                    val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    photoFile3 = getPhotoFile(FILE_NAME)
                    back_photo_uri = photoFile3.absolutePath.toString()

                    //Doesn't work for API >= 24
                    //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile1)

                    fileProvider =
                        FileProvider.getUriForFile(
                            this,
                            getString(R.string.file_provider),
                            photoFile3
                        )

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

                    if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                        startActivityForResult(takePictureIntent, code)
                    } else {
                        Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        } else {
            checkPermissionCamera()
        }


    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_LICENSE -> {
                    val takenImage = BitmapFactory.decodeFile(photoFile1.absolutePath)

                    imageview!!.scaleType = ImageView.ScaleType.CENTER_CROP

                    val i = takenImage.rotate(90F) // value must be float
                    addSurveyViewModel.photoUrl.value = i
                }

                REQUEST_CODE_ENGINE -> {
                    val takenImage = BitmapFactory.decodeFile(photoFile2.absolutePath)

                    imageview!!.scaleType = ImageView.ScaleType.CENTER_CROP

                    val i = takenImage.rotate(90F) // value must be float
                    addSurveyViewModel.photoUrl.value = i
                }
                REQUEST_CODE_BACK -> {
                    val takenImage = BitmapFactory.decodeFile(photoFile3.absolutePath)

                    imageview!!.scaleType = ImageView.ScaleType.CENTER_CROP

                    val i = takenImage.rotate(90F) // value must be float
                    addSurveyViewModel.photoUrl.value = i

                }
                REQUEST_CODE_LOCATION -> {
                    PackageManager.PERMISSION_GRANTED
                }


            }

        }

    }

    private fun getBitmapFile(reduceBitmap: Bitmap): File {
        val file = getPhotoFile(FILE_NAME)
        val bos = ByteArrayOutputStream()
        reduceBitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos)

        val bitmapdata = bos.toByteArray()
        try {
            file.createNewFile()
            val fos = FileOutputStream(file)
            fos.write(bitmapdata)
            fos.flush()
            fos.close()
            return file
        } catch (err: Error) {
            err.message
        }
        return file

    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        Log.i("storageDirectory", storageDirectory.toString())
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    private fun uploadImage(survey: Survey) {

        val chassiFile = File(survey.chassis_photo!!)
        val engineFile = File(survey.engine_photo!!)
        val backFile = File(survey.back_photo!!)

        // create RequestBody instance from file
        val requestChassis =
            reduceResize(chassiFile).asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val requestEngine =
            reduceResize(engineFile).asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val requestBack =
            reduceResize(backFile).asRequestBody("multipart/form-data".toMediaTypeOrNull())

        val chassiPhoto =
            MultipartBody.Part.createFormData("chassis_photo", chassiFile.name, requestChassis)
        val enginePhoto =
            MultipartBody.Part.createFormData("engine_photo", engineFile.name, requestEngine)
        val backPhoto =
            MultipartBody.Part.createFormData("back_photo", backFile.name, requestBack)

        val licensePlate = survey.license_plate.toRequestBody("text/plain".toMediaTypeOrNull())
        val renavam = survey.renavam.toRequestBody("text/plain".toMediaTypeOrNull())
        val year = survey.year.toRequestBody("text/plain".toMediaTypeOrNull())
        val brand = survey.brand.toRequestBody("text/plain".toMediaTypeOrNull())
        val type = survey.type.toRequestBody("text/plain".toMediaTypeOrNull())
        val color = survey.color.toRequestBody("text/plain".toMediaTypeOrNull())
        val kind = survey.kind.toRequestBody("text/plain".toMediaTypeOrNull())
        val uf = survey.uf.toRequestBody("text/plain".toMediaTypeOrNull())
        val chassis = survey.chassis.toRequestBody("text/plain".toMediaTypeOrNull())
        val engine = survey.engine.toRequestBody("text/plain".toMediaTypeOrNull())
        val chassisObs = survey.chassis_obs!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val engineObs = survey.engine_obs!!.toRequestBody("text/plain".toMediaTypeOrNull())
        val backObs = survey.back_obs!!.toRequestBody("text/plain".toMediaTypeOrNull())

        val electricDependency =
            survey.electric_pendency?.toRequestBody("text/plain".toMediaTypeOrNull())
        val externalDependency =
            survey.internal_pendency?.toRequestBody("text/plain".toMediaTypeOrNull())
        val internalDependency =
            survey.external_pendency?.toRequestBody("text/plain".toMediaTypeOrNull())
        val mandatoryDependency =
            survey.mandatory_pendency?.toRequestBody("text/plain".toMediaTypeOrNull())
        val surveyPlace = survey.survey_place.toRequestBody("text/plain".toMediaTypeOrNull())
        val status = survey.status.toRequestBody("text/plain".toMediaTypeOrNull())
        val latitude = survey.latitude?.toRequestBody("text/plain".toMediaTypeOrNull())
        val longitude = survey.longitude?.toRequestBody("text/plain".toMediaTypeOrNull())

        val requestBodySurvey = RequestBodySurvey(
            licensePlate,
            renavam,
            year,
            brand,
            type,
            color,
            kind,
            uf,
            chassis,
            engine,
            chassiPhoto,
            chassisObs,
            enginePhoto,
            engineObs,
            backPhoto,
            backObs,
            electricDependency,
            externalDependency,
            internalDependency,
            mandatoryDependency,
            surveyPlace,
            status,
            latitude,
            longitude
        )
        addSurveyViewModel.insert(requestBodySurvey).observe(this, {

            Log.i(TAG, "it value: ${it.message}")


            when (it.status) {
                Status.SUCCESS -> {
                    progressDialog.dialog.dismiss()
                    // progressBar.visibility = View.GONE


                    it.data?.let { response ->
                        Log.i(TAG, "message-----${response.message}")
                        Log.i(TAG, "id----- ${response._id}")

                        Log.i(TAG, "Cheio")

                        Log.i(TAG, "Vistoria Cadastrada: $response")
                        // Toast.makeText(this, "Vistoria Salva com Sucesso!", Toast.LENGTH_LONG).show()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        deleteAllArchives(getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!)
                    }
                }

                Status.ERROR -> {
                    Log.i(TAG, "Houve um erro: ${it.message}")
                    //recyclerView.visibility = View.VISIBLE
                    //progressBar.visibility = View.GONE

                }
                Status.LOADING -> {
                    // Show progress dialog with Title
                    progressDialog.show(this, "Salvando...")
                    //  progressBar.visibility = View.VISIBLE
                }
            }
        })

    }

    //Check permisssions to camera
    private fun checkPermissionCamera(): Boolean {
        return if (checkSelfPermission(
                this@AddSurveyActivity,
                Manifest.permission.CAMERA
            )
            != PackageManager.PERMISSION_GRANTED
        ) { // Requesting the permission
            ActivityCompat.requestPermissions(
                this@AddSurveyActivity,
                arrayOf(Manifest.permission.CAMERA), 100
            )
            false
        } else {
            true
        }
    }

    //Check permisssions to camera
    private fun checkPermissionLocation(): Boolean {
        return if (checkSelfPermission(
                this@AddSurveyActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) { // Requesting the permission
            ActivityCompat.requestPermissions(
                this@AddSurveyActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 104
            )
            false
        } else {
            true
        }
    }

    // Create bitmap rotated
    private fun reduceResize(file: File): File {
        Log.i("file1: =======", file.name)
        //val fullSizeBitmap = BitmapFactory.decodeFile(photoFile1.absolutePath)
        val reduceBitmap =
            ImageResizer.reduceBitmapSize(BitmapFactory.decodeFile(file.absolutePath), 240000)
        val rotatedBitmap = reduceBitmap.rotate(90F) // value must be float
        return getBitmapFile(rotatedBitmap!!)
    }

    companion object {
        const val REQUEST_CODE_LICENSE = 101
        const val REQUEST_CODE_ENGINE = 102
        const val REQUEST_CODE_BACK = 103
        const val REQUEST_CODE_LOCATION = 104

    }
}

//Create a list of categories for expandedListView
private fun prepareItensToListExpanded() {
    listDataHeader = ArrayList()
    listDataChild = HashMap()
    // Adding child data
    (listDataHeader as ArrayList<FilterHeader>).add(FilterHeader("Parte Elétrica", "Selecione"))
    (listDataHeader as ArrayList<FilterHeader>).add(FilterHeader("Parte Externa", "Selecione"))
    (listDataHeader as ArrayList<FilterHeader>).add(FilterHeader("Parte Interna", "Selecione"))
    (listDataHeader as ArrayList<FilterHeader>).add(FilterHeader("Uso Obrigatório",
        "Selecione"))

// Adding child data
    val electricList: MutableList<String> = ArrayList()
    electricList.add("Farol")
    electricList.add("Pisca")
    electricList.add("Farolete")
    electricList.add("Luz de freio")
    electricList.add("Pisca alerta")
    electricList.add("Luz de placa")
    electricList.add("Luz do painel")
    electricList.add("Buzina")

    val externalList: MutableList<String> = ArrayList()
    externalList.add("Pneus")
    externalList.add("Para-choques")
    externalList.add("Lanternagem")
    externalList.add("Pintura Externa")
    externalList.add("Limpador de Para-brisa")
    externalList.add("Retrovisor")
    externalList.add("Alteração de característica")
    externalList.add("Escapamento")
    externalList.add("Suspensão")
    externalList.add("Placa")
    externalList.add("Lacre")
    externalList.add("Tarjeta")

    val internalList: MutableList<String> = ArrayList()
    internalList.add("Freio/estacionamento")
    internalList.add("Película")
    internalList.add("Quebra-sol")
    internalList.add("Bancos")
    internalList.add("Extintor")
    internalList.add("Cinto de segurança")
    internalList.add("Chassi")

    val mandatoryList: MutableList<String> = ArrayList()
    mandatoryList.add("Macaco")
    mandatoryList.add("Triângulo")
    mandatoryList.add("Chave de Rodas")
    mandatoryList.add("Estepe")
    mandatoryList.add("Outros")

    listDataChild[(listDataHeader as ArrayList<FilterHeader>)[0].title] =
        electricList // Header, Child data
    listDataChild[(listDataHeader as ArrayList<FilterHeader>)[1].title] = externalList
    listDataChild[(listDataHeader as ArrayList<FilterHeader>)[2].title] = internalList
    listDataChild[(listDataHeader as ArrayList<FilterHeader>)[3].title] = mandatoryList

    listAdapter = FIltersExpandableAdapter(
        activity,
        listDataHeader as ArrayList<FilterHeader>,
        listDataChild
    )
    binding.expandablelistviewFilter.setAdapter(listAdapter)
    Log.i("aaa", listAdapter.listOfStatusFilterElectric.toString())
    Log.i("bbb", listAdapter.childCheckboxElectricState.toString())
    Log.i("ccc", listAdapter.getValuesStatusEletric())
    Log.i("ddd", listAdapter.getValuesStatusExternal())
    Log.i("eee", listAdapter.getValuesStatusInternal())
    Log.i("fff", listAdapter.getValuesStatusMandatory())


}

//Delete directory and all archives from files/pictures
private fun deleteAllArchives(fileOrDirectory: File) {
    if (fileOrDirectory.isDirectory) for (child in fileOrDirectory.listFiles()!!) deleteAllArchives(
        child
    )
    fileOrDirectory.delete()
}

// Extension function to rotate bitmap
fun Bitmap.rotate(degrees: Float = 180F): Bitmap? {
    val matrix = Matrix()
    matrix.postRotate(degrees)

    return Bitmap.createBitmap(
        this, // source bitmap
        0, // x coordinate of the first pixel in source
        0, // y coordinate of the first pixel in source
        width, // The number of pixels in each row
        height, // The number of rows
        matrix, // Optional matrix to be applied to the pixels
        false // true if the source should be filtered
    )

}






