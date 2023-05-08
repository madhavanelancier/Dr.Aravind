package com.elancier.domdox.Common

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import com.elancier.meenatchi_CRM.retrofit.ScalingUtilities
import org.json.JSONObject
import org.json.JSONTokener
import java.io.*
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("NewApi")
object CommonFunctions {

    fun checkLogin(resp: String): Boolean {
        try {
            val json = JSONObject(resp)
            Log.i("resp", resp)
            if (json.getBoolean("status") == true) {
                return true
            } else if (json.getBoolean("status") == false) {
                if (json.has("data")) {
                    val jsonTok = JSONTokener(json.getString("data"))
                    val jObj = JSONObject(jsonTok)
                    return if (jObj.has("redirectToLogin") && jObj.getBoolean("redirectToLogin") == true) {
                        false
                    } else {
                        true
                    }
                } else {
                    return true
                }
            } else {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return true
        }

    }

    fun decodeFile(context: Context, path: String, DESIREDWIDTH: Int, DESIREDHEIGHT: Int): String? {
        var strMyImagePath: String? = null
        var scaledBitmap: Bitmap? = null

        try {

            val unscaledBitmap = ScalingUtilities.decodeFile(
                path,
                DESIREDWIDTH,
                DESIREDHEIGHT,
                ScalingUtilities.ScalingLogic.FIT
            )

            if (!(unscaledBitmap.width <= DESIREDWIDTH && unscaledBitmap.height <= DESIREDHEIGHT)) {
                scaledBitmap =
                    ScalingUtilities.createScaledBitmap(
                        unscaledBitmap,
                        DESIREDWIDTH,
                        DESIREDHEIGHT,
                        ScalingUtilities.ScalingLogic.FIT
                    )
            } else {
                scaledBitmap = unscaledBitmap
            }
            val file = File(path)
            val filename = file.name.replace(" ", "-")
            val contextWrapper = ContextWrapper(context)
            val directory = contextWrapper.filesDir
            val to = File(directory, filename)
            strMyImagePath = to.absolutePath
            try {
                val fos = FileOutputStream(to)
                scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos)//75
                fos.flush()
                fos.close()

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            scaledBitmap.recycle()
        } catch (e: Throwable) {

        }

        return strMyImagePath ?: path

    }

    fun createImageFile(): File? {
        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "Loan_" + timeStamp + "_"
            val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)

            return File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    fun createCacheImageFile(context: Context): File? {
        try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "Loan_" + timeStamp + "_"
            val storageDir = context.externalCacheDir
            //val storageDir = File(Environment.getDownloadCacheDirectory().absolutePath+"/gp")

            return File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir      /* directory */
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }

    }

    fun decodeFile1(path: String, DESIREDWIDTH: Int, DESIREDHEIGHT: Int): Bitmap? {
        var scaledBitmap: Bitmap? = null
        try {

            val unscaledBitmap = ScalingUtilities.decodeFile(
                path,
                DESIREDWIDTH,
                DESIREDHEIGHT,
                ScalingUtilities.ScalingLogic.FIT
            )
            if ((unscaledBitmap.width <= DESIREDWIDTH && unscaledBitmap.height <= DESIREDHEIGHT)) {
                scaledBitmap = unscaledBitmap
                println("if")
            } else {
                scaledBitmap =
                    ScalingUtilities.createScaledBitmap(
                        unscaledBitmap,
                        DESIREDWIDTH,
                        DESIREDHEIGHT,
                        ScalingUtilities.ScalingLogic.FIT
                    )
                println("else")
            }
            println("unscaledBitmapwidth : "+unscaledBitmap.width)
            println("unscaledBitmapheight : "+unscaledBitmap.height)
        } catch (e: Throwable) {

        }

        return scaledBitmap

    }

    fun decodeFile2(path: String, DESIREDWIDTH: Int, DESIREDHEIGHT: Int): Bitmap? {
        var scaledBitmap: Bitmap? = null
        try {

            val unscaledBitmap = ScalingUtilities.decodeFile(
                path,
                DESIREDWIDTH,
                DESIREDHEIGHT,
                ScalingUtilities.ScalingLogic.FIT
            )
            if (DESIREDHEIGHT > 0 && DESIREDHEIGHT > 0) {
                if (!(unscaledBitmap.width <= DESIREDWIDTH && unscaledBitmap
                        .height <= DESIREDHEIGHT)
                ) {
                    scaledBitmap =
                        ScalingUtilities.createScaledBitmap(
                            unscaledBitmap, DESIREDWIDTH, DESIREDHEIGHT,
                            ScalingUtilities.ScalingLogic.FIT
                        )
                } else {
                    scaledBitmap = unscaledBitmap
                }
            } else {
                scaledBitmap = unscaledBitmap
            }

        } catch (e: Throwable) {

        }

        return scaledBitmap

    }

   /* private fun getBitmap(path:String):Bitmap? {
        Log.e("inside of", "getBitmap = " + path)
        try
        {
            var b:Bitmap? = null
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true
            val matrix = Matrix()
            val exifReader = ExifInterface(path)
            val orientation = exifReader.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1)
            var rotate = 0f
            if (orientation == ExifInterface.ORIENTATION_NORMAL)
            {
                // Do nothing. The original image is fine.
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_90)
            {
                rotate = 90f
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_180)
            {
                rotate = 180f
            }
            else if (orientation == ExifInterface.ORIENTATION_ROTATE_270)
            {
                rotate = 270f
            }
            matrix.postRotate(rotate)
            try
            {
                b = loadBitmap(path, rotate, screenWidth, screenHeight)
                btn_RotateImg.setEnabled(true)
            }
            catch (e:OutOfMemoryError) {
                btn_RotateImg.setEnabled(false)
            }
            System.gc()
            return b
        }
        catch (e:Exception) {
            Log.e("my tag", e.message, e)
            return null
        }
    }*/

    fun loadBitmap(path:String, orientation:Float, targetWidth:Int, targetHeight:Int):Bitmap {
        var bitmap:Bitmap? = null
        try
        {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options)
            var sourceWidth:Int
            var sourceHeight:Int
            if (orientation == 90f || orientation == 270f)
            {
                sourceWidth = options.outHeight
                sourceHeight = options.outWidth
            }
            else
            {
                sourceWidth = options.outWidth
                sourceHeight = options.outHeight
            }
            if (sourceWidth > targetWidth || sourceHeight > targetHeight)
            {
                val widthRatio = sourceWidth.toFloat() / targetWidth.toFloat()
                val heightRatio = sourceHeight.toFloat() / targetHeight.toFloat()
                val maxRatio = Math.max(widthRatio, heightRatio)
                options.inJustDecodeBounds = false
                options.inSampleSize = maxRatio.toInt()
                bitmap = BitmapFactory.decodeFile(path, options)
            }
            else
            {
                bitmap = BitmapFactory.decodeFile(path)
            }
            if (orientation > 0)
            {
                val matrix = Matrix()
                matrix.postRotate(orientation)
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true)
            }
            sourceWidth = bitmap.getWidth()
            sourceHeight = bitmap.getHeight()
            if (sourceWidth != targetWidth || sourceHeight != targetHeight)
            {
                val widthRatio = sourceWidth.toFloat() / targetWidth.toFloat()
                val heightRatio = sourceHeight.toFloat() / targetHeight.toFloat()
                val maxRatio = Math.max(widthRatio, heightRatio)
                sourceWidth = (sourceWidth.toFloat() / maxRatio).toInt()
                sourceHeight = (sourceHeight.toFloat() / maxRatio).toInt()
                bitmap = Bitmap.createScaledBitmap(bitmap, sourceWidth, sourceHeight, true)
            }
        }
        catch (e:Exception) {}
        return bitmap!!
    }

    fun ImageToByte(imageView: ImageView): ByteArray{
        //val imageView = findViewById(R.id.imageView) as ImageView
        val bitmap = (imageView.getDrawable() as BitmapDrawable).getBitmap()
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return  baos.toByteArray()
    }

    fun getImgPath(context: Context,uri: Uri?): String? {
        val result: String?
        val cursor = uri?.let { context.getContentResolver().query(it, null, null, null, null) }
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = uri!!.path
        } else {
            cursor!!.moveToFirst()
            val idx = cursor!!.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor!!.getString(idx)
            cursor!!.close()
        }
        Log.i("utilsresult", result!! + "")
        return result

    }

    /*public static InputStream createfalseJson(){
		try{
		JSONObject json = new JSONObject();
		json.put("success", false);
		JSONObject lo = new JSONObject();
		lo.put("redirectToLogin", true);
		json.put("data", lo);
		return new ByteArrayInputStream(json.toString().getBytes("UTF-8"));
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}*/
    /*

	 public static ArrayList<DetailBo> getEmptyList(String typeid, int level, String name) {
		ArrayList<DetailBo> val = new ArrayList<DetailBo>();
	    DetailBo bo = new DetailBo();
		bo.setId(typeid);
		bo.setName(name);
		bo.setDesc("");
		bo.setOutletid("0");
		bo.setParentid(typeid);
		bo.setLevel(level);
		bo.setRank(0);
		bo.setMediaurl(null);
		bo.setCount(0);
		val.add(bo);
	    return val;
	  }
	 */
    /*public static String generateRandomEmail(){
	    	String orgKey = "";
	    	String keyInstance = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	    	Random numGen = new Random();
	    	StringBuilder keyCollector = new StringBuilder();

	    	for(int i=0;i<10;i++){
	    		int keySelector = numGen.nextInt(keyInstance.length());
	    		keyCollector.append(keyInstance.charAt(keySelector));
	    	}
	    	orgKey = new String(keyCollector);
	    	return orgKey+"@tomatotail.com";
	  }*/

    /*@Throws(IOException::class)
    fun sendJsonHttpPost1(url: String, nameValuePairs: String): StringBuffer? {
        var result: StringBuffer? = null
        try {
            var conn: HttpURLConnection? = null
            var inStream: DataInputStream? = null
            try {
                val servletURL = URL(url)
                conn = servletURL.openConnection() as HttpURLConnection
                conn.doInput = true
                conn.doOutput = true
                conn.useCaches = false
                conn.requestMethod = "POST"
                conn.setRequestProperty("Accept", "application/json")
                conn.setRequestProperty("Content-type", "application/json")
                val osw = OutputStreamWriter(conn.outputStream)
                osw.write(nameValuePairs)
                osw.flush()
                osw.close()
                inStream = DataInputStream(conn.inputStream)
                result = StringBuffer()
                var chr: Int
                while ((chr = inStream.read()) != -1) {
                    result.append(chr.toChar())
                }
            } catch (ioex: IOException) {
                throw ioex
            } catch (e: Exception) {
                throw e
            } finally {
                try {
                    inStream?.close()
                } catch (e: Exception) {
                }

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        return result
    }*/


}
