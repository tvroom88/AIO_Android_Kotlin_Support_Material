package w2022v9o12.simple.camerax_analysis.model

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

class DateUtil {
    fun stringToDate(dateStr: String?, dateFormat: DateFormat): Date? {
        var date: Date? = null
        try {
            date = dateFormat.parse(dateStr)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date
    }

    fun dateToString(date: Date?, dateFormat: DateFormat): String? {
        return dateFormat.format(date)
    }

    fun convertFromMrzDate(mrzDate: String?): String? {
        val date = stringToDate(mrzDate, SimpleDateFormat("yyMMdd"))
        return dateToString(date, SimpleDateFormat("dd.MM.yyyy"))
    }

}