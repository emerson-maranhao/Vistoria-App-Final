package br.gov.am.detran.appvistoria.presentation.ui.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import br.gov.am.detran.appvistoria.R
import br.gov.am.detran.appvistoria.databinding.ActivitySearchDataBinding
import br.gov.am.detran.appvistoria.domain.VehicleRequest
import br.gov.am.detran.appvistoria.presentation.ui.add.AddSurveyActivity
import br.gov.am.detran.appvistoria.presentation.ui.home.Status
import br.gov.am.detran.appvistoria.until.Utils
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchDataActivity : AppCompatActivity() {
    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySearchDataBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Set the toolbar as support action bar
        setSupportActionBar(binding.toolbar)
        //Set name in toolbar
        supportActionBar?.apply {
            title = "Consulta"

            // show back button on toolbar
            setDisplayHomeAsUpEnabled(true)

            // on back button press, it will navigate to parent activity
            setDisplayShowHomeEnabled(true)

        }

        binding.btnSearch.setOnClickListener {
            Utils().hideSoftKeyboard(this@SearchDataActivity, binding.edtLicensePlateSearch)
            Utils().hideSoftKeyboard(this@SearchDataActivity, binding.edtChassisSearch)

            var placa = binding.edtLicensePlateSearch.text?.toString()
            val chassi = binding.edtChassisSearch.text?.toString()
            //            val chassi = edt_chassis_search.text.toString()
            // placa = "JXS0855"

            if (placa!!.trim().isEmpty() && chassi!!.trim().isEmpty()) {
                Toast.makeText(this, "Placa e chassi vazios!", Toast.LENGTH_LONG).show()
            }
            if (placa.isNotEmpty() && chassi!!.isNotEmpty()) {
                Toast.makeText(this, "Digite somente a placa ou chassi", Toast.LENGTH_LONG).show()
            } else {
                if (placa.trim().length < 7 && chassi!!.isEmpty()) {
                    Toast.makeText(this, "Placa inválida", Toast.LENGTH_LONG).show()
                } else {

                    val vehicle = VehicleRequest(placa.toUpperCase(), chassi, "S")
                    Log.i("Vehicle2: ", "" + vehicle.toString())

                    searchViewModel.getVehicle(vehicle).observe(this, Observer {

                        Log.i("it", it.toString())
                        it?.let { resource ->
                            Log.i("status-----", resource.status.toString())
                            Log.i("data-----", resource.data.toString())

                            when (resource.status) {
                                Status.SUCCESS -> {
                                    binding.progressBar.visibility = View.GONE
                                    Log.i("data-------", resource.data.toString())

                                    resource.data?.let { vehicle ->

                                        if (vehicle.situacao.contains("ROUBADO")) {

                                            // Use the Builder class for convenient dialog construction
                                            val builder = AlertDialog.Builder(this,R.style.AlertDialogTheme)
                                            builder.setTitle("Atenção!")
                                            builder.setMessage("Veículo com restrição de roubo. Deseja continuar?")
                                            builder.setPositiveButton("Sim") { dialog, id ->
                                                val intent =
                                                    Intent(this, AddSurveyActivity::class.java)
                                                intent.putExtra("request", resource.data)
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                                startActivity(intent)
                                            }
                                            builder.setNegativeButton("Não") { dialog, id ->
                                                dialog.cancel()
                                            }
                                            // Create the AlertDialog object and return it
                                            builder.create().show()

                                        } else {
                                            Log.i("data-send", resource.data.toString())

                                            val intent =
                                                Intent(this, AddSurveyActivity::class.java)
                                            intent.putExtra("request", resource.data)
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                            startActivity(intent)
                                        }
                                    }
                                }

                                Status.ERROR -> {
                                    //recyclerView.visibility = View.VISIBLE
                                    binding.progressBar.visibility = View.GONE
                                    // if (resource.data ==)
//                                    if (resource.message!!.contains("404")) {
//                                        val builder = AlertDialog.Builder(this)
//                                        builder.setTitle("Atenção!")
//                                        builder.setMessage("Veiculo não encontrado!")
//                                        builder.setPositiveButton("Ok") { dialog, i ->
//                                            dialog.cancel()
//                                        }
//                                        builder.create().show()
//                                    }
                                    if (resource.message!!.contains("512")) {
                                        val builder = AlertDialog.Builder(this)
                                        builder.setTitle("Atenção!")
                                        builder.setMessage("Sem conexão com a Prodam!")
                                        builder.setPositiveButton("Ok") { dialog, i ->
                                            dialog.cancel()
                                        }
                                        builder.create().show()
                                    }
                                    if (resource.message!!.contains("513")) {
                                        val builder = AlertDialog.Builder(this)
                                        builder.setTitle("Atenção!")
                                        builder.setMessage("Veículo não encontrado!")
                                        builder.setPositiveButton("Ok") { dialog, i ->
                                            dialog.cancel()
                                        }
                                        builder.create().show()
                                    }

                                    else {
//                                                return@let
                                        //Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                                        val builder = AlertDialog.Builder(this,R.style.AlertDialogTheme)
                                        builder.setTitle("Atenção!")
                                        builder.setMessage("Não foi possível conectar ao servidor... Por favor, tente mais tarde!")
                                        builder.setPositiveButton("Ok") { dialog, i ->
                                            dialog.cancel()
                                        }

                                        val mDialog = builder.create()
                                        mDialog.show()



                                    }
                                }
                                Status.LOADING -> {
                                    binding.progressBar.visibility = View.VISIBLE
                                    // recyclerView.visibility = View.GONE
                                }
                            }
                        }
                    })
                }
                //}

            }
        }
    }


}