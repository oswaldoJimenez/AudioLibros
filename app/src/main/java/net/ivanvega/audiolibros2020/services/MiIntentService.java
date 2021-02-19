package net.ivanvega.audiolibros2020.services;

import android.app.AsyncNotedAppOp;
import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

public class MiIntentService extends IntentService {

    public MiIntentService() {
        super("MiIntentSevice");
    }

    class MiTareaAsincrona extends AsyncTask<Integer, Integer, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //inicilizar recursos
            Log.d("MIIS", "Se inicio el subproceso");
        }

        @Override
        protected Boolean doInBackground(Integer... integers) {
             Log.d("MIIS", "Se recibieron en el subproceso " + integers.length +" parametros" );

             for (int i=0; i<integers.length ; i++){

                 try {
                     Thread.sleep(5000);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }

                 Log.d("MIIS", "Valor del parametro "+ (i+1) + " = " + integers[i]  );
                 publishProgress(i, i);
             }

            return integers.length>0;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            Log.d("MIIS", "Progreso en subproceso " + values[0] );
    }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Log.d("MIIS", "Subproceso finalizado" );
            }
        }
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        try {
            Thread.sleep(15000);

            //ejecucion de un subproceso ya que la tarea es pesada

            new MiTareaAsincrona().execute(1,4,5,2,6, 12,99,11, 100, 89);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d ("MIIS", "Tarea prolongada realizadA en Intent Service");
    }
}
