package br.gov.am.detran.appvistoria.presentation.ui.detail

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import br.gov.am.detran.appvistoria.R
import br.gov.am.detran.appvistoria.databinding.FragmentImageDialogBinding
import br.gov.am.detran.appvistoria.domain.Survey
import br.gov.am.detran.appvistoria.network.OAuthInterceptor
import br.gov.am.detran.appvistoria.session.UserPreferences
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient


class ImageDialogFragment : DialogFragment(), View.OnClickListener {
    lateinit var survey: Survey
     var _binding: FragmentImageDialogBinding? = null
    private val binding get() = _binding!!

    var imgs = arrayOf<String>()

    companion object {
        const val KEY = "image_dialog"

        /*
         * Todas as constantes do projeto são importantes, mas as
         * seguintes facilitarão a leitura do código ao invés de
         * apenas utilizar "1" ou "-1".
         * */
        const val COUNT_PLUS = 1
        const val COUNT_LESS = -1
    }

    var imagePosition = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Log.i("cccc",survey.toString())

        /*
         * Código que deixa o DialogFragment em modo fullscreen.
         * */
        setStyle(
            STYLE_NORMAL,
            android.R.style.Theme_Black_NoTitleBar_Fullscreen

        )
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS,
            WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity?.window?.statusBarColor = Color.BLACK

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        /*
        * Até o sinal da invocação deste método ainda não podemos
        * acessar as Views do layout fragment_image_dialog com a
        * sintaxe permitida pelo plugin kotlin-android-extensions.
        * */
        _binding = FragmentImageDialogBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i("on", "onAttach")
    }

    override fun onDetach() {
        //activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        activity?.window?.statusBarColor = requireActivity().getColor(R.color.colorPrimaryDark)
        super.onDetach()
        Log.i("on", "onDetach")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.i("on", "onDestroy")
    }

    /*
     * Os acessos as Views estão no método onResume(), pois somente
     * depois de onCreateView() é que podemos utilizar a sintaxe
     * do plugin kotlin-android-extensions, sem necessidade de uso
     * do método findViewById().
     * */
    override fun onResume() {
        super.onResume()

        /*
         * Acessando dados enviados de SurveyAdapter depois do
         * acionamento de algum item em lista.
         * */
        // val parcelable = arguments!!.getParcelable<Survey>("survey")
        val parcelable = requireArguments().getParcelable<Survey>("survey")
        survey =
            (if (parcelable != null) parcelable else throw NullPointerException("Expression 'arguments!!.getParcelable( \"survey\")' must not be null"))
        Log.i("cccc", survey.toString())
        //imagePosition = arguments!!.getInt("position")
        imagePosition = requireArguments().getInt("position")

        binding.ivClose.setOnClickListener(this)
        binding.ivArrowLeft.setOnClickListener(this)
        binding.ivArrowRight.setOnClickListener(this)

        setImage(false)

        /*
         * Ampliando a capacidade de zoom máximo na imagem.
         * */
        // iv_image.maximumScale = 5.0F
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_arrow_left -> prevImage()
            R.id.iv_arrow_right -> nextImage()
            R.id.iv_close -> close()
        }
    }

    private fun prevImage() {
        setImage(true, COUNT_LESS)
    }

    private fun nextImage() {
        setImage(true, COUNT_PLUS)
    }

    private fun setImage(applyCount: Boolean, typeCount: Int = COUNT_PLUS) {

        /*
         * Verificação se deve ou não ser aplicado o contador a
         * propriedade imagePosition, pois logo na abertura do
         * DialogFragment não há necessidade de invocar o algoritmo
         * contador, posteriormente, na mudança de imagem em tela, o
         * contador deve ser invocado para o correto cálculo de qual
         * botão passador de imagem poderá ou não ficar em tela.
         * */
        if (applyCount) {
            imagePosition += when (typeCount) {
                COUNT_PLUS -> 1
                else -> -1
            }
        }
        imgs = arrayOf(
            survey.chassis_photo.toString(),
            survey.engine_photo.toString(),
            survey.back_photo.toString()
        )

        // Adicionar header na request
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(OAuthInterceptor("Bearer", UserPreferences.token))
            .build()

        // Inserir o OkHttpClient no picasso
        val picasso = Picasso.Builder(requireContext())
            .downloader(OkHttp3Downloader(okHttpClient))
            .build()

        picasso
            .load(imgs[imagePosition])
            //.load(imgs[imagePosition])
            .into(binding.ivImage)

        verifyButtons()
    }

    /*
     * Verificação se os botões passadores de imagem podem ou não
     * permanecer em tela de acordo com a posição atual da imagem
     * em exibição.
     * */
    private fun verifyButtons() {
       binding.ivArrowLeft.visibility =
            if (imagePosition == 0)
                View.GONE
            else
                View.VISIBLE

        binding.ivArrowRight.visibility =
            if (imagePosition == imgs.size - 1)
                View.GONE
            else
                View.VISIBLE
    }

    private fun close() {
        dismiss()
    }
}