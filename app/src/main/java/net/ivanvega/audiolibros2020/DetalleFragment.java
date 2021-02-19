package net.ivanvega.audiolibros2020;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;

import net.ivanvega.audiolibros2020.services.MiIntentService;
import net.ivanvega.audiolibros2020.services.MiServicio;

import java.io.IOException;


public class DetalleFragment extends Fragment implements View.OnTouchListener, MediaPlayer.OnPreparedListener, MediaController.MediaPlayerControl {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String ARG_ID_LIBRO = "id_libro";
    MediaPlayer mediaPlayer;
    MediaController mediaController;
    MiServicio  miServicio;
    Uri audio;
    public static MediaPlayer mediaPlayer1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Intent iSer;

    public DetalleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetalleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetalleFragment newInstance(String param1, String param2) {
        DetalleFragment fragment = new DetalleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_detalle, container, false);
        Bundle args = getArguments();
        if(args != null){
            int position = args.getInt(ARG_ID_LIBRO);
            ponInfoLibro(position, vista);
        }else{
            ponInfoLibro(0, vista);
        }
        return vista;
    }

    public void ponInfoLibro(int id){
        ponInfoLibro(id, getView());
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MiServicio.MiServicioBinder miServicioBinder =
                    (MiServicio.MiServicioBinder) iBinder ;
            miServicio =
                    miServicioBinder.getService();
            mediaPlayer = miServicio.getMediaPlayer();
            mediaController.setMediaPlayer(DetalleFragment.this);
            mediaController.setAnchorView(getView());
            mediaController.setEnabled(true);
            mediaPlayer.setOnCompletionListener( l -> {
                Log.d("Complete", "onServiceConnected: Desconectado");
                getContext().unbindService(serviceConnection);
                miServicio.stopForeground(true);
                getContext().stopService(iSer);
            });
            int randf =  miServicio.getRandomNumber();
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };

    private void ponInfoLibro(int id, View vista){
        Libro libro =
                Libro.ejemploLibros().elementAt(id);
        ((TextView) vista.findViewById(R.id.titulo)).setText(libro.titulo);
        ((TextView) vista.findViewById(R.id.autor)).setText(libro.autor);
        ((ImageView) vista.findViewById(R.id.portada)).setImageResource(libro.recursoImagen);
        vista.setOnTouchListener(this);
        mediaController = new MediaController(getContext());
        audio = Uri.parse(libro.urlAudio);
        iSer = new Intent(getContext(), MiServicio.class);
        iSer.putExtra("Audio", audio.toString());
        iSer.putExtra("ID", id);
        if (!getArguments().getBoolean("Service")){
            getContext().startService(iSer);
        }
        getContext().bindService(iSer, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d("Tocar", "onTouch: Tocado");
        mediaController.show();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d("Audiolibros", "Entramos en onPrepared de MediaPlayer");
        mediaController.setMediaPlayer(this);
        mediaController.setAnchorView(getView());
        mediaController.setEnabled(true);
        mediaController.show();
    }

    @Override
    public void start() {
            mediaPlayer.start();
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    @Override
    public int getCurrentPosition() {
            return mediaPlayer.getCurrentPosition();
    }

    @Override
    public void seekTo(int i) {
        mediaPlayer.seekTo(i);
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return mediaPlayer.getAudioSessionId();
    }

    @Override
    public void onStop() {
        mediaController.hide();

        super.onStop();
    }

}