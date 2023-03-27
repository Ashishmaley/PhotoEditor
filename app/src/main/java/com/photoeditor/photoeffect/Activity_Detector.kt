package com.photoeditor.photoeffect

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.TextureView
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.photoeditor.photoeffect.databinding.ActivityDetectorBinding

class Activity_Detector : AppCompatActivity() {
    lateinit var imageView: ImageView
    lateinit var bitmap: Bitmap
    lateinit var cameraDevice: CameraDevice
    lateinit var handler:Handler
    private lateinit var binding: ActivityDetectorBinding
    lateinit var cameraManager: CameraManager
    lateinit var textureView: TextureView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityDetectorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val handlerThread= HandlerThread("videoThread")
        handlerThread.start()
        handler=Handler(handlerThread.looper)

        imageView=findViewById(R.id.image_view)
        textureView=findViewById(R.id.texture_view)
        textureView.surfaceTextureListener=object :TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureAvailable(p0: SurfaceTexture?, p1: Int, p2: Int) {
                openCamera()
            }

            override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture?, p1: Int, p2: Int) {
            }

            override fun onSurfaceTextureDestroyed(p0: SurfaceTexture?): Boolean {
                return false
            }

            override fun onSurfaceTextureUpdated(p0: SurfaceTexture?) {
                bitmap=textureView.bitmap
            }
        }
        cameraManager=getSystemService(Context.CAMERA_SERVICE)as CameraManager
    }


    @SuppressLint("MissingPermission")
    private fun openCamera(){
        cameraManager.openCamera(cameraManager.cameraIdList[0],object : CameraDevice.StateCallback(){
                override fun onOpened(p0: CameraDevice) {
                    cameraDevice=p0

                    var surfaceTexture=textureView.surfaceTexture
                    var surface= Surface(surfaceTexture)

                    var captureRequest=cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
                    captureRequest.addTarget(surface)

                    cameraDevice.createCaptureSession(listOf(surface),object :CameraCaptureSession.StateCallback(){

                        override fun onConfigured(p0: CameraCaptureSession) {
                            p0.setRepeatingRequest(captureRequest.build(),null,null)
                        }

                        override fun onConfigureFailed(p0: CameraCaptureSession) {
                        }
                    },handler)
                }

                override fun onDisconnected(p0: CameraDevice) {
                    TODO("Not yet implemented")
                }

                override fun onError(p0: CameraDevice, p1: Int) {
                    TODO("Not yet implemented")
                }
            },handler)


        getPermission()


        }
     private fun getPermission(){
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(android.Manifest.permission.CAMERA),101)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0]!=PackageManager.PERMISSION_GRANTED) {
            getPermission()
        }
    }
}