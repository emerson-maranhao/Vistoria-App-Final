package br.gov.am.detran.appvistoria.until

import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import br.gov.am.detran.appvistoria.R
import retrofit2.Response
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Utils {

    fun <T : Any> handleApiError(resp: Response<T>): AppResult.Error {
        val error = ApiErrorUtils.parseError(resp)
        return AppResult.Error(Exception(error.message))
    }

    fun <T : Any> handleSuccess(response: Response<T>): AppResult<T> {
        response.body()?.let {
            return AppResult.Success(it)
        } ?: return handleApiError(response)
    }



    fun showSoftKeyboard(context: Context?, view: View) {
        view.requestFocus()
        val manager = context?.getSystemService(Context.INPUT_METHOD_SERVICE)
        if (manager is InputMethodManager) manager.toggleSoftInputFromWindow(
            view.windowToken,
            InputMethodManager.RESULT_UNCHANGED_SHOWN, InputMethodManager.RESULT_UNCHANGED_HIDDEN
        )
    }

    fun hideSoftKeyboard(context: Context?, view: View) {
        val inputManager: InputMethodManager =
            context?.getSystemService(Context.INPUT_METHOD_SERVICE)
                    as InputMethodManager
        inputManager.hideSoftInputFromWindow(view.windowToken, InputMethodManager.SHOW_FORCED)
    }

    fun addHourInTime(inputDate: String?): String? {
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        val dat: Date? = sdf.parse(inputDate)
        val calendar = Calendar.getInstance()
        calendar.time = dat!!
        calendar.add(Calendar.HOUR, -4)
        return sdf.format(calendar.time)
    }


    fun formatDateFromDateString(insertedDate: String, now: String): String? {

        val addedTime = addHourInTime(insertedDate)
        val addedTime2 = addHourInTime(now)

        val inputDateFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        val parttner = "dd"

        val mInputDateFormat = SimpleDateFormat(inputDateFormat, Locale.getDefault())
        val sdf = SimpleDateFormat(parttner, Locale("pt", "BR"))

        val mParsedDateSurvey = mInputDateFormat.parse(addedTime!!)
        val mParsedDateCurrent = mInputDateFormat.parse(addedTime2!!)

//        if (mParsedDateCurrent.compareTo(mParsedDateSurvey) > 0) {
//            Log.i("app", "Date1 is after Date2");} else if (mParsedDateCurrent.compareTo(mParsedDateSurvey) < 0) {
//            Log.i("app", "Date1 is before Date2");} else if (mParsedDateCurrent.compareTo(mParsedDateSurvey) == 0) {
//            Log.i("app", "Date1 is equal to Date2");
//        }


        val day1 = sdf.format(mParsedDateSurvey!!)
        val day2 = sdf.format(mParsedDateCurrent!!)

        when (day2.toInt() - day1.toInt()) {
            0 -> {
                val outputDateFormat = "HH:mm"
                val mOutputDateFormat = SimpleDateFormat(outputDateFormat, Locale("pt", "BR"))
                return mOutputDateFormat.format(mParsedDateSurvey)
            }
            1 -> {
                return "Ontem"
            }
            else -> {
                val sdf: SimpleDateFormat = SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                    Locale.US
                )
                val dat: Date? = sdf.parse(insertedDate)
//                Log.i("dat", dat.toString())

                val ldt = convertISOTimeToDate(insertedDate)
                return ldt.toString()


            }
        }

    }

    private fun convertISOTimeToDate(isoTime: String): String? {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        var convertedDate: Date? = null
        var formattedDate: String? = null
        try {
            convertedDate = sdf.parse(isoTime)
            Log.i("Hora entrada", convertedDate.toString())
            val t = convertedDate.time.minus(4)

            formattedDate = SimpleDateFormat("dd/MM/yyyy").format(convertedDate)

        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return formattedDate
    }

    fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }


}