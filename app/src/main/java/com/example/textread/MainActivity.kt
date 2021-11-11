package com.example.textread

import android.content.res.AssetManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.googlecode.tesseract.android.TessBaseAPI
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {

    lateinit var tess : TessBaseAPI
    var dataPath : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dataPath = filesDir.toString()+"/tessseract/"

        checkFile(File(dataPath+"tessdata/"),"kor")
        checkFile(File(dataPath+"tessdata/"),"eng")

        var lang : String="kor+eng"
        tess = TessBaseAPI()
        tess.init(dataPath,lang)

        processimage(BitmapFactory.decodeResource(resources,R.drawable.delete2))



    }
    fun copyFile(lang : String){
        try{
            var filePath : String = dataPath+"/tessdata/"+ lang+".traineddata"

            var assetManager : AssetManager = getAssets()

            var InputStream : InputStream = assetManager.open("tessdata/"+lang+".traineddata")
            var outStream : OutputStream = FileOutputStream(filePath)

            var buffer : ByteArray = ByteArray(1024)

            var read : Int=0
            read = InputStream.read(buffer)
            while(read!=-1){
                outStream.write(buffer,0,read)
                read = InputStream.read(buffer)
            }
            outStream.flush()
            outStream.close()
            InputStream.close()

        }
        catch(e:Exception){
            Log.d("Error Occured",e.toString())
        }
    }

    fun checkFile(dir : File, lang :String){
        if(!dir.exists()&&dir.mkdirs()){
            copyFile(lang)
        }

        if(dir.exists()){
            var datafilePath : String = dataPath+"/tessdata/"+lang+".traineddata"
            var dataFile : File = File(datafilePath)
            if(!dataFile.exists()){
                copyFile(lang)
            }

        }
    }

    fun processimage(bitmap : Bitmap){
        var text = findViewById(R.id.activitymain_tv_result) as TextView
        Toast.makeText(this,"Wait",Toast.LENGTH_SHORT).show()
        var ocrResult : String? =null
        tess.setImage(bitmap)
        ocrResult = tess.utF8Text
        text.text = ocrResult
    }
}