package com.elancier.meenatchi_CRM.Harvest


import android.app.DatePickerDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView

import com.elancier.meenatchi_CRM.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

import java.io.IOException
import java.util.*


/**
 * Created by anupamchugh on 09/02/16.
 */
class Fish_adap(
    private val dataSet2: ArrayList<String>,
    private val dataSet: ArrayList<String>,
    private val dataSet1: ArrayList<String>,


    internal var resource: Int,
    internal var mContext: Context
) : ArrayAdapter<String>(mContext, R.layout.incen_items, dataSet), View.OnClickListener {


    private var audioPlayer: MediaPlayer? = null

    internal var totalTime = 0

    private val audioFileUri: Uri? = null


    private val audioIsPlaying = false
    private val audioProgressHandler: Handler? = null

    private val updateAudioPalyerProgressThread: Thread? = null

    internal var audiourl = ""

    internal var audioline = ""
    internal var myDialog = Dialog(context)
    internal var inflater: LayoutInflater
    internal var seekBar: SeekBar? = null
    internal var wasPlaying = false
    internal var fab: FloatingActionButton? = null
    internal lateinit var picker: DatePickerDialog

    //


    private val lastPosition = -1


    // View lookup cache
    private class ViewHolder {
        internal var tips: TextView? = null
        internal var trainer: EditText? = null
        internal var title: EditText? = null

        internal var datetime: TextView? = null
        internal var textView50: TextView? = null
        internal var close: ImageButton? = null
        internal var download: ImageButton? = null
        internal var pause: ImageButton? = null


        internal var likes: TextView? = null
        internal var views: TextView? = null
        internal var notice_layout: LinearLayout? = null
        internal var tips_like: ImageView? = null
        internal var tips_view: TextView? = null


    }


    init {


        this.inflater = LayoutInflater.from(mContext)


    }


    override fun onClick(v: View) {


        val position = v.tag as Int





        when (v.id) {

        }//
        //            case R.id.feedlayout:
        //
        //                Intent ints=new Intent(getContext(),ParentsFeedbackActivity.class);
        //                ints.putExtra("pro_names",dataModel.getParentname());
        //                ints.putExtra("rating",dataModel.getRating());
        //                ints.putExtra("datetime",dataModel.getDatetime());
        //                ints.putExtra("desc",dataModel.getDesc());
        //                ints.putExtra("feed",String.valueOf(dataModel.getIds()));
        //                mContext.startActivity(ints);
        //
        //                break;


    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        // Get the data item for this position
        val dataModel = getItem(position)
        // Check if an existing view is being reused, otherwise inflate the view
        val holder = ViewHolder()
        // view lookup cache stored in tag

        val result: View

        if (convertView == null) {
            convertView = inflater.inflate(resource, null)
            convertView!!.tag = holder
        }




        holder.tips = convertView.findViewById<View>(R.id.centres) as TextView
        holder.trainer = convertView.findViewById<View>(R.id.pplp1) as EditText
        holder.datetime = convertView.findViewById<View>(R.id.centresseed) as TextView
        holder.close = convertView.findViewById<View>(R.id.close) as ImageButton



        try {
            holder.tips!!.text = dataSet[position]
            holder.trainer!!.setText(dataSet1[position])
            holder.datetime!!.setText(dataSet2[position])

        } catch (e: Exception) {

        }


        holder.datetime!!.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)
            // date picker dialog
            picker = DatePickerDialog(mContext,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    holder.datetime!!.text =
                        dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                    dataSet[position] = holder.tips!!.text.toString().replace("", "")
                    dataSet1[position] = holder.trainer!!.text.toString().replace("", "")
                    dataSet2[position] = holder.datetime!!.text.toString().replace("", "")

                    // dataSet2[position] = holder.title!!.selectedItem.toString().replace("", "")
                    //dataSet3[position] = holder.qty!!.text.toString().replace("", "")
                    (mContext as Harvest_Main).getnmsfish(dataSet2,dataSet, dataSet1!!, position)

                }, year, month, day
            )
            picker.show()
        }


        holder.tips!!.setOnClickListener {
            val cldr = Calendar.getInstance()
            val day = cldr.get(Calendar.DAY_OF_MONTH)
            val month = cldr.get(Calendar.MONTH)
            val year = cldr.get(Calendar.YEAR)
            // date picker dialog
            picker = DatePickerDialog(mContext,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    holder.tips!!.text =
                        dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year
                    dataSet[position] = holder.tips!!.text.toString().replace("", "")
                    dataSet1[position] = holder.trainer!!.text.toString().replace("", "")
                    dataSet2[position] = holder.datetime!!.text.toString().replace("", "")
                   // dataSet2[position] = holder.title!!.selectedItem.toString().replace("", "")
                    //dataSet3[position] = holder.qty!!.text.toString().replace("", "")
                    (mContext as Harvest_Main).getnmsfish(dataSet2,dataSet, dataSet1!!, position)

                }, year, month, day
            )
            picker.show()
        }


        holder.close!!.setOnClickListener {
            (context as Harvest_Main).getnmsfishremove(position)
        }

        holder.trainer!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable:Editable) {

                try {
                    if (!holder.trainer!!.text.toString().isEmpty()) {

                        dataSet[position] = holder.tips!!.text.toString().replace("%", "")
                        dataSet1[position] = holder.trainer!!.text.toString().replace("₹", "")
                        dataSet2[position] = holder.datetime!!.text.toString().replace("", "")

                        Log.e("Values In Arraylist", dataSet1.toString())
                        (mContext as Harvest_Main).getnmsfish(dataSet2,dataSet, dataSet1!!, position)

                    }

                } catch (e: Exception) {

                }

            }

        })


        val extStore = Environment.getExternalStorageDirectory()


        //File file = new File(extStore.getAbsoluteFile() + "/MyAudioFolder"+"/"+dataSet5.get(position)+".mp3");
        /*if(file.exists()&&audioline.isEmpty()){
            holder.call.setVisibility(View.VISIBLE);

            holder.download.setVisibility(View.GONE);
        }
        else{
            if(audioline.isEmpty()) {
                holder.call.setVisibility(View.GONE);

                holder.download.setVisibility(View.VISIBLE);
            }
        }*/


        Log.e("audiourl", audiourl)


        /*holder.download.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                    dialog = new ProgressDialog(mContext);
                                                   dialog.setMessage("Downloading.....");
                                                   dialog.show();
                                                   audiourl=dataSet4.get(position);
                                                   String fnam=dataSet5.get(position);

                                                   Log.e("audiourlval",audiourl);
                                                   String SDCardRoot = Environment.getExternalStorageDirectory()
                                                           .toString();
                                                   downloadFile(audiourl, fnam+".mp3",
                                                           SDCardRoot+"/MyAudioFolder",holder,mContext);
                                                   //new DownloadFileFromURL().execute(audiourl);


                                               }
                                               });


                holder.call.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {

                        audioline="2";

                        //holder.prog.setVisibility(View.VISIBLE);

                     *//*   holder.call.setVisibility(View.GONE);

                        holder.pause.setVisibility(View.VISIBLE);*//*

                        //s//topButton.setEnabled(true);
try {
    String path = Environment.getExternalStorageDirectory() + "/MyAudioFolder" + "/" + dataSet5.get(position) + ".mp3";
    MediaPlayer player = new MediaPlayer();
    audioPlayer = player;

    if (!(path.isEmpty()) && (!(path.equals("null")))) {
        audioPlayer.stop();

        //initAudioPlayer();


        audioIsPlaying = true;
        try {
            totalTime = audioPlayer.getDuration();
            //holder.textView50.setText(String.valueOf(totalTime));
        } catch (Exception e) {
            //holder.textView50.setText("0.00");

        }
        ((Details_calls) mContext).playsong(path);

                            *//*Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    // Execute a comethode in the intervel 3sec

                                    seekBar.setProgress((int) audioPlayer.getCurrentPosition());

                                }
                            },1000);*//*
        // Display progressbar.
        //itemViewHolder.playAudioProgress.setVisibility(ProgressBar.VISIBLE);

                           *//* try {
                                player.setDataSource(path);
                                player.prepare();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            } catch (Exception e) {
                                System.out.println("Exception of type : " + e.toString());
                                e.printStackTrace();
                            }

                            player.start();*//*

        //holder.prog.setVisibility(View.GONE);

    } else {
        //holder.prog.setVisibility(View.GONE);

        holder.call.setVisibility(View.VISIBLE);

        holder.pause.setVisibility(View.GONE);
        Toast.makeText(mContext, "Please specify an audio file to play.", Toast.LENGTH_LONG).show();
    }
}
catch (Exception e){

}
                    }
                });

                try {
                    audioPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            holder.pause.setVisibility(View.GONE);
                            holder.call.setVisibility(View.VISIBLE); // finish current activity
                        }
                    });
                }
                catch (Exception e){

                }
       *//* audioProgressHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == UPDATE_AUDIO_PROGRESS_BAR) {

                    if(audioPlayer!=null) {
                        // Get current play time.
                        int currPlayPosition = audioPlayer.getCurrentPosition();

                        // Get total play time.
                        int totalTime = audioPlayer.getDuration();

                        // Calculate the percentage.
                        int currProgress = (currPlayPosition * 1000) / totalTime;

                        // Update progressbar.
                        //holder.playAudioProgress.setProgress(currProgress);
                    }
                }
            }
        };*//*


        holder.pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(audioIsPlaying)
                {
                    audioPlayer.pause();
                    holder.call.setVisibility(View.VISIBLE);

                    holder.pause.setVisibility(View.GONE);


                    audioIsPlaying = false;

                    updateAudioPalyerProgressThread = null;
                }
            }
        });
*/


        //viewHolder.tips_like.setImageResource(R.drawable.likedis);


        //            Call<Get_Noticelike> call = userService.getnoticelike(sender,trainer,ids);
        //            call.enqueue(new Callback<Get_Noticelike>() {
        //                @Override
        //                public void onResponse(Call<Get_Noticelike> call, Response<Get_Noticelike> response) {
        //                    if(response.isSuccessful())
        //                    {
        //                        Get_Noticelike noticelike=response.body();
        //                        if(noticelike.getStatus().equals("Success"))
        //                        {
        //
        //                            viewHolder.tips_like.setImageResource(R.drawable.likeful);
        //                            viewHolder.tips_like.setTag("2");
        //                            viewHolder.likes.setText(String.valueOf(noticelike.getLikes()));
        //                            Log.e("img","1");
        //
        //                        }
        //                        else if(noticelike.getStatus().equals("Failure"))
        //                        {
        //                            viewHolder.tips_like.setImageResource(R.drawable.likedis);
        //                            viewHolder.tips_like.setTag("1");
        //                           viewHolder.likes.setText(dataModel.getLikes());
        //                            Log.e("img","2");
        //                        }
        //                        else
        //                        {
        //                            Toast.makeText(getContext(),noticelike.getMessage(),Toast.LENGTH_SHORT).show();
        //
        //                        }
        //                    }
        //                }
        //
        //                @Override
        //                public void onFailure(Call<Get_Noticelike> call, Throwable throwable) {
        //
        //                }
        //            });  // viewHolder.tips_like.setTag("2");

        //            viewHolder.tips_like.setOnClickListener(new View.OnClickListener() {
        //                @Override
        //                public void onClick(View v) {
        //                    String sender=dataModel.getSender();
        //                    String trainer=dataModel.getTrainer_id();
        //                    String ids=dataModel.getIds();
        //
        //
        //
        //                    if(viewHolder.tips_like.getTag().equals("1"))
        //                    {
        //
        //                        Call<Get_Noticelike> call = userService.getnoticelike(sender,trainer,ids);
        //                        call.enqueue(new Callback<Get_Noticelike>() {
        //                            @Override
        //                            public void onResponse(Call<Get_Noticelike> call, Response<Get_Noticelike> response) {
        //                                if(response.isSuccessful())
        //                                {
        //                                    Get_Noticelike noticelike=response.body();
        //                                    if(noticelike.getStatus().equals("Success"))
        //                                    {
        //                                        Toast.makeText(getContext(),noticelike.getMessage(),Toast.LENGTH_SHORT).show();
        //                                        viewHolder.tips_like.setImageResource(R.drawable.likeful);
        //                                        viewHolder.tips_like.setTag("2");
        //                                        viewHolder.likes.setText(String.valueOf(noticelike.getLikes()));
        //
        //                                    }
        //                                    else if(noticelike.getStatus().equals("Failure"))
        //                                    {
        //                                        viewHolder.tips_like.setImageResource(R.drawable.likedis);
        //                                        viewHolder.tips_like.setTag("1");
        //                                    }
        //                                    else
        //                                    {
        //                                        Toast.makeText(getContext(),noticelike.getMessage(),Toast.LENGTH_SHORT).show();
        //
        //                                    }
        //                                }
        //                            }
        //
        //                            @Override
        //                            public void onFailure(Call<Get_Noticelike> call, Throwable throwable) {
        //
        //                            }
        //                        });
        //                    }
        //                    else
        //                    {
        //
        //                        Call<Get_NoticeDislike> call = userService.getnoticedislike(sender,trainer,ids);
        //                        call.enqueue(new Callback<Get_NoticeDislike>() {
        //                            @Override
        //                            public void onResponse(Call<Get_NoticeDislike> call, Response<Get_NoticeDislike> response) {
        //                                if(response.isSuccessful())
        //                                {
        //                                    Get_NoticeDislike dislike=response.body();
        //                                    if(dislike.getStatus().equals("Success"))
        //                                    {
        //                                        Toast.makeText(getContext(),dislike.getMessage(),Toast.LENGTH_SHORT).show();
        //
        //                                        viewHolder.likes.setText(String.valueOf(dislike.getLikes()));
        //                                        viewHolder.tips_like.setImageResource(R.drawable.likedis);
        //                                        viewHolder.tips_like.setTag("1");
        //
        //                                    }
        //                                    else if(dislike.getStatus().equals("Failure"))
        //                                    {
        //                                        viewHolder.tips_like.setImageResource(R.drawable.likedis);
        //                                        viewHolder.tips_like.setTag("1");
        //                                    }
        //                                    else
        //                                    {
        //                                        Toast.makeText(getContext(),dislike.getMessage(),Toast.LENGTH_SHORT).show();
        //
        //                                    }
        //                                }
        //                            }
        //
        //                            @Override
        //                            public void onFailure(Call<Get_NoticeDislike> call, Throwable throwable) {
        //                                viewHolder.tips_like.setImageResource(R.drawable.likeful);
        //                                viewHolder.tips_like.setTag("2");
        //                            }
        //                        });
        //
        //                    }
        //
        //
        //
        //                }
        //            });


        //        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        //        result.startAnimation(animation);
        //        lastPosition = position;


        //        if(dataModel.getSender().equals("parent"))
        //        {
        //            viewHolder.trainer.setText(dataModel.getStudentname());
        //        }
        //        else  if(dataModel.getSender().equals("trainer"))
        //        { viewHolder.trainer.setText(dataModel.getTrainer());
        //
        //        }
        //        else if(dataModel.getSender().equals("principal"))
        //        {
        //            viewHolder.trainer.setText(dataModel.getPrincipal());
        //
        //        }


        // Return the completed view to render on screen
        return convertView
    }

    private fun addnoticedislike(sender: String, trainer: String, ids: String) {

    }

    private fun addnoticelike(sender: String, trainer: String, ids: String) {

    }

    private fun addnoticeviews(sender: String, trainer: String, ids: String) {

    }

    private fun initAudioPlayer() {
        try {
            if (audioPlayer == null) {
                audioPlayer = MediaPlayer()

                val audioFilePath = audiourl

                Log.d(TAG_PLAY_AUDIO, audioFilePath)

                if (audioFilePath.toLowerCase().startsWith("http://")) {
                    // Web audio from a url is stream music.
                    audioPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
                    // Play audio from a url.
                    audioPlayer!!.setDataSource(audioFilePath)
                    audioPlayer!!.prepare()
                    totalTime = audioPlayer!!.duration
                } else {
                    if (audioFileUri != null) {
                        // Play audio from selected local file.
                        audioPlayer!!.setDataSource(mContext, audioFileUri)
                        audioPlayer!!.prepare()
                        totalTime = audioPlayer!!.duration

                    }
                }


            }
        } catch (ex: IOException) {
            Log.e(TAG_PLAY_AUDIO, ex.message, ex)
        }

    }

    private fun stopCurrentPlayAudio(ca: ImageButton, pa: ImageButton) {
        if (audioPlayer != null && audioPlayer!!.isPlaying) {
            audioPlayer!!.stop()
            audioPlayer!!.release()
            audioPlayer = null
            ca.visibility = View.VISIBLE
            pa.visibility = View.GONE
        }
    }

    fun playSong(path: String) {
        val length = intArrayOf(0)


    }


    fun runs() {

        var currentPosition = audioPlayer!!.currentPosition
        val total = audioPlayer!!.duration


        while (audioPlayer != null && audioPlayer!!.isPlaying && currentPosition < total) {
            try {
                Thread.sleep(1000)
                currentPosition = audioPlayer!!.currentPosition
            } catch (e: InterruptedException) {
                return
            } catch (e: Exception) {
                return
            }

            seekBar!!.progress = currentPosition

        }
    }


    private fun clearMediaPlayer() {
        audioPlayer!!.stop()
        audioPlayer!!.release()
        audioPlayer = null
    }

    companion object {


        internal val ITEM_CONTENT_VIEW_TYPE = 1
        private val dialog: ProgressDialog? = null
        private val UPDATE_AUDIO_PROGRESS_BAR = 3

        val TAG_PLAY_AUDIO = "PLAY_AUDIO"


        /* class DownloadFileFromURL extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String pathFolder = "";
        String pathFile = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setTitle("Processing...");
            pd.setMessage("Please wait.");
            pd.setMax(100);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCancelable(true);
            pd.show();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;

            try {
                Log.e("f_url", String.valueOf(f_url));
                pathFolder = Environment.getExternalStorageDirectory() + "/YourAppDataFolder";
                audiourl=audiourl.replaceAll("http://www.elancier.com/app/audio/","");
                pathFile = pathFolder + "/"+"newfile.mp3";
                File futureStudioIconFile = new File(pathFolder);
                if(!futureStudioIconFile.exists()){
                    futureStudioIconFile.mkdirs();
                }

                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lengthOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream());
                FileOutputStream output = new FileOutputStream(pathFile);

                byte data[] = new byte[1024]; //anybody know what 1024 means ?
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lengthOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();


            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return pathFile;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pd.setProgress(Integer.parseInt(progress[0]));
        }

        @Override
        protected void onPostExecute(String file_url) {
            if (pd!=null) {
                pd.dismiss();
            }
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            *//*Intent i = new Intent(Intent.ACTION_VIEW);

            i.setDataAndType(Uri.fromFile(new File(file_url)), "application/vnd.android.package-archive" );
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            getApplicationContext().startActivity(i);*//*
        }

    }*/

    }
}
