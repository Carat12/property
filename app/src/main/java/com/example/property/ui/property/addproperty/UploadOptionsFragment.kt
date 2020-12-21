package com.example.property.ui.property.addproperty

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.property.R
import com.example.property.ui.property.PropertyViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.bottom_sheet_upload_image.view.*

class UploadOptionsFragment(var prefix: String) : BottomSheetDialogFragment(), View.OnClickListener {

    private lateinit var fragmentView: View
    private lateinit var viewModel: PropertyViewModel
    private lateinit var listener: OnImageAddedListener
    private val IMAGE_CAPTURE = 111
    private val PICK_FROM_GALLERY = 222
    private val OPEN_SETTINGS = 333

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.bottom_sheet_upload_image, container, false)

        init()
        return fragmentView
    }

    private fun init() {
        viewModel = ViewModelProvider(activity!!).get(PropertyViewModel::class.java)

        fragmentView.btn_camera.setOnClickListener(this)
        fragmentView.btn_gallery.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        Dexter.withContext(activity)
            .withPermissions(
                arrayListOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        when (v) {
                            fragmentView.btn_gallery -> openGallery()
                            fragmentView.btn_camera -> openCamera()
                        }
                    }
                    if (report.isAnyPermissionPermanentlyDenied) {
                        showDialog()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    p1!!.continuePermissionRequest()
                }
            })
            .check()
    }

    private fun showDialog() {
        AlertDialog.Builder(activity)
            .setTitle("Permissions Permanently Denied")
            .setMessage("If you want to upload images, please go to Settings to grant all required permissions")
            .setPositiveButton("Go to Settings", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    openSettings()
                }
            })
            .setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    dialog!!.dismiss()
                }
            })
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", activity!!.packageName, null)
        intent.setData(uri)
        startActivity(intent)//, OPEN_SETTINGS)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, IMAGE_CAPTURE)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_FROM_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            PICK_FROM_GALLERY -> {
                if (resultCode == Activity.RESULT_OK) {
                    val uri = data!!.data
                    val path = getPathFromUri(uri!!)
                    listener.onImageAddedFromGallery(uri, path)

                    /*viewModel.setImagePath(path)*/
                    Log.d("jun", "in bottom: get result from gallery")
                }
            }
            IMAGE_CAPTURE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val img = data?.extras?.get("data") as? Bitmap
                    val uri = saveToGallery(img)
                    val path = getPathFromUri(uri)
                    listener.onImageAddedFromCamera(img, uri, path)

                    /* viewModel.setImagePath(getPathFromUri(uri))*/
                    Log.d("jun", "in bottom: get result from camera")
                }
            }
        }
        this.dismiss()
    }

    private fun saveToGallery(img: Bitmap?): Uri? {
        var uri: Uri? = null
        if (img != null) {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, prefix + System.currentTimeMillis())
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)

            uri = activity!!.contentResolver.insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
            if (uri != null) {
                val outputStreem = activity!!.contentResolver.openOutputStream(uri)
                try {
                    img.compress(Bitmap.CompressFormat.JPEG, 100, outputStreem)
                } catch (e: Exception) {
                    Log.d("jun", e.message.toString())
                } finally {
                    outputStreem?.close()
                }
            }
        }
        return uri
    }

    fun getPathFromUri(uri: Uri?): String {
        if (uri == null)
            return ""
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = activity!!.contentResolver.query(uri, proj, null, null, null)
            val column_index = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            cursor.getString(column_index)
        } catch (e: Exception) {
            ""
        } finally {
            cursor?.close()
        }
    }

    interface OnImageAddedListener {
        fun onImageAddedFromCamera(img: Bitmap?, uri: Uri?, path: String)
        fun onImageAddedFromGallery(uri: Uri?, path: String)
    }

    fun setImageAddedListener(listener: OnImageAddedListener) {
        this.listener = listener
    }
}