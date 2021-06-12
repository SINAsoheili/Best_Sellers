package net.sinasoheili.best_sellers.view

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Bundle
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.sinasoheili.best_sellers.R
import net.sinasoheili.best_sellers.viewModel.ShowQRcodeViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ShowQRcodeFragment : Fragment(R.layout.fragment_show_qr_code), View.OnClickListener {

    @Inject
    lateinit var viewModel: ShowQRcodeViewModel

    private lateinit var ivQRcode: ImageView
    private lateinit var btnSaveImage: Button
    private lateinit var progressBar: ProgressBar

    private var bitmap: Bitmap? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObj(view)
        setObserver()
        viewModel.generateQRcode(getDimen())
    }

    private fun initObj(view: View) {
        ivQRcode = view.findViewById(R.id.iv_showQrCode)

        btnSaveImage = view.findViewById(R.id.btn_showQrCode_saveImage)
        btnSaveImage.setOnClickListener(this)

        progressBar = view.findViewById(R.id.pb_showQrCode)
    }

    private fun setObserver() {
        viewModel.imageData.observe(viewLifecycleOwner , Observer {
            bitmap = it
            ivQRcode.setImageBitmap(it)
        })

        viewModel.saveImageData.observe(viewLifecycleOwner , Observer {
            inVisibleProgressBar()
            if(it) {
                closeFragment()
                showMessage(getString(R.string.image_saved_successfully))
            } else {
                showMessage(getString(R.string.image_dose_not_saved_successfully))
            }
        })
    }

    private fun getDimen() : Int {
        val windowManager: WindowManager= requireContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display: Display = windowManager.defaultDisplay
        val point: Point = Point()
        display.getRealSize(point)
        val width: Int = point.x
        val height: Int = point.y
        var dimen: Int = if(width<height) width else height
        dimen = dimen * 3 / 4;
        return dimen
    }

    override fun onClick(v: View?) {
        when(v) {
            btnSaveImage -> {
                if(bitmap != null) {
                    if(requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)  {
                        getExternalStoragePermission()
                    } else {
                        viewModel.saveImage(bitmap!!)
                        visibleProgressBar()
                    }
                }
            }
        }
    }

    private fun visibleProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun inVisibleProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun closeFragment() {
        parentFragmentManager.beginTransaction().remove(this).commit()
        parentFragmentManager.popBackStack()
    }

    private fun showMessage(text: String) {
        Snackbar.make(progressBar , text, Snackbar.LENGTH_LONG)
            .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            .show()
    }

    private fun getExternalStoragePermission() {
        if(requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED) {
            requireActivity().requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE) , 100)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        if((requestCode == 100) && (grantResults[0]==PackageManager.PERMISSION_GRANTED)) {
            if(bitmap != null)
                viewModel.saveImage(bitmap!!)
        }
    }
}