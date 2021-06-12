package net.sinasoheili.best_sellers.viewModel

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import android.util.Log
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidmads.library.qrgenearator.QRGSaver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.util.CacheToPreference
import java.lang.Exception

class ShowQRcodeViewModel constructor(val context: Context): ViewModel() {

    val imageData: MutableLiveData<Bitmap> = MutableLiveData()
    val saveImageData: MutableLiveData<Boolean> = MutableLiveData()

    fun generateQRcode(dimen: Int) {
        val generator: QRGEncoder = QRGEncoder(fetchShopIdFromCache().toString() , null, QRGContents.Type.TEXT , dimen)
        imageData.value = generator.bitmap
    }

    fun saveImage(image: Bitmap) {
        try {
            val saver: QRGSaver = QRGSaver()
            saver.save(
                Environment.getExternalStorageDirectory().path,
                context.getString(R.string.app_name),
                image,
                QRGContents.ImageType.IMAGE_JPEG
            )
            saveImageData.value = true
        } catch (e: Exception) {
            saveImageData.value = false
        }
    }

    private fun fetchShopIdFromCache(): Int {
        return CacheToPreference.getShopId(context)
    }
}