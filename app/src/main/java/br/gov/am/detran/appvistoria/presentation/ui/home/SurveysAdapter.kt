package br.gov.am.detran.appvistoria.presentation.ui.home

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import br.gov.am.detran.appvistoria.R
import br.gov.am.detran.appvistoria.databinding.ItemListSurveyBinding
import br.gov.am.detran.appvistoria.domain.Survey
import br.gov.am.detran.appvistoria.until.Utils

private var _binding: ItemListSurveyBinding?=null
private val binding get() = _binding!!
class SurveysAdapter(
    private val surveys: List<Survey>,
    val onItemClickListener: ((survey: Survey) -> Unit),// criaçao de lambda
) : RecyclerView.Adapter<SurveysAdapter.SurveyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SurveyViewHolder {

         _binding = ItemListSurveyBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return SurveyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return surveys.count()
    }

    override fun onBindViewHolder(holder: SurveyViewHolder, position: Int) {
        holder.bindView(surveys[position])
    }

    inner class SurveyViewHolder(
        itemView: ItemListSurveyBinding,
    ) : RecyclerView.ViewHolder(itemView.root) {
        private var license = itemView.tvLicensePlate
        private var brand = itemView.tvBrand
        private var status = itemView.btnStatus
        private var data = itemView.tvDataInsert

        fun bindView(survey: Survey) {
            //date now
            val now = survey.date_now

            with(itemView) {
                license.text = survey.license_plate
                brand.text = survey.brand

                when (survey.status) {
                    "Aprovada" -> binding.btnStatus.background.tint(context, R.color.aprovada)
                    "Pendente" -> binding.btnStatus.background.tint(context, R.color.pendente)
                    "Reprovada" -> binding.btnStatus.background.tint(context, R.color.reprovada)
                }

                val insertedDate = survey.data_insert
                data.text = Utils().formatDateFromDateString(insertedDate, now)
                setOnClickListener {
                    onItemClickListener.invoke(survey)

                }

            }

        }
    }

    fun Drawable.tint(context: Context, @ColorRes color: Int) {
        DrawableCompat.setTint(this, context.resources.getColor(color, context.theme))
    }

}